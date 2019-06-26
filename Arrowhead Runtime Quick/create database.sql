CREATE DATABASE  IF NOT EXISTS `arrowhead_test_cloud_1` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `arrowhead_test_cloud_1`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: arrowhead_test_cloud_1
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
  `id` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `authentication_info` varchar(2047) DEFAULT NULL,
  `cloud_name` varchar(255) DEFAULT NULL,
  `gatekeeper_service_uri` varchar(255) DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `is_secure` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9cjou6d7x3w0pvnnb27bc4c4d` (`operator`,`cloud_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arrowhead_cloud`
--

LOCK TABLES `arrowhead_cloud` WRITE;
/*!40000 ALTER TABLE `arrowhead_cloud` DISABLE KEYS */;
INSERT INTO `arrowhead_cloud` VALUES (1,'arrowhead.tmit.bme.hu','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7GA65Tt0L+cOcQzrIpSau3d79MX9jlEx4GaGbPHR7NOo0BgDU8db6I4L+baMq4VYD5XHyCKvOInpOqPEfUwEm7q8w3rFBSJY8lOv31YAOo9xz0rQKGzqNFQv2KvZE2Y11sXkfnVKimWqZelutvzv9CT+vDbrEaLd6MAmxyBJI+4ztdTAliID89ZhIrBrV3/rEq+OQl39MJNWOO/t3HXPcGPdURRfdLNvgtGRfL2WzGB3bT3pEw2eV/QdFcudsFM0Cx25wOfPAPs9Ia6qvkzMYkvqv3mE9f+nd6QACjX6H+xy4zkdhSlaNTjAwnasSoaKuq7xeBkD/Q6lV/fivC2LJwIDAQAB','SmartGrid','gatekeeper','SmartGridOperator',8446,'N'),(2,'arrowhead2.tmit.bme.hu','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArIwxpELoHQAZps9fVn2X8FpWj6pG2wQfC6KLiTaQKOUniL5ePJ8Vprc6uB9gZkgcdWEGyaeGKe/XdfywB2uSpHWd0JlkNEYFGLi6Pg54z6n3XjUTiuviSbOrcDxVbCNYwHHoBW6URogTEN0BUG/0lSPVEsSU741KmXMy+oMXFnvPFt6bM3nw7RJDaQOnqTd4OSteGYSmnnYX1UzVTUPjTFampQN1gtzKIJqKjgH3ujhlPaVbWIKIP+xz69IAkjpLdEmcbU1SocNndaVdjEtsHdmx6Xxfsc+5Wudp331a7iIOpddefSXIaffh59/2vexZRgdtdMrtzQnLhjUERavCywIDAQAB','EVCloud','gatekeeper','EVManufacturer',8446,'N');
/*!40000 ALTER TABLE `arrowhead_cloud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `arrowhead_service`
--

