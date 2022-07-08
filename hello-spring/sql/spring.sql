--=======================================================
-- 관리자 계정
--=======================================================
-- spring 계정 생성
alter session set  "_oracle_script" = true;

create user spring
identified by spring
default tablespace users;

alter user spring quota unlimited on users;

grant connect, resource to spring;

--=======================================================
-- spring 계정
--=======================================================
create table dev (
        no number,
        name varchar2(50) not null,
        career number not null,
        email varchar2(200) not null,
        gender char(1),
        lang varchar2(100) not null,
        created_at date default sysdate, -- Java, C (1정규화 위반이지만 역정규화 처리)
        constraint pk_dev_no primary key(no),
        constraint ck_dev_gender check(gender in ('M', 'F'))
);

create sequence seq_dev_no;

select * from dev;


