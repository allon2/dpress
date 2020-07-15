
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;



-- ----------------------------
-- Table structure for attachments
-- ----------------------------
DROP TABLE IF EXISTS attachments;
CREATE TABLE attachments  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  file_key varchar(2047)   NULL DEFAULT NULL,
  height int(11) NULL DEFAULT 0,
  media_type varchar(127)   NOT NULL,
  name varchar(255)   NOT NULL,
  path varchar(1023)   NULL DEFAULT NULL,
  size bigint(20) NULL DEFAULT NULL,
  suffix varchar(50)   NULL DEFAULT NULL,
  thumb_path varchar(1023)   NULL DEFAULT NULL,
  type int(11) NULL DEFAULT 0,
  width int(11) NULL DEFAULT 0,
  siteid bigint(20) NULL DEFAULT NULL,
  content longblob NULL,
  thumbnailcontent longblob NULL,
  PRIMARY KEY (id) USING BTREE
) ;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS categories;
CREATE TABLE categories  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  description varchar(100)   NULL DEFAULT NULL,
  name varchar(255)   NOT NULL,
  parent_id int(11) NULL DEFAULT 0,
  slug varchar(255)   NOT NULL,
  slug_name varchar(50)   NULL DEFAULT NULL,
  thumbnail varchar(1023)   NULL DEFAULT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX UK_oul14ho7bctbefv8jywp5v3i2(slug,siteid) USING BTREE,
  INDEX categories_name(name) USING BTREE,
  INDEX categories_parent_id(parent_id) USING BTREE
) ;

-- ----------------------------
-- Table structure for comment_black_list
-- ----------------------------
DROP TABLE IF EXISTS comment_black_list;
CREATE TABLE comment_black_list  (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  ban_time datetime(6) NULL DEFAULT NULL,
  ip_address varchar(127)   NOT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE
);

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS comments;
CREATE TABLE comments  (
  type int(11) NOT NULL DEFAULT 0,
  id bigint(20) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  allow_notification bit(1) NULL DEFAULT b'1',
  author varchar(50)   NOT NULL,
  author_url varchar(511)   NULL DEFAULT NULL,
  content varchar(1023)   NOT NULL,
  email varchar(255)   NOT NULL,
  gravatar_md5 varchar(127)   NULL DEFAULT NULL,
  ip_address varchar(127)   NULL DEFAULT NULL,
  is_admin bit(1) NULL DEFAULT b'0',
  parent_id bigint(20) NULL DEFAULT 0,
  post_id int(11) NOT NULL,
  status int(11) NULL DEFAULT 1,
  top_priority int(11) NULL DEFAULT 0,
  user_agent varchar(511)   NULL DEFAULT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE,
  INDEX comments_post_id(post_id,siteid) USING BTREE,
  INDEX comments_type_status(type, status) USING BTREE,
  INDEX comments_parent_id(parent_id) USING BTREE
) ;

-- ----------------------------
-- Table structure for dpress_siteinfo
-- ----------------------------
DROP TABLE IF EXISTS dpress_siteinfo;
CREATE TABLE dpress_siteinfo  (
  id bigint(20) NULL DEFAULT NULL,
  domain varchar(255)   NULL DEFAULT NULL,
  sitename varchar(255)   NULL DEFAULT NULL,
  isDefault varchar(1)   NULL DEFAULT NULL COMMENT '0:default 1非默认值'
) ;

