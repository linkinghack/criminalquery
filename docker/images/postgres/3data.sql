insert into b_departments(ch_department_name, i_supervisor_id, i_level, i_district_id)
    values ('公安部', 0, 1, 100000);

insert into b_users (ch_user_id, ch_passwd_sha256, ch_email, ch_realname, i_role, i_department_id, b_activated, ch_phone)
    values ('admin', 'd7190eb194ff9494625514b6d178c87f99c5973e28c398969d2233f2960a573e', 'linkinghack@outlook.com', '刘磊', 1, 1, true, '18235101905');
