# qys_filedemo
文件上传与下载-qys

# 开发环境
- jdk1.8
- idea
- 嵌入式jetty,derby
- Apache Maven

# 项目简介
1. 客户端使用了SpringBoot+Layui,实现了文件的上传，以及显示元数据等功能
2. 服务端使用嵌入式jetty来代替tomcat,使用apache derby数据库来存储数据

# 存在的bug
- 2019.11.6 
    - 文件上传和下载的目录还存在BUG
    - 服务端的过滤器还存在BUG
    - 还有一些方法很模糊，需要写些注解
    - 由于是边运行项目，边测试功能，所以单元测试没有写
    
# 总结

- 了解到了以前没有使用到的新的技术，比如jetty,derby等，同时也知道了自己有许多的不足的地方需要改进。后续还会不断的完善这个项目

