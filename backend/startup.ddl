create table users (
    id BINARY(16) not null,
    changed_pw_date datetime not null,
    created_date datetime not null,
    is_locked bit not null,
    login_id varchar(15) not null,
    login_pw varchar(20) not null,
    nickname varchar(15) not null,
    team_id bigint,
    primary key (id)
) engine=InnoDB;

create table teams(
    id bigint not null,

) engine=InnoDB;