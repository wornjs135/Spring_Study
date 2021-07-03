package inu.appcenter.study2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //AuditingEntityListener.class 사용 가능하게 해줌
public class JpaConfig {
}
