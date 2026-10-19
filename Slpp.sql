-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: slpp
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `petitioners`
--

DROP TABLE IF EXISTS `petitioners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `petitioners` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bio_id` varchar(255) NOT NULL,
  `date_of_birth` date NOT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6la14moh9mt6int2ky9h5c23` (`bio_id`),
  UNIQUE KEY `UK_68yc95m889buo5n84esvfn0aw` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `petitioners`
--

LOCK TABLES `petitioners` WRITE;
/*!40000 ALTER TABLE `petitioners` DISABLE KEYS */;
INSERT INTO `petitioners` VALUES (1,'K1YL8VA2HG','2000-11-11','tteflicks@gmail.com','test','$2a$10$2J21hGWmwdfZ0E63T7uveeYkkL49dYLanJaxlDQbDHCOk6HWpjD1G'),(2,'7DMPYAZAP2','2000-11-11','tteflick@gmail.com','test','$2a$10$yGHbUGMk3GPcmaU4SpIXv.41P6mRnl0uT2iKQYW6z//VPhcws3acq'),(3,'D05HPPQNJ4','2000-11-11','test@gmail.com','test','$2a$10$yzZDKv6WqJwXSUtiTMrri.C50WN46olOJ7RKSYTFx.CALlb/hpHW.'),(4,'DHKFIYHMAZ','2000-03-03','test2@gmail.com','test','$2a$10$oyokOjx87G0Yp5F79rqlzuJ2r9x8WnNrGCD4psRxuXbJH8rDL28PC');
/*!40000 ALTER TABLE `petitioners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `petitions`
--

DROP TABLE IF EXISTS `petitions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `petitions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(2000) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `response` varchar(255) DEFAULT NULL,
  `signature_count` int NOT NULL,
  `status` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `petitioner_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg3r0kahbx8qr4a7bnfko3k33n` (`petitioner_id`),
  CONSTRAINT `FKg3r0kahbx8qr4a7bnfko3k33n` FOREIGN KEY (`petitioner_id`) REFERENCES `petitioners` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `petitions`
--

LOCK TABLES `petitions` WRITE;
/*!40000 ALTER TABLE `petitions` DISABLE KEYS */;
INSERT INTO `petitions` VALUES (1,'fthrytkyukioip','2025-01-19 16:00:24.526162',NULL,0,'open','sfgtretyu6ru6ru65i',3),(2,'kykyuilulu','2025-01-19 16:00:45.308851',NULL,0,'open','fehtytuki6ui6i',3),(3,'fbfgrjytituii6i','2025-01-19 16:00:57.537591',NULL,0,'open','wertertyrtyu',3);
/*!40000 ALTER TABLE `petitions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `signatures`
--

DROP TABLE IF EXISTS `signatures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `signatures` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `signed_at` datetime(6) NOT NULL,
  `petition_id` bigint NOT NULL,
  `petitioner_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6y57dr6tidadnqiji14gy6tlg` (`petition_id`,`petitioner_id`),
  KEY `FKrvv4ndd6hu8dis6d0x0hdyyi9` (`petitioner_id`),
  CONSTRAINT `FKd12d9sb1uas2ej88c90293jqo` FOREIGN KEY (`petition_id`) REFERENCES `petitions` (`id`),
  CONSTRAINT `FKrvv4ndd6hu8dis6d0x0hdyyi9` FOREIGN KEY (`petitioner_id`) REFERENCES `petitioners` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `signatures`
--

LOCK TABLES `signatures` WRITE;
/*!40000 ALTER TABLE `signatures` DISABLE KEYS */;
/*!40000 ALTER TABLE `signatures` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-19 16:46:17
