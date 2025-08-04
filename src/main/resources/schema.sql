

INSERT INTO member (name, email, password, role)
VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN'),
       ('브라운', 'brown@email.com', 'password', 'USER');

INSERT INTO theme (name, description, deleted)
VALUES ('테마1', '테마1입니다.', 0),
       ('테마2', '테마2입니다.', 0),
       ('테마3', '테마3입니다.', 0);

INSERT INTO "TIME" (time_value, deleted)
VALUES ('10:00', 0),
       ('12:00', 0),
       ('14:00', 0),
       ('16:00', 0),
       ('18:00', 0),
       ('20:00', 0);

INSERT INTO reservation (member_id, name, date, time_id, theme_id)
VALUES (1, '', '2024-03-01', 1, 1),
       (1, '', '2024-03-01', 2, 2),
       (1, '', '2024-03-01', 3, 3);

INSERT INTO reservation (member_id, name, date, time_id, theme_id)
VALUES (2, '브라운', '2024-03-01', 1, 2);
