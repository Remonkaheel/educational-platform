package com.global.hr.config;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.Collections;

@Configuration
public class FtpServerConfig {

    @Value("${ftp.enabled:false}")
    private boolean ftpEnabled;

    @Value("${ftp.port:2121}")
    private int ftpPort;

    @Value("${ftp.username:}")
    private String ftpUsername;

    @Value("${ftp.password:}")
    private String ftpPassword;

    @Value("${ftp.home-dir:src/main/resources/static/videos}")
    private String ftpHomeDir;

    @PostConstruct
    public void startFtpServer() throws Exception {
        // The FTP server is only started when explicitly enabled, with configured credentials
        if (!ftpEnabled) {
            return;
        }
        if (ftpUsername.isBlank() || ftpPassword.isBlank()) {
            throw new IllegalStateException("يجب تعيين FTP_USERNAME و FTP_PASSWORD عند تفعيل خادم الـ FTP");
        }

        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();

        factory.setPort(ftpPort);  // Default FTP port, change if needed

        serverFactory.addListener("default", factory.createListener());

        // Set up user with write permissions
        BaseUser user = new BaseUser();
        user.setName(ftpUsername);
        user.setPassword(ftpPassword);
        user.setHomeDirectory(ftpHomeDir);  // Set the directory to save the files

        user.setAuthorities(Collections.singletonList(new WritePermission()));

        serverFactory.getUserManager().save(user);

        FtpServer server = serverFactory.createServer();
        server.start();
        System.out.println("FTP Server started on port " + ftpPort + "...");
    }
}
