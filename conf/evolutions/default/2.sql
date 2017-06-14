# --- !Ups


CREATE TABLE "ARTEFACTS" ("ID" INTEGER PRIMARY KEY, "CONTENT" VARCHAR NOT NULL, "CREATOR" FOREIGN KEY (USER) REFERENCES USERS(USER))
insert into "USERS" ("USER","PASSWORD","NICKNAME") values ("admin","admin","");

insert into "USERS" ("USER","PASSWORD","NICKNAME") values ("delete","me","Showing integrity");	

# --- !Downs


