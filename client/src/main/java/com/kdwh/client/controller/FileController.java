package com.kdwh.client.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kdwh.client.entity.FileInfo;
import com.kdwh.client.util.HttpClientUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@Controller
public class FileController {

    @ResponseBody
    @RequestMapping("/upload")
    public FileInfo upload(MultipartFile file) throws IOException {
        String uuid = null;
        try {
            uuid = HttpClientUtils.uploadFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileInfo fileInfo = HttpClientUtils.getFileInfoByUUID(uuid);
        return fileInfo;
    }

    @RequestMapping("/download")
    public void download(String uuid, HttpServletResponse response, HttpServletRequest request) throws IOException {
            System.out.println("download:"+uuid);
            HttpClientUtils httpClientUtils = new HttpClientUtils();
            String i = httpClientUtils.downloadFile(request,uuid);
            System.out.println(i);
    }
    }


