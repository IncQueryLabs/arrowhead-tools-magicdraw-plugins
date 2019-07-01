CREATE DATABASE  IF NOT EXISTS `arrowhead` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `arrowhead`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: arrowhead
-- ------------------------------------------------------
-- Server version	5.7.10-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `arrowhead_cloud`
--

DROP TABLE IF EXISTS `arrowhead_cloud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arrowhead_cloud` (
  `id` bigint(20) NOT NULL,
  `address` varchar(255) NOT NULL,
  `authentication_info` varchar(2047) DEFAULT NULL,
  `cloud_name` varchar(255) NOT NULL,
  `gatekeeper_service_uri` varchar(255) NOT NULL,
  `operator` varchar(255) NOT NULL,
  `port` int(11) NOT NULL,
  `is_secure` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9cjou6d7x3w0pvnnb27bc4c4d` (`operator`,`cloud_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `arrowhead_service`
--

DROP TABLE IF EXISTS `arrowhead_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arrowhead_service` (
  `id` bigint(20) NOT NULL,
  `service_definition` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKg90gjpqpv7tpmy1eou5u4umyk` (`service_definition`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `arrowhead_service_interfaces`
--

DROP TABLE IF EXISTS `arrowhead_service_interfaces`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arrowhead_service_interfaces` (
  `arrowhead_service_id` bigint(20) NOT NULL,
  `interfaces` varchar(255) DEFAULT NULL,
  KEY `FKsb09f6kft101e8rixhm5t53f3` (`arrowhead_service_id`),
  CONSTRAINT `FKsb09f6kft101e8rixhm5t53f3` FOREIGN KEY (`arrowhead_service_id`) REFERENCES `arrowhead_service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `arrowhead_system`
--

DROP TABLE IF EXISTS `arrowhead_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arrowhead_system` (
  `id` bigint(20) NOT NULL,
  `address` varchar(255) NOT NULL,
  `authentication_info` varchar(2047) DEFAULT NULL,
  `port` int(11) NOT NULL,
  `system_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjiab72gx1c0711gjfr39mhck9` (`system_name`,`address`,`port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `broker`
--

DROP TABLE IF EXISTS `broker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `broker` (
  `id` bigint(20) NOT NULL,
  `address` varchar(255) NOT NULL,
  `port` int(11) NOT NULL,
  `is_secure` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_filter`
--

DROP TABLE IF EXISTS `event_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_filter` (
  `id` bigint(20) NOT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `event_type` varchar(255) NOT NULL,
  `match_metadata` char(1) DEFAULT NULL,
  `notify_uri` varchar(255) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `consumer_system_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKbkos27fkducgbn6rxqty2k6n1` (`event_type`,`consumer_system_id`),
  KEY `FK8k1vieqrr0cxw4x0ubocsrrpo` (`consumer_system_id`),
  CONSTRAINT `FK8k1vieqrr0cxw4x0ubocsrrpo` FOREIGN KEY (`consumer_system_id`) REFERENCES `arrowhead_system` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_filter_metadata`
--

DROP TABLE IF EXISTS `event_filter_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_filter_metadata` (
  `filter_id` bigint(20) NOT NULL,
  `metadata_value` varchar(2047) DEFAULT NULL,
  `metadata_key` varchar(255) NOT NULL,
  PRIMARY KEY (`filter_id`,`metadata_key`),
  CONSTRAINT `FK1iu2vhxo8211io6weiwryguib` FOREIGN KEY (`filter_id`) REFERENCES `event_filter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_filter_sources_list`
--

DROP TABLE IF EXISTS `event_filter_sources_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_filter_sources_list` (
  `filter_id` bigint(20) NOT NULL,
  `sources_id` bigint(20) NOT NULL,
  PRIMARY KEY (`filter_id`,`sources_id`),
  UNIQUE KEY `UK_nbe4wrcv5w6rga8uc6t0cb0ck` (`sources_id`),
  CONSTRAINT `FK7gulo44n997tr1146xxi2xhfe` FOREIGN KEY (`sources_id`) REFERENCES `arrowhead_system` (`id`),
  CONSTRAINT `FKqihrii4ab12xo3oxp5d5pb77j` FOREIGN KEY (`filter_id`) REFERENCES `event_filter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inter_cloud_authorization`
--

DROP TABLE IF EXISTS `inter_cloud_authorization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inter_cloud_authorization` (
  `id` bigint(20) NOT NULL,
  `consumer_cloud_id` bigint(20) NOT NULL,
  `arrowhead_service_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKj4pymxepq7mf82wx7f8e4hd9b` (`consumer_cloud_id`,`arrowhead_service_id`),
  KEY `FKsh4gbm0vs76weoq1lti6awtwf` (`arrowhead_service_id`),
  CONSTRAINT `FKsh4gbm0vs76weoq1lti6awtwf` FOREIGN KEY (`arrowhead_service_id`) REFERENCES `arrowhead_service` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKsw50x8tjybx1jjrkj6aamxt8c` FOREIGN KEY (`consumer_cloud_id`) REFERENCES `arrowhead_cloud` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `intra_cloud_authorization`
--

DROP TABLE IF EXISTS `intra_cloud_authorization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `intra_cloud_authorization` (
  `id` bigint(20) NOT NULL,
  `consumer_system_id` bigint(20) NOT NULL,
  `provider_system_id` bigint(20) NOT NULL,
  `arrowhead_service_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4ie5ps7a6w40iqdte0u53mw1u` (`consumer_system_id`,`provider_system_id`,`arrowhead_service_id`),
  KEY `FKt01tq84ypy16yfpt2q9v7qn2b` (`provider_system_id`),
  KEY `FK1nx371ky16pl2rl0f4hk3puk4` (`arrowhead_service_id`),
  CONSTRAINT `FK1nx371ky16pl2rl0f4hk3puk4` FOREIGN KEY (`arrowhead_service_id`) REFERENCES `arrowhead_service` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK58r9imuaq3dy3o96w5xcxkemh` FOREIGN KEY (`consumer_system_id`) REFERENCES `arrowhead_system` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKt01tq84ypy16yfpt2q9v7qn2b` FOREIGN KEY (`provider_system_id`) REFERENCES `arrowhead_system` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime(6) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `message` varchar(2047) DEFAULT NULL,
  `origin` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `neighbor_cloud`
--

DROP TABLE IF EXISTS `neighbor_cloud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `neighbor_cloud` (
  `cloud_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cloud_id`),
  CONSTRAINT `FK9j46xue240bjfr6u5vvi3qsmi` FOREIGN KEY (`cloud_id`) REFERENCES `arrowhead_cloud` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orchestration_store`
--

DROP TABLE IF EXISTS `orchestration_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orchestration_store` (
  `id` bigint(20) NOT NULL,
  `is_default` char(1) DEFAULT NULL,
  `instruction` varchar(255) DEFAULT NULL,
  `last_updated` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `consumer_system_id` bigint(20) NOT NULL,
  `provider_cloud_id` bigint(20) DEFAULT NULL,
  `provider_system_id` bigint(20) NOT NULL,
  `arrowhead_service_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK328vwkn9l8phjq4j276wb13w9` (`arrowhead_service_id`,`consumer_system_id`,`priority`,`is_default`),
  KEY `FKg9jtg1go2yety7s6qimnbqdtc` (`consumer_system_id`),
  KEY `FK4as8nlx9s4a6a9r6y4oswj5do` (`provider_cloud_id`),
  KEY `FK1a9yusgvqs0jrna2y8cgdeusb` (`provider_system_id`),
  CONSTRAINT `FK1a9yusgvqs0jrna2y8cgdeusb` FOREIGN KEY (`provider_system_id`) REFERENCES `arrowhead_system` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK4as8nlx9s4a6a9r6y4oswj5do` FOREIGN KEY (`provider_cloud_id`) REFERENCES `arrowhead_cloud` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKg9jtg1go2yety7s6qimnbqdtc` FOREIGN KEY (`consumer_system_id`) REFERENCES `arrowhead_system` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKnjr4mytp6bipwyc9sv9y1ip51` FOREIGN KEY (`arrowhead_service_id`) REFERENCES `arrowhead_service` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orchestration_store_attributes`
--

DROP TABLE IF EXISTS `orchestration_store_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orchestration_store_attributes` (
  `store_entry_id` bigint(20) NOT NULL,
  `attribute_value` varchar(2047) DEFAULT NULL,
  `attribute_key` varchar(255) NOT NULL,
  PRIMARY KEY (`store_entry_id`,`attribute_key`),
  CONSTRAINT `FKrtqe93seoude4elrqmk1qdowj` FOREIGN KEY (`store_entry_id`) REFERENCES `orchestration_store` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `own_cloud`
--

DROP TABLE IF EXISTS `own_cloud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `own_cloud` (
  `cloud_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cloud_id`),
  CONSTRAINT `FKr3avkpkrx88jt4atfmxewqkl8` FOREIGN KEY (`cloud_id`) REFERENCES `arrowhead_cloud` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_registry`
--

DROP TABLE IF EXISTS `service_registry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_registry` (
  `id` bigint(20) NOT NULL,
  `end_of_validity` datetime(6) DEFAULT NULL,
  `metadata` varchar(255) DEFAULT NULL,
  `service_uri` varchar(255) DEFAULT NULL,
  `udp` char(1) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `arrowhead_service_id` bigint(20) NOT NULL,
  `provider_system_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3q3tqiu7f92u946p33plj5fxq` (`arrowhead_service_id`,`provider_system_id`),
  KEY `FK4lc944mp4x24pr09wuxbb08ky` (`provider_system_id`),
  CONSTRAINT `FK4lc944mp4x24pr09wuxbb08ky` FOREIGN KEY (`provider_system_id`) REFERENCES `arrowhead_system` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKr0x7pvbi16w5b6ao6q43t606p` FOREIGN KEY (`arrowhead_service_id`) REFERENCES `arrowhead_service` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `table_generator`
--

DROP TABLE IF EXISTS `table_generator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table_generator` (
  `sequence_name` varchar(255) NOT NULL,
  `next_val` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sequence_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
flush privileges;