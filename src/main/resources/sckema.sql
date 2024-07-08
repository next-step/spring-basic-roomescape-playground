CREATE TABLE if not exists time
(
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    time_value VARCHAR(20) NOT NULL,
    deleted    BOOLEAN     NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE if not exists theme
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE if not exists member
(
    id       BIGINT              NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255)        NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     VARCHAR(255)        NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE if not exists reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    date     VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    time_id  BIGINT,
    theme_id BIGINT,
    member_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE if not exists waiting
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    date     VARCHAR(255) NOT NULL,
    time_id  BIGINT,
    theme_id BIGINT,
    member_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (time_id) REFERENCES time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

INSERT INTO member (name, email, password, role)
VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN'),
       ('브라운', 'brown@email.com', 'password', 'USER');
INSERT INTO theme (name, description)
VALUES ('테마1', '테마1입니다.'),
       ('테마2', '테마2입니다.'),
       ('테마3', '테마3입니다.');
INSERT INTO time (time_value)
VALUES ('10:00'),
       ('12:00'),
       ('14:00'),
       ('16:00'),
       ('18:00'),
       ('20:00');

-- INSERT INTO reservation (name, date, time_id, theme_id)
-- VALUES ('어드민', '2024-03-01', 1, 1),
--        ('어드민', '2024-03-01', 2, 2),
--        ('어드민', '2024-03-01', 3, 3);

INSERT INTO reservation (member_id, name, date, time_id, theme_id)
VALUES (1, '', '2024-03-01', 1, 1),
       (1, '', '2024-03-01', 2, 2),
       (1, '', '2024-03-01', 3, 3);

INSERT INTO reservation (name, date, time_id, theme_id)
VALUES ('브라운', '2024-03-01', 1, 2);