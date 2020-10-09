package de.gerritstapper.casscheduler.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirestoreConfiguration {

    private final String PROJECT_ID;

    public FirestoreConfiguration(
            @Value("${google.firebase.project-id}") String projectId
    ) {
        this.PROJECT_ID = projectId;
    }

    @Bean
    public Firestore firestore() throws IOException {
        InputStream account = new ClassPathResource("cas-scheduler-firestore-access.json").getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(account);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        FirebaseApp.initializeApp(options);

        return FirestoreClient.getFirestore();
    }
}
