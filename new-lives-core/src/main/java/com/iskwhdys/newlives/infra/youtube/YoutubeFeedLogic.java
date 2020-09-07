package com.iskwhdys.newlives.infra.youtube;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.iskwhdys.newlives.domain.youtube.YoutubeChannelEntity;
import com.iskwhdys.newlives.domain.youtube.YoutubeVideoEntity;
import com.iskwhdys.newlives.infra.youtube.YoutubeFeedEntity.Video;

import org.jdom2.Element;

public class YoutubeFeedLogic {

    private YoutubeFeedLogic() {
    }

    public static YoutubeVideoEntity setElementData(YoutubeChannelEntity channel, YoutubeVideoEntity video,
            Video feedVideo) {
        video.setId(feedVideo.getId());
        video.setYoutubeChannelEntity(channel);
        video.setEnabled(true);
        video.setUpdateDate(LocalDateTime.now());
        for (Element element : feedVideo.getElement().getChildren()) {
            switch (element.getName()) {
                case "title":
                    video.setTitle(element.getValue());
                    break;
                case "published":
                    video.setPublished(LocalDateTime.parse(element.getValue(), DateTimeFormatter.ISO_DATE_TIME));
                    break;
                case "updated":
                    video.setUpdated(LocalDateTime.parse(element.getValue(), DateTimeFormatter.ISO_DATE_TIME));
                    break;
                case "group":
                    setElementGroupData(video, element);
                    break;
                default:
                    break;
            }
        }
        return video;
    }

    private static YoutubeVideoEntity setElementGroupData(YoutubeVideoEntity video, Element group) {
        for (Element element : group.getChildren()) {
            switch (element.getName()) {
                case "description":
                    video.setDescription(element.getValue());
                    break;
                case "thumbnail":
                    video.setThumbnailUrl(element.getAttributeValue("url"));
                    break;
                case "community":
                    setElementGroupCommunityData(video, element);
                    break;
                default:
                    break;
            }
        }
        return video;
    }

    private static YoutubeVideoEntity setElementGroupCommunityData(YoutubeVideoEntity video, Element community) {
        for (Element element : community.getChildren()) {
            switch (element.getName()) {
                case "starRating":
                    int count = Integer.parseInt(element.getAttributeValue("count"));
                    String ave = element.getAttributeValue("average");
                    int like = YoutubeCommon.getLikeCount(count, ave);
                    int dislike = count - like;
                    video.setLikes(like);
                    video.setDislikes(dislike);
                    break;
                case "statistics":
                    video.setViews(Integer.parseInt(element.getAttributeValue("views")));
                    break;
                default:
                    break;
            }
        }
        return video;
    }

}
