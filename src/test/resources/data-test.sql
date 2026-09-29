insert into member (nickname, email, password, role)
values ('더미_어드민', 'admin@dummy.com', 'dummy', 'ADMIN'),
       ('더미_유저', 'user@dummy.com', 'dummy', 'USER');

insert into theme (name, description)
values ('dummy', 'dummy');

insert into time (time_value)
values ('00:00');

insert into reservation (member_id, date, time_id, theme_id)
values (2, '9999-12-31', 1, 1);

insert into reserve_waiting (member_id, date, time_id, theme_id)
values (2, '9999-01-01', 1, 1);
