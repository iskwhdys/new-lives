package com.iskwhdys.newlives.app.twitter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.iskwhdys.newlives.app.youtube.YoutubeVideoLogic;
import com.iskwhdys.newlives.domain.liver.LiverRepository;
import com.iskwhdys.newlives.domain.liver.LiverTagRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;
import com.iskwhdys.newlives.infra.twitter.TwitterApi;
import com.twitter.twittertext.TwitterTextParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TweetService {
    @Autowired
    YoutubeChannelRepository channelRepository;
    @Autowired
    LiverRepository liverRepository;
    @Autowired
    LiverTagRepository liverTagRepository;

    @Autowired
    TwitterApi twitterApi;

    public void tweet(YoutubeVideoEntity v) {
        try {
            if (!tweet(v, true, false, true) /* || !tweet(v, true, false, true) */ || !tweet(v, true, false, false)) {
                tweet(v, false, false, false);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private boolean tweet(YoutubeVideoEntity v, boolean useLiver, boolean useChannel, boolean use2j3jInfo) {
        String msg = getTweetMessage(v, useLiver, useChannel, use2j3jInfo);
        if (TwitterTextParser.parseTweet(msg).isValid) {
            return twitterApi.tweet(msg);
        }
        return false;
    }

    private String getTweetMessage(YoutubeVideoEntity v, boolean useLiver, boolean useChannel, boolean use2j3jInfo) {
        var result = new StringBuilder();

        result.append(getHeader(v)).append("\r\n");
        result.append(v.getTitle()).append("\r\n");
        result.append("https://www.youtube.com/watch?v=" + v.getId()).append("\r\n");

        if (useLiver) {
            String name = getLiverName(v);
            if (name != null) {
                result.append(name).append("\r\n");
            }
        }

        if (useChannel) {
            channelRepository.findById(v.getYoutubeChannelEntity().getId()).ifPresent(c -> {
                if (!result.toString().contains(c.getTitle())) {
                    result.append(c.getTitle()).append("\r\n");
                }
            });
        }

        if (use2j3jInfo) {
            result.append("\r\n").append("その他配信情報：https://nijisanji-live.com");
        }

        return result.toString();
    }

    private String getHeader(YoutubeVideoEntity v) {
        var result = new StringBuilder();
        var diffMin = ChronoUnit.MINUTES.between(v.getLiveStart(), LocalDateTime.now());
        if (diffMin == 0) {
            result.append("～");
        } else {
            result.append("～" + diffMin + "分前に");
        }
        if (YoutubeVideoLogic.isTypeLive(v)) {
            result.append("配信開始");
        } else if (YoutubeVideoLogic.isTypeUpload(v)) {
            result.append("投稿されました");
        } else if (YoutubeVideoLogic.isTypePremier(v)) {
            result.append("プレミア公開開始");
        }
        result.append("～");
        return result.toString();
    }

    private String getLiverName(YoutubeVideoEntity v) {
        var l = liverRepository.findByYoutubeChannelEntityId(v.getYoutubeChannelEntity().getId());
        if (l.isPresent()) {
            return l.get().getName();
        } else {
            var tag = liverTagRepository.findByIdKeyAndValue("youtube", v.getYoutubeChannelEntity().getId());
            if (tag.isPresent()) {
                return tag.get().getLiverEntity().getName();
            }
        }
        return null;
    }

}
