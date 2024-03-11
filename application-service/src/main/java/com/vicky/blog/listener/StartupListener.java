package com.vicky.blog.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.vicky.blog.ApplicationServiceApplication;

@Component
@Profile("prod")
public class StartupListener {

    @EventListener(ContextRefreshedEvent.class)
    public void startupListener() throws IOException {

        ClassLoader classLoader = ApplicationServiceApplication.class.getClassLoader();

        File firebaseSecretFile =
                new File(Objects.requireNonNull(classLoader.getResource("secrets/firebase_secret.json")).getFile());
        FileInputStream serviceAccount = new FileInputStream(firebaseSecretFile);

        FirebaseOptions options = new FirebaseOptions.Builder()
                                                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                                    .build();

        FirebaseApp.initializeApp(options);
    }
}
