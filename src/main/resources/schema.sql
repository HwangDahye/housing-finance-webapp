DROP TABLE IF EXISTS TB_USER CASCADE;
DROP TABLE IF EXISTS TB_CREDIT_GUARANTEE_HISTORY CASCADE;

CREATE TABLE TB_USER (
  USER_SEQ      INT NOT NULL AUTO_INCREMENT,
  ID            VARCHAR(50) NOT NULL,
  PASSWORD      VARCHAR(100) NOT NULL,
  NAME          VARCHAR(20),
  REFRESH_TOKEN VARCHAR(200),
  CREATE_DATETIME     DATETIME DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (USER_SEQ),
  CONSTRAINT UNIQUE_ID UNIQUE (ID)
);

CREATE TABLE TB_CREDIT_GUARANTEE_HISTORY (
  YEAR  INT(4) NOT NULL,
  MONTH INT(2) NOT NULL,
  BANK  VARCHAR(20) NOT NULL,
  AMOUNT INT(11) NOT NULL,
  CREATE_DATETIME     DATETIME DEFAULT CURRENT_TIMESTAMP(),
  PRIMARY KEY (YEAR, MONTH, BANK)
);

CREATE TABLE TB_BANK (
  INSTITUTE_CODE  VARCHAR(20) NOT NULL,
  INSTITUTE_NAME  VARCHAR(50) NOT NULL,
  CREATE_DATETIME DATETIME DEFAULT CURRENT_TIMESTAMP(),
  USE_YN CHAR(1) DEFAULT 'Y',
  PRIMARY KEY (INSTITUTE_CODE)
);
