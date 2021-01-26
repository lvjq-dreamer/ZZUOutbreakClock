-- --------------------------------------------------------
-- 主机:                           保密
-- 服务器版本:                        8.0.22 - MySQL Community Server - GPL
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  10.2.0.5704
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出  表 clock.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` varchar(20) NOT NULL COMMENT '用户账号',
  `upw` varchar(20) NOT NULL COMMENT '用户疫情登录密码',
  `myvs_1` varchar(2) NOT NULL DEFAULT '否' COMMENT '1、您今天是否有发热症状',
  `myvs_2` varchar(2) NOT NULL DEFAULT '否' COMMENT '2、您今天是否有咳嗽症状',
  `myvs_3` varchar(2) NOT NULL DEFAULT '否' COMMENT '3、您今天是否有乏力或轻微乏力症状',
  `myvs_4` varchar(2) NOT NULL DEFAULT '否' COMMENT '4、您今天是否有鼻塞、流涕、咽痛或者腹泻等症状',
  `myvs_5` varchar(2) NOT NULL DEFAULT '否' COMMENT '5、您今天是否被所在地医疗机构确定为确诊病例?',
  `myvs_6` varchar(2) NOT NULL DEFAULT '否' COMMENT '6、您今天是否被所在地医疗机构确定为疑似病例?',
  `myvs_7` varchar(2) NOT NULL DEFAULT '否' COMMENT '7、您今天是否被所在地政府或医疗卫生部门确定为密切接触者?',
  `myvs_8` varchar(2) NOT NULL DEFAULT '否' COMMENT '8、您今天是否被所在地医疗机构进行院内隔离观察治疗?',
  `myvs_9` varchar(2) NOT NULL DEFAULT '否' COMMENT '9、您今天是否被要求在政府集中隔离点进行隔离观察?',
  `myvs_10` varchar(2) NOT NULL DEFAULT '否' COMMENT '10、您今天是否被要求在政府集中隔离点进行隔离观察?',
  `myvs_11` varchar(2) NOT NULL DEFAULT '否' COMMENT '11、所在小区（村）是否有确诊病例?(以当地政府公开信息为准)',
  `myvs_12` varchar(2) NOT NULL DEFAULT '否' COMMENT '12、共同居住人是否有确诊病例?',
  `myvs_13a` varchar(2) NOT NULL COMMENT '当前居住地：省份（自治区）',
  `myvs_13b` varchar(4) NOT NULL COMMENT '当前居住地：地市',
  `myvs_13c` varchar(52) NOT NULL COMMENT '当前居住地：具体',
  `myvs_14` varchar(2) NOT NULL DEFAULT '否' COMMENT '14、您是否为当日返郑人员',
  `record` varchar(1) NOT NULL DEFAULT '1' COMMENT '0 表示未打卡，1表示打卡',
  `email` varchar(30) NOT NULL COMMENT 'qq邮箱',
  `send` varchar(1) NOT NULL DEFAULT '1' COMMENT '0 表示未发送，1表示发送',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

/*!40000 ALTER TABLE `user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
