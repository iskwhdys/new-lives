create index on youtube_channel (enabled);

create index on youtube_video (channel);
create index on youtube_video (enabled);
create index on youtube_video (type);
create index on youtube_video (upload_date);
create index on youtube_video (live_start);
create index on youtube_video (live_schedule);


