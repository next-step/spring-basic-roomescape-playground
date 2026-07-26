DROP TABLE IF EXISTS waiting CASCADE;
DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS theme CASCADE;
DROP TABLE IF EXISTS time CASCADE;

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
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    date     VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    member_id BIGINT,
    time_id  BIGINT,
    theme_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (time_id) REFERENCES time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);

CREATE TABLE waiting
(
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    date      VARCHAR(255) NOT NULL,
    member_id BIGINT,
    time_id   BIGINT,
    theme_id  BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (time_id) REFERENCES time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id)
);


INSERT INTO member (id, name, email, password, role)
VALUES (1,'어드민', 'admin@email.com', 'password', 'ADMIN'),
       (2, '브라운', 'brown@email.com', 'password', 'USER');

INSERT INTO theme (id, name, description)
VALUES (1, '테마1', '테마1입니다.'),
       (2, '테마2', '테마2입니다.'),
       (3, '테마3', '테마3입니다.');

INSERT INTO time (id, time_value)
VALUES (1,'10:00'),
       (2, '12:00'),
       (3, '14:00'),
       (4, '16:00'),
       (5, '18:00'),
       (6, '20:00');

INSERT INTO reservation (id, name, date, time_id, theme_id)
VALUES (4, '브라운', '2024-03-01', 1, 2);

INSERT INTO reservation (id, member_id, name, date, time_id, theme_id)
VALUES (1, 1, '', '2024-03-01', 1, 1),
       (2, 1, '', '2024-03-01', 2, 2),
       (3, 1, '', '2024-03-01', 3, 3);
