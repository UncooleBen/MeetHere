-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: meethere
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
SET NAMES utf8;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `t_building`
--

DROP TABLE IF EXISTS `t_building`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `t_building`
(
    `id`          int(11) unsigned NOT NULL AUTO_INCREMENT,
    `name`        varchar(45)      NOT NULL,
    `description` varchar(45)      NOT NULL,
    `price`       decimal(11, 0)   NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_building`
--

LOCK TABLES `t_building` WRITE;
/*!40000 ALTER TABLE `t_building`
    DISABLE KEYS */;
INSERT INTO `t_building`
VALUES (2, '2116 Allston Way, Room 603', 'Downtown Berkeley', 1200);
/*!40000 ALTER TABLE `t_building`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_comment`
--

DROP TABLE IF EXISTS `t_comment`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `t_comment`
(
    `id`       int(11)    NOT NULL AUTO_INCREMENT,
    `userId`   int(11)    NOT NULL,
    `date`     bigint(20) NOT NULL,
    `content`  text       NOT NULL,
    `verified` tinyint(4) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_comment`
--

LOCK TABLES `t_comment` WRITE;
/*!40000 ALTER TABLE `t_comment`
    DISABLE KEYS */;
INSERT INTO `t_comment`
VALUES (2, 9, 1575881257798, 'No 2 comment', 1),
       (4, 9, 1575883379896, 'No 4 comment', 0);
/*!40000 ALTER TABLE `t_comment`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_news`
--

DROP TABLE IF EXISTS `t_news`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `t_news`
(
    `id`            int(11) unsigned NOT NULL AUTO_INCREMENT,
    `title`         varchar(45)      NOT NULL,
    `created`       bigint(20)       NOT NULL,
    `last_modified` bigint(20)       NOT NULL,
    `author`        varchar(45)      NOT NULL,
    `detail`        text             NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_news`
--

LOCK TABLES `t_news` WRITE;
/*!40000 ALTER TABLE `t_news`
    DISABLE KEYS */;
INSERT INTO `t_news`
VALUES (0, 'null', 0, 0, 'null', 'null');
/*!40000 ALTER TABLE `t_news`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_record`
--

DROP TABLE IF EXISTS `t_record`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `t_record`
(
    `id`          int(11) unsigned NOT NULL AUTO_INCREMENT,
    `time`        bigint(20)       NOT NULL,
    `start_date`  bigint(20)       NOT NULL,
    `end_date`    bigint(20)       NOT NULL,
    `user_id`     int(11)          NOT NULL,
    `building_id` int(11)          NOT NULL,
    `verified`    tinyint(1)       NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_record`
--

LOCK TABLES `t_record` WRITE;
/*!40000 ALTER TABLE `t_record`
    DISABLE KEYS */;
INSERT INTO `t_record`
VALUES (0, 0, 0, 0, 0, 0, 1),
       (2, 1575962704322, 1575962704322, 1575962704322, 9, 2, 0),
       (3, 1575962704336, 1575962704336, 1575962704336, 9, 2, 0),
       (4, 1575962740815, 1575962740815, 1575962740815, 9, 2, 0),
       (5, 1575962799675, 1575962799675, 1575962799675, 9, 2, 0);
/*!40000 ALTER TABLE `t_record`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
SET character_set_client = utf8mb4;
CREATE TABLE `t_user`
(
    `id`         int(11) unsigned                        NOT NULL AUTO_INCREMENT,
    `username`   varchar(20)                             NOT NULL,
    `password`   varchar(20)                             NOT NULL,
    `name`       varchar(20)                             NOT NULL,
    `sex`        enum ('MALE','FEMALE','TRANSGENDER','') NOT NULL,
    `permission` int(11)                                 NOT NULL,
    `tel`        varchar(15) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user`
    DISABLE KEYS */;
INSERT INTO `t_user`
VALUES (0, 'root', 'root', 'pjt', 'MALE', 0, '999'),
       (4, 'pjt', 'pjt', 'john', 'MALE', 1, '123123123'),
       (7, 'jiege', '123', 'guoyuanjie', 'MALE', 1, '123123'),
       (8, 'zqf', '123', 'zhouqifeng', 'MALE', 1, '123123'),
       (9, '999', '999', '999', 'MALE', 1, '999'),
       (10, '888', '888', '888', 'MALE', 1, '888');
/*!40000 ALTER TABLE `t_user`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'meethere'
--

--
-- Dumping routines for database 'meethere'
--
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2019-12-10 15:28:38
