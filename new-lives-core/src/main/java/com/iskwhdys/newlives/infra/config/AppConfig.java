package com.iskwhdys.newlives.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties("newlives")
public class AppConfig {
    Youtube youtube;
    Twitter twitter;

    @Data
    public static class Youtube {
        Apikey apikey;

        @Data
        public static class Apikey {
            String channel;
            String video;
        }
    }

    @Data
    public static class Twitter {
        Apikey apikey;

        @Data
        public static class Apikey {
            String consumerKey;
            String consumerSecret;
            String accessToken;
            String accessTokenSecret;
        }
    }

}
