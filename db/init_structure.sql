CREATE TABLE IF NOT EXISTS `RADIO` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(8) NOT NULL,
  `NAME` varchar(64) NOT NULL,
  `URL` varchar(128) NOT NULL,
  `WEBSITE` varchar(128) DEFAULT NULL,
  `BITRATE` int NOT NULL,
  `CONTENT_TYPE` varchar(10) NOT NULL,
  `PLAY` bit(1) DEFAULT 0 NOT NULL,
  `CONNECT` bit(1) DEFAULT 0 NOT NULL,
  `ACTIVE` bit(1) DEFAULT 0 NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CODE` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `USER` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(16) NOT NULL,
  `PASSWORD` varchar(32) NOT NULL,
  `FIRSTNAME` varchar(16) NOT NULL,
  `LASTNAME` varchar(16) NOT NULL,
  `EMAIL` varchar(128) NOT NULL,
  `CREATION_DATE` datetime NOT NULL,
  `ACCOUNT_TYPE` varchar(10) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USERNAME` (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `JINGLE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(128) NOT NULL,
  `FORMAT` varchar(16) NOT NULL,
  `CREATION_DATE` datetime NOT NULL,
  `DURATION` decimal(5,2) NOT NULL,
  `FINGERPRINT` blob NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_JINGLE_1` (`USER_ID`),
  CONSTRAINT `FK_JINGLE_1` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `RL_RADIO_SUSCRIBE_JINGLE` (
  `RADIO_ID` BIGINT(20) NOT NULL,
  `JINGLE_ID` BIGINT(20) NOT NULL,
  PRIMARY KEY (`RADIO_ID`, `JINGLE_ID`),
  KEY `FK_RADIO_SUSCRIBE_JINGLE_1` (`RADIO_ID`),
  KEY `FK_RADIO_SUSCRIBE_JINGLE_2` (`JINGLE_ID`),
  CONSTRAINT `FK_RADIO_SUSCRIBE_JINGLE_1` FOREIGN KEY (`RADIO_ID`) REFERENCES `RADIO` (`ID`),
  CONSTRAINT `FK_RADIO_SUSCRIBE_JINGLE_2` FOREIGN KEY (`JINGLE_ID`) REFERENCES `JINGLE` (`ID`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `COINCIDENCE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `JINGLE_ID` bigint(20) NOT NULL,
  `RADIO_ID` bigint(20) NOT NULL,
  `TIMESTAMP` datetime NOT NULL,
  `MATCH_PERCENT` DECIMAL(3,2) NOT NULL,
  `FINGERPRINT_SIMILARITY` DECIMAL(3,2) NOT NULL,
   PRIMARY KEY (`ID`),
  KEY `FK_COINCIDENCE_1` (`JINGLE_ID`),
  KEY `FK_COINCIDENCE_2` (`RADIO_ID`),
  CONSTRAINT `FK_COINCIDENCE_1` FOREIGN KEY (`JINGLE_ID`) REFERENCES `JINGLE` (`ID`),
  CONSTRAINT `FK_COINCIDENCE_2` FOREIGN KEY (`RADIO_ID`) REFERENCES `RADIO` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;