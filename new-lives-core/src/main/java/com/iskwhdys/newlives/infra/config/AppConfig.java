package com.iskwhdys.newlives.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties("newlives")
public class AppConfig {
    String url;

    Image image;
    Youtube youtube;
    Twitter twitter;
    Google google;

    @Data
    public static class Image {
        Youtube youtube;
        Liver liver;
        Integer thumbnailCacheCount;
        Integer iconCacheCount;

        @Data
        public static class Youtube {
            String thumbnailPath;
            String channelPath;
        }

        @Data
        public static class Liver {
            String iconPath;
        }
    }

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

    @Data
    public static class Google {
        Sitemap sitemap;

        @Data
        public static class Sitemap {
            String xmlPath;
        }
    }

}
