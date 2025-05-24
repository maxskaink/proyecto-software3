package unicauca.coreservice.infrastructure.configuration;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseApp;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void init() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("src/main/java/unicauca/coreservice/key/sera-ca640-firebase-adminsdk-fbsvc-bf7c318ee6.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
