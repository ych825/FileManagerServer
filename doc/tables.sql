/*
 Navicat Premium Data Transfer

 Source Server         : 本地MySQL
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : tables

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 19/11/2019 17:18:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `type` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `size` bigint(20) NULL DEFAULT NULL,
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `create_date` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_info
-- ----------------------------
INSERT INTO `file_info` VALUES ('51feab0b-769c-4168-84e0-9f2e52697c5a', '8VBFLHBH9MFLCW6O{T]Q89B', 'jpg', 110473, 'D:/FileCenter/20191119/', 'FYtMLUUjxY27HxXDlckcAfbxLP6w0lrJaQJBE3Fif5DiXZqGQfwLzW+WsJCjkbn6YyeqCHvju2WF4iboD98tZ9uhD9zE97GkiQWtfXvZAeEUDXZ/MqdhKwOaOqDYAl98ZRJ087mImxkaZ9Qoa6+D6fa6GtSDqsm8M9iyvsU9Nh3O9e+lfUXRVBsDR/xSXeCpQanI5yHZEz5/FpiA88QldFt1qLtVwUGJUdrijs7tyV5PnkacKmXP/9fGVaCfZGzkE79bt3xwDBzZYpdW3errgjZYefJKagF5NCTDjcnI+lsOcZurU0leCBq+qNWKnRkuoqnj0QdUI81uSM49vN5ARw==', '2019-11-19 17:18:01');

SET FOREIGN_KEY_CHECKS = 1;
