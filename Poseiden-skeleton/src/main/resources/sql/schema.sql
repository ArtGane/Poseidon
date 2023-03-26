-- Create database
CREATE DATABASE IF NOT EXISTS poseidon;


-- Use database
USE poseidon;


-- Drop tables if they exist
DROP TABLE IF EXISTS bid;
DROP TABLE IF EXISTS curvepoint;
DROP TABLE IF EXISTS rating;
DROP TABLE IF EXISTS rule;
DROP TABLE IF EXISTS trade;
DROP TABLE IF EXISTS users;


-- Table structure for table `bid`
CREATE TABLE bid (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  account VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  bid DOUBLE DEFAULT NULL,
  bid_quantity DOUBLE DEFAULT NULL,
  bid_date DATETIME DEFAULT NULL,
  ask DOUBLE DEFAULT NULL,
  ask_quantity DOUBLE DEFAULT NULL,
  benchmark VARCHAR(125) DEFAULT NULL,
  commentary VARCHAR(125) DEFAULT NULL,
  security VARCHAR(125) DEFAULT NULL,
  status VARCHAR(10) DEFAULT NULL,
  trader VARCHAR(125) DEFAULT NULL,
  book VARCHAR(125) DEFAULT NULL,
  side VARCHAR(125) DEFAULT NULL,
  creation_date DATETIME DEFAULT NULL,
  creation_name VARCHAR(255) DEFAULT NULL,
  deal_name VARCHAR(255) DEFAULT NULL,
  deal_type VARCHAR(255) DEFAULT NULL,
  revision_date DATETIME DEFAULT NULL,
  revision_name VARCHAR(255) DEFAULT NULL,
  source_list_id VARCHAR(255) DEFAULT NULL,
  bid_list_date DATETIME DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Table containing all bids';


-- Table structure for table `curvepoint`
CREATE TABLE curvepoint (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  curve_id BIGINT UNSIGNED DEFAULT NULL,
  term DOUBLE DEFAULT NULL,
  value DOUBLE DEFAULT NULL,
  as_of_date DATETIME DEFAULT NULL,
  creation_date DATETIME DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Table curvepoint all bids';


-- Table structure for table `rating`
CREATE TABLE rating (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  fitch_rating VARCHAR(255) DEFAULT NULL,
  moodys_rating VARCHAR(255) DEFAULT NULL,
  sandp_rating VARCHAR(255) DEFAULT NULL,
  order_number INT DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Table containing all ratings';


-- Table structure for table `rule`
CREATE TABLE rule (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(125) DEFAULT NULL,
  description VARCHAR(125) DEFAULT NULL,
  json VARCHAR(125) DEFAULT NULL,
  template VARCHAR(512) DEFAULT NULL,
  sql_part VARCHAR(255) DEFAULT NULL,
  sql_str VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Table containing all rules';


-- Table structure for table trade
CREATE TABLE trade (
id INT UNSIGNED NOT NULL AUTO_INCREMENT,
account VARCHAR(30) NOT NULL,
type VARCHAR(30) NOT NULL,
buy_quantity DECIMAL(10, 2) DEFAULT NULL,
sell_quantity DECIMAL(10, 2) DEFAULT NULL,
buy_price DECIMAL(10, 2) DEFAULT NULL,
sell_price DECIMAL(10, 2) DEFAULT NULL,
security VARCHAR(125) DEFAULT NULL,
status ENUM('OPEN', 'CLOSED') DEFAULT NULL,
trader_id INT UNSIGNED DEFAULT NULL,
benchmark VARCHAR(125) DEFAULT NULL,
book VARCHAR(125) DEFAULT NULL,
side ENUM('BUY', 'SELL') DEFAULT NULL,
creation_date DATETIME DEFAULT NULL,
creation_name VARCHAR(255) DEFAULT NULL,
deal_name VARCHAR(255) DEFAULT NULL,
deal_type VARCHAR(255) DEFAULT NULL,
revision_date DATETIME DEFAULT NULL,
revision_name VARCHAR(255) DEFAULT NULL,
source_list_id VARCHAR(255) DEFAULT NULL,
trade_date DATETIME DEFAULT NULL,
PRIMARY KEY (id),
CONSTRAINT fk_trader FOREIGN KEY (trader_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Table containing all trades';

-- Table structure for table users
CREATE TABLE users (
id INT UNSIGNED NOT NULL AUTO_INCREMENT,
username VARCHAR(125) DEFAULT NULL,
password VARCHAR(125) DEFAULT NULL,
fullname VARCHAR(125) DEFAULT NULL,
role ENUM('ADMIN', 'TRADER') DEFAULT NULL,
PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Table containing all users';

-- Password's admin : Poseidon@0
-- Password's user : Jelly*4

INSERT users (username, password, fullname, role) VALUES
	 ('poseidon','$2y$10$XgJUMSVrwQTTsMaW9LayreyzVpIyx.t4tRqwJmURSfB0nslHLVKH6','Poseidon Administration','admin'),
	 ('jelly','$2y$10$d4xPXdA0BN3r30VXjVSqGe5Kh0k6vlHKzgUPJNkw0fQJxereY2KrG','Jelly Fish','user');