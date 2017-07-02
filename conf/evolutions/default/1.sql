# --- !Ups

create table "USERS" (
  "ID" INTEGER PRIMARY KEY,
  "USER" VARCHAR NOT NULL,
  "PASSWORD" VARCHAR NOT NULL,
  "NICKNAME" VARCHAR NOT NULL
);

create table "BLOGS" (
  ID INTEGER PRIMARY KEY,
  USERS_ID INTEGER NOT NULL,
  "WHEN" TIMESTAMP NOT NULL,
  WHAT VARCHAR NOT NULL,
  FOREIGN KEY(USERS_ID) REFERENCES USERS(ID)
);

create table "ARTEFACTS" (
  ID INTEGER PRIMARY KEY,
  CONTENT VARCHAR NOT NULL,
  CREATOR_ID VARCHAR NOT NULL,
  CATEGORIES_ID INTEGER,
  TAGS_ID INTEGER,
  "CREATED" TIMESTAMP NOT NULL,
  FOREIGN KEY (CREATOR_ID) REFERENCES USERS(ID),
  FOREIGN KEY (CATEGORIES_ID) REFERENCES ARTEFACT_CATEGORIES(ID)
);

CREATE TABLE ARTEFACT_TAGS (
  ID INTEGER PRIMARY KEY,
  CATEGORY_TAG VARCHAR UNIQUE NOT NULL,
  CREATOR_ID INTEGER NOT NULL,
  CREATED_DATE TIMESTAMP NOT NULL,
  FOREIGN KEY (CREATOR_ID) REFERENCES USERS(ID)
);

CREATE TABLE TAGS_FOR_ARTEFACTS (
  ARTEFACT_ID INTEGER NOT NULL,
  TAG_ID INTEGER NOT NULL,
  ADDED_DATE TIMESTAMP NOT NULL,
  ADDED_BY_USER INTEGER,
  FOREIGN KEY (ARTEFACT_ID) REFERENCES ARTEFACTS(ID),
  FOREIGN KEY (TAG_ID) REFERENCES ARTEFACT_TAGS(ID),
  FOREIGN KEY (ADDED_BY_USER) REFERENCES USERS(ID)
);

CREATE TABLE ARTEFACT_CATEGORIES (
  ID INTEGER PRIMARY KEY NOT NULL,
  CATEGORY_TITLE VARCHAR NOT NULL,
  "CREATOR" VARCHAR NOT NULL,
  CREATED_DATE TIMESTAMP NOT NULL

);

CREATE TABLE "ARTEFACT_VIEWS" (
  ARTEFACT_ID INTEGER,
  USER_ID INTEGER,
  "WHEN" TIMESTAMP NOT NULL,
  FOREIGN KEY (ARTEFACT_ID) REFERENCES ARTEFACTS(ID),
  FOREIGN KEY (USER_ID) REFERENCES USERS(ID)

);

CREATE TABLE "INTERACTIONS" (
  ARTEFACT_ID INTEGER,
  USER_ID INTEGER,
  INTERACTION_TYPE_ID INTEGER,
  INTERACTION_DATE TIMESTAMP NOT NULL,
  FOREIGN KEY (ARTEFACT_ID) REFERENCES ARTEFACTS(ID),
  FOREIGN KEY (USER_ID) REFERENCES USERS(ID),
  FOREIGN KEY (INTERACTION_TYPE_ID) REFERENCES INTERACTION_TYPES(ID)
);

CREATE TABLE "INTERACTION_TYPES" (
  ID INTEGER PRIMARY KEY,
  INTERACTION_TYPE VARCHAR NOT NULL,
  ACTIVE INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE LIKES (
  ARTEFACT_ID INTEGER,
  USER_ID INTEGER,
  LIKE_DATE TIMESTAMP NOT NULL,
  FOREIGN KEY (ARTEFACT_ID) REFERENCES ARTEFACTS(ID),
  FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);

CREATE TABLE COMMENTS (
  ARTEFACT_ID INTEGER,
  USER_ID INTEGER,
  TIMESTAMP TIMESTAMP NOT NULL,
  COMMENT_CONTENT VARCHAR NOT NULL,
  FOREIGN KEY (ARTEFACT_ID) REFERENCES ARTEFACTS(ID),
  FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
);

insert into "USERS" ("USER","PASSWORD","NICKNAME") values ("admin","admin","");




# --- !Downs

drop table "BLOGS";
drop table "USERS";
drop table "ARTEFACTS"