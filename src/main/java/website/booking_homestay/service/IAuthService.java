package website.booking_homestay.service;

import org.springframework.http.ResponseEntity;
import website.booking_homestay.DTO.auth.Signin;
import website.booking_homestay.DTO.auth.Signup;

public interface IAuthService {
    public ResponseEntity<?> register(Signup signUpRequest);
    public ResponseEntity<?> login(Signin signin);
    public ResponseEntity<?> refreshToken(String refreshToken);
}
