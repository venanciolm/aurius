DROP TABLE IF EXISTS USUARIO_USR;
CREATE TABLE USUARIO_USR (
  USR_ID CHAR(32) NOT NULL,
  USR_LOGIN char(8) DEFAULT NULL,
  USR_ACTIVO char(1) DEFAULT NULL,
  USR_PASSWORD char(50) DEFAULT NULL,
  USR_NOMBRE char(50) DEFAULT NULL,
  USR_APELLIDO1 char(50) DEFAULT NULL,
  USR_APELLIDO2 char(50) DEFAULT NULL,
  USR_COUNTRY char(2) DEFAULT NULL,
  USR_LANG char(2) DEFAULT NULL,
  USR_VARIANT char(25) DEFAULT NULL,
  PRIMARY KEY (USR_ID),
  UNIQUE KEY INX_LOGIN (USR_LOGIN),
  KEY INX_ACTIVO (USR_ACTIVO,USR_LOGIN)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

DROP TABLE IF EXISTS GRUPO_GRP;
CREATE TABLE GRUPO_GRP (
  GRP_ID CHAR(32) NOT NULL,
  GRP_DESC char(255) DEFAULT NULL,
  PRIMARY KEY (GRP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

DROP TABLE IF EXISTS ROLE_ROL;
CREATE TABLE ROLE_ROL (
  ROL_ID CHAR(32) NOT NULL,
  ROL_NAME char(20) DEFAULT NULL,
  ROL_DESC char(255) DEFAULT NULL,
  PRIMARY KEY (ROL_ID),
  UNIQUE KEY INX_ROLNAME (ROL_NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;


DROP TABLE IF EXISTS GRP_GRP;
CREATE TABLE GRP_GRP (
  GRP_ID CHAR(32) NOT NULL,
  GRP_ID1 CHAR(32) NOT NULL,
  PRIMARY KEY (GRP_ID,GRP_ID1),
  KEY INDX_GRP_ROL (GRP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;


DROP TABLE IF EXISTS GRP_ROL;
CREATE TABLE GRP_ROL (
  GRP_ID CHAR(32) NOT NULL,
  ROL_ID CHAR(32) NOT NULL,
  PRIMARY KEY (GRP_ID,ROL_ID),
  KEY INDX_GRP_ROL (GRP_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;


DROP TABLE IF EXISTS USR_GRP;
CREATE TABLE USR_GRP (
  USR_ID CHAR(32) NOT NULL,
  GRP_ID CHAR(32) NOT NULL,
  PRIMARY KEY (USR_ID,GRP_ID),
  KEY INDX_USR_ROL (USR_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

DROP TABLE IF EXISTS USR_ROL;
CREATE TABLE USR_ROL (
  USR_ID CHAR(32) NOT NULL,
  ROL_ID CHAR(32) NOT NULL,
  PRIMARY KEY (USR_ID,ROL_ID),
  KEY INDX_USR_ROL (USR_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;