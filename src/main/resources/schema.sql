-- schema.sql
-- Drop foreign key constraints
ALTER TABLE reservation DROP CONSTRAINT IF EXISTS FKlthp3qwugirblkd0anlpuu9gh;
ALTER TABLE reservation DROP CONSTRAINT IF EXISTS FKs8fda3381waf76c0cvwcp0c1k;

-- Drop tables if they exist
DROP TABLE IF EXISTS reservation;
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
                             theme_id BIGINT NOT NULL,
                             time_id BIGINT NOT NULL,
                             date VARCHAR(255) NOT NULL,
                             name VARCHAR(255) NOT NULL,
                             PRIMARY KEY (id),
                             FOREIGN KEY (theme_id) REFERENCES theme(id),
                             FOREIGN KEY (time_id) REFERENCES event_time(id)
);
