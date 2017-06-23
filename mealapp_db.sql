/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : mealapp_db

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2017-06-08 15:17:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admins`
-- ----------------------------
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginid` varchar(255) DEFAULT NULL,
  `passwords` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admins
-- ----------------------------
INSERT INTO `admins` VALUES ('1', 'admin', 'lxy');

-- ----------------------------
-- Table structure for `comments`
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dishesid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL COMMENT '回复人ID',
  `username` text COMMENT '回复人姓名',
  `body` text COMMENT '回复内容',
  `createtime` varchar(20) DEFAULT NULL COMMENT '回复时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES ('24', '4', '1', '小马', '好', '2017-04-13 01:43:47');
INSERT INTO `comments` VALUES ('25', '2', '1', '小马', '不错', '2017-04-13 03:09:38');
INSERT INTO `comments` VALUES ('26', '2', '1', '小马', '厉害', '2017-04-13 03:13:11');
INSERT INTO `comments` VALUES ('27', '4', '8', 'LXY', 'good', '2017-04-20 13:51:26');

-- ----------------------------
-- Table structure for `dishes`
-- ----------------------------
DROP TABLE IF EXISTS `dishes`;
CREATE TABLE `dishes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` text COMMENT '标题',
  `businessid` int(11) DEFAULT NULL COMMENT '类型ID',
  `typename` text COMMENT '类型名称',
  `intro` text COMMENT '简介',
  `img_url` varchar(255) DEFAULT NULL COMMENT '封面图片名称',
  `price` double(11,1) DEFAULT NULL COMMENT '单价',
  `amount` double(11,1) DEFAULT NULL COMMENT '数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dishes
-- ----------------------------
INSERT INTO `dishes` VALUES ('1', '扣肉饭美食', '2', '', '随点提供金牌五香扣肉饭美食在线订购,支持金牌五香扣肉饭网上订餐', '14ababcc-45ca-48dc-a8d6-5b87b5713616.jpg', '12.0', '100.0');
INSERT INTO `dishes` VALUES ('2', '名悦精致私房火锅', '2', '', '名悦精致私房火锅对食材精益求精的追求,满足您挑剔的味觉需求,我们的每一位顾客都会忍不住赞叹这里选材的精良', '957d6efa3bfb7ed2aa55997dd97459e2147480.jpg', '12.0', '100.0');
INSERT INTO `dishes` VALUES ('3', '糖醋排骨', '2', '', '原料:小排、料酒、生抽、老抽、香醋、糖、盐、味精、芝麻。', '14ababcc-45ca-48dc-a8d6-5b87b571361611.jpg', '12.0', '100.0');
INSERT INTO `dishes` VALUES ('4', '可乐鸡翅', '5', '', '排骨饭是砂锅饭里的代表作，也是“先煮饭后放料”煮法的代表。这种煮法最适合排骨类红肉有荤有素的搭配，而且最好多烧出些汁儿。因为七八成熟的米饭米粒之间空隙大，汁能很快渗入进味。', '957d6efa3bfb7ed2aa4655997dd97459e2147480.jpg', '12.0', '100.0');
INSERT INTO `dishes` VALUES ('5', '【三道工序轻松做人气美食】辣子鸡', '3', '', '原料:鸡肉、干辣椒、蒜、姜、花生米、八角、花椒、盐、生抽、五香粉', '957d6efa243bfb7ed2aa55997dd97459e2147480.jpg', '12.0', '100.0');
INSERT INTO `dishes` VALUES ('10', '\r\n窑鸡（原味）', '2', null, '窑鸡是一道美味可口的广东客家名菜，流行于深圳、惠州、河源、梅州、赣州一带。做法类似于盐焗鸡', '957d6efa3bfb7ed2aa4655997dd89459e2147480.jpg', '2.0', '100.0');
INSERT INTO `dishes` VALUES ('11', '窑鸡+盐焗鸡脆骨', '3', null, '是有名的“农家乐”菜之一。 它的做法是把鸡宰杀洗净后把掏空腹腔，塞入蒜头、香菇等佐料，鸡身内外都抹满精盐，再用锡箔纸严严实实包好。', '14ababcc-45ca-765dc-a8d6-5b87b5713616.jpg', '4.0', '100.0');
INSERT INTO `dishes` VALUES ('12', '\r\n牛肉串', '2', null, '牛肉串是一种美食，有多种烹饪方法，如鲜果牛肉串、蚝油牛肉串、烤鲜菇牛肉串、烧烤牛肉串等。原料介绍牛肉是中国人的第二大肉类食品，仅次于猪肉，牛肉蛋白质含量高', '14ababc745ca-48dc-a8d6-5b87b571361611.jpg', '56.0', '100.0');
INSERT INTO `dishes` VALUES ('13', '\r\n鸡脆骨', '2', null, '鸡脆骨，又称掌中宝、鸡脆，它以其独特的口感而倍受食客青睐。常见的菜例有宫保鸡脆骨、椒盐鸡脆，麻辣鸡脆骨', '14ababcc-45ca-48dc-a8d6-5434b5713616.jpg', '54.0', '100.0');
INSERT INTO `dishes` VALUES ('14', '小龙虾', '3', null, '别名：丰本、草钟乳、起阳草、懒人菜、长生韭、壮阳草、扁菜等；属百合科多年生草本植物，具特殊强烈气味，根茎横卧', '14ababcc-45ca-48dc-a8d6-734b5713616.jpg', '32.0', '100.0');
INSERT INTO `dishes` VALUES ('15', '大生蚝', '3', null, '你来比比你们那的生蚝给楼主看看有多大', '14ababcc-45ca-765dc-a8d6-5b87b5713616.jpg', '23.0', '100.0');
INSERT INTO `dishes` VALUES ('16', '多春鱼', '4', null, '多春鱼是一道名菜，以多春鱼为主要食材的私房菜，肉质却特别嫩滑，表面金黄，鱼骨已经煎酥，配上爽口的鱼酱，味道十分鲜美。多春鱼的营养价值非常高', '14ababcc-45ca-48dc-a8d6-5b87713616.jpg', '45.0', '100.0');
INSERT INTO `dishes` VALUES ('17', '\r\n烤韭菜', null, null, '烧烤韭菜是在南方时兴起来的一种美食，韭菜有强肾壮阳之功效，口感鲜香爽口，不仅是男人的下酒健身的好菜，也是女孩子们喜欢吃的一种小吃，在南方比较常见', '14ababc745ca-48dc-a8d6-5b87b571361611.jpg', '65.0', '100.0');
INSERT INTO `dishes` VALUES ('18', '掌中宝', '4', null, '掌中宝半打', '14ababcc-45ca-48dc-a8d6-5b5713616.jpg', '45.0', '100.0');
INSERT INTO `dishes` VALUES ('19', '\r\n岌炭烤豆腐串', '4', null, '吃烧烤的各位,可能都知道有一个烤豆腐串很是抢味好食,但每次怕卫生问题,又让人不敢大胆食用', '957d6efa3bfb7ed2aa55997dd97459e2184802.jpg', '54.0', '100.0');

