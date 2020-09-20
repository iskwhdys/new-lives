package com.iskwhdys.newlives.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import com.iskwhdys.newlives.app.twitter.TweetService;
import com.iskwhdys.newlives.app.youtube.YoutubeVideoLogic;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class TweetServiceTests {

    @Autowired
    TweetService tweetService;
    Method getTweetMessage;

    @BeforeEach
    void setUp() throws NoSuchMethodException, SecurityException {

        getTweetMessage = TweetService.class.getDeclaredMethod("getTweetMessage", YoutubeVideoEntity.class,
                boolean.class, boolean.class, boolean.class);
        getTweetMessage.setAccessible(true);
    }

    @Test
    void liveStart() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        var v = getLiveEntity();
        String text = "～配信開始～\r\nタイトル\r\nhttps://www.youtube.com/watch?v=null\r\n";
        assertEquals(text, getTweetMessage.invoke(tweetService, v, false, false, false));
    }

    @Test
    void liveStartLiver() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        var v = getLiveEntity();
        String text = "～配信開始～\r\nタイトル\r\nhttps://www.youtube.com/watch?v=null\r\nアンジュ・カトリーナ\r\n";
        assertEquals(text, getTweetMessage.invoke(tweetService, v, true, false, false));
    }

    @Test
    void liveStartChannel() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        var v = getLiveEntity();
        String text = "～配信開始～\r\nタイトル\r\nhttps://www.youtube.com/watch?v=null\r\nアンジュ・カトリーナ - Ange Katrina - \r\n";
        assertEquals(text, getTweetMessage.invoke(tweetService, v, false, true, false));
    }

    @Test
    void liveStart2j3jinfo() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        var v = getLiveEntity();
        String text = "～配信開始～\r\nタイトル\r\nhttps://www.youtube.com/watch?v=null\r\n\r\nその他配信情報：https://nijisanji-live.com";
        assertEquals(text, getTweetMessage.invoke(tweetService, v, false, false, true));
    }

    @Test
    void liveStartAll() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        var v = getLiveEntity();
        String text = "～配信開始～\r\nタイトル\r\nhttps://www.youtube.com/watch?v=null\r\nアンジュ・カトリーナ\r\nアンジュ・カトリーナ - Ange Katrina - \r\n\r\nその他配信情報：https://nijisanji-live.com";
        assertEquals(text, getTweetMessage.invoke(tweetService, v, true, true, true));
    }

    @Test
    void secondChannel() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        var v = getLiveEntity();
        v.getYoutubeChannelEntity().setId("UCTi_rzf5QIkXjhJjkbcAdTg");
        String text = "～配信開始～\r\nタイトル\r\nhttps://www.youtube.com/watch?v=null\r\n緑仙\r\n";
        assertEquals(text, getTweetMessage.invoke(tweetService, v, true, false, false));
    }

    @Test
    void liveStartMinus() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        var v = getLiveEntity();
        v.setLiveStart(v.getLiveStart().minusMinutes(5));
        String text = "～5分前に配信開始～\r\nタイトル\r\nhttps://www.youtube.com/watch?v=null\r\n";
        assertEquals(text, getTweetMessage.invoke(tweetService, v, false, false, false));
    }

    @Test
    void premierStart() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        var v = getLiveEntity();
        v.setType(YoutubeVideoLogic.TYPE_PREMIER);
        String text = "～プレミア公開開始～\r\nタイトル\r\nhttps://www.youtube.com/watch?v=null\r\n";
        assertEquals(text, getTweetMessage.invoke(tweetService, v, false, false, false));
    }

    @Test
    void upload() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        var v = getLiveEntity();
        v.setType(YoutubeVideoLogic.TYPE_UPLOAD);
        String text = "～投稿されました～\r\nタイトル\r\nhttps://www.youtube.com/watch?v=null\r\n";
        assertEquals(text, getTweetMessage.invoke(tweetService, v, false, false, false));
    }

    YoutubeVideoEntity getLiveEntity() {
        var v = new YoutubeVideoEntity();
        v.setType(YoutubeVideoLogic.TYPE_LIVE);
        v.setTitle("タイトル");
        v.setLiveStart(LocalDateTime.now());
        var c = new YoutubeChannelEntity();
        c.setId("UCHVXbQzkl3rDfsXWo8xi2qw");
        v.setYoutubeChannelEntity(c);
        return v;
    }

}
