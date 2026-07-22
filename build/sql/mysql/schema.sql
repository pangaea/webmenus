CREATE DATABASE  IF NOT EXISTS `webmenus` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `webmenus`;
-- MySQL dump 10.13  Distrib 8.0.45, for Linux (x86_64)
--
-- Host: localhost    Database: webmenus
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `base_account`
--

DROP TABLE IF EXISTS `base_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_account` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `type` varchar(16) NOT NULL,
  `accept_license` varchar(1) NOT NULL,
  `allow_email` varchar(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_location`
--

DROP TABLE IF EXISTS `base_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_location` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `tax_rate` double NOT NULL,
  `timezone` varchar(48) NOT NULL,
  `phone_num` varchar(32) NOT NULL,
  `fax_num` varchar(32) NOT NULL,
  `fax_orders` varchar(1) NOT NULL,
  `email_addr` varchar(64) NOT NULL,
  `email_orders` varchar(1) NOT NULL,
  `logo` varchar(255) NOT NULL,
  `theme` varchar(36) NOT NULL,
  `exit_url` varchar(255) NOT NULL,
  `delivery_avail` varchar(1) NOT NULL,
  `pay_on_pickup` varchar(1) NOT NULL,
  `email_orders_pdf` varchar(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_patron`
--

DROP TABLE IF EXISTS `base_patron`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_patron` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `email` varchar(64) NOT NULL,
  `firstname` varchar(48) NOT NULL,
  `lastname` varchar(48) NOT NULL,
  `phone_num` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role` (`role`,`email`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base_theme`
--

DROP TABLE IF EXISTS `base_theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_theme` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `menuwidth` varchar(32) NOT NULL,
  `font` varchar(36) NOT NULL,
  `font_size` varchar(36) DEFAULT NULL,
  `bkcolor` varchar(6) NOT NULL,
  `option_text_color` varchar(6) NOT NULL,
  `titlebar_color` varchar(6) NOT NULL,
  `cat_text_color` varchar(6) NOT NULL,
  `item_text_color` varchar(6) NOT NULL,
  `itemdesc_text_color` varchar(6) NOT NULL,
  `system_text_color` varchar(6) NOT NULL,
  `template` longtext,
  `columns` int NOT NULL DEFAULT '1',
  `itemwidth` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_access`
--

DROP TABLE IF EXISTS `sys_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_access` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `view` varchar(64) DEFAULT NULL,
  `access` varchar(32) DEFAULT NULL,
  `groupid` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_addr`
--

DROP TABLE IF EXISTS `sys_addr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_addr` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `address` varchar(64) DEFAULT NULL,
  `address2` varchar(64) DEFAULT NULL,
  `address3` varchar(64) DEFAULT NULL,
  `city` varchar(64) DEFAULT NULL,
  `state` varchar(16) DEFAULT NULL,
  `zip` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_client`
--

DROP TABLE IF EXISTS `sys_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_client` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `roleid` varchar(36) DEFAULT NULL,
  `last_logged_in` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `PK_sys_client_username` (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_group`
--

DROP TABLE IF EXISTS `sys_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_group` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `description` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `description` varchar(64) DEFAULT NULL,
  `parent` varchar(36) DEFAULT NULL,
  `admin` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role_group_ref`
--

DROP TABLE IF EXISTS `sys_role_group_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_group_ref` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `roleid` varchar(36) DEFAULT NULL,
  `groupid` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `firstname` varchar(64) DEFAULT NULL,
  `lastname` varchar(64) DEFAULT NULL,
  `emailaddr` varchar(64) DEFAULT NULL,
  `phonenum` varchar(64) DEFAULT NULL,
  `show_welcome` varchar(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `w3_css`
--

DROP TABLE IF EXISTS `w3_css`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `w3_css` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `description` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `body` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `w3_js`
--

DROP TABLE IF EXISTS `w3_js`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `w3_js` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `description` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `code` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `w3_link`
--

DROP TABLE IF EXISTS `w3_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `w3_link` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `title` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `uri` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `w3_page`
--

DROP TABLE IF EXISTS `w3_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `w3_page` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `title` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `viewname` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `show_alerts` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `show_giftcerts` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `w3_page_css_list`
--

DROP TABLE IF EXISTS `w3_page_css_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `w3_page_css_list` (
  `fkey` varchar(36) NOT NULL,
  `objid` varchar(36) NOT NULL,
  `objindex` int NOT NULL,
  KEY `PK_w3_page_css_list` (`fkey`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `w3_page_js_list`
--

DROP TABLE IF EXISTS `w3_page_js_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `w3_page_js_list` (
  `fkey` varchar(36) NOT NULL,
  `objid` varchar(36) NOT NULL,
  `objindex` int NOT NULL,
  KEY `PK_w3_page_js_list` (`fkey`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `w3_page_link_list`
--

DROP TABLE IF EXISTS `w3_page_link_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `w3_page_link_list` (
  `fkey` varchar(36) NOT NULL,
  `objid` varchar(36) NOT NULL,
  `objindex` int NOT NULL,
  KEY `PK_w3_page_link_list` (`fkey`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_location_payment_method_list`
--

DROP TABLE IF EXISTS `webmenus_location_payment_method_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_location_payment_method_list` (
  `fkey` varchar(36) NOT NULL,
  `objid` varchar(36) NOT NULL,
  `objindex` int NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_menu`
--

DROP TABLE IF EXISTS `webmenus_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_menu` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `hidden` varchar(1) NOT NULL,
  `show_options` varchar(1) NOT NULL,
  `menu_index` int NOT NULL,
  `location_id` varchar(36) NOT NULL,
  `schedule_id` varchar(36) NOT NULL,
  `take_orders` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_webmenus_menu__location` (`location_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_menu_category`
--

DROP TABLE IF EXISTS `webmenus_menu_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_menu_category` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `hidden` varchar(1) NOT NULL,
  `cat_index` int NOT NULL,
  `menu_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_webmenus_menu_category__menu` (`menu_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_menu_item`
--

DROP TABLE IF EXISTS `webmenus_menu_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_menu_item` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(255) NOT NULL,
  `hidden` varchar(1) NOT NULL,
  `special_instructions` varchar(1) NOT NULL DEFAULT 'N',
  `image` varchar(255) NOT NULL,
  `item_index` int NOT NULL,
  `menu_cat_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_webmenus_menu_item__cat` (`menu_cat_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_menu_item_option`
--

DROP TABLE IF EXISTS `webmenus_menu_item_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_menu_item_option` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `type` varchar(32) NOT NULL,
  `option_index` int NOT NULL,
  `menuitem_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_webmenus_menu_item_option__menu` (`menuitem_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_menu_item_option_choice`
--

DROP TABLE IF EXISTS `webmenus_menu_item_option_choice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_menu_item_option_choice` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `price` double NOT NULL,
  `choice_index` int NOT NULL,
  `menuitem_option_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_webmenus_menu_item_option_choice__menu` (`menuitem_option_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_menu_item_size`
--

DROP TABLE IF EXISTS `webmenus_menu_item_size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_menu_item_size` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `size_desc` varchar(32) NOT NULL,
  `price` double NOT NULL,
  `size_index` int DEFAULT NULL,
  `menuitem_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_webmenus_menu_item_size__menu` (`menuitem_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_menu_order`
--

DROP TABLE IF EXISTS `webmenus_menu_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_menu_order` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `order_time` datetime NOT NULL,
  `email` varchar(64) NOT NULL,
  `subtotal` double NOT NULL,
  `tax_rate` double NOT NULL,
  `tax` double NOT NULL,
  `total` double NOT NULL,
  `location_id` varchar(36) NOT NULL,
  `delivery` varchar(1) NOT NULL,
  `delivery_info` varchar(255) NOT NULL,
  `notification_status` varchar(11) NOT NULL,
  `status` int NOT NULL DEFAULT '0',
  `payment_type` int NOT NULL DEFAULT '0',
  `payment_info` varchar(255) DEFAULT NULL,
  `invoice` varchar(64) DEFAULT NULL,
  `estimated_time` datetime DEFAULT NULL,
  `notes` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_webmenus_menu_order__location` (`location_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_menu_order_item`
--

DROP TABLE IF EXISTS `webmenus_menu_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_menu_order_item` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(255) NOT NULL,
  `options` varchar(2048) NOT NULL,
  `size_desc` varchar(32) NOT NULL,
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  `special_instructions` varchar(1024) DEFAULT NULL,
  `menuorder_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_webmenus_menu_order_item__menu` (`menuorder_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_ophours`
--

DROP TABLE IF EXISTS `webmenus_ophours`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_ophours` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `on_sunday` varchar(1) NOT NULL,
  `on_monday` varchar(1) NOT NULL,
  `on_tuesday` varchar(1) NOT NULL,
  `on_wednesday` varchar(1) NOT NULL,
  `on_thursday` varchar(1) NOT NULL,
  `on_friday` varchar(1) NOT NULL,
  `on_saturday` varchar(1) NOT NULL,
  `start_time` time NOT NULL,
  `hours` int NOT NULL,
  `minutes` int NOT NULL,
  `schedule_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_ophours_menus_list`
--

DROP TABLE IF EXISTS `webmenus_ophours_menus_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_ophours_menus_list` (
  `fkey` varchar(36) NOT NULL,
  `objid` varchar(36) NOT NULL,
  `objindex` int NOT NULL,
  KEY `PK_webmenus_ophours_menus_list` (`fkey`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_payment_method`
--

DROP TABLE IF EXISTS `webmenus_payment_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_payment_method` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `type` int NOT NULL,
  `description` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `config` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_schedule`
--

DROP TABLE IF EXISTS `webmenus_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_schedule` (
  `id` varchar(36) NOT NULL,
  `owner` varchar(36) DEFAULT NULL,
  `role` varchar(36) DEFAULT NULL,
  `author` varchar(36) DEFAULT NULL,
  `publisher` varchar(36) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `webmenus_schedule_menu_list`
--

DROP TABLE IF EXISTS `webmenus_schedule_menu_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `webmenus_schedule_menu_list` (
  `fkey` varchar(36) NOT NULL,
  `objid` varchar(36) NOT NULL,
  `objindex` int NOT NULL,
  KEY `PK_webmenus_schedule_menu_list` (`fkey`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-22  1:58:19