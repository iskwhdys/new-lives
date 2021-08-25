package com.iskwhdys.newlives.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import com.iskwhdys.newlives.app.image.YoutubeVideoImageService;
import com.iskwhdys.newlives.app.twitter.TweetService;
import com.iskwhdys.newlives.app.youtube.YoutubeVideoLogic;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({ "local", "test" })
class YoutubeVideoImageServiceTests {

    @Autowired
    YoutubeVideoImageService service;
    Method isUpdated;

    @BeforeEach
    void setUp() throws NoSuchMethodException, SecurityException {

        isUpdated = YoutubeVideoImageService.class.getDeclaredMethod("isUpdated", String.class);
        isUpdated.setAccessible(true);
    }

    @Test
    void test() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {

        String videoId = "https://i.ytimg.com/vi/RtNj2TytkmM/hqdefault_live.jpg";
        while (true) {
            isUpdated.invoke(service, videoId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // assertEquals(true, isUpdated.invoke(service, videoId));
    }

}
