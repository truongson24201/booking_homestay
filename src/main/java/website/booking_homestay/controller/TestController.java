package website.booking_homestay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.booking_homestay.service.IAuthService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private IAuthService authService;

    @GetMapping("/")
    public ResponseEntity<?> get(){
        int date = (int) new Date().getTime();
        Date date1 = new Date(new Date().getTime()+604800000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Format the date into a string
        String formattedDate = dateFormat.format(date1);
        System.out.println(formattedDate);
        return ResponseEntity.ok(date1);
    }

//    @GetMapping("/hello")
//    public ResponseEntity<?> hello(){
//
//        return ResponseEntity.ok();
//    }
}
