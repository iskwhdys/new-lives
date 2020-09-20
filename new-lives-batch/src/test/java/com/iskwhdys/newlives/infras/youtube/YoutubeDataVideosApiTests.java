package com.iskwhdys.newlives.infras.youtube;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.youtube.YoutubeDataVideosApi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class YoutubeDataVideosApiTests {
    @Autowired
    AppConfig appConfig;

    YoutubeDataVideosApi dataApi;

    @BeforeEach
    void setUp() {
        dataApi = new YoutubeDataVideosApi(appConfig.getYoutube().getApikey().getVideo());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getExistVideo() {
        var map = dataApi.getAll("G30Evu25kw8");
        var snippet = (Map<String, Object>) map.get("statistics");
        assertTrue(snippet.containsKey("viewCount"));
    }

    @Test
    void getNonExistVideo() {
        assertEquals(0, dataApi.getAll("").size());
    }

    @Test
    void getDeletedVideo() {
        assertEquals(0, dataApi.getAll("K8Ir2zcVw9c").size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getMembrtOnlyVideo() {
        var map = dataApi.getAll("KFCt8EXJLIU");
        var snippet = (Map<String, Object>) map.get("statistics");
        assertFalse(snippet.containsKey("viewCount"));
    }

}
