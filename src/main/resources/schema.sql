CREATE TABLE time
(
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    time_value VARCHAR(20) NOT NULL,
    deleted    BOOLEAN     NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE member
(
    id       BIGINT              NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255)        NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     VARCHAR(255)        NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE reservation
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    date       VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    time_id    BIGINT,
    theme_id   BIGINT,
    member_id  BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE waiting
(
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    member_id BIGINT       NOT NULL,
    date      VARCHAR(255) NOT NULL,
    time_id   BIGINT,
    theme_id  BIGINT,
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (time_id) REFERENCES time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);