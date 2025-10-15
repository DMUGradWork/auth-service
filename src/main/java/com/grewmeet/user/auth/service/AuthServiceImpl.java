package com.grewmeet.user.auth.service;

import com.grewmeet.user.auth.domain.AuthCredential;
import com.grewmeet.user.auth.domain.User;
import com.grewmeet.user.auth.domain.UserRole;
import com.grewmeet.user.auth.dto.PasswordUpdateRequest;
import com.grewmeet.user.auth.dto.LoginRequest;
import com.grewmeet.user.auth.dto.LoginResponse;
import com.grewmeet.user.auth.dto.UserRegisterRequest;
import com.grewmeet.user.auth.dto.UserRegisterResponse;
import com.grewmeet.user.auth.dto.UserSearchResponse;
import com.grewmeet.user.auth.exception.AlreadyExistsEmailException;
import com.grewmeet.user.auth.repository.AuthCredentialRepository;
import com.grewmeet.user.auth.repository.UserRepository;

import java.util.concurrent.ThreadLocalRandom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.grewmeet.user.auth.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthCredentialRepository authCredentialRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 사용자 ID 생성
    private String generateUserId() {
        long timestamp = System.currentTimeMillis();
        long random = ThreadLocalRandom.current().nextLong(100000000000L, 999999999999L);
        return "user_" + timestamp + "_" + random;
    }

    // 회원가입
    @Override
    @Transactional
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsEmailException(request.email());
        }

        String userId = generateUserId();
        
        String passwordHash = passwordEncoder.encode(request.password());
        
        AuthCredential authCredential = AuthCredential.create(
                userId,
                request.email(),
                passwordHash
        );
        
        User user;
        if (request.role() == UserRole.USER) {
            user = User.createUser(userId, request.email(), request.name(), request.phoneNumber(), authCredential);
        } else {
            user = User.createAdmin(userId, request.email(), request.phoneNumber(), authCredential);
        }

        authCredentialRepository.save(authCredential);
        userRepository.save(user);

        return UserRegisterResponse.from(user);
    }

    // 비밀번호 수정
    @Override
    @Transactional
    public void updatePassword(String userId, PasswordUpdateRequest request) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 userID를 가진 회원이 없습니다 : " + userId));

        AuthCredential authCredential = user.getAuthCredential();
        if (authCredential == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저 인증 정보가 없습니다 : " + userId);
        }

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.currentPassword(), authCredential.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "현재 비밀번호가 올바르지 않습니다.");
        }

        // 새 비밀번호로 업데이트
        String newPasswordHash = passwordEncoder.encode(request.newPassword());
        user.updatePassword(newPasswordHash);

        userRepository.save(user);
    }

    // 인증 정보 삭제
    @Override
    @Transactional
    public void deleteAuthCredential(String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 userID를 가진 회원이 없습니다 : " + userId));

        user.deleteAuthCredential();
        userRepository.save(user);

    }

    // ID로 회원 조회
    @Override
    @Transactional(readOnly = true)
    public UserSearchResponse getUserById(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 userID를 가진 회원이 없습니다 : " + userId));

        return UserSearchResponse.from(user);
    }

    // 전체 사용자 조회
    @Override
    @Transactional(readOnly = true)
    public Page<UserSearchResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);

        return users.map(UserSearchResponse::from);
    }

    // 관리자 OR 일반 사용자 조회
    @Override
    @Transactional(readOnly = true)
    public Page<UserSearchResponse> getUsersByRole(UserRole role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findByRole(role, pageable);

        return users.map(UserSearchResponse::from);
    }

    // 로그인
    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        String normalizedEmail = request.email() == null ? null : request.email().toLowerCase().trim();

        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."));

        AuthCredential auth = user.getAuthCredential();
        if (auth == null || !passwordEncoder.matches(request.password(), auth.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        java.util.Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("userId", user.getUserId());

        String token = jwtTokenProvider.generateToken(user.getEmail(), claims);
        return LoginResponse.bearer(token, jwtTokenProvider.getAccessTokenValiditySeconds());
    }

}
