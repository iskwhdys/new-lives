CREATE TABLE youtube_channel (
    id varchar(32) PRIMARY KEY,
    title varchar(200),
    description text,
    subscriber_count integer,
    thumbnail_url varchar(200),
    start_date date,
    end_date date,
    check_expires boolean DEFAULT true,
    scraping boolean DEFAULT false
);


CREATE TABLE youtube_video (
    id varchar(32) PRIMARY KEY,
    channel varchar(32) references youtube_channel(id),
    enabled boolean,
    type varchar(32),
    title varchar(200),
    description text,
    published timestamp(6) without time zone,
    updated timestamp(6) without time zone,
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
    upload_status varchar(32),
    thumbnail_url varchar(200),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    update_date timestamp without time zone
);


CREATE TABLE liver (
    id varchar(32) PRIMARY KEY,
    name varchar(32),
    kana varchar(32),
    company varchar(32),
    "group" varchar(32),
    debut varchar(32),

    twitter varchar(16),
    youtube varchar(32) references youtube_channel(id),
    
    start_date date,
    end_date date,

    official text,
    icon text,
    wiki text
);

CREATE TABLE liver_tag (
    id varchar(32) references liver(id),
    key varchar(32),
    value varchar(200),
    primary key (id, key)
)


