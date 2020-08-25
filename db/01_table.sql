CREATE TABLE youtube_channel (
    id text PRIMARY KEY,
    enabled boolean DEFAULT true,    
    title text,
    description text,
    subscriber_count integer,
    thumbnail_url text,
    start_date date,
    end_date date
);


CREATE TABLE youtube_video (
    id text PRIMARY KEY,
    channel text NOT NULL,
    enabled boolean,
    type text,
    title text,
    description text,
    upload_date timestamp(6) without time zone,
    live_schedule timestamp(6) without time zone,
    live_start timestamp(6) without time zone,
    live_end timestamp(6) without time zone,
    duration integer,
    views integer,
    live_views integer,
    likes integer,
    dislikes integer,
    favorites integer,
    comments integer,
    upload_status text,
    thumbnail_url text,
    etag text,
    create_date timestamp without time zone,
    update_date timestamp without time zone
);


CREATE TABLE liver (
    id text PRIMARY KEY,
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


