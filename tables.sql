CREATE DATABASE  IF NOT EXISTS `OpenFleetr` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `OpenFleetr`;
-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: OpenFleetr
-- ------------------------------------------------------
-- Server version	5.7.21-0ubuntu0.16.04.1

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
-- Table structure for table `CurrentLocationEntity`
--

DROP TABLE IF EXISTS `CurrentLocationEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CurrentLocationEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleId` int(11) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `timeStamp` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_CurrentLocationEntity_1_idx` (`vehicleId`),
  CONSTRAINT `fk_CurrentLocationEntity_1` FOREIGN KEY (`vehicleId`) REFERENCES `VehicleEntity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CurrentLocationEntity`
--

LOCK TABLES `CurrentLocationEntity` WRITE;
/*!40000 ALTER TABLE `CurrentLocationEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `CurrentLocationEntity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CurrentStatusEntity`
--

DROP TABLE IF EXISTS `CurrentStatusEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CurrentStatusEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleId` int(11) DEFAULT NULL,
  `driverId` int(11) DEFAULT NULL,
  `checkOutDate` varchar(45) DEFAULT NULL,
  `checkInDate` varchar(45) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `notes` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_CurrentStatusEntity_1_idx` (`vehicleId`),
  KEY `fk_CurrentStatusEntity_2_idx` (`driverId`),
  CONSTRAINT `fk_CurrentStatusEntity_1` FOREIGN KEY (`vehicleId`) REFERENCES `VehicleEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_CurrentStatusEntity_2` FOREIGN KEY (`driverId`) REFERENCES `UserEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CurrentStatusEntity`
--

LOCK TABLES `CurrentStatusEntity` WRITE;
/*!40000 ALTER TABLE `CurrentStatusEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `CurrentStatusEntity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DriverEntity`
--

DROP TABLE IF EXISTS `DriverEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DriverEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  `birthDate` varchar(45) DEFAULT NULL,
  `phoneNumber` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_DriverEntity_1_idx` (`userId`),
  CONSTRAINT `fk_DriverEntity_1` FOREIGN KEY (`userId`) REFERENCES `UserEntity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DriverEntity`
--

LOCK TABLES `DriverEntity` WRITE;
/*!40000 ALTER TABLE `DriverEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `DriverEntity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HistoricalLocationEntity`
--

DROP TABLE IF EXISTS `HistoricalLocationEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HistoricalLocationEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleId` int(11) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `timeStamp` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_HistoricalLocationEntity_1_idx` (`vehicleId`),
  CONSTRAINT `fk_HistoricalLocationEntity_1` FOREIGN KEY (`vehicleId`) REFERENCES `VehicleEntity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HistoricalLocationEntity`
--

LOCK TABLES `HistoricalLocationEntity` WRITE;
/*!40000 ALTER TABLE `HistoricalLocationEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `HistoricalLocationEntity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HistoricalStatusEntity`
--

DROP TABLE IF EXISTS `HistoricalStatusEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HistoricalStatusEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleId` int(11) DEFAULT NULL,
  `driverId` int(11) DEFAULT NULL,
  `checkOutDate` varchar(45) DEFAULT NULL,
  `checkInDate` varchar(45) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `notes` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_HistoricalStatusEntity_1_idx` (`vehicleId`),
  KEY `fk_HistoricalStatusEntity_2_idx` (`driverId`),
  CONSTRAINT `fk_HistoricalStatusEntity_1` FOREIGN KEY (`vehicleId`) REFERENCES `VehicleEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_HistoricalStatusEntity_2` FOREIGN KEY (`driverId`) REFERENCES `UserEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HistoricalStatusEntity`
--

LOCK TABLES `HistoricalStatusEntity` WRITE;
/*!40000 ALTER TABLE `HistoricalStatusEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `HistoricalStatusEntity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserEntity`
--

DROP TABLE IF EXISTS `UserEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `token` varchar(45) DEFAULT NULL,
  `level` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserEntity`
--

LOCK TABLES `UserEntity` WRITE;
/*!40000 ALTER TABLE `UserEntity` DISABLE KEYS */;
INSERT INTO `UserEntity` VALUES (1,'tareq','tareq','ed572a00-7efe-4193-b718-faf61e7410c6',4);
/*!40000 ALTER TABLE `UserEntity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VehicleEntity`
--

DROP TABLE IF EXISTS `VehicleEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VehicleEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleType` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VehicleEntity`
--

LOCK TABLES `VehicleEntity` WRITE;
/*!40000 ALTER TABLE `VehicleEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `VehicleEntity` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-19 14:50:48
