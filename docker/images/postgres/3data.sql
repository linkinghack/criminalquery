insert into b_departments(ch_department_name, i_supervisor_id, i_level, i_district_id)
    values ('公安部', 0, 1, 100000);

insert into b_users (ch_user_id, ch_passwd_sha256, ch_email, ch_realname, i_role, i_department_id, b_activated, ch_phone)
    values ('admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'linkinghack@outlook.com', '刘磊', 1, 1, true, '18235101905');