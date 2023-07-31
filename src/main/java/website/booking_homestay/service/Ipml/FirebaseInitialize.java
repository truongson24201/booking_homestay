package website.booking_homestay.service.Ipml;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

@Service
public class FirebaseInitialize {
    @PostConstruct
    public void initialize() {
        try {
//            Path path= Paths.get("src/main/resources/");
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/serviceAccount.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("booking-3f25f.appspot.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
