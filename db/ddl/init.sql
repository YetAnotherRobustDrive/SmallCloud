create table if not exists admin_config
(
    admin_config_id bigint auto_increment
        primary key,
    code            varchar(255) null,
    local_date_time datetime(6)  null,
    value           varchar(255) null
);

create table if not exists answer
(
    answer_id    bigint auto_increment
        primary key,
    content      varchar(255) null,
    created_date datetime(6)  null
);

create table if not exists boards
(
    board_id     bigint auto_increment
        primary key,
    board_type   varchar(255)  null,
    content      varchar(5000) null,
    created_date datetime(6)   null,
    title        varchar(255)  null
);

create table if not exists `groups`
(
    group_id        bigint auto_increment
        primary key,
    name            varchar(15) not null,
    parent_group_id bigint      null,
    constraint UK_8mf0is8024pqmwjxgldfe54l7
        unique (name),
    constraint FK6jti6ckllxdprv7nn8of6molx
        foreign key (parent_group_id) references `groups` (group_id)
);

create table if not exists index_data
(
    index_data_id bigint auto_increment
        primary key,
    location      varchar(255) null,
    size          bigint       null
);

create table if not exists members
(
    member_id             bigint auto_increment
        primary key,
    changed_password_date datetime(6)  null,
    expired_date          datetime(6)  null,
    joined_date           datetime(6)  null,
    locked                bit          null,
    nickname              varchar(15)  not null,
    password              varchar(60)  not null,
    location              varchar(40)  null,
    role                  varchar(255) null,
    username              varchar(15)  not null,
    `group`               bigint       null,
    constraint UK_lj4daw762ura5d2y6iu7g5n1i
        unique (username),
    constraint FKa3356afnmba9kpgvd240s9b8u
        foreign key (`group`) references `groups` (group_id)
);

create table if not exists data_nodes
(
    dtype        varchar(31)  not null,
    data_node_id bigint auto_increment
        primary key,
    create_date  datetime(6)  null,
    name         varchar(255) null,
    type         varchar(255) null,
    author_id    bigint       null,
    folder_id    bigint       null,
    constraint FKafvi2p9c0dr5l74xxtwxfmwha
        foreign key (author_id) references members (member_id)
);

create table if not exists files
(
    location      varchar(40) null,
    size          bigint      null,
    data_node_id  bigint      not null
        primary key,
    index_data_id bigint      null,
    constraint FK6fuovkepxnp2tgw520foqpse7
        foreign key (index_data_id) references index_data (index_data_id),
    constraint FKgl17svu4fbfrpfcsgg84qqdyd
        foreign key (data_node_id) references data_nodes (data_node_id)
);

create table if not exists folders
(
    data_node_id bigint not null
        primary key,
    constraint FKsjwcxabari0x9b5aiaou5g1o9
        foreign key (data_node_id) references data_nodes (data_node_id)
);

alter table data_nodes
    add constraint FKnirlp11e9o1bscmt2727crc96
        foreign key (folder_id) references folders (data_node_id);

create table if not exists labels
(
    label_id bigint auto_increment
        primary key,
    name     varchar(255) null,
    owner    bigint       null,
    constraint FKq0gx3uxxbstnjlr39xgpx1krv
        foreign key (owner) references members (member_id)
);

create table if not exists label_data_node
(
    data_node_id bigint not null,
    label_id     bigint not null,
    constraint FK7fds7qeos5py9u4tn4sa5rgat
        foreign key (data_node_id) references data_nodes (data_node_id),
    constraint FK9oucl9mi0ghyk0wx3dtul34nv
        foreign key (label_id) references labels (label_id)
);

create table if not exists notifications
(
    notification_id bigint auto_increment
        primary key,
    content         varchar(255) null,
    local_date_time datetime(6)  null,
    owner           bigint       null,
    constraint FK2qg4vb1dti4bvkpsc4qp6hxxe
        foreign key (owner) references members (member_id)
);

create table if not exists question
(
    question_id  bigint auto_increment
        primary key,
    contact      varchar(255) null,
    content      varchar(255) null,
    created_date datetime(6)  null,
    title        varchar(255) null,
    writer       varchar(255) null,
    answer_id    bigint       null,
    constraint FK4necknmeljfb3mka3lblgwlxl
        foreign key (answer_id) references answer (answer_id)
);

create table if not exists segments
(
    id       bigint auto_increment
        primary key,
    location varchar(255) null,
    name     varchar(255) null,
    size     bigint       null,
    file_id  bigint       null,
    constraint FK33naunvt6pd93aiwjxemvt0ho
        foreign key (file_id) references files (data_node_id)
);

create table if not exists shares
(
    dtype    varchar(31) not null,
    share_id bigint auto_increment
        primary key,
    file     bigint      null,
    constraint FKqdrk5jfpa3gnux6uxm6grdtd2
        foreign key (file) references data_nodes (data_node_id)
);

create table if not exists group_shares
(
    share_id bigint not null
        primary key,
    group_id bigint null,
    constraint FK1g6nitlnapc4ivkos3i2bv24
        foreign key (group_id) references `groups` (group_id),
    constraint FKq7si2trj71wn9qonllygq9xui
        foreign key (share_id) references shares (share_id)
);

create table if not exists member_shares
(
    share_id bigint not null
        primary key,
    user_id  bigint null,
    constraint FK2fkm9qkp90f5hvhqnbglmrqyv
        foreign key (user_id) references members (member_id),
    constraint FKt3aj8dke0qfhoy2a8igki9i2j
        foreign key (share_id) references shares (share_id)
);

create table if not exists user_log
(
    share_id  bigint auto_increment
        primary key,
    action    varchar(255) null,
    id_addr   varchar(255) null,
    status    bit          null,
    time      datetime(6)  null,
    member_id bigint       null,
    constraint FKhsm7ycac9ot41w94licsb898u
        foreign key (member_id) references members (member_id)
);


