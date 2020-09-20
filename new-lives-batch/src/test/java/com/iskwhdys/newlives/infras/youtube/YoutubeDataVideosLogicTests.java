package com.iskwhdys.newlives.infras.youtube;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;
import com.iskwhdys.newlives.infra.config.AppConfig;
import com.iskwhdys.newlives.infra.youtube.YoutubeDataVideosApi;
import com.iskwhdys.newlives.infra.youtube.YoutubeDataVideosLogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class YoutubeDataVideosLogicTests {

    @Autowired
    AppConfig appConfig;

    YoutubeDataVideosApi dataApi;

    @BeforeEach
    void setUp() {
        dataApi = new YoutubeDataVideosApi(appConfig.getYoutube().getApikey().getVideo());
    }

    @Test
    void getExistVideo() {
        var v = YoutubeDataVideosLogic.updateData(new YoutubeVideoEntity(), dataApi.getAll("G30Evu25kw8"));
        assertNotEquals(0, v.getViews());
    }

    @Test
    void getNonExistVideo() {
        var v = YoutubeDataVideosLogic.updateData(new YoutubeVideoEntity(), dataApi.getAll(""));
        assertNull(v.getViews());
    }

    @Test
    void getDeletedVideo() {
        var v = YoutubeDataVideosLogic.updateData(new YoutubeVideoEntity(), dataApi.getAll("K8Ir2zcVw9c"));
        assertNull(v.getViews());
    }

    @Test
    void getMembrtOnlyVideo() {
        var v = YoutubeDataVideosLogic.updateData(new YoutubeVideoEntity(), dataApi.getAll("KFCt8EXJLIU"));
        assertNull(v.getViews());
    }
}
