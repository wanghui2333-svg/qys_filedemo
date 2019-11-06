package com.kdwh.server.utils;

import com.kdwh.server.entity.FileInfo;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class FileUtils {

    /**
     * 利用NIO的Channel实现文件的复制功能
     * @param source 源文件
     * @param dest 目标文件
     * @throws IOException
     */
    public static void copyFileUsingFileChannels(File source, File dest) throws IOException
    {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    /**
     * 保存文件
     * @param file 从客户端传过来的file对象
     * @return 文件信息，用于保存到数据库中
     */
    public static FileInfo fileSave(FileItem file){
        //随机生成唯一标识UUID
        UUID uuid = UUID.randomUUID();
        //获取当前时间的毫秒数
        long l = System.currentTimeMillis();
        //格式化时间，格式为yyyyMMdd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String createTime = sdf.format(l);
        //保存文件信息
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileCreateTime(createTime);
        fileInfo.setUuid(uuid.toString());
        fileInfo.setFileOriginName(file.getName());
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileType(file.getContentType());

        System.out.println(fileInfo);
        //获取当前系统路径
        String path = System.getProperty("user.dir");
        //设置上传路径
        String uploadPath = path+"\\upload\\"+createTime;
        fileInfo.setFileSavePath(uploadPath);
        //新建fike
        File file1 = new File(uploadPath);
        if (!file1.isDirectory()){
            //如果file不是文件夹，生成目录
            file1.mkdirs();
        }
        if (!file1.isFile()) {
            try {
                //保存文件
                file.write(new File(uploadPath+"\\"+uuid));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return fileInfo;
    }



}
