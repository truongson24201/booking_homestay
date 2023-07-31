package website.booking_homestay.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import website.booking_homestay.entity.User;
import website.booking_homestay.exception.BaseException;
import website.booking_homestay.repository.UserRepository;
import website.booking_homestay.utils.MessageResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BaseException(HttpStatus.BAD_REQUEST, MessageResponse.LOGIN_INVALID));
        return UserDetailsImpl.build(user);
    }
}
