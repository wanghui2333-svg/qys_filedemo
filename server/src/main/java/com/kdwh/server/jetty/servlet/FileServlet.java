package com.kdwh.server.jetty.servlet;

import com.alibaba.fastjson.JSON;
import com.kdwh.server.entity.FileInfo;
import com.kdwh.server.entity.JSONResult;
import com.kdwh.server.utils.DBUtils;
import com.kdwh.server.utils.FileUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@WebServlet(name = "FileServlet")
public class FileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        String url = req.getRequestURL().toString();
        if (url.contains("fileUpload.do")){
            fileUpload(req,resp);
        }
        if (url.contains("getFileInfo.do")){
            getFileInfo(req,resp);
        }
        if (url.contains("fileDownload.do")){
            fileDownload(req,resp);
            
        }
    }

    private void fileDownload(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONResult jsonResult = new JSONResult();
        String uuid = req.getParameter("uuid");
        FileInfo fileInfo = DBUtils.findByUUID(uuid);
        //设置文件的类型
        resp.setContentType(fileInfo.getFileType());
        resp.setHeader("Content-Disposition", "attachment; filename=" + fileInfo.getFileOriginName());
        FileInputStream inputStream = new FileInputStream(fileInfo.getFileSavePath()+"\\"+fileInfo.getUuid());
        OutputStream outputStream = resp.getOutputStream();
        byte[] bytes = new byte[4096];
        int length;
        while ((length = inputStream.read(bytes)) > 0){
            outputStream.write(bytes, 0, length);
        }
        inputStream.close();
        outputStream.flush();
    }

    private void getFileInfo(HttpServletRequest req, HttpServletResponse resp) {
        JSONResult jsonResult = new JSONResult();

        String uuid = req.getParameter("uuid");
        System.out.println(uuid);
        FileInfo fileInfo = DBUtils.findByUUID(uuid);
        System.out.println(fileInfo);
        if (fileInfo!=null){
            jsonResult.setFileInfo(fileInfo);
            jsonResult.setCode(200);
        }
        try {
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(JSON.toJSON(jsonResult));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void fileUpload(HttpServletRequest request, HttpServletResponse response) {
        JSONResult jsonResult = new JSONResult();
        try {
            request.setCharacterEncoding("UTF-8");
            //1.声明diskfileitemfactory工厂类，用于在指定的磁盘上设置一个临时目录
            DiskFileItemFactory disk = new DiskFileItemFactory(1024*10,new File("F:/temp"));
            //2.声明servletFileUpload，接受上面的临时目录
            ServletFileUpload up = new ServletFileUpload(disk);
            //3.解析request
            List<FileItem> list = up.parseRequest(request);
            for (FileItem fileItem : list) {
                if (fileItem.isFormField()){
                    String fieldName = fileItem.getFieldName();
                }else {
                    //如果就一个文件
                    FileItem file = list.get(0);
                    //获取文件名，带路径
                    FileInfo fileInfo = FileUtils.fileSave(file);
                    int j = DBUtils.insert(fileInfo);
                    if (j>0){
                        jsonResult.setCode(200);
                        jsonResult.setUuid(fileInfo.getUuid());
                    }
                    //删除上传的临时文件
                    file.delete();
                }
            }
            //显示数据
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter ot = response.getWriter();
            ot.print(JSON.toJSON(jsonResult));

        }catch (Exception e){
            e.printStackTrace();
        }
        // return path;

    }

}
