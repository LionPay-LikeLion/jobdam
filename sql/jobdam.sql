-- LookUp 테이블 설계
CREATE TABLE member_type_code (
  member_type_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30)
);

CREATE TABLE role_code (
  role_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30)
);

CREATE TABLE admin_status_code (
  admin_status_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL
);

CREATE TABLE subscription_status_code (
  subscription_status_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL
);

CREATE TABLE subscription_level_code (
  subscription_level_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30)
);

CREATE TABLE community_member_role_code (
  community_member_role_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL
);

CREATE TABLE board_type_code (
  board_type_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL
);

CREATE TABLE board_status_code (
  board_status_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL
);

CREATE TABLE post_type_code (
  post_type_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL
);

CREATE TABLE payment_type_code (
  payment_type_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(30) NOT NULL UNIQUE,
  name VARCHAR(50) NOT NULL
);

CREATE TABLE payment_status_code (
  payment_status_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL
);

CREATE TABLE ai_feedback_type_code (
  ai_feedback_type_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(30) NOT NULL UNIQUE,
  name VARCHAR(50) NOT NULL
);

CREATE TABLE report_type_code (
  report_type_code_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(30) NOT NULL
);

INSERT INTO member_type_code (code, name) VALUES
  ('GENERAL', '일반회원'),
  ('HUNTER', '중개자'),
  ('EMPLOYEE', '실무자');

INSERT INTO role_code (code, name) VALUES
  ('USER', '일반유저'),
  ('ADMIN', '관리자');

INSERT INTO admin_status_code (code, name) VALUES
  ('PENDING', '대기'),
  ('APPROVED', '승인'),
  ('REJECTED', '거절');

INSERT INTO subscription_status_code (code, name) VALUES
  ('ACTIVE', '활성'),
  ('EXPIRED', '만료'),
  ('CANCELLED', '취소');

INSERT INTO subscription_level_code (code, name) VALUES
  ('BASIC', '기본'),
  ('PREMIUM', '프리미엄');

INSERT INTO community_member_role_code (code, name) VALUES
  ('OWNER', '운영자'),
  ('MEMBER', '일반멤버');

INSERT INTO board_type_code (code, name) VALUES
  ('GENERAL', '일반'),
  ('NOTICE', '공지'),
  ('QNA', '질문'),
  ('ANNOUNCEMENT', '알림'),
  ('FEEDBACK', '피드백');

INSERT INTO board_status_code (code, name) VALUES
  ('ACTIVE', '활성'),
  ('DELETED', '삭제됨');

INSERT INTO post_type_code (code, name) VALUES
  ('NORMAL', '일반'),
  ('NOTICE', '공지'),
  ('PINNED', '상단고정');

INSERT INTO payment_type_code (code, name) VALUES
  ('RECHARGE', '포인트 충전'),
  ('COMMUNITY_ENTRY', '커뮤니티 입장'),
  ('USER_GRADE_UPGRADE', '유저 등급 업그레이드'),
  ('COMMUNITY_GRADE_UPGRADE', '커뮤니티 등급 업그레이드'),
  ('REFUND', '환불');

INSERT INTO payment_status_code (code, name) VALUES
  ('SUCCESS', '성공'),
  ('FAILED', '실패'),
  ('CANCELLED', '취소');

INSERT INTO ai_feedback_type_code (code, name) VALUES
  ('RESUME', '이력서'),
  ('COVER_LETTER', '자기소개서'),
  ('PORTFOLIO', '포트폴리오');

INSERT INTO report_type_code (code, name) VALUES
  ('POST', '게시글 신고'),
  ('COMMENT', '댓글 신고'),
  ('USER', '유저 신고');


