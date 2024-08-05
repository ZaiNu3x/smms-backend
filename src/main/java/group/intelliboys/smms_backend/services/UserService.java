package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.models.dtos.UserAuthInfo;
import group.intelliboys.smms_backend.models.entities.User;
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
                .orElse(null);
    }

    public User getUserReferenceByEmail(String email) {
        return userRepository.getReferenceByEmail(email);
    }

    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isPhoneNumberAlreadyRegistered(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
