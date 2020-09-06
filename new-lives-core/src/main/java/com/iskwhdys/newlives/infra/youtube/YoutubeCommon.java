package com.iskwhdys.newlives.infra.youtube;

public class YoutubeCommon {

    private static final int YOUTUBE_LIKE_VALUE = 5;
    private static final int YOUTUBE_DISLIKE_VALUE = 1;

    private YoutubeCommon() {
    }

    public static int getLikeCount(int count, String strStarAve) {
        int starAve = Integer.parseInt(strStarAve.replace(".", ""));

        for (int i = count; i > 0; i--) {
            int like = YOUTUBE_LIKE_VALUE * i;
            int dislike = YOUTUBE_DISLIKE_VALUE * (count - i);
            double ave = (like + dislike) / (double) count;
            int num = (int) (ave * 100);

            if (num <= starAve) {
                return i;
            }
        }

        return 0;
    }
}
