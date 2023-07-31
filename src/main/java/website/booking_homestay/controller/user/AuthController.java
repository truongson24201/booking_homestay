package website.booking_homestay.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.booking_homestay.DTO.auth.Signin;
import website.booking_homestay.DTO.auth.Signup;
import website.booking_homestay.service.IAuthService;

@RestController
@RequestMapping("api/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    // user
    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody Signup signup){
        return authService.register(signup);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody Signin signin){
        return authService.login(signin);
    }

    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken ){
        return authService.refreshToken(refreshToken);
    }
}
