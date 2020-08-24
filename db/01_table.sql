CREATE TABLE broadcaster (
    id text NOT NULL,
    name text,
    kana text,
    "group" text,
    twitter text,
    youtube text,
    youtube2 text,
    start_date date,
    end_date date,
    official text,
    icon text,
    wiki text
);


CREATE TABLE youtube_channel (
    id text NOT NULL,
    title text,
    description text,
    subscriber_count integer,
    thumbnail_url text,
    enabled boolean DEFAULT true,
    rss_expires timestamp without time zone
);

CREATE TABLE video (
    id text NOT NULL,
    channel_id text NOT NULL,
    title text,
    description text,
    views integer,
    likes integer,
    dislikes integer,
    favorites integer,
    duration integer,
    comments integer,
    etag text,
    upload_date timestamp(6) without time zone,
    live_start timestamp(6) without time zone,
    live_end timestamp(6) without time zone,
    live_schedule timestamp(6) without time zone,
    live_views integer,
    enabled boolean,
    type text,
    upload_status text,
    thumbnail_url text,
    create_date timestamp without time zone,
    update_date timestamp without time zone
);


insert into broadcasters VALUES ('hoge');