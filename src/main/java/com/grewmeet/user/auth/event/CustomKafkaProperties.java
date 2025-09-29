package com.grewmeet.user.auth.event;

import org.springframework.boot.context.properties.ConfigurationProperties;

// application.yml(properties)의 'app.kafka' 하위 프로퍼티를 바인딩
@ConfigurationProperties(prefix = "app.kafka")
public record CustomKafkaProperties(Topics topics) {

    /**
     * app.kafka.topics.* 와 매핑됨
     * application.yml app.kafka.topics: 에 토픽 추가한 뒤에 record 필드 추가할 것
     */
    public record Topics(
            String userRegistered
    ) {}
}
