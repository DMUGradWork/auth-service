package com.grewmeet.user.auth.service;

import com.grewmeet.user.auth.domain.AuthCredential;
import com.grewmeet.user.auth.dto.RegisterRequest;
import com.grewmeet.user.auth.dto.RegisterResponse;
import com.grewmeet.user.auth.event.UserEventPublisher;
import com.grewmeet.user.auth.exception.AlreadyExistsEmailException;
import com.grewmeet.user.auth.repository.AuthCredentialRepository;
import com.grewmeet.user.saga.UserRegisteredEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthCredentialRepository authCredentialRepository;
    private final UserEventPublisher userEventPublisher;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        log.info("회원 가입 시작 : {}", request);
        if (authCredentialRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsEmailException(request.email());
        }
        AuthCredential authCredential = AuthCredential.create(
                generateUserId(),
                request.email(),
                passwordEncoder.encode(request.password())
        );
        authCredentialRepository.save(authCredential);
        userEventPublisher.publishUserRegistered(UserRegisteredEvent.of(authCredential));

        return RegisterResponse.from(authCredential);
    }

    private String generateUserId() {
        return "user_" + UUID.randomUUID()
                .toString()
                .replace("-", "").substring(0, 12);
    }
}
