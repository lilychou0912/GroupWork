package com.example.myapplication.utils;
import org.apache.http.HttpEntity;

import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.text.ParseException;
import java.lang.String;

public class info_token {
    public String getTitle(String url)throws ParseException, IOException, JSONException {
        JSONObject studentJSONObject = new JSONObject();
        studentJSONObject.put("appkey","NVR7LV4TT36S");
        studentJSONObject.put("url",url);
        studentJSONObject.put("contentwithhtml",true);
        String url_api="https://api.gugudata.com/news/fetchcontent";
        String enc="UTF-8";
        String result=send(url_api,studentJSONObject,enc);
        JSONObject json=new JSONObject(result);
        String title="Title";
        String j_title=json.get("Data").toString();
        JSONObject j_Title=new JSONObject(j_title);
        String article_title=j_Title.get(title).toString();
        return article_title;





    }
    public  String send(String url, JSONObject jsonObject,String encoding)throws ParseException, IOException{
        String body = "";


        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");
        s.setContentEncoding((Header) new BasicHeader(HTTP.CONTENT_TYPE,
                "application/json"));
        //设置参数到请求对象中
        httpPost.setEntity(s);
        System.out.println("请求地址："+url);
//        System.out.println("请求参数："+nvps.toString());


        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
//        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");


        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }

       entity.consumeContent();
        //释放链接
        response.close();
        return body;

    }



}
