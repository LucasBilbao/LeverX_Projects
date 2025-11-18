package com.leverx.trugame;

import com.leverx.trugame.configurations.AppConfig;
import com.leverx.trugame.filters.TransactionLoggingFilter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class App {

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        Context tomcatContext = tomcat.addContext("", new File(".").getAbsolutePath());

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);

        Tomcat.addServlet(tomcatContext, "dispatcher", new DispatcherServlet(rootContext))
                .setLoadOnStartup(1);
        tomcatContext.addServletMappingDecoded("/trugame/api/*", "dispatcher");

        FilterDef filterDef = new FilterDef();
        filterDef.setFilter(new TransactionLoggingFilter());
        filterDef.setFilterName("transactionLoggingFilter");
        filterDef.addInitParameter("encoding", "UTF-8");

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName("transactionLoggingFilter");
        filterMap.addURLPattern("/*");

        tomcatContext.addFilterDef(filterDef);
        tomcatContext.addFilterMap(filterMap);

        tomcat.getConnector();
        tomcat.start();
        System.out.println("✅ Server running at http://localhost:8080");

        tomcat.getServer().await();

    }
}
