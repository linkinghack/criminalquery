-- 更新时间自动更新 触发器
create or REPLACE FUNCTION update_on_modified()
RETURNS TRIGGER AS $$
BEGIN
	IF row(NEW.*) IS DISTINCT FROM row(OLD.*) THEN
		NEW.ts_updated_at = now(); 
			RETURN NEW;
		ELSE
			RETURN OLD;
		END IF;
END
$$ LANGUAGE 'plpgsql';


CREATE TABLE public.b_users (
    i_id SERIAL NOT NULL,
    ch_user_id varchar(64) NOT NULL UNIQUE,
    ch_password_sha256 varchar(128) NOT NULL,
    ch_email varchar(128) NOT NULL,
    ch_realname varchar NOT NULL,
    i_role integer NOT NULL,
    i_department_id integer NOT NULL,
    b_activated boolean NOT NULL,
    ch_phone varchar NOT NULL,
    PRIMARY KEY (i_id)
);
COMMENT ON COLUMN public.b_users.ch_user_id
    IS '用于用户登录的用户名';
COMMENT ON COLUMN public.b_users.ch_password_sha256
    IS 'SHA256加密的密码';
COMMENT ON COLUMN public.b_users.ch_email
    IS '用户邮箱';
COMMENT ON COLUMN public.b_users.ch_realname
    IS '用户姓名';
COMMENT ON COLUMN public.b_users.i_role
    IS '0-普通用户,1-管理员用户,用于区分是否具有编辑权限';
COMMENT ON COLUMN public.b_users.i_department_id
    IS '用户所属部门id';
COMMENT ON COLUMN public.b_users.b_activated
    IS '是否已激活';
COMMENT ON COLUMN public.b_users.ch_phone
    IS '用户电话';


CREATE TABLE public.b_departments (
    i_id SERIAL NOT NULL,
    ch_department_name varchar NOT NULL,
    i_supervisor_id integer NOT NULL,
    i_level integer NOT NULL,
    i_district_id integer NOT NULL,
    PRIMARY KEY (i_id)
);
COMMENT ON TABLE public.b_departments
    IS '部门表';
COMMENT ON COLUMN public.b_departments.i_id
    IS '部门id';
COMMENT ON COLUMN public.b_departments.ch_department_name
    IS '部门名称';
COMMENT ON COLUMN public.b_departments.i_supervisor_id
    IS '上级部门id';
COMMENT ON COLUMN public.b_departments.i_level
    IS '部门级别:1-国家级,2-省部级,3-市级别,4-县/区级';
COMMENT ON COLUMN public.b_departments.i_district_id
    IS '辖区id';


CREATE TABLE public.b_criminals (
    i_id SERIAL NOT NULL,
    ch_name varchar NOT NULL,
    i_sex integer NOT NULL,
    i_height_cm integer,
    d_birthday date,
    ch_born_place varchar,
    ch_idcard_id varchar UNIQUE,
    ch_other_features text,
    ch_portrait_fileid varchar,
    ch_edu_background varchar,
    ch_job varchar,
    ch_workfor varchar,
    ch_phone varchar,
    ch_address varchar,
    ts_created_at timestamp default now() NOT NULL,
    ts_updated_at timestamp default now() NOT NULL,
    i_created_by integer NOT NULL,
    i_updated_by integer NOT NULL,
    PRIMARY KEY (i_id)
);
COMMENT ON COLUMN public.b_criminals.ch_name
    IS '逃犯姓名';
COMMENT ON COLUMN public.b_criminals.i_sex
    IS '0-未知,1-男,2-女';
COMMENT ON COLUMN public.b_criminals.i_height_cm
    IS '逃犯身高,单位厘米';
COMMENT ON COLUMN public.b_criminals.d_birthday
    IS '逃犯生日';
COMMENT ON COLUMN public.b_criminals.ch_born_place
    IS '籍贯';
COMMENT ON COLUMN public.b_criminals.ch_idcard_id
    IS '身份证号';
COMMENT ON COLUMN public.b_criminals.ch_other_features
    IS '自然语言描述的逃犯特征';
COMMENT ON COLUMN public.b_criminals.ch_portrait_fileid
    IS '逃犯主识别图像的文件id';
COMMENT ON COLUMN public.b_criminals.ch_edu_background
    IS '教育背景';
COMMENT ON COLUMN public.b_criminals.ch_job
    IS '工作种类';
COMMENT ON COLUMN public.b_criminals.ch_workfor
    IS '工作单位';
COMMENT ON COLUMN public.b_criminals.ch_phone
    IS '逃犯曾用电话';
COMMENT ON COLUMN public.b_criminals.ch_address
    IS '住所地址';
COMMENT ON COLUMN public.b_criminals.ts_created_at
    IS '逃犯信息入库时间';
COMMENT ON COLUMN public.b_criminals.ts_updated_at
    IS '逃犯个人信息更新日期时间';
COMMENT ON COLUMN public.b_criminals.i_created_by
    IS '逃犯信息创建人id';
COMMENT ON COLUMN public.b_criminals.i_updated_by
    IS '最后一个更新逃犯信息人id';

CREATE TRIGGER t_updatetime_criminal BEFORE UPDATE ON b_criminals
    FOR EACH ROW
    EXECUTE PROCEDURE update_on_modified();


