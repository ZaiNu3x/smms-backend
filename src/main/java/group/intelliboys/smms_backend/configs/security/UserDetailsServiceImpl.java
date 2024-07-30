package group.intelliboys.smms_backend.configs.security;

import group.intelliboys.smms_backend.exceptions.EmailNotFoundException;
import group.intelliboys.smms_backend.models.dtos.UserAuthInfo;
import group.intelliboys.smms_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws EmailNotFoundException {
        UserAuthInfo userAuthInfo = userService.getUserAuthInfoByEmail(username);

        return User.builder()
                .username(userAuthInfo.getEmail())
                .password(userAuthInfo.getPassword())
                .authorities(new SimpleGrantedAuthority(userAuthInfo.getRole().name()))
                .build();
    }
}
