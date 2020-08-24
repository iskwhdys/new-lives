package com.iskwhdys.newlives.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties("newlives")
public class AppConfig {
    Youtube youtube;

    @Data
    public static class Youtube {
        Apikey apikey;

        @Data
        public static class Apikey {
            String channel;
            String video;
        }
    }
}