CREATE TABLE public.b_criminal_contacts (
    i_id SERIAL NOT NULL,
    i_criminal_id integer NOT NULL,
    ch_name varchar NOT NULL,
    i_sex integer NOT NULL,
    d_birthday date,
    ch_relation varchar NOT NULL,
    ch_phone varchar NOT NULL,
    ch_address varchar,
    PRIMARY KEY (i_id)
);
COMMENT ON TABLE public.b_criminal_contacts
    IS '逃犯相关联系人';
COMMENT ON COLUMN public.b_criminal_contacts.i_id
    IS '表主键业务无关';
COMMENT ON COLUMN public.b_criminal_contacts.i_criminal_id
    IS '对应逃犯id';
COMMENT ON COLUMN public.b_criminal_contacts.ch_name
    IS '联系人姓名';
COMMENT ON COLUMN public.b_criminal_contacts.i_sex
    IS '联系人性别';
COMMENT ON COLUMN public.b_criminal_contacts.d_birthday
    IS '联系人生日';
COMMENT ON COLUMN public.b_criminal_contacts.ch_relation
    IS '联系人与逃犯关系';
COMMENT ON COLUMN public.b_criminal_contacts.ch_phone
    IS '联系人电话';
COMMENT ON COLUMN public.b_criminal_contacts.ch_address
    IS '联系人地址';


CREATE TABLE public.b_wanted_orders (
    i_id SERIAL NOT NULL,
    i_criminal_id integer NOT NULL,
    ch_arrest_reason varchar NOT NULL,
    i_arrest_level integer NOT NULL,
    i_arrest_status integer NOT NULL,
    i_district_id integer NOT NULL,
    i_created_by integer NOT NULL,
    ts_created_at timestamp default now() NOT NULL,
    ts_updated_at timestamp default now() NOT NULL,
    PRIMARY KEY (i_id)
);
COMMENT ON TABLE public.b_wanted_orders
    IS '通缉令表';
COMMENT ON COLUMN public.b_wanted_orders.ch_arrest_reason
    IS '通缉原因,罪行描述';
COMMENT ON COLUMN public.b_wanted_orders.i_arrest_level
    IS '通缉级别';
COMMENT ON COLUMN public.b_wanted_orders.i_arrest_status
    IS '通缉状态:0-停止通缉,1-正在通缉';
COMMENT ON COLUMN public.b_wanted_orders.i_district_id
    IS '统计范围行政区划id';

create index orders_level_district_id_reason_index
	on b_wanted_orders (i_arrest_level, i_district_id, ch_arrest_reason);


CREATE TABLE public.b_districts (
    i_id integer NOT NULL,
    i_supervisor_id integer NOT NULL,
    ia_path_root integer[] NOT NULL,
    i_level integer NOT NULL,
    ch_name varchar NOT NULL,
    ch_province_name varchar NOT NULL,
    ch_city_name varchar NOT NULL,
    ch_county_name varchar NOT NULL,
    PRIMARY KEY (i_id)
);
COMMENT ON TABLE public.b_districts
    IS '行政区划';
COMMENT ON COLUMN public.b_districts.i_id
    IS '区划id';
COMMENT ON COLUMN public.b_districts.i_supervisor_id
    IS '父级区划id';
COMMENT ON COLUMN public.b_districts.ia_path_root
    IS '回溯id路径,逗号分隔,从省id开始';
COMMENT ON COLUMN public.b_districts.i_level
    IS '区划等级:0-国家,1-省/直辖市,2-市,3-县/区';
COMMENT ON COLUMN public.b_districts.ch_name
    IS '区划名称';
COMMENT ON COLUMN public.b_districts.ch_province_name
    IS '所属省或直辖市名称';
COMMENT ON COLUMN public.b_districts.ch_city_name
    IS '所属市名称,省级区划为空';
COMMENT ON COLUMN public.b_districts.ch_county_name
    IS '所属县区名称,省和市级区划此项为空';


CREATE TABLE public.b_clue (
    i_id SERIAL NOT NULL,
    i_criminal_id integer NOT NULL,
    ch_fileids varchar,
    ch_description varchar NOT NULL,
    ts_created_at timestamp default now() NOT NULL,
    PRIMARY KEY (i_id)
);
COMMENT ON TABLE public.b_clue
    IS '逃犯线索表';
COMMENT ON COLUMN public.b_clue.i_id
    IS '表主键,业务无关';
COMMENT ON COLUMN public.b_clue.ch_fileids
    IS '逗号分隔的资源文件id';
COMMENT ON COLUMN public.b_clue.ch_description
    IS '线索描述';
COMMENT ON COLUMN public.b_clue.ts_created_at
    IS '线索上传时间';


CREATE TABLE public.b_publish_applications (
    i_id SERIAL NOT NULL,
    i_order_id integer NOT NULL,
    i_applicant_id integer NOT NULL,
    i_status integer NOT NULL,
    i_target_district integer NOT NULL,
    i_apporver_id integer,
    PRIMARY KEY (i_id)
);
COMMENT ON COLUMN public.b_publish_applications.i_id
    IS '表主键业务无关';
COMMENT ON COLUMN public.b_publish_applications.i_applicant_id
    IS '申请者id';
COMMENT ON COLUMN public.b_publish_applications.i_status
    IS '审批状态:0-待审批,1-审批通过,2-拒绝发布';
COMMENT ON COLUMN public.b_publish_applications.i_target_district
    IS '目标通缉区域id';
COMMENT ON COLUMN public.b_publish_applications.i_apporver_id
    IS '审批人id';
