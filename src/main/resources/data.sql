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

INSERT INTO inventory (date_value, time_id, theme_id)
VALUES ('2024-03-01', 1, 1),
       ('2024-03-01', 2, 2),
       ('2024-03-01', 3, 3),
       ('2024-03-01', 1, 2);

INSERT INTO reservation (inventory_id, member_id, status)
VALUES (1, 1, 'CONFIRMED'),
       (2, 1, 'CONFIRMED'),
       (3, 1, 'CONFIRMED'),
       (4, 2, 'CONFIRMED');
