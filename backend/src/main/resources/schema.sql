DROP TABLE IF EXISTS sys_log;
DROP TABLE IF EXISTS busi_reservation;
DROP TABLE IF EXISTS busi_classroom;
DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  nickname VARCHAR(50),
  role VARCHAR(20) NOT NULL,
  email VARCHAR(100),
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE busi_classroom (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  room_number VARCHAR(20) UNIQUE NOT NULL,
  capacity INT NOT NULL,
  equipment VARCHAR(200),
  status TINYINT DEFAULT 1
);

CREATE TABLE busi_reservation (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  classroom_id BIGINT NOT NULL,
  reservation_date DATE NOT NULL,
  slot_start INT NOT NULL,
  slot_end INT NOT NULL,
  purpose VARCHAR(255) NOT NULL,
  status TINYINT DEFAULT 0,
  reject_reason VARCHAR(255),
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
  CONSTRAINT fk_reservation_classroom FOREIGN KEY (classroom_id) REFERENCES busi_classroom(id),
  INDEX idx_conflict_lock (classroom_id, reservation_date, status, slot_start, slot_end)
);

CREATE TABLE sys_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50),
  operation VARCHAR(80),
  method VARCHAR(160),
  params TEXT,
  ip VARCHAR(64),
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
