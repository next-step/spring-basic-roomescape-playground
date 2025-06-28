INSERT INTO member (id, name, email, password, role)
VALUES (1, '어드민', 'admin@email.com', 'password', 'ADMIN'),
       (2, '브라운', 'brown@email.com', 'password', 'USER');

INSERT INTO theme (id, name, description, deleted)
VALUES (1, '공포', '무서운 테마입니다.', false),
       (2, '모험', '신나는 테마입니다.', false),
       (3, '미스터리', '수수께끼를 풀어보세요.', false);

INSERT INTO time (id, time_value, deleted)
VALUES (1, '10:00', false),
       (2, '12:00', false),
       (3, '14:00', false);