package com.kdwh.server.jetty.main;

import com.kdwh.server.jetty.filter.AuthorityFilter;
import com.kdwh.server.jetty.servlet.BaseServlet;
import com.kdwh.server.jetty.servlet.FileServlet;
import com.kdwh.server.jetty.servlet.IndexServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.net.InetSocketAddress;
import java.util.EnumSet;

public class Main {
    private static String host = "127.0.0.1";
    private static String port = "9681";
    private final static String CONTEXT = "/";
    public static void main(String[] args) throws Exception {
        InetSocketAddress address = new InetSocketAddress(host,Integer.parseInt(port));
        Server server = new Server(address);
        //添加自定义Servlet
        ServletContextHandler handler = new ServletContextHandler();
        handler.addFilter(new FilterHolder(new AuthorityFilter()),"/fileUpload.do", EnumSet.allOf(DispatcherType.class));
        handler.addServlet(BaseServlet.class,"/base");
        handler.addServlet(FileServlet.class,"/fileUpload.do");
        handler.addServlet(FileServlet.class,"/getFileInfo.do");
        handler.addServlet(FileServlet.class,"/fileDownload.do");
        handler.addServlet(IndexServlet.class,"");

        server.setHandler(handler);
        server.start();

    }
}
