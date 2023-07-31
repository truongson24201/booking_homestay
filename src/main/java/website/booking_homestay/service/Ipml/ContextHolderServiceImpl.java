package website.booking_homestay.service.Ipml;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import website.booking_homestay.entity.User;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.UserRepository;
import website.booking_homestay.service.IContextHolder;
import website.booking_homestay.utils.MessageResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContextHolderServiceImpl implements IContextHolder {
    private final UserRepository userRepository;

    public Boolean checkAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            return true;
        }
        return false;
    }
    @Override
    public String getUsernameFromContext() {
        if (!checkAuthentication()){
            throw new BaseException(HttpStatus.UNAUTHORIZED, MessageResponse.UN_AUTHENTICATION);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @Override
    public String getRoleFromContext() {
        if (!checkAuthentication()){
            throw new BaseException(HttpStatus.UNAUTHORIZED, MessageResponse.UN_AUTHENTICATION);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        // You can modify the code above to extract specific roles as needed
        return roles.get(0);
    }

    @Override
    public User getUser() {
        String username = getUsernameFromContext();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(HttpStatus.NOT_FOUND,"Your account not found!"));
        return user;
    }
}
