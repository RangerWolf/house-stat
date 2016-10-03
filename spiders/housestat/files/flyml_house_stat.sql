/*
Navicat MySQL Data Transfer

Source Server         : SkyAid-Dev
Source Server Version : 50628
Source Host           : 10.64.34.44:3306
Source Database       : flyml_house_stat

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2016-10-03 09:17:56
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
) ENGINE=MyISAM AUTO_INCREMENT=149 DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM AUTO_INCREMENT=149 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for njhouse_yearly_deal_stat
-- ----------------------------
DROP TABLE IF EXISTS `njhouse_yearly_deal_stat`;
CREATE TABLE `njhouse_yearly_deal_stat` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `district` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL COMMENT '住宅/办公/商业/其他',
  `type` varchar(255) DEFAULT NULL COMMENT '商品房/二手房/租赁/抵押/抵押注销',
  `column` varchar(255) DEFAULT NULL COMMENT '列值名称',
  `value` int(11) DEFAULT NULL COMMENT '列的值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
