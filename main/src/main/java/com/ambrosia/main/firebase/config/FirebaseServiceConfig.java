package com.ambrosia.main.firebase.config;

import com.ambrosia.main.MainApplication;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class FirebaseServiceConfig {

    public static void configureFirebaseService() throws IOException {
        ClassLoader classLoader = MainApplication.class.getClassLoader();

        File file = new File(Objects.requireNonNull(classLoader.getResource("firebase.json")).getFile());
        FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }
}
