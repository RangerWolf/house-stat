/*
Navicat MySQL Data Transfer

Source Server         : LocalMySQL
Source Server Version : 50709
Source Host           : localhost:3306
Source Database       : flyml_webdata

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2016-10-02 20:39:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for njhouse_simple_summary
-- ----------------------------
DROP TABLE IF EXISTS `njhouse_simple_summary`;
CREATE TABLE `njhouse_simple_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `subscribe_unit` int(8) DEFAULT NULL,
  `new_pub_2nd_house_unit` int(8) DEFAULT NULL,
  `deal_unit` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for njhouse_supply_summary
-- ----------------------------
DROP TABLE IF EXISTS `njhouse_supply_summary`;
CREATE TABLE `njhouse_supply_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '当前的数据时间',
  `house_onsale_area` float(8,2) DEFAULT NULL COMMENT '全市其中住宅可售面积',
  `house_onsale_unit` int(8) DEFAULT NULL,
  `monthly_published_house_area` float(8,2) DEFAULT NULL,
  `monthly_published_house_unit` int(8) DEFAULT NULL,
  `non_house_onsale_area` float(8,2) DEFAULT NULL,
  `non_house_onsale_unit` int(8) DEFAULT NULL,
  `onsale_area` float(8,2) DEFAULT NULL,
  `onsale_unit` int(8) DEFAULT NULL,
  `total_area` float(8,2) DEFAULT NULL,
  `total_item_cnt` int(8) DEFAULT NULL,
  `total_unit` int(8) DEFAULT NULL,
  `yearly_published_area` float(8,2) DEFAULT NULL,
  `yearly_published_unit` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
