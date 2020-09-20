package com.iskwhdys.newlives.infra.twitter;

import javax.annotation.PostConstruct;

import com.iskwhdys.newlives.infra.config.AppConfig;
import com.twitter.twittertext.TwitterTextParser;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

@Slf4j
@Component
public class TwitterApi {

    @Autowired
    private AppConfig appConfig;

    private Twitter twitter;

    @PostConstruct
    public void init() {
        var api = appConfig.getTwitter().getApikey();
        log.info("API Twitter consumerKey:" + api.getConsumerKey());
        log.info("API Twitter consumerSecret:" + api.getConsumerSecret());
        log.info("API Twitter accessToken:" + api.getAccessToken());
        log.info("API Twitter accessTokenSecret:" + api.getAccessTokenSecret());
        if (StringUtils.isEmpty(api.getConsumerKey()) || StringUtils.isEmpty(api.getConsumerSecret())
                || StringUtils.isEmpty(api.getAccessToken()) || StringUtils.isEmpty(api.getAccessTokenSecret())) {
            log.info("Twitterの各種キーが無いためツイートは行いません。");
            return;
        }
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(api.getConsumerKey(), api.getConsumerSecret());
        twitter.setOAuthAccessToken(new AccessToken(api.getAccessToken(), api.getAccessTokenSecret()));
    }

    public boolean tweet(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }

        String logText = text.replace("\r", "").replace("\n", "");

        if (!TwitterTextParser.parseTweet(text).isValid) {
            log.info("isInValid:" + logText);
            return false;
        }

        try {
            if (twitter == null) {
                log.info("Tweet Dummy:" + logText);
            } else {
                log.info("Tweet:" + logText);
                twitter.updateStatus(text);
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
            return false;
        }

        return true;

    }

}
