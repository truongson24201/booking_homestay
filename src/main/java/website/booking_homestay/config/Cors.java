//package website.booking_homestay.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class Cors implements WebMvcConfigurer{
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:3000")
//                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
////                        .allowedHeaders("Authorization","Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method","Access-Control-Request-Headers")
//                        .allowedHeaders("*")
//                        .allowCredentials(true)
//                        .maxAge(3600);
//            }
//        };
//    }
//
////    @Override
////    public void addCorsMappings(CorsRegistry registry) {
////        System.out.println("config cors");
////        registry.addMapping("/**");
////    }
//}
