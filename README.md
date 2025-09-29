# Auth Service

유저 도메인의 인증 서비스 (회원가입, 로그인, 비밀번호 관리)

## 🔧 환경 설정
- **포트**: 8081
- **데이터베이스**: H2 (인메모리)
- **Kafka**: localhost:9092

## 📡 이벤트 발행
회원가입 시 `user-registered` 토픽으로 이벤트 발행:
```json
{
  "email": "user@example.com",
  "userId": "user_abc123def456",
  "registeredAt": "2024-08-21T14:30:00"
}
```

## 🚀 실행
```bash
./gradlew bootRun
```

## 📄 API 문서
- Swagger UI: http://localhost:8081/swagger-ui.html
- H2 Console: http://localhost:8081/h2-console