package website.booking_homestay.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import website.booking_homestay.security.UserDetailsServiceImpl;
import website.booking_homestay.security.JwtServiceImpl;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtServiceImpl jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if (request.getServletPath().contains("/api/v1/auth")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
            String jwt = parseJwt(request);
//            System.out.println("jwt: "+jwt);
            if (StringUtils.hasText(jwt) && jwtService.validateJwtToken(jwt)) {
//                System.out.println("JWT"+jwt);
                String username = jwtService.getUserNameFromJwtToken(jwt);
//                System.out.println(username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        filterChain.doFilter(request, response);
    }
    private String parseJwt(HttpServletRequest request) {

//        System.out.println(request.getServletPath());
        String headerAuth = request.getHeader("Authorization");
//        System.out.println(headerAuth);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//            System.out.println("VoVo");
//            System.out.println(headerAuth.substring(7).toUpperCase());
            String token = headerAuth.substring(7);
            if (token.equals("null")) return null;
            return token;
        }

        return null;
    }
}