DROP TABLE IF EXISTS `arrowhead_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arrowhead_service` (
  `id` int(11) NOT NULL,
  `service_definition` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKg90gjpqpv7tpmy1eou5u4umyk` (`service_definition`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arrowhead_service`
--

LOCK TABLES `arrowhead_service` WRITE;
/*!40000 ALTER TABLE `arrowhead_service` DISABLE KEYS */;
INSERT INTO `arrowhead_service` VALUES (1,'Billing'),(2,'ChargingReservation'),(3,'DCCharging'),(4,'IndoorTemperature');
/*!40000 ALTER TABLE `arrowhead_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `arrowhead_service_interface_list`
--

DROP TABLE IF EXISTS `arrowhead_service_interface_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arrowhead_service_interface_list` (
  `arrowhead_service_id` int(11) NOT NULL,
  `interfaces` varchar(255) DEFAULT NULL,
  KEY `FKfaxi77ynuub343wunfiny2p0` (`arrowhead_service_id`),
  CONSTRAINT `FKfaxi77ynuub343wunfiny2p0` FOREIGN KEY (`arrowhead_service_id`) REFERENCES `arrowhead_service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arrowhead_service_interface_list`
--

LOCK TABLES `arrowhead_service_interface_list` WRITE;
/*!40000 ALTER TABLE `arrowhead_service_interface_list` DISABLE KEYS */;
INSERT INTO `arrowhead_service_interface_list` VALUES (1,'JSON'),(2,'JSON'),(3,'JSON'),(4,'json');
/*!40000 ALTER TABLE `arrowhead_service_interface_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `arrowhead_system`
--

DROP TABLE IF EXISTS `arrowhead_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `arrowhead_system` (
  `id` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `authentication_info` varchar(2047) DEFAULT NULL,
  `system_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3rj1egf6gi1enagslqry0pkkl` (`system_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arrowhead_system`
--

LOCK TABLES `arrowhead_system` WRITE;
/*!40000 ALTER TABLE `arrowhead_system` DISABLE KEYS */;
INSERT INTO `arrowhead_system` VALUES (1,'dummy_address_1','Base64 coded Public Key','SmartGridManagerSystem1'),(2,'dummy_address_2','Base64 coded Public Key','SmartGridManagerSystem2'),(3,'dummy_address_3','Base64 coded Public Key','SmartGridManagerSystem3'),(4,'dummy_address_4','Base64 coded Public Key','ChargePointSystem'),(5,'127.0.0.1','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyzDRU+P6h8Jwp9eiGYqqlgoAmPLo6M/PTZX+pkKr2MIg7VLdnjUeXzKFljwJKjYGG3nus53F4RFnymT7VoIQT+SmkuLy90Ir6O3XRWiD74XlOIkthT8/fq5FP9sJIusaRc9jkx3Y8jC3yCz1BPJDa+0A+heWarN+K7W7985aBFiJ1ycsB7yJFYAt7wVRc2fkgGpmp4l34Ta4J7QVwzYBOx5w5hIE29EzXOhl0GB6c/licclhisOnN31OWizoWJWAdexmjR9ugHgFSv4eUbjQ3/Qc0tM3ljmbnMMmj54fKZHtpesLXrCi44aQ88e7UOd/xplAbntEPvz168oie4IzFQIDAQAB','SecureTemperatureSensor'),(6,'127.0.0.1',NULL,'InsecureTemperatureSensor'),(7,'localhost',NULL,'client1');
/*!40000 ALTER TABLE `arrowhead_system` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `broker`
--

DROP TABLE IF EXISTS `broker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `broker` (
  `id` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `authentication_info` varchar(2047) DEFAULT NULL,
  `broker_name` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `is_secure` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7dsnnaysnsjuho4drpldwouao` (`broker_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `broker`
--

LOCK TABLES `broker` WRITE;
/*!40000 ALTER TABLE `broker` DISABLE KEYS */;
INSERT INTO `broker` VALUES (1,'mantis3.tmit.bme.hu','','insecure_broker',5672,'N'),(2,'mantis3.tmit.bme.hu','MIIC+DCCAeCgAwIBAgIEWlYfrjANBgkqhkiG9w0BAQsFADBHMSEwHwYDVQQKDBhBSVRJQSBJbnRlcm5hdGlvbmFsIEluYy4xCzAJBgNVBAYTAkhVMRUwEwYDVQQDDAxhcnJvd2hlYWQuZXUwHhcNMTgwMTEwMTQxNDA2WhcNMjgwMTEwMTQxNDA2WjA1MQswCQYDVQQGEwJIVTEmMCQGA1UEAwwddGVzdGJyb2tlci5haXRpYS5hcnJvd2hlYWQuZXUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDRkYEc6KdAXmy1gvuSu4u6MIJbS9JlkdtXnsP4G/HWFx3LuObIAMzbBw3NOYCIhhFwVxjvf0ME0gTA+llw7zdY+7dUj+TJ3EBtfgvajfHm2IO71S1kZTCCTfFdbbtXMj8uf7hCEFLvKM4GUNe6i368xkVh5eBihCbm/F77jKt/tV/K73NB91dJBC290RJjrkq0mj5Hs4+WY1ezX/B1XR1iOzjs6ZmL5gxh1A7PqQHkbL7/Qotos3qzHUIqzUR1QlpJYgS/fjZfrLoJvrfqWvKsQ8sD0y5wxdO1QXOt2EDA0SUUjkJDkePujUsU5ljoXEOgYLGVQJoz+MGVrH4rSFepAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAEJUFWWWMMNnogOYiWdH4rUNVESw8rj1oczkZg+h+oQV4Qg6GxXFr9qL5LUOlcDRalJbWjd8yJBtQDIT7A2AuCQjLocgF1FZDa8nWcPkYNr7h4QX7E/7PNqAghjARSVaycMDtqaVCB0RlmcYMjreFjM71kRfHNcMOKLpdIMPhpfr2MC8E7EG6zfK0zsN3+qgZizqfR7Q8f6S0T8srIMuvBjk2h1aiM13ftu1/cn/d2RMAom46Mh1Z3qwhucO58BMwHzHJX24UE9xGWgOW3u/OrHNMmhzmNvlTNpbu3hZWoVMKnkWM3PVgGJSSJ2LlMBqO3uaOo1rdQ9WwPORST/Urgc=','secure_broker',5671,'Y');
/*!40000 ALTER TABLE `broker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_filter`
--

DROP TABLE IF EXISTS `event_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_filter` (
  `id` int(11) NOT NULL,
  `end_date` datetime DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  `match_metadata` char(1) DEFAULT NULL,
  `notify_uri` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `consumer_system_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKbkos27fkducgbn6rxqty2k6n1` (`event_type`,`consumer_system_id`),
  KEY `FK8k1vieqrr0cxw4x0ubocsrrpo` (`consumer_system_id`),
  CONSTRAINT `FK8k1vieqrr0cxw4x0ubocsrrpo` FOREIGN KEY (`consumer_system_id`) REFERENCES `arrowhead_system` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_filter`
--

LOCK TABLES `event_filter` WRITE;
/*!40000 ALTER TABLE `event_filter` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_filter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_filter_metadata`
--

DROP TABLE IF EXISTS `event_filter_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_filter_metadata` (
  `filter_id` int(11) NOT NULL,
  `metadata_value` varchar(2047) DEFAULT NULL,
  `metadata_key` varchar(255) NOT NULL,
  PRIMARY KEY (`filter_id`,`metadata_key`),
  CONSTRAINT `FK1iu2vhxo8211io6weiwryguib` FOREIGN KEY (`filter_id`) REFERENCES `event_filter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_filter_metadata`
--

LOCK TABLES `event_filter_metadata` WRITE;
/*!40000 ALTER TABLE `event_filter_metadata` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_filter_metadata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_filter_sources_list`
--

DROP TABLE IF EXISTS `event_filter_sources_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_filter_sources_list` (
  `filter_id` int(11) NOT NULL,
  `sources_id` int(11) NOT NULL,
  UNIQUE KEY `UK_nbe4wrcv5w6rga8uc6t0cb0ck` (`sources_id`),
  KEY `FKqihrii4ab12xo3oxp5d5pb77j` (`filter_id`),
  CONSTRAINT `FK7gulo44n997tr1146xxi2xhfe` FOREIGN KEY (`sources_id`) REFERENCES `arrowhead_system` (`id`),
  CONSTRAINT `FKqihrii4ab12xo3oxp5d5pb77j` FOREIGN KEY (`filter_id`) REFERENCES `event_filter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_filter_sources_list`
--

LOCK TABLES `event_filter_sources_list` WRITE;
/*!40000 ALTER TABLE `event_filter_sources_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_filter_sources_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (25),(25);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inter_cloud_authorization`
--

DROP TABLE IF EXISTS `inter_cloud_authorization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inter_cloud_authorization` (
  `id` int(11) NOT NULL,
  `consumer_cloud_id` int(11) DEFAULT NULL,
  `arrowhead_service_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKj4pymxepq7mf82wx7f8e4hd9b` (`consumer_cloud_id`,`arrowhead_service_id`),
  KEY `FKsh4gbm0vs76weoq1lti6awtwf` (`arrowhead_service_id`),
  CONSTRAINT `FKsh4gbm0vs76weoq1lti6awtwf` FOREIGN KEY (`arrowhead_service_id`) REFERENCES `arrowhead_service` (`id`),
  CONSTRAINT `FKsw50x8tjybx1jjrkj6aamxt8c` FOREIGN KEY (`consumer_cloud_id`) REFERENCES `arrowhead_cloud` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inter_cloud_authorization`
--

LOCK TABLES `inter_cloud_authorization` WRITE;
/*!40000 ALTER TABLE `inter_cloud_authorization` DISABLE KEYS */;
INSERT INTO `inter_cloud_authorization` VALUES (1,2,3);
/*!40000 ALTER TABLE `inter_cloud_authorization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `intra_cloud_authorization`
--

DROP TABLE IF EXISTS `intra_cloud_authorization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `intra_cloud_authorization` (
  `id` int(11) NOT NULL,
  `consumer_system_id` int(11) DEFAULT NULL,
  `provider_system_id` int(11) DEFAULT NULL,
  `arrowhead_service_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4ie5ps7a6w40iqdte0u53mw1u` (`consumer_system_id`,`provider_system_id`,`arrowhead_service_id`),
  KEY `FKt01tq84ypy16yfpt2q9v7qn2b` (`provider_system_id`),
  KEY `FK1nx371ky16pl2rl0f4hk3puk4` (`arrowhead_service_id`),
  CONSTRAINT `FK1nx371ky16pl2rl0f4hk3puk4` FOREIGN KEY (`arrowhead_service_id`) REFERENCES `arrowhead_service` (`id`),
  CONSTRAINT `FK58r9imuaq3dy3o96w5xcxkemh` FOREIGN KEY (`consumer_system_id`) REFERENCES `arrowhead_system` (`id`),
  CONSTRAINT `FKt01tq84ypy16yfpt2q9v7qn2b` FOREIGN KEY (`provider_system_id`) REFERENCES `arrowhead_system` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `intra_cloud_authorization`
--

LOCK TABLES `intra_cloud_authorization` WRITE;
/*!40000 ALTER TABLE `intra_cloud_authorization` DISABLE KEYS */;
INSERT INTO `intra_cloud_authorization` VALUES (1,4,1,1),(2,4,3,1),(3,4,3,2),(4,7,5,4),(5,7,6,4);
/*!40000 ALTER TABLE `intra_cloud_authorization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `neighbor_cloud`
--

DROP TABLE IF EXISTS `neighbor_cloud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `neighbor_cloud` (
  `cloud_id` int(11) NOT NULL,
  PRIMARY KEY (`cloud_id`),
  CONSTRAINT `FK9j46xue240bjfr6u5vvi3qsmi` FOREIGN KEY (`cloud_id`) REFERENCES `arrowhead_cloud` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `neighbor_cloud`
--

LOCK TABLES `neighbor_cloud` WRITE;
/*!40000 ALTER TABLE `neighbor_cloud` DISABLE KEYS */;
INSERT INTO `neighbor_cloud` VALUES (2);
/*!40000 ALTER TABLE `neighbor_cloud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orchestration_store`
--

DROP TABLE IF EXISTS `orchestration_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orchestration_store` (
  `id` int(11) NOT NULL,
  `is_default` char(1) DEFAULT NULL,
  `instruction` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `consumer_system_id` int(11) DEFAULT NULL,
  `provider_cloud_id` int(11) DEFAULT NULL,
  `provider_system_id` int(11) DEFAULT NULL,
  `arrowhead_service_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK328vwkn9l8phjq4j276wb13w9` (`arrowhead_service_id`,`consumer_system_id`,`priority`,`is_default`),
  KEY `FKg9jtg1go2yety7s6qimnbqdtc` (`consumer_system_id`),
  KEY `FK4as8nlx9s4a6a9r6y4oswj5do` (`provider_cloud_id`),
  KEY `FK1a9yusgvqs0jrna2y8cgdeusb` (`provider_system_id`),
  CONSTRAINT `FK1a9yusgvqs0jrna2y8cgdeusb` FOREIGN KEY (`provider_system_id`) REFERENCES `arrowhead_system` (`id`),
  CONSTRAINT `FK4as8nlx9s4a6a9r6y4oswj5do` FOREIGN KEY (`provider_cloud_id`) REFERENCES `arrowhead_cloud` (`id`),
  CONSTRAINT `FKg9jtg1go2yety7s6qimnbqdtc` FOREIGN KEY (`consumer_system_id`) REFERENCES `arrowhead_system` (`id`),
  CONSTRAINT `FKnjr4mytp6bipwyc9sv9y1ip51` FOREIGN KEY (`arrowhead_service_id`) REFERENCES `arrowhead_service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orchestration_store`
--

LOCK TABLES `orchestration_store` WRITE;
/*!40000 ALTER TABLE `orchestration_store` DISABLE KEYS */;
INSERT INTO `orchestration_store` VALUES (1,'N','command args','2018-02-18 12:46:00','Billing Service for Section1',1,4,NULL,1,1),(2,'N','command args','2018-02-18 12:46:00','Billing Service for Section2',2,4,NULL,2,1),(3,'N','command args','2018-02-18 12:46:00','Billing Service for Section3',3,4,NULL,3,1);
/*!40000 ALTER TABLE `orchestration_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orchestration_store_attributes`
--

DROP TABLE IF EXISTS `orchestration_store_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orchestration_store_attributes` (
  `store_entry_id` int(11) NOT NULL,
  `attribute_value` varchar(2047) DEFAULT NULL,
  `attribute_key` varchar(255) NOT NULL,
  PRIMARY KEY (`store_entry_id`,`attribute_key`),
  CONSTRAINT `FKrtqe93seoude4elrqmk1qdowj` FOREIGN KEY (`store_entry_id`) REFERENCES `orchestration_store` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orchestration_store_attributes`
--

LOCK TABLES `orchestration_store_attributes` WRITE;
/*!40000 ALTER TABLE `orchestration_store_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `orchestration_store_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `own_cloud`
--

DROP TABLE IF EXISTS `own_cloud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `own_cloud` (
  `cloud_id` int(11) NOT NULL,
  PRIMARY KEY (`cloud_id`),
  CONSTRAINT `FKr3avkpkrx88jt4atfmxewqkl8` FOREIGN KEY (`cloud_id`) REFERENCES `arrowhead_cloud` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `own_cloud`
--

LOCK TABLES `own_cloud` WRITE;
/*!40000 ALTER TABLE `own_cloud` DISABLE KEYS */;
INSERT INTO `own_cloud` VALUES (1);
/*!40000 ALTER TABLE `own_cloud` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_registry`
--

DROP TABLE IF EXISTS `service_registry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_registry` (
  `id` int(11) NOT NULL,
  `end_of_validity` datetime DEFAULT NULL,
  `metadata` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `service_uri` varchar(255) DEFAULT NULL,
  `udp` char(1) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `arrowhead_service_id` int(11) DEFAULT NULL,
  `provider_system_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3q3tqiu7f92u946p33plj5fxq` (`arrowhead_service_id`,`provider_system_id`),
  KEY `FK4lc944mp4x24pr09wuxbb08ky` (`provider_system_id`),
  CONSTRAINT `FK4lc944mp4x24pr09wuxbb08ky` FOREIGN KEY (`provider_system_id`) REFERENCES `arrowhead_system` (`id`),
  CONSTRAINT `FKr0x7pvbi16w5b6ao6q43t606p` FOREIGN KEY (`arrowhead_service_id`) REFERENCES `arrowhead_service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_registry`
--

LOCK TABLES `service_registry` WRITE;
/*!40000 ALTER TABLE `service_registry` DISABLE KEYS */;
INSERT INTO `service_registry` VALUES (1,'2018-12-30 16:42:39','currency=EUR',8082,'billing','N',1,1,2),(2,'2018-12-30 16:42:39','currency=EUR',8083,'billing','N',1,1,3),(3,'2018-12-30 16:42:39','carID=20',8083,'reserve_charging','N',1,2,3),(4,'2018-12-30 16:42:39','amper=15',8084,'charging/dc','N',1,3,4);
/*!40000 ALTER TABLE `service_registry` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

CREATE DATABASE  IF NOT EXISTS `log`;
USE `log`;

DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `origin` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `level` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `message` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1557 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';
flush privileges;