-- ----------------------------
-- Table structure for dpress_template
-- ----------------------------
DROP TABLE IF EXISTS dpress_template;
CREATE TABLE dpress_template  (
  variable text   NULL COMMENT 'json格式，key为返回值 value为执行方法',
  content longtext   NULL,
  bcontent longblob NULL,
  lastModified timestamp(0) NULL DEFAULT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  encoding varchar(255)   NULL DEFAULT NULL,
   mediatype varchar(255)   NULL DEFAULT NULL,
path varchar(255)   NULL DEFAULT NULL,
  cifseq bigint(255) NULL DEFAULT NULL,
  theme varchar(255)   NULL DEFAULT NULL
) ;
DROP TABLE IF EXISTS system_themes;
CREATE TABLE system_themes  (
  variable text  NULL COMMENT 'json格式，key为返回值 value为执行方法',
  content longtext  NULL,
  bcontent longblob NULL,
  encoding varchar(255)  NULL DEFAULT NULL,
  mediatype varchar(255)   NULL DEFAULT NULL,
  path varchar(255)   NULL DEFAULT NULL,
  theme varchar(255)   NULL DEFAULT NULL
);

-- ----------------------------
-- Table structure for journals
-- ----------------------------
DROP TABLE IF EXISTS journals;
CREATE TABLE journals  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  content text   NOT NULL,
  likes bigint(20) NULL DEFAULT 0,
  source_content longtext   NOT NULL,
  type int(11) NULL DEFAULT 1,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE
) ;

-- ----------------------------
-- Table structure for links
-- ----------------------------
DROP TABLE IF EXISTS links;
CREATE TABLE links  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  description varchar(255)   NULL DEFAULT NULL,
  logo varchar(1023)   NULL DEFAULT NULL,
  name varchar(255)   NOT NULL,
  priority int(11) NULL DEFAULT 0,
  team varchar(255)   NULL DEFAULT NULL,
  url varchar(1023)   NOT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE,
  INDEX links_name(name) USING BTREE
) ;

-- ----------------------------
-- Table structure for logs
-- ----------------------------
DROP TABLE IF EXISTS logs;
CREATE TABLE logs  (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  content varchar(1023)   NOT NULL,
  ip_address varchar(127)   NULL DEFAULT NULL,
  log_key varchar(1023)   NULL DEFAULT NULL,
  type int(11) NOT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE,
  INDEX logs_create_time(create_time) USING BTREE
) ;

-- ----------------------------
-- Table structure for menus
-- ----------------------------
DROP TABLE IF EXISTS menus;
CREATE TABLE menus  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  icon varchar(50)   NULL DEFAULT NULL,
  name varchar(50)   NOT NULL,
  parent_id int(11) NULL DEFAULT 0,
  priority int(11) NULL DEFAULT 0,
  target varchar(20)   NULL DEFAULT '_self',
  team varchar(255)   NULL DEFAULT NULL,
  url varchar(1023)   NOT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE,
  INDEX menus_parent_id(parent_id) USING BTREE
) ;

-- ----------------------------
-- Table structure for metas
-- ----------------------------
DROP TABLE IF EXISTS metas;
CREATE TABLE metas  (
  type int(11) NOT NULL DEFAULT 0,
  id bigint(20) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  meta_key varchar(255)   NOT NULL,
  post_id int(11) NOT NULL,
  meta_value varchar(1023)   NOT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE
) ;

-- ----------------------------
-- Table structure for options
-- ----------------------------
DROP TABLE IF EXISTS options;
CREATE TABLE options  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  option_key varchar(100)   NOT NULL,
  type int(11) NULL DEFAULT 0,
  option_value longtext   NOT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE
) ;

-- ----------------------------
-- Table structure for photos
-- ----------------------------
DROP TABLE IF EXISTS photos;
CREATE TABLE photos  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  description varchar(255)   NULL DEFAULT NULL,
  location varchar(255)   NULL DEFAULT NULL,
  name varchar(255)   NOT NULL,
  take_time datetime(6) NULL DEFAULT NULL,
  team varchar(255)   NULL DEFAULT NULL,
  thumbnail varchar(1023)   NULL DEFAULT NULL,
  url varchar(1023)   NOT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE,
  INDEX photos_team(team) USING BTREE
) ;

-- ----------------------------
-- Table structure for post_categories
-- ----------------------------
DROP TABLE IF EXISTS post_categories;
CREATE TABLE post_categories  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  category_id int(11) NULL DEFAULT NULL,
  post_id int(11) NULL DEFAULT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE,
  INDEX post_categories_post_id(post_id) USING BTREE,
  INDEX post_categories_category_id(category_id) USING BTREE
) ;

