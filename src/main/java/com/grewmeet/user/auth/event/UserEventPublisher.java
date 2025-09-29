package com.grewmeet.user.auth.event;

import com.grewmeet.user.saga.UserRegisteredEvent;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CustomKafkaProperties kafkaProperties;

    public void publishUserRegistered(UserRegisteredEvent event) {
        String topic = kafkaProperties.topics().userRegistered();
        publishEvent(topic, event);
    }

    private void publishEvent(String topic, Object event) {
        try {
            // send 메소드는 CompletableFuture 를 반환합니다.
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, event);

            // 비동기 처리 결과를 콜백으로 처리합니다. (프로덕션 환경에서 권장)
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    // 성공 로직
                    log.info("이벤트 발행 성공: topic={}, offset={}, event={}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().offset(),
                            event.getClass().getSimpleName());
                } else {
                    // 실패 로직
                    log.error("이벤트 발행 실패: topic={}, error={}", topic, ex.getMessage());
                    // 여기에 실패에 대한 추가 처리(ex: 재시도, DB에 실패 기록 등)를 할 수 있습니다.
                }
            });

        } catch (Exception e) {
            // send 호출 자체에서 발생하는 동기적 예외 처리 (ex: 직렬화 실패)
            log.error("이벤트 발행 중 동기적 오류 발생: topic={}, error={}", topic, e.getMessage());
            // 필요하다면 RuntimeException을 던져 트랜잭션을 롤백시킬 수 있습니다.
            throw new RuntimeException("이벤트 발행 중 오류 발생", e);
        }
    }
}
