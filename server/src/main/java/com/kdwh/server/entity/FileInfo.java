package com.kdwh.server.entity;


public class FileInfo {
    //uuid
    private String uuid;
    //文件大小
    private double fileSize;
    //文件类型
    private String fileType;
    //文件原始名
    private String fileOriginName;
    //文件创建时间
    private String fileCreateTime;
    //文件保存路径

    private String fileSavePath;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileOriginName() {
        return fileOriginName;
    }

    public void setFileOriginName(String fileOriginName) {
        this.fileOriginName = fileOriginName;
    }

    public String getFileCreateTime() {
        return fileCreateTime;
    }

    public void setFileCreateTime(String fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    @Override
    public String toString() {
        return "\nFileInfo{" +
                "uuid='" + uuid + '\'' +
                ", fileSize=" + fileSize +
                ", fileType='" + fileType + '\'' +
                ", fileOriginName='" + fileOriginName + '\'' +
                ", fileCreateTime='" + fileCreateTime + '\'' +
                ", fileSavePath='" + fileSavePath + '\'' +
                '}';
    }
}