-- ----------------------------
-- Table structure for post_tags
-- ----------------------------
DROP TABLE IF EXISTS post_tags;
CREATE TABLE post_tags  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  post_id int(11) NOT NULL,
  tag_id int(11) NOT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE,
  INDEX post_tags_post_id(post_id) USING BTREE,
  INDEX post_tags_tag_id(tag_id) USING BTREE
) ;

-- ----------------------------
-- Table structure for posts
-- ----------------------------
DROP TABLE IF EXISTS posts;
CREATE TABLE posts  (
  type int(11) NOT NULL DEFAULT 0,
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  disallow_comment bit(1) NULL DEFAULT b'0',
  edit_time datetime(6) NULL DEFAULT NULL,
  editor_type int(11) NULL DEFAULT 0,
  format_content longtext   NULL,
  likes bigint(20) NULL DEFAULT 0,
  meta_description varchar(1023)   NULL DEFAULT NULL,
  meta_keywords varchar(511)   NULL DEFAULT NULL,
  original_content longtext   NOT NULL,
  password varchar(255)   NULL DEFAULT NULL,
  slug varchar(255)   NOT NULL,
  status int(11) NULL DEFAULT 1,
  summary longtext   NULL,
  template varchar(255)   NULL DEFAULT NULL,
  thumbnail varchar(1023)   NULL DEFAULT NULL,
  title varchar(255)   NOT NULL,
  top_priority int(11) NULL DEFAULT 0,
  url varchar(255)   NULL DEFAULT NULL,
  visits bigint(20) NULL DEFAULT 0,
  siteid bigint(20) NULL DEFAULT NULL,
  baidudate date NULL DEFAULT NULL,
   ourl varchar(1024)  NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX UK_qmmso8qxjpbxwegdtp0l90390(slug,siteid) USING BTREE,
  INDEX posts_type_status_siteid(siteid,type, status) USING BTREE,
  INDEX posts_create_time(create_time) USING BTREE,
  INDEX posts_siteid_priority_create_time(siteid,top_priority,create_time) USING BTREE,
   INDEX posts_index(type, id, create_time, status, top_priority, siteid) USING BTREE
) ;

-- ----------------------------
-- Table structure for tags
-- ----------------------------
DROP TABLE IF EXISTS tags;
CREATE TABLE tags  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  name varchar(255)   NOT NULL,
  slug varchar(50)   NOT NULL,
  slug_name varchar(255)   NULL DEFAULT NULL,
  thumbnail varchar(1023)   NULL DEFAULT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX UK_sn0d91hxu700qcw0n4pebp5vc(slug,siteid) USING BTREE,
  INDEX tags_name(name) USING BTREE
);

-- ----------------------------
-- Table structure for theme_settings
-- ----------------------------
DROP TABLE IF EXISTS theme_settings;
CREATE TABLE theme_settings  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  setting_key varchar(255)   NOT NULL,
  theme_id varchar(255)   NOT NULL,
  setting_value longtext   NOT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE,
  INDEX theme_settings_setting_key(setting_key) USING BTREE,
  INDEX theme_settings_theme_id(theme_id) USING BTREE
) ;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS users;
CREATE TABLE users  (
  id int(11) NOT NULL AUTO_INCREMENT,
  create_time datetime(6) NULL DEFAULT NULL,
  update_time datetime(6) NULL DEFAULT NULL,
  avatar varchar(1023)   NULL DEFAULT NULL,
  description varchar(1023)   NULL DEFAULT NULL,
  email varchar(127)   NULL DEFAULT NULL,
  expire_time datetime(6) NULL DEFAULT NULL,
  nickname varchar(255)   NOT NULL,
  password varchar(255)   NOT NULL,
  username varchar(50)   NOT NULL,
  siteid bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE
) ;

SET FOREIGN_KEY_CHECKS = 1;
