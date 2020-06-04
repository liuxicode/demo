//package com.zhongfei.springcloudzuul.filter;
//
//import com.alibaba.fastjson.JSON;
//import com.ca.common.tools.entity.Result;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.netflix.zuul.exception.ZuulException;
//import com.netflix.zuul.http.ServletInputStreamWrapper;
//import com.zhongfei.springcloudzuul.code.FilterOrderConstant;
//import com.zhongfei.springcloudzuul.config.FilterConfig;
//import com.zhongfei.springcloudzuul.config.ZuulConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.util.DigestUtils;
//import org.springframework.util.StreamUtils;
//
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
//import static com.zhongfei.springcloudzuul.config.ZuulConstant.IMAGE_CONTENTTYPE;
//import static com.zhongfei.springcloudzuul.config.ZuulConstant.NONCE_KEY;
//
///**
// * @Auther: yuli
// * @Date: 2019/4/12 10:05
// * @Description: 请求随机数filter，防止重放攻击
// */
//@Slf4j
//@Component
//public class NonceFilter extends ZuulFilter {
//    @Value("${nonce.timeout}")
//    private int timeout;
//    @Autowired
//    private FilterConfig filterConfig;
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    @Override
//    public String filterType() {
//        return FilterConstants.PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return FilterOrderConstant.NONCE;
//    }
//
//
//    @Override
//    public boolean shouldFilter() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request = ctx.getRequest();
//        String url = request.getRequestURI();
//
//        String contetnType = request.getContentType();
//
//        if (contetnType != null && (contetnType.contains(IMAGE_CONTENTTYPE) || contetnType.contains(ZuulConstant.UPLOAD_CONTENTTYPE))) {
//            return false;
//        }
//
//        for (String exclude : filterConfig.getExcludes()) {
//            if (url.contains(exclude)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public Object run() throws ZuulException {
//        //获取request
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request = ctx.getRequest();
//        //构造请求参数字符串
//        //获取请求头信息
//        String enSign = request.getHeader(ZuulConstant.SIGN);
//        String url = request.getHeader(ZuulConstant.PATH_URL);
//        String nonce = request.getHeader(ZuulConstant.NONCE);
//        String timestamp = request.getHeader(ZuulConstant.TIMESTAMP);
//        //获取请求参数
//        Map<String, String> params;
//        String paramString = "";
//        try {
//            params = getParams(ctx);
//            paramString = buildParamString(params, ctx);
//        } catch (IOException e) {
//            log.error("获取参数异常", e);
//        }
//
//        //如果有参数为空，就返回异常
//        if (StringUtils.isBlank(enSign) || StringUtils.isBlank(url) || StringUtils.isBlank(nonce) || StringUtils.isBlank(timestamp)) {
//            log.error("验签失败  验签参数为空");
//            signError(ctx);
//            return null;
//        }
//        //如果时间戳超时，就返回异常
//        long serverTimeStamp = System.currentTimeMillis();
//        long interval = serverTimeStamp - Long.parseLong(timestamp);
//        if (interval > timeout) {
//            log.error("验签失败  时间戳超时，接收到的时间为{}，服务端当前时间戳为{}", timestamp, serverTimeStamp);
//            signError(ctx);
//            return null;
//        }
//        //请求只有一次有效
//        Boolean hasKey = redisTemplate.hasKey(NONCE_KEY + nonce);
//        if (hasKey) {
//            log.error("验签失败  redis中已存在对应的nonce:{} 请求只有一次有效", nonce);
//            signError(ctx);
//            return null;
//        }
//        //进行数据验签
//        String deSign = sign(url, paramString, nonce, timestamp);
//        log.info("{},原始的sign:{},解析后的sign：{},完全请求body：[{}]", url, deSign, enSign, paramString);
//        //如果验证失败，就返回异常信息
//        if (!enSign.equals(deSign)) {
//            log.error("验签失败   服务端的签名结果和前端不匹配");
//            signError(ctx);
//            return null;
//        }
//        //将使用过的nonce放进缓存中
//        redisTemplate.opsForValue().set(NONCE_KEY + nonce, "", timeout, TimeUnit.MILLISECONDS);
//        return null;
//    }
//
//    private void signError(final RequestContext ctx) {
//        ctx.setSendZuulResponse(false);
//        ctx.setResponseStatusCode(403);
//        ctx.setResponseBody(JSON.toJSONString(Result.build("403", "sign error", null)));
//    }
//
//    private Map<String, String> getParams(final RequestContext ctx) throws IOException {
//        HttpServletRequest request = ctx.getRequest();
//        Map<String, String> params = new HashMap<>();
//        //获取url的请求参数
//        Enumeration<String> enumeration = request.getParameterNames();
//        if (enumeration != null) {
//            while (enumeration.hasMoreElements()) {
//                String key = enumeration.nextElement();
//                String value = request.getParameter(key);
//                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
//                    params.put(key, value);
//                }
//
//            }
//        }
//
//        return params;
//    }
//
//
//    /**
//     * 获取验签
//     *
//     * @param url       请求路径
//     * @param param     请求参数
//     * @param nonce     随机数
//     * @param timestamp 时间戳
//     * @return
//     */
//    public static String sign(String url, String param, String nonce, String timestamp) {
//        String data = url +
//                param +
//                nonce +
//                timestamp;
//        return DigestUtils.md5Hex(data);
//    }
//
//    /**
//     * 构造参数字符串
//     *
//     * @param params
//     * @return
//     */
//    public static String buildParamString(Map<String, String> params, final RequestContext ctx) throws IOException {
//        List<String> keys = new ArrayList<>(params.keySet());
//        Collections.sort(keys);
//
//        StringBuffer stringBuffer = new StringBuffer();
//        keys.forEach(key -> {
//            stringBuffer.append(key);
//            stringBuffer.append(params.get(key));
//        });
//        //获取body里的请求参数
//        HttpServletRequest request = ctx.getRequest();
//        String contentType = request.getContentType();
//        if (contentType != null && !contentType.contains(IMAGE_CONTENTTYPE)) {
//            String bodyString = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
//            log.info("请求body:" + bodyString);
//            //重新放入request
//            if (StringUtils.isNotBlank(bodyString)) {
//
//                ctx.setRequest(new HttpServletRequestWrapper(request) {
//                    @Override
//                    public ServletInputStream getInputStream() throws IOException {
//                        return new ServletInputStreamWrapper(bodyString.getBytes());
//                    }
//
//                    @Override
//                    public int getContentLength() {
//                        return bodyString.getBytes().length;
//                    }
//
//                    @Override
//                    public long getContentLengthLong() {
//                        return bodyString.getBytes().length;
//                    }
//                });
//
//                if (StringUtils.isNotBlank(bodyString) && !"{}".equals(bodyString)) {
//                    return stringBuffer.append(bodyString).toString();
//                }
//            }
//        }
//        return stringBuffer.toString();
//    }
//
//    public static void main(String[] args) {
///*
//        nonce: 3f69a93f0a1b4391a1f58bc1ebfc352c
//        sign: 2c01e42f7edca01cce133f9d43f3653d
//        timestamp: 1590996249982
//
//        {"codeType":1,"mobilePhone":"+86-15708431920","title":"Registration of ca-b2b","token":"5xmkpel9x1s0000"}*/
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("ca-user-provider/v1/source-open/reg/sendSMS");
//        sb.append("codeType");
//        sb.append("1");
//        sb.append("mobilePhone");
//        sb.append("+86-15708431920");
//        sb.append("title");
//        sb.append("Registration of ca-b2b");
//        sb.append("token");
//        sb.append("5xmkpel9x1s0000");
//        sb.append("3f69a93f0a1b4391a1f58bc1ebfc352c");
//        sb.append("1590996249982");
//
//        System.out.println(DigestUtils.md5Digest());
//
//    }
//}