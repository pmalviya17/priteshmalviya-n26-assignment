package org.pmalviya.n26.transaction.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties
@Component
@Getter
public class AppProperties {
    @Value("${secondsToUseForStats:60}")
    private long secondsToUseForStats;
}
