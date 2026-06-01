INSERT INTO sys_user(id, username, password, nickname, role, email) VALUES
(1, 'admin', '$2a$10$mjGLTFejMJ46IDXnL21FYeI00.G/kAWjNwwW2GMI9sPZp3FyKnrNG', '系统管理员', 'ADMIN', 'admin@example.edu'),
(2, 'student01', '$2a$10$mjGLTFejMJ46IDXnL21FYeI00.G/kAWjNwwW2GMI9sPZp3FyKnrNG', '张同学', 'USER', 'student01@example.edu');

INSERT INTO busi_classroom(id, room_number, capacity, equipment, status) VALUES
(1, '教一302', 80, '多媒体, 投影仪, 无线麦克风', 1),
(2, '教二205', 48, '白板, 投影仪', 1),
(3, '实验楼501', 36, '机房, 高性能工作站', 1),
(4, '逸夫楼101', 120, '阶梯教室, 扩声系统', 1);

INSERT INTO busi_reservation(user_id, classroom_id, reservation_date, slot_start, slot_end, purpose, status) VALUES
(2, 1, CAST(CURRENT_DATE AS DATE), 3, 4, '课程项目讨论', 1),
(2, 3, CAST(CURRENT_DATE + 1 AS DATE), 5, 6, '程序设计实验', 0);

