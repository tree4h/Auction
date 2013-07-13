# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table auction (
  id                        bigint not null,
  start                     timestamp not null,
  end                       timestamp not null,
  min_bid_price             integer not null,
  item_id                   bigint,
  member_id                 bigint,
  status                    integer not null,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint ck_auction_status check (status in (0,1,2,3)),
  constraint pk_auction primary key (id))
;

create table bid (
  id                        bigint not null,
  bid_date                  timestamp not null,
  bid_price                 integer not null,
  status                    integer not null,
  member_id                 bigint,
  auction_id                bigint,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint ck_bid_status check (status in (0,1,2,3)),
  constraint pk_bid primary key (id))
;

create table item (
  id                        bigint not null,
  member_id                 bigint,
  name                      varchar(255) not null,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_item primary key (id))
;

create table member (
  id                        bigint not null,
  name                      varchar(255) not null,
  create_date               timestamp not null,
  update_date               timestamp not null,
  constraint pk_member primary key (id))
;

create sequence auction_seq;

create sequence bid_seq;

create sequence item_seq;

create sequence member_seq;

alter table auction add constraint fk_auction_item_1 foreign key (item_id) references item (id) on delete restrict on update restrict;
create index ix_auction_item_1 on auction (item_id);
alter table auction add constraint fk_auction_member_2 foreign key (member_id) references member (id) on delete restrict on update restrict;
create index ix_auction_member_2 on auction (member_id);
alter table bid add constraint fk_bid_member_3 foreign key (member_id) references member (id) on delete restrict on update restrict;
create index ix_bid_member_3 on bid (member_id);
alter table bid add constraint fk_bid_auction_4 foreign key (auction_id) references auction (id) on delete restrict on update restrict;
create index ix_bid_auction_4 on bid (auction_id);
alter table item add constraint fk_item_member_5 foreign key (member_id) references member (id) on delete restrict on update restrict;
create index ix_item_member_5 on item (member_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists auction;

drop table if exists bid;

drop table if exists item;

drop table if exists member;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists auction_seq;

drop sequence if exists bid_seq;

drop sequence if exists item_seq;

drop sequence if exists member_seq;

