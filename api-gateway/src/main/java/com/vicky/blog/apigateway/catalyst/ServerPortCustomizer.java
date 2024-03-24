package com.vicky.blog.apigateway.catalyst;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class ServerPortCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        String port = System.getenv("X_ZOHO_CATALYST_LISTEN_PORT");
        int listenPort;
        if(port != null && !port.isEmpty()) {
            listenPort = Integer.parseInt(port);
        } else {
            listenPort = 9000;
        }
        factory.setPort(listenPort);
    }
}
