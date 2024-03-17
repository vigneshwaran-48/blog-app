package com.vicky.blog.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Component
@Profile("prod")
public class StartupListener {

    @Autowired
    private ResourceLoader resourceLoader;

    @EventListener(ContextRefreshedEvent.class)
    public void startupListener() throws IOException {

        InputStream firebaseSecretFile = 
            resourceLoader.getResource("classpath:secrets/firebase_secret.json").getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                                    .setCredentials(GoogleCredentials.fromStream(firebaseSecretFile))
                                    .setDatabaseUrl("https://blog-application-a5473-default-rtdb.firebaseio.com")
                                    .build();

        FirebaseApp.initializeApp(options);
    }
}
