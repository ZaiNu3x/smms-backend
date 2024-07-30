package group.intelliboys.smms_backend.services;

import group.intelliboys.smms_backend.models.entities.Token;
import group.intelliboys.smms_backend.repositories.TokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    public Token getTokenByValue(String value) {
        return tokenRepository.findByValue(value)
                .orElse(null);
    }

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }
}
