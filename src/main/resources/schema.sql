-- Drop foreign key constraints
ALTER TABLE reservation DROP CONSTRAINT IF EXISTS FKlthp3qwugirblkd0anlpuu9gh;
ALTER TABLE reservation DROP CONSTRAINT IF EXISTS FKs8fda3381waf76c0cvwcp0c1k;
ALTER TABLE waiting DROP CONSTRAINT IF EXISTS FKgpwphj92a6g5210wg7o9niq4q;
ALTER TABLE waiting DROP CONSTRAINT IF EXISTS FKbl3hps2bbcxa7q4bvwj8s95pd;
ALTER TABLE waiting DROP CONSTRAINT IF EXISTS FK7u9rhlu5kqgoombwckeij2ng8;

-- Drop tables if they exist
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS waiting;
DROP TABLE IF EXISTS event_time;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS theme;

-- Create tables
CREATE TABLE event_time (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            event_value VARCHAR(20) NOT NULL,
                            PRIMARY KEY (id)
);

CREATE TABLE member (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        email VARCHAR(255) NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        role VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE theme (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       description VARCHAR(255) NOT NULL,
                       name VARCHAR(255) NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE reservation (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             member_id BIGINT,
                             theme_id BIGINT NOT NULL,
                             time_id BIGINT NOT NULL,
                             date VARCHAR(255) NOT NULL,
                             name VARCHAR(255) NOT NULL,
                             PRIMARY KEY (id),
                             FOREIGN KEY (theme_id) REFERENCES theme(id),
                             FOREIGN KEY (time_id) REFERENCES event_time(id),
                             FOREIGN KEY (member_id) REFERENCES member(id)
);

CREATE TABLE waiting (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         member_id BIGINT,
                         theme_id BIGINT NOT NULL,
                         time_id BIGINT NOT NULL,
                         date VARCHAR(255) NOT NULL,
                         PRIMARY KEY (id),
                         FOREIGN KEY (member_id) REFERENCES member(id),
                         FOREIGN KEY (theme_id) REFERENCES theme(id),
                         FOREIGN KEY (time_id) REFERENCES event_time(id)
);
