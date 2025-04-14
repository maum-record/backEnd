INSERT INTO USERS (
    email, password, nick_name, role, active
) VALUES (
             'admin@maumrecord.com',
             '$2a$12$4iOFmpSOhKIbV8/WAKbSNePoiahpSOcMX7HoQY.vGGnsBUs8ucUIi',  -- 인코딩된 비밀번호
             '관리자',
             'ADMIN',
             true
         );
INSERT INTO USERS (
    email, password, nick_name, role, active
) VALUES (
             'test@test.com',
             '$2a$10$BkW.bFZlSehxOwOef56gduR3vopT9j1ohtjYhCRdHMJTaPBz7vsI6',  -- 인코딩된 비밀번호
             'test',
             'USER',
             true
         );

