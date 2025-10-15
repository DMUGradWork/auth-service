# Auth Service

유저 도메인의 인증 서비스 (회원가입, 로그인, 비밀번호 관리)

## 🔧 환경 설정
- **포트**: 8080
- **데이터베이스**: MySQL

## 🔐 인증 API
운영 서비스에 필요한 인증/회원 기능을 제공합니다.

### 엔드포인트
- POST `/auth/users/register`: 회원가입
  - 요청 바디
  ```json
  {
    "email": "user1@example.com",
    "password": "Passw0rd!",
    "name": "홍길동",
    "phoneNumber": "010-1234-5678",
    "role": "USER"
  }
  ```
  - 응답: `201 Created`, `UserRegisterResponse`


- POST `/auth/login`: 로그인(JWT 발급)
  - 요청 바디
  ```json
  {
    "email": "user1@example.com",
    "password": "Passw0rd!"
  }
  ```
  - 응답: `200 OK`, `{ accessToken, tokenType, expiresIn }`


- PUT `/auth/users/{userId}/password`: 비밀번호 변경
  - 요청 바디
  ```json
  {
    "currentPassword": "Passw0rd!",
    "newPassword": "NewPassw0rd!"
  }
  ```
  - 응답: `200 OK` 


- DELETE `/auth/users/{userId}/auth`: 인증 자격 정보 삭제
  - 응답: `200 OK` 


- GET `/auth/users/{userId}`: 사용자 단건 조회
  - 응답: `200 OK`, `UserSearchResponse`


- GET `/auth/users?page=0&size=10`: 사용자 목록 조회 (페이지네이션)
  - 응답: `200 OK`


- GET `/auth/users/role/{role}?page=0&size=10`: 역할별 사용자 조회
  - 응답: `200 OK`


## 🚀 실행
```bash
./gradlew bootRun
```

## 📦 이벤트 스키마
이 서비스는 도메인 이벤트를 발행할 수 있습니다. 아래는 표준 스키마 예시입니다.

공통 메타데이터:
```json
{
  "eventType": "EventName",
  "eventId": "b2d0f9d9-6e2f-4d7a-8b0b-0a1c2d3e4f5a",
  "occurredAt": "2025-10-15T12:34:56Z",
  "producer": "auth-service",
  "data": { /* 이벤트별 페이로드 */ }
}
```

### user-registered
회원가입 완료 시 발행.
```json
{
  "eventType": "user-registered",
  "eventId": "1e5f1c3a-3a59-4c2e-9d0f-1234567890ab",
  "occurredAt": "2025-10-15T12:34:56Z",
  "producer": "auth-service",
  "data": {
    "userId": "user_1728960000000_123456789012",
    "email": "user1@example.com",
    "name": "홍길동",
    "phoneNumber": "010-1234-5678",
    "role": "USER",
    "createdAt": "2025-10-15T12:34:56Z"
  }
}
```

### password-changed
비밀번호가 정상 변경되었을 때 발행.
```json
{
  "eventType": "password-changed",
  "eventId": "5b7a2a4c-8d2e-4f3b-9c1d-abcdefabcdef",
  "occurredAt": "2025-10-15T12:45:00Z",
  "producer": "auth-service",
  "data": {
    "userId": "user_1728960000000_123456789012",
    "email": "user1@example.com",
    "passwordChangedAt": "2025-10-15T12:45:00Z"
  }
}
```

### auth-credential-deleted
인증 자격 정보가 삭제되었을 때 발행.
```json
{
  "eventType": "auth-credential-deleted",
  "eventId": "9c8d7e6f-5a4b-3c2d-1e0f-abcdef123456",
  "occurredAt": "2025-10-15T13:00:00Z",
  "producer": "auth-service",
  "data": {
    "userId": "user_1728960000000_123456789012",
    "email": "user1@example.com",
    "deletedAt": "2025-10-15T13:00:00Z",
    "reason": "user-request"
  }
}
