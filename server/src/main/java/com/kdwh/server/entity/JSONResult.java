package com.kdwh.server.entity;

public class JSONResult {
    //状态码
    private int code;

    //uuid
    private String uuid;

    //文件信息
    private FileInfo fileInfo;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public com.kdwh.server.entity.FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    @Override
    public String toString() {
        return "\nJSONResult{" +
                "code=" + code +
                ", uuid='" + uuid + '\'' +
                ", fileInfo=" + fileInfo +
                '}';
    }
}