-- 1. user
CREATE TABLE user (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nickname VARCHAR(50) NOT NULL,
  subscription_level_code_id INT NOT NULL DEFAULT 1,  
  role_code_id INT NOT NULL DEFAULT 1,
  member_type_code_id INT NOT NULL DEFAULT 1,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  point INT DEFAULT 0,
  phone VARCHAR(20),
  profile_image_url VARCHAR(255),
  provider_id VARCHAR(255) DEFAULT NULL COMMENT 'OAuth provider unique ID (e.g., Google sub)',
  provider_type VARCHAR(50) DEFAULT NULL COMMENT 'OAuth provider name (e.g., GOOGLE, KAKAO, LOCAL)',
  email_verified BOOLEAN DEFAULT FALSE COMMENT 'Whether the user\'s email is verified',
  FOREIGN KEY (subscription_level_code_id) REFERENCES subscription_level_code(subscription_level_code_id),
  FOREIGN KEY (role_code_id) REFERENCES role_code(role_code_id),
  FOREIGN KEY (member_type_code_id) REFERENCES member_type_code(member_type_code_id)
);



-- 2. membership_change
CREATE TABLE membertype_change (
  membertype_change_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  current_member_type_code_id INT NOT NULL,        
  requested_member_type_code_id INT NOT NULL,  
  request_admin_status_code_id INT NOT NULL DEFAULT 1,
  requested_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  processed_at DATETIME,
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (current_member_type_code_id) REFERENCES member_type_code(member_type_code_id),
  FOREIGN KEY (requested_member_type_code_id) REFERENCES member_type_code(member_type_code_id),
  FOREIGN KEY (request_admin_status_code_id) REFERENCES admin_status_code(admin_status_code_id)
);




-- 3. user_subscription
CREATE TABLE user_subscription (
  user_subscription_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  subscription_level_code_id INT NOT NULL, 
  paid_point INT NOT NULL,
  start_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  end_date DATETIME,
  subscription_status_code_id INT NOT NULL DEFAULT 1,
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (subscription_level_code_id) REFERENCES subscription_level_code(subscription_level_code_id),
  FOREIGN KEY (subscription_status_code_id) REFERENCES subscription_status_code(subscription_status_code_id)
);

CREATE TABLE community (
  community_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  subscription_level_code_id INT NOT NULL DEFAULT 1,    
  user_id INT NOT NULL,
  enter_point INT DEFAULT 0,
  max_member INT DEFAULT 30,
  current_member INT DEFAULT 0,
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (subscription_level_code_id) REFERENCES subscription_level_code(subscription_level_code_id)
);


-- 5. community_subscription
CREATE TABLE community_subscription (
  community_subscription_id INT AUTO_INCREMENT PRIMARY KEY,
  community_id INT NOT NULL,
  subscription_level_code_id INT NOT NULL,
  paid_point INT NOT NULL,      
  start_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  end_date DATETIME,
  subscription_status_code_id INT NOT NULL DEFAULT 1,
  FOREIGN KEY (community_id) REFERENCES community(community_id),
  FOREIGN KEY (subscription_level_code_id) REFERENCES subscription_level_code(subscription_level_code_id),
  FOREIGN KEY (subscription_status_code_id) REFERENCES subscription_status_code(subscription_status_code_id)
);




-- 6. community_member
CREATE TABLE community_member (
  community_member_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  community_id INT NOT NULL,
  community_member_role_code_id INT NOT NULL DEFAULT 2,
  paid_point INT DEFAULT 0,
  joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (community_id) REFERENCES community(community_id),
  FOREIGN KEY (community_member_role_code_id) REFERENCES community_member_role_code(community_member_role_code_id)
);



-- 7. community_board
CREATE TABLE community_board (
  community_board_id INT AUTO_INCREMENT PRIMARY KEY,
  community_id INT NOT NULL,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(255),
  board_type_code_id INT NOT NULL DEFAULT 1,  
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  board_status_code_id INT NOT NULL DEFAULT 1,      
  FOREIGN KEY (community_id) REFERENCES community(community_id),
  FOREIGN KEY (board_type_code_id) REFERENCES board_type_code(board_type_code_id),
  FOREIGN KEY (board_status_code_id) REFERENCES board_status_code(board_status_code_id)
);





