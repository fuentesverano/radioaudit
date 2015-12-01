-- CREATE USER 'radioaudit_app'@'localhost' IDENTIFIED BY 'cielo01';
-- GRANT ALL PRIVILEGES ON *.* TO 'radioaudit_app'@'localhost' WITH GRANT OPTION;
-- FLUSH PRIVILEGES;

DROP database IF EXISTS radioaudit;
CREATE DATABASE radioaudit;
use radioaudit;

SET names UTF8;

source init_structure.sql;
