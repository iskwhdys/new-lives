package com.iskwhdys.newlives.app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import com.iskwhdys.newlives.app.twitter.TweetService;
import com.iskwhdys.newlives.app.youtube.YoutubeVideoLogic;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class TweetServiceTests {

    @Autowired
    TweetService tweetService;
    Method getTweetMessage;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, SecurityException {

        getTweetMessage = TweetService.class.getDeclaredMethod("getTweetMessage", YoutubeVideoEntity.class,
                boolean.class, boolean.class, boolean.class);
        getTweetMessage.setAccessible(true);

    }

    @Test
    public void liveStart() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        var v = new YoutubeVideoEntity();
        v.setType(YoutubeVideoLogic.TYPE_LIVE);
        v.setTitle("タイトル");
        v.setLiveStart(LocalDateTime.now());

        String text = "～配信開始～\r\nタイトル\r\nhttps://www.youtube.com/watch?v=";

        assertTrue(((String) getTweetMessage.invoke(tweetService, v, false, false, false)).startsWith(text));
    }

}