-- 8. community_post
CREATE TABLE community_post (
  community_post_id INT AUTO_INCREMENT PRIMARY KEY,
  community_board_id INT NOT NULL,
  user_id INT NOT NULL,
  title VARCHAR(100) NOT NULL,
  content TEXT NOT NULL,
  post_type_code_id INT NOT NULL DEFAULT 1,  
  view_count INT DEFAULT 0,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  board_status_code_id INT NOT NULL DEFAULT 1, 
  FOREIGN KEY (community_board_id) REFERENCES community_board(community_board_id),
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (board_status_code_id) REFERENCES board_status_code(board_status_code_id),
  FOREIGN KEY (post_type_code_id) REFERENCES post_type_code(post_type_code_id)
);



-- 9. community_comment
CREATE TABLE community_comment (
  community_comment_id INT AUTO_INCREMENT PRIMARY KEY,
  community_post_id INT NOT NULL,
  user_id INT NOT NULL,
  content TEXT NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  board_status_code_id INT NOT NULL DEFAULT 1, 
  FOREIGN KEY (community_post_id) REFERENCES community_post(community_post_id),
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (board_status_code_id) REFERENCES board_status_code(board_status_code_id)
);


-- 10. sns_post
CREATE TABLE sns_post (
  sns_post_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  title VARCHAR(100) NOT NULL,
  content TEXT NOT NULL,
  image_url VARCHAR(255),
  attachment_url VARCHAR(255),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(user_id)
);


-- 11. sns_comment
CREATE TABLE sns_comment (
  sns_comment_id INT AUTO_INCREMENT PRIMARY KEY,
  sns_post_id INT NOT NULL,
  user_id INT NOT NULL,
  content TEXT NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (sns_post_id) REFERENCES sns_post(sns_post_id),
  FOREIGN KEY (user_id) REFERENCES user(user_id)
);


-- 12. bookmark
CREATE TABLE bookmark (
  bookmark_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  sns_post_id INT NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (user_id, sns_post_id),
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (sns_post_id) REFERENCES sns_post(sns_post_id)
);

-- 13. `like`
CREATE TABLE `like` (
  like_id INT AUTO_INCREMENT PRIMARY KEY,
  sns_post_id INT NOT NULL,
  user_id INT NOT NULL,
  UNIQUE (sns_post_id, user_id),
  FOREIGN KEY (sns_post_id) REFERENCES sns_post(sns_post_id),
  FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- 14. payment
CREATE TABLE payment (
  payment_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  point INT NOT NULL,  -- 포인트 변화량 (+충전, -소모)
  amount INT DEFAULT 0,  -- 실제 결제 금액 (카드 등 실제 돈)
  payment_type_code_id INT NOT NULL, 
  payment_status_code_id INT NOT NULL DEFAULT 1,
  method VARCHAR(50) NOT NULL COMMENT '결제 수단 (예: CARD, POINT, SYSTEM 등)',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  imp_uid VARCHAR(100) UNIQUE,
  merchant_uid VARCHAR(100) UNIQUE,
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (payment_type_code_id) REFERENCES payment_type_code(payment_type_code_id),
  FOREIGN KEY (payment_status_code_id) REFERENCES payment_status_code(payment_status_code_id)
);



-- 15. ai_feedback
CREATE TABLE ai_feedback (
  ai_feedback_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  ai_feedback_type_code_id INT NOT NULL,    
  input_text TEXT NOT NULL,
  output_text TEXT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,            
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (ai_feedback_type_code_id) REFERENCES ai_feedback_type_code(ai_feedback_type_code_id)
);




-- 16. message
CREATE TABLE message (
  message_id INT AUTO_INCREMENT PRIMARY KEY,
  sender_user_id INT NOT NULL,
  receiver_user_id INT NOT NULL,
  content TEXT NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (sender_user_id) REFERENCES user(user_id),
  FOREIGN KEY (receiver_user_id) REFERENCES user(user_id)
);


-- 17. report
CREATE TABLE report (
  report_id INT AUTO_INCREMENT PRIMARY KEY,
  report_type_code_id INT NOT NULL,    
  target_id BIGINT NOT NULL,
  user_id INT NOT NULL,
  reason TEXT NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(user_id),
  FOREIGN KEY (report_type_code_id) REFERENCES report_type_code(report_type_code_id)
);

