package com.kdwh.server.utils;

import com.kdwh.server.entity.FileInfo;

import java.sql.*;


public class DBUtils {
    private final static String DBURL        = "jdbc:derby:F:\\qys_filedemo\\server\\mydb";
    private final static String DERBY_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static Connection connection = null;

    public static Connection getConnection() {
        Connection conn = null;

        try {
            Class.forName(DERBY_DRIVER);

            try {
                conn = DriverManager.getConnection(DBURL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return conn;
    }

    public static void close(Connection conn, Statement statement, ResultSet resultSet){
        try {
            if (!conn.isClosed()){
                conn.close();
            }
            if (!statement.isClosed()){
                statement.close();
            }
            if (!resultSet.isClosed()){
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int insert(FileInfo fileInfo){
        connection = getConnection();

        String sql = "insert into file (uuid,file_size,file_type,file_origin_name," +
                "file_create_time,file_save_path) values(?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,fileInfo.getUuid());
            ps.setDouble(2,fileInfo.getFileSize());
            ps.setString(3,fileInfo.getFileType());
            ps.setString(4,fileInfo.getFileOriginName());
            ps.setString(5,fileInfo.getFileCreateTime());
            ps.setString(6,fileInfo.getFileSavePath());
           int i = ps.executeUpdate();
            if (i > 0){
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static FileInfo findByUUID(String uuid){
        FileInfo fileInfo = new FileInfo();
        String sql = "select * from file where uuid = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                fileInfo.setUuid(rs.getString(1));
                fileInfo.setFileType(rs.getString(3));
                fileInfo.setFileSize(rs.getDouble(2));
                fileInfo.setFileOriginName(rs.getString(4));
                fileInfo.setFileCreateTime(rs.getString(5));
                fileInfo.setFileSavePath(rs.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fileInfo;
    }
}



