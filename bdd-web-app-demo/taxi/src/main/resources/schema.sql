-- User
CREATE TABLE IF NOT EXISTS USER
(
  USER_ID INTEGER NOT NULL AUTO_INCREMENT,
  USERNAME VARCHAR (255
) NOT NULL UNIQUE,
PASSWORD VARCHAR (255
),
  FIRST_NAME VARCHAR (255
),
  LAST_NAME VARCHAR (255
),
  CREATION_DATE TIMESTAMP,
  MODIFICATION_DATE TIMESTAMP,
PRIMARY KEY (USER_ID
)
);
CREATE SEQUENCE IF NOT EXISTS USER_ID_SEQ;

-- Reservation
CREATE TABLE IF NOT EXISTS RESERVATION
(
  RESERVATION_ID INTEGER NOT NULL AUTO_INCREMENT,
  USER_ID INTEGER NOT NULL,
DATE VARCHAR (255
),
  DEPARTURE VARCHAR (255
),
  EARLIEST_START_TIME VARCHAR (255
),
  DESTINATION VARCHAR (255
),
  LATEST_START_TIME VARCHAR (255
),
  CREATION_DATE TIMESTAMP,
  MODIFICATION_DATE TIMESTAMP,
PRIMARY KEY (RESERVATION_ID
),
FOREIGN KEY (USER_ID
) REFERENCES USER (USER_ID
) ON UPDATE CASCADE,
);
CREATE SEQUENCE IF NOT EXISTS RESERVATION_ID_SEQ;