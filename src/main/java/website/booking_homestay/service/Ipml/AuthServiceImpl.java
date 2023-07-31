package website.booking_homestay.service.Ipml;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import website.booking_homestay.DTO.TokenResponse;
import website.booking_homestay.DTO.auth.Signin;
import website.booking_homestay.DTO.auth.Signup;
import website.booking_homestay.entity.RefreshToken;
import website.booking_homestay.entity.Role;
import website.booking_homestay.entity.User;
import website.booking_homestay.entity.enumreration.ERole;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.RefreshTokenRepository;
import website.booking_homestay.repository.RoleRepository;
import website.booking_homestay.repository.UserRepository;
import website.booking_homestay.security.JwtServiceImpl;
import website.booking_homestay.service.IAuthService;
import website.booking_homestay.utils.MessageResponse;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public ResponseEntity<?> register(Signup signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse.EXIST_USERNAME);
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse.EXIST_GMAIL);
        }
        Role userRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new BaseException(HttpStatus.BAD_REQUEST,MessageResponse.ERROR_ROLE));
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getFullName(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPhoneNumber(),
                userRole);
        try {
            userRepository.save(user);
        }catch (Exception e){
            logger.error("Create account failed!");
            return ResponseEntity.badRequest().body(MessageResponse.ERROR_CREATE);
        }

        return ResponseEntity.ok("Register account successfully!");
    }

    @Override
    public ResponseEntity<?> login(@Valid Signin signin) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signin.getUsername(), signin.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Invalid credentials.");
        }
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(signin.getUsername(),signin.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<User> userOptional = userRepository.findByUsername(signin.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }
        User user = userOptional.get();
        if (!user.getStatus()){
            return ResponseEntity.badRequest().body("Your Account is block!");
        }
        TokenResponse tokenResponse = new TokenResponse();
        RefreshToken refreshToken = user.getRefreshToken();
        tokenResponse.setAccessToken(jwtService.generateJwtToken(signin.getUsername(),new Date(new Date().getTime()+JwtServiceImpl.jwtExpirationMs)));
        tokenResponse.setUsername(signin.getUsername());
        tokenResponse.setRole(user.getRole().getName().name());
        if (refreshToken != null){
            tokenResponse.setRefreshToken(refreshToken.getToken());
        }else {
            tokenResponse.setRefreshToken(jwtService.generateJwtToken(signin.getUsername(),new Date(new Date().getTime()+JwtServiceImpl.jwtExpirationMsRefresh)));
            refreshToken = new RefreshToken(tokenResponse.getRefreshToken(),new Date(new Date().getTime()+JwtServiceImpl.jwtExpirationMsRefresh));
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        }
//        System.out.println(authentication);
        return ResponseEntity.ok(tokenResponse.getAccessToken());
    }

    @Override
    public ResponseEntity<?> refreshToken(String refreshToken) {

        if(!refreshTokenRepository.existsByToken(refreshToken)){
            return ResponseEntity.badRequest().body("RefreshToken isn't true");
        }
        String username = jwtService.getUserNameFromJwtToken(refreshToken);
        Optional<User> User = userRepository.findByUsername(username);
        RefreshToken refreshTokenDb = User.get().getRefreshToken();
        if (User.isEmpty()){
            return ResponseEntity.badRequest().body("Admin or Manager isn't found!");
        }
        if(refreshToken == null) {
            return ResponseEntity.badRequest().body("Account have no refresh token!");
        }
        refreshTokenDb.setRefreshTokenId(User.get().getRefreshToken().getRefreshTokenId());
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(jwtService.generateJwtToken(username,new Date(new Date().getTime()+JwtServiceImpl.jwtExpirationMs)));
        tokenResponse.setUsername(username);
        tokenResponse.setRole(User.get().getRole().getName().name());

        // token expiration -> new
        if (jwtService.isTokenExpired(refreshToken)) {
            tokenResponse.setRefreshToken(jwtService.generateJwtToken(username,new Date(new Date().getTime()+JwtServiceImpl.jwtExpirationMsRefresh)));
            refreshTokenDb.setToken(tokenResponse.getRefreshToken());
            refreshTokenDb.setExpiration(new Date(new Date().getTime()+JwtServiceImpl.jwtExpirationMsRefresh));
        }else {
            tokenResponse.setRefreshToken(jwtService.generateJwtToken(username,refreshTokenDb.getExpiration()));
            refreshTokenDb.setToken(tokenResponse.getRefreshToken());
        }
        refreshTokenRepository.save(refreshTokenDb);
        return ResponseEntity.ok(tokenResponse);
    }
}
