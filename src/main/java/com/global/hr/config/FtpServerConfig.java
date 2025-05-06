package com.global.hr.config;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.Collections;

@Configuration
public class FtpServerConfig {

    @PostConstruct
    public void startFtpServer() throws Exception {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();

        factory.setPort(21);  // Default FTP port, change if needed

        serverFactory.addListener("default", factory.createListener());

        // Set up user with write permissions
      BaseUser user = new BaseUser();
        user.setName("ftpuser");
        user.setPassword("ftppassword");
        user.setHomeDirectory("\\src\\main\\resources\\static\\videos");  // Set the directory to save the files

        user.setAuthorities(Collections.singletonList(new WritePermission()));

        serverFactory.getUserManager().save(user);

        FtpServer server = serverFactory.createServer();
        server.start();
        System.out.println("FTP Server started...");
    }
}