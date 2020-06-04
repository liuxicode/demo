package com.example.demo;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author xi.liu1
 * @version 0.0.1
 * @time 2020/6/1 17:18
 * @Desc description
 * @email xi.liu1@dmall.com
 */
public class c {

    public static void main(String[] args) throws UnsupportedEncodingException {
/*
        nonce: 3f69a93f0a1b4391a1f58bc1ebfc352c
        sign: 2c01e42f7edca01cce133f9d43f3653d
        timestamp: 1590996249982

        {"codeType":1,"mobilePhone":"+86-15708431920","title":"Registration of ca-b2b","token":"5xmkpel9x1s0000"}*/

        Map<String,String> params = new HashMap<>();
        params.put("codeType","1");
        params.put("mobilePhone","+86-15708431920");
        params.put("title","Registration of ca-b2b");
        params.put("token","3v2cbrsxvak0000");

        StringBuilder sb = new StringBuilder();
        sb.append("ca-user-provider/v1/source-open/reg/sendSMS");

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        keys.forEach(key -> {
            sb.append(key);
            sb.append(params.get(key));
        });

        /*sb.append("ca-user-provider/v1/source-open/reg/sendSMS");
        sb.append("codeType");
        sb.append("1");
        sb.append("mobilePhone");
        sb.append("+86-15708431920");
        sb.append("title");
        sb.append("Registration of ca-b2b");
        sb.append("token");
        sb.append("9wdi20ok1e00000");*/
        sb.append("f16acba4b74241448380716b03e71064");
        sb.append("1591006110681");

        //217213bc63d9fa414b3bb6b3c8a22fe3
        StringBuilder sb1 = new StringBuilder();
        sb1.append("ca-user-provider/v1/source-open/reg/sendSMS{\"codeType\":1,\"mobilePhone\":\"+86-15708431920\",\"title\":\"Registration of ca-b2b\",\"token\":\"3v2cbrsxvak0000\"}");
        sb1.append("f16acba4b74241448380716b03e71064");
        sb1.append("1591006110681");
        System.out.println(sb.toString());

        System.out.println(DigestUtils.md5DigestAsHex(sb.toString().getBytes()));
        System.out.println(sb1.toString());
        System.out.println(DigestUtils.md5DigestAsHex(sb1.toString().getBytes()));


    }

}
