INSERT INTO member (id, name, email, password, role)
VALUES (1, '어드민', 'admin@email.com', 'password', 'ADMIN'),
       (2, '브라운', 'brown@email.com', 'password', 'USER');

INSERT INTO theme (id, name, description)
VALUES (1, '테마1', '테마1입니다.'),
       (2, '테마2', '테마2입니다.'),
       (3, '테마3', '테마3입니다.');

INSERT INTO "time" (id, "value")
VALUES (1, '10:00'),
       (2, '12:00'),
       (3, '14:00'),
       (4, '16:00'),
       (5, '18:00'),
       (6, '20:00');

INSERT INTO reservation (id, member_id, date, "time_id", theme_id)
VALUES (1, 1, '2024-03-01', 1, 1),
       (2, 1, '2024-03-01', 2, 2),
       (3, 1, '2024-03-01', 3, 3),
       (4, 2, '2024-03-01', 1, 2);

ALTER SEQUENCE member_seq RESTART WITH (SELECT MAX(id) + 50 FROM member);
ALTER SEQUENCE theme_seq RESTART WITH (SELECT MAX(id) + 50 FROM theme);
ALTER SEQUENCE time_seq RESTART WITH (SELECT MAX(id) + 50 FROM "time");
ALTER SEQUENCE reservation_seq RESTART WITH (SELECT MAX(id) + 50 FROM reservation);
