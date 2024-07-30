package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.exceptions.EmailNotFoundException;
import group.intelliboys.smms_backend.models.dtos.UserAuthInfo;
import group.intelliboys.smms_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserAuthInfo getUserAuthInfoByEmail(String email) {
        return userRepository.getUserAuthInfoByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email + " was not found!"));
    }
}
