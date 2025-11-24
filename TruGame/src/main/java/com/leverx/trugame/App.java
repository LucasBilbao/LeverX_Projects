package com.leverx.trugame;

import com.leverx.trugame.configurations.AppConfig;
import com.leverx.trugame.filters.TransactionLoggingFilter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
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

        FilterDef securityFilterDef = new FilterDef();
        DelegatingFilterProxy delegatingFilter = new DelegatingFilterProxy("springSecurityFilterChain");
        securityFilterDef.setFilter(delegatingFilter);
        securityFilterDef.setFilterName("springSecurityFilterChain");
        tomcatContext.addFilterDef(securityFilterDef);

        FilterMap securityFilterMap = new FilterMap();
        securityFilterMap.setFilterName("springSecurityFilterChain");
        securityFilterMap.addURLPattern("/*");
        tomcatContext.addFilterMap(securityFilterMap);

        FilterDef transactionFilterDef = new FilterDef();
        transactionFilterDef.setFilter(new TransactionLoggingFilter());
        transactionFilterDef.setFilterName("transactionLoggingFilter");
        transactionFilterDef.addInitParameter("encoding", "UTF-8");
        tomcatContext.addFilterDef(transactionFilterDef);

        FilterMap transactionFilterMap = new FilterMap();
        transactionFilterMap.setFilterName("transactionLoggingFilter");
        transactionFilterMap.addURLPattern("/*");
        tomcatContext.addFilterMap(transactionFilterMap);

        tomcat.getConnector();
        tomcat.start();
        System.out.println("✅ Server running at http://localhost:8080");

        tomcat.getServer().await();

    }
}
