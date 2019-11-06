package com.kdwh.client.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kdwh.client.entity.FileInfo;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

public class HttpClientUtils {
    private static String url = "http://127.0.0.1:9681";
    private static   final String PUBLIC_KEY_STR = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhQFWWI1WPNECVUuAKRUDuEXLCAz6Ym4P\n" +
            "1D5+/LVzXUpA0B3xmYYhE+UhFGYtDL/O2CTXHtm1aOiJyk14CN4naWWXoqwul4zecj1o4ZoSUN0e\n" +
            "rGuDGyJldCB415ji7c4cxpatz8rDj0mzxKm7fVQxx6Gk4wqQlhBHWB51CJQKqNY1leApuLWqf1MZ\n" +
            "BxYXfEEuUsGMfvua4G8MjDam2qqo7cnLk9H89fqhuWs5uW6w8v9pEq1w69YyinTZkf6JenyMFkGb\n" +
            "9vSD/GQwYTLjRO6DLOqwmT23SDZQ04JXKjbswy4UD07uMv41fcxHGAVdN1N+ISRTWxYF/DWrGPEk\n" +
            "cW3+ZwIDAQAB";



    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    public static void init() throws Exception {
        KeyPair keyPair = RSAUtil.getKeyPair();
        String publicKeyStr = RSAUtil.getPublicKey(keyPair);
        String privateKeyStr = RSAUtil.getPrivateKey(keyPair);
        System.out.println("RSA公钥Base64编码:" + publicKeyStr);
        System.out.println("RSA私钥Base64编码:" + privateKeyStr);
    }


    public static String  uploadFile(MultipartFile file) throws Exception {
        String uuid = "";
        String uploadUrl = url+"/fileUpload.do";
        //获取文件流对象
        InputStream inputStream = file.getInputStream();
        String filename = file.getOriginalFilename();
        //创建httpPost，发送post请求
        HttpPost httpPost = new HttpPost(uploadUrl);
        String str = StringUtil.generateString(10);
        System.out.println(str);

        //获取公钥
        PublicKey publicKey = RSAUtil.string2PublicKey(PUBLIC_KEY_STR);
        //用公钥加密
        byte[] publicEncrypt = RSAUtil.publicEncrypt(str.getBytes(), publicKey);
        System.out.println(publicEncrypt.toString());
        System.out.println(publicEncrypt.length);
        String s = RSAUtil.byte2Base64(publicEncrypt);
        System.out.println(s);

        httpPost.setHeader("X-SID",str);
        httpPost.setHeader("X-Signature",s);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //绑定文件参数，传入文件流和contenttype
        builder.addBinaryBody("file",inputStream, ContentType.MULTIPART_FORM_DATA,filename);
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        //执行提交
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        if (responseEntity!=null){
            //将响应内容转换成字符串
            String result = EntityUtils.toString(responseEntity);
            //根据服务器返回的参数转换，返回的是JSON格式
            JSONObject output = JSON.parseObject(result);
            uuid = output.get("uuid").toString();
        }
        if (inputStream!=null){
            inputStream.close();
        }
        return uuid;
    }


    public static FileInfo getFileInfoByUUID(String uuid){
        String getFileInfoUrl = url+"/getFileInfo.do?uuid="+uuid;
        HttpGet httpGet = new HttpGet(getFileInfoUrl);
        FileInfo output = new FileInfo();
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity!=null){
                //将响应内容转换成字符串
                String result = EntityUtils.toString(responseEntity);
                //根据服务器返回的参数转换，返回的是JSON格式
                JSONObject jsonObject = JSON.parseObject(result);
                String fileInfo = jsonObject.get("fileInfo").toString();
                 output = JSON.parseObject(fileInfo,FileInfo.class);
                System.out.println(result);
                System.out.println(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }


    public  String downloadFile(HttpServletRequest request,String uuid) throws IOException {
        String getFileInfoUrl = url+"/fileDownload.do?uuid="+uuid;
        HttpGet httpGet = new HttpGet(getFileInfoUrl);
        HttpResponse response = httpClient.execute(httpGet);
        System.out.println(response);
        String fileName = response.getHeaders("Content-Disposition")[0].getValue().split("filename=")[1];
        System.out.println("Ddd:"+this.getClass().getResource("download"));
        String downloadPath = "F:\\qys_filedemo\\client\\src\\main\\resources\\download"+"\\"+fileName;
        System.out.println(fileName);
        System.out.println(downloadPath);

        if (response.getStatusLine().getStatusCode()==200){
            //得到实体
            HttpEntity entity = response.getEntity();
            byte[] data = EntityUtils.toByteArray(entity);
            FileOutputStream fos = new FileOutputStream(downloadPath);
            fos.write(data);
            fos.close();
        }
        return downloadPath;
    }
}
