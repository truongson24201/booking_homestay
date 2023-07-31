package website.booking_homestay.config;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import website.booking_homestay.config.filter.JwtAuthenticationFilter;
import website.booking_homestay.exception.AuthEntryPointJwt;
import website.booking_homestay.exception.CustomAccessDeniedHandler;
import website.booking_homestay.security.UserDetailsServiceImpl;

import java.util.List;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {
    @Autowired
    AuthEntryPointJwt AuthEntryPointJwt;
    @Autowired
    CustomAccessDeniedHandler CustomAccessDeniedHandler;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.mail.password}")
    private String password;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        String username = this.getFrom();
        String password = this.getPassword();
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setProtocol("smtp");
        Properties mailSer = System.getProperties();
        mailSer.put("mail.smtp.auth", "true");
        mailSer.put("mail.smtp.starttls.enable", "true");

        Session getMail = Session.getInstance(mailSer, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });

        mailSender.setSession(getMail);

        return mailSender;
    }

    @Bean
    public ModelMapper modelMapper() {
        // Tạo object và cấu hình
        ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(AuthEntryPointJwt))
                .exceptionHandling(exception -> exception.accessDeniedHandler(CustomAccessDeniedHandler))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/api/clients/**").hasAnyAuthority("USER")
                                .requestMatchers("api/accounts/**").hasAnyAuthority("ADMIN")
                                .requestMatchers("api/homestays/**").hasAnyAuthority("ADMIN","MANAGER")
                                .requestMatchers("api/branches/**").hasAnyAuthority("ADMIN","MANAGER")
                                .requestMatchers("api/facilities/**").hasAnyAuthority("ADMIN","MANAGER")
                                .requestMatchers("api/tourists/**").hasAnyAuthority("ADMIN","MANAGER")
                                .requestMatchers("api/invoices/**").hasAnyAuthority("ADMIN","MANAGER")
                                .requestMatchers("api/prices/**").hasAnyAuthority("ADMIN","MANAGER")
                                .requestMatchers("api/roles/**").hasAnyAuthority("ADMIN","MANAGER")
                                .requestMatchers("api/charts/**").hasAnyAuthority("ADMIN","MANAGER")
                                .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
                new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