-- ----------------------------
-- Table structure for `orders`
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `username` text COMMENT '下单人名称',
  `phone` text COMMENT '座位',
  `addr` varchar(255) DEFAULT NULL,
  `status` text COMMENT '状态 0 进行中 1 已完成 -1 已取消',
  `dishesid` int(11) DEFAULT NULL COMMENT '菜式ID',
  `price` double(10,0) DEFAULT NULL COMMENT '单价',
  `amount` double(10,0) DEFAULT NULL,
  `createtime` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES ('12', '1', '小宇', '15676559808', '健翔桥学一公寓', '已配送', '2', '12', '2', '2017-04-13 01:09:59');
INSERT INTO `orders` VALUES ('15', '8', 'LXY', '89076655', 'BISTU', '已配送', '4', '12', '2', '2017-04-20 13:46:36');
INSERT INTO `orders` VALUES ('16', '1', '比斯兔', '123456789011', '健翔桥学三公寓', '待接单', '14', '32', '4', '2017-04-24 21:37:10');
INSERT INTO `orders` VALUES ('17', '1', '比斯兔', '123456789011', '健翔桥学三公寓', '待接单', '19', '54', '2', '2017-04-26 18:02:31');
INSERT INTO `orders` VALUES ('18', '1', '比斯兔', '123456789011', '健翔桥学三公寓', '待接单', '1', '12', '1', '2017-05-20 23:14:58');
INSERT INTO `orders` VALUES ('19', '1', '比斯兔', '123456789011', '健翔桥学三公寓', '待接单', '14', '32', '1', '2017-06-08 11:50:04');

-- ----------------------------
-- Table structure for `tb_addrs`
-- ----------------------------
DROP TABLE IF EXISTS `tb_addrs`;
CREATE TABLE `tb_addrs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `addr` text,
  `person` text,
  `isdefault` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_addrs
-- ----------------------------
INSERT INTO `tb_addrs` VALUES ('17', '1', '1234567890', '北京信息科技大学健翔桥学三公寓', '比斯兔', '0');
INSERT INTO `tb_addrs` VALUES ('18', '9', '12424', '345', 'q', '0');

-- ----------------------------
-- Table structure for `tb_collect`
-- ----------------------------
DROP TABLE IF EXISTS `tb_collect`;
CREATE TABLE `tb_collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `createtime` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_collect
-- ----------------------------
INSERT INTO `tb_collect` VALUES ('8', '2', '1', '2017-04-13 01:32:21');
INSERT INTO `tb_collect` VALUES ('10', '5', '1', '2017-04-13 01:43:57');
INSERT INTO `tb_collect` VALUES ('11', '6', '1', '2017-04-13 03:12:53');
INSERT INTO `tb_collect` VALUES ('12', '5', '8', '2017-04-20 13:47:54');

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginid` varchar(255) DEFAULT NULL,
  `name` text,
  `passwords` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `addr` varchar(255) DEFAULT NULL,
  `typename` varchar(255) DEFAULT NULL,
  `isenable` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '1001', '比斯兔', '123456', '123456789011', '健翔桥学三公寓', '用户', '1');
INSERT INTO `users` VALUES ('2', '2001', '一层食堂', '123456', '15676559876', '健翔桥食堂一层', '商家', '1');
INSERT INTO `users` VALUES ('3', '2002', '二层食堂', '123456', '15676559876', '健翔桥食堂一层', '商家', '1');
INSERT INTO `users` VALUES ('4', '2003', '清真食堂', '123456', '12345678', '健翔桥食堂一层', '商家', '1');
INSERT INTO `users` VALUES ('8', '1002', '小雨', '123456', '89076655', '健翔桥学二公寓', '用户', '1');
INSERT INTO `users` VALUES ('9', '1002', 'q', '123', '12345', '123', '用户', '1');
