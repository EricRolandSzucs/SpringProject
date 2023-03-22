-- Database creation
CREATE DATABASE IF NOT EXISTS announcements;
USE announcements;

-- User creation
CREATE USER 'user'@'localhost' IDENTIFIED WITH mysql_native_password BY 'godbless123!';
GRANT ALL PRIVILEGES ON announcements.* TO 'user'@'localhost';

-- User and database cleanup
-- DROP DATABASE IF EXISTS announcements;
-- DROP USER user@localhost;
-- FLUSH PRIVILEGES;