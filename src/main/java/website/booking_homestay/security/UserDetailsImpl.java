package website.booking_homestay.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import website.booking_homestay.entity.User;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private Long getAccountId;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long getAccountId, String username, String password, String email,  Collection<? extends GrantedAuthority> authorities) {
        this.getAccountId = getAccountId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user){
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().getName().name()));
        return new UserDetailsImpl(
                user.getAccountId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
