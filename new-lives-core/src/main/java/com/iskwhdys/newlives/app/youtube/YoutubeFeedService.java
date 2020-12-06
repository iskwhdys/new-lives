package com.iskwhdys.newlives.app.youtube;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeChannelRepository;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoRepository;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedApi;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedEntity;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedLogic;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedEntity.Video;

import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class YoutubeFeedService {

    @Autowired
    YoutubeChannelRepository channelRepository;
    @Autowired
    YoutubeVideoRepository videoRepository;

    Map<String, YoutubeFeedEntity> feedCache = new HashMap<>();

    public List<String> getFeedVideoIdList(YoutubeChannelEntity channel) {
        try {
            return getFeed(channel).getVideos().stream().map(Video::getId).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return List.of();
        }
    }

    public void updateAllChannelVideo() {

        channelRepository.findByEnabledTrue().stream().forEach(channel -> {
            try {
                update(channel);
            } catch (Exception e) {
                log.error(channel.getId() + " " + channel.getTitle());
                log.error(e.getMessage(), e);
                channel.setEnabled(false);
                channelRepository.save(channel);
            }
        });
        channelRepository.findByEnabledFalse().stream().forEach(channel -> {
            try {
                update(channel);
                channel.setEnabled(true);
                channelRepository.save(channel);
            } catch (Exception e) {
                // BAN解除確認用
            }
        });
    }

    private YoutubeFeedEntity getFeed(YoutubeChannelEntity channel) throws JDOMException, IOException {
        YoutubeFeedEntity feed = feedCache.get(channel.getId());
        if (Boolean.TRUE.equals(channel.getCheckExpires()) && feed != null
                && System.currentTimeMillis() < feed.getExpires()) {
            return feed;
        }
        return YoutubeFeedApi.download(channel.getId());
    }

    private void update(YoutubeChannelEntity channel) throws JDOMException, IOException {
        YoutubeFeedEntity feed = getFeed(channel);
        log.info(channel.getId() + ":expire:" + feed.getFormattedLocalDateTimeExpires());

        feed.getVideos().parallelStream().forEach(feedVideo -> {
            updateVideo(channel, feedVideo);
        });
        feedCache.put(channel.getId(), feed);
    }

    private void updateVideo(YoutubeChannelEntity channel, Video feedVideo) {
        YoutubeVideoEntity v = videoRepository.findById(feedVideo.getId()).orElse(null);
        if (v == null) {
            v = YoutubeVideoLogic.createNewVideo();
        }
        try {
            if (true /* YoutubeFeedLogic.isUpdated(v, feedVideo) */) {
                // log.info(v.getId() + ":" + v.getUpdated() + ":" + v.getTitle());
                YoutubeFeedLogic.setElementData(channel, v, feedVideo);
                videoRepository.save(v);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            v.setEnabled(false);
            videoRepository.save(v);
        }
    }

    /**
     * 検証用
     * 
     * @param channelIds
     */
    public void checkExpires() {
        String[] channelIds = { "UCXU7YYxy_iQd3ulXyO-zC2w", "UCWz0CSYCxf4MhRKPDm220AQ", "UCXRlIK3Cw_TJIQC5kSJJQMg",
                "UCveZ9Ic1VtcXbsyaBgxPMvg", "UCfipDDn7wY-C-SoUChgxCQQ", "UCuz0vzQgC8LRdS6lVV0UkUg",
                "UCRWOdwLRsenx2jLaiCAIU4A", "UC53UDnhAAYwvNO7j_2Ju1cQ", "UCpfjQCCavrO-rnKaAaIF9dg",
                "UCHVXbQzkl3rDfsXWo8xi2qw", "UCRqBKoKuX30ruKAq05pCeRQ", "UCJubINhCcFXlsBwnHp0wl_g",
                "UCF1JdALrXgub24weQpqDy9Q", "UCIytNcoz4pWzXfLda0DoULQ", "UCufQu4q65z63IgE4cfKs1BQ",
                "UCoM_XmK45j504hfUWvN06Qg", "UCP19rQ5V-46B-6ZeLDJqp5w", "UCmeyo5pRj_6PXG-CsGUuWWg",
                "UCtpB6Bvhs1Um93ziEDACQ8g", "UCHBhnG2G-qN0JrrWmMO2FTA", "UC1QgXt46-GEvtNjEC1paHnw",
                "UCGw7lrT-rVZCWHfdG9Frcgg", "UCeShTCVgZyq2lsBW9QwIJcw", "UCX7YkU9nEeaoZbkVLVajcMg",
                "UCt0clH12Xk1-Ej5PXKGfdPA", "UC9EjSJ8pvxtvPdxLOElv73w", "UCHK5wkevfaGrPr7j3g56Jmw",
                "UCRcLAVTbmx2-iNcXSsupdNA", "UCqEp6RdtsMbUNrCdCswr6pA", "UChUJbHiTVeGrSkTdBzVfNCQ",
                "UC_a1ZYZ8ZTXpjg9xUY9sj8w", "UCwQ9Uv-m8xkE5PzRc7Bqx3Q", "UCNW1Ex0r6HsWRD4LCtPwvoQ",
                "UC2OacIzd2UxGHRGhdHl1Rhw", "UC48jH1ul-6HOrcSSfoR02fQ", "UCeK9HFcRZoTrvqcUCtccMoQ",
                "UCfQVs_KuXeNAlGa3fb8rlnQ", "UCfM_A7lE6LkGrzx6_mOtI4g", "UCfki3lMEF6SGBFiFfo9kvUA",
                "UCuvk5PilcvDECU7dDZhQiEw", "UCspv01oxUFf_MTSipURRhkA", "UCvzVB-EYuHFXHZrObB8a_Og",
                "UCPvGypSgfDkVe7JG2KygK7A", "UCuep1JCrMvSxOGgGhBfJuYw", "UCKMYISTJAQ8xTplUPHiABlA",
                "UCvmppcdYf4HOv-tFQhHHJMA", "UCkIimWZ9gBJRamKF0rmPU8w", "UCD-miitqNY3nyukJ4Fnf4_A",
                "UC0g1AE0DOjBYnLhkgoRWN1w", "UCllKI7VjyANuS1RXatizfLQ", "UCb5JxV6vKlYVknoJB8TnyYg",
                "UCt9qik4Z-_J-rj3bKKQCeHg", "UC1zFJrfEKvCixhsjNSb1toQ", "UCl1oLKcAq93p-pwKfDGhiYQ",
                "UCHX7YpFG8rVwhsHCx34xt7w", "UC69URn8iP4u8D_zUp-IJ1sg", "UCwokZsOK_uEre70XayaFnzA",
                "UC_GCs6GARLxEHxy1w40d6VQ", "UCkngxfPbmGyGl_RIq4FA3MQ", "UC0WwEfE-jOM2rzjpdfhTzZA",
                "UCdpUojq0KWZCN9bxXnZwz5w", "UCks41vQN-hN-1KHmpZyPY3A", "UCb6ObE-XGCctO3WrjRZC-cw",
                "UC_T8F2CvqZOwa2kme0WwRhg", "UCGYAYLDE7TZiiC8U6teciDQ", "UCryOPk2GZ1meIDt53tL30Tw",
                "UCtnO2N4kPTXmyvedjGWdx3Q", "UCNUgrFCo2Hr_VXc9bEwjcHQ", "UCmUjjW5zF1MMOhYUwwwQv9Q",
                "UCiSRx1a2k-0tOg-fs6gAolQ", "UC6wvdADTJ88OfIbJYIpAaDA", "UCo2N7C-Z91waaR6lF3LL_jw",
                "UCL_O_HXgLJx3Auteer0n0pA", "UCebT4Aq-3XWb5je1S1FvR_A", "UCFaDvgez8USXHiKidt0NtZg",
                "UCjlmCrq4TP1I4xguOtJ-31w", "UCoztvTULBYd3WmStqYeoHcA", "UCmovZ2th3Sqpd00F5RdeigQ",
                "UCe_p3YEuYJb8Np0Ip9dk-FQ", "UCpnvhOIJ6BN-vPkYU9ls-Eg", "UCTi_rzf5QIkXjhJjkbcAdTg",
                "UCZ1xuCK1kNmn5RzPYIZop3w", "UCnRQYHTnRLSF0cLJwMnedCg", "UCV5ZZlLjk5MKGg3L0n0vbzw",
                "UCsFn_ueskBkMCEyzCEqAOvg", "UC6TfqY40Xt1Y0J-N18c85qQ", "UCKtHRql9pczvUR_1kCDA6vw",
                "UCYKP16oMX9KKPbrNgo_Kgag", "UCCVwhI5trmaSxfcze_Ovzfw", "UCpNH2Zk2gw3JBjWAKSyZcQQ",
                "UCBi8YaVyZpiKWN3_Z0dCTfQ", "UCUc8GZfFxtmk7ZwSO7ccQ0g", "UCv1fFr156jc65EMiLbaLImw",
                "UCXW4MqCQn-jCaxlX-nn-BYg", "UCBiqkFJljoxAj10SoP2w2Cg", "UCLO9QDxVL4bnvRRsz6K4bsQ",
                "UCqQV8xEBWd5SVZBLlYrS_5Q", "UCaF-mODqAziHivqg0Q61XKQ", "UCTIE7LM5X15NVugV7Krp9Hw",
                "UC6oDys1BGgBsIC3WhG1BovQ", "UC_4tXjqecqox5Uc05ncxpxg", "UCSFCh5NL4qXrAy9u-u2lX3g",
                "UCwrjITPwG4q71HzihV2C7Nw", "UCP2o-o6u4uX3uq1hXspl0rg", "UC9V3Y3_uzU5e-usObb6IE1w",
                "UC-o-E6I3IC2q8sAoAuM6Umg", "UCsg-YqdqQ-KFF0LNk23BY4A", "UCz6vnIbgiqFT9xUcD6Bp65Q",
                "UC8C1LLhBhf_E2IBPLSDJXlQ", "UCIG9rDtgR45VCZmYnd-4DUw", "UCUzJ90o1EjqUbk2pBAy0_aw",
                "UCtAvQ5U0aXyKwm2i4GqFgJg", "UCt5-0i4AVHXaWJrL8Wql3mw", "UC_82HBGtvwN1hcGeOGHzUBQ",
                "UC3lNFeJiTq6L3UWoz4g1e-A", "UCL34fAoFim9oHLbVzMKFavQ", "UCmZ1Rbthn-6Jm_qOGjYsh5A",
                "UCRV9d6YCYIMUszK-83TwxVA", "UCwcyyxn6h9ex4sMXGtpQE_g", "UCbc8fwhdUNlqi-J99ISYu4A",
                "UCerkculBD7YLc_vOGrF7tKg", "UCg63a3lk6PNeWhVvMRM_mrQ", "UCo7TRj3cS-f_1D9ZDmuTsjw",
                "UCLpYMk5h1bq8_GAFVBgXhPQ" };

        log.info(LocalDateTime.now().toString());

        for (String id : channelIds) {
            try {
                YoutubeFeedEntity feed = YoutubeFeedApi.download(id);
                var last = feedCache.get(id);
                if (last != null && last.getExpires() != feed.getExpires()
                        && System.currentTimeMillis() < last.getExpires()) {
                    log.info(id + "now.expire:" + feed.getFormattedLocalDateTimeExpires() + ":last.expire:"
                            + last.getFormattedLocalDateTimeExpires());
                } else {
                    log.info(id + ": now.expire:" + feed.getFormattedLocalDateTimeExpires());
                }
                feedCache.put(id, feed);
            } catch (JDOMException | IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
