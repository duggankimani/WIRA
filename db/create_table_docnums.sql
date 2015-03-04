CREATE TABLE docnums (
  casenumber varchar(20) NOT NULL DEFAULT '',
  doctype varchar(45) NOT NULL DEFAULT '',
  docno varchar(20) NOT NULL,
  PRIMARY KEY (casenumber,doctype)
);

CREATE TABLE doctypeseq (
  doctype varchar(45) NOT NULL,
  startval int DEFAULT '0',
  nextval int NOT NULL,
  PRIMARY KEY (doctype)
);
