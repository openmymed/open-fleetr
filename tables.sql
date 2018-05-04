-- MySQL dump 10.13  Distrib 5.7.22, for Linux (x86_64)
--
-- Host: localhost    Database: OpenFleetr
-- ------------------------------------------------------
-- Server version	5.7.22-0ubuntu18.04.1

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
-- Table structure for table `APIUserEntity`
--

DROP TABLE IF EXISTS `APIUserEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `APIUserEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `applicationName` varchar(45) DEFAULT NULL,
  `maintainerEmail` varchar(60) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `APIUserEntity`
--

LOCK TABLES `APIUserEntity` WRITE;
/*!40000 ALTER TABLE `APIUserEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `APIUserEntity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CurrentDispatchOrderEntity`
--

DROP TABLE IF EXISTS `CurrentDispatchOrderEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CurrentDispatchOrderEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `startLatitude` double DEFAULT NULL,
  `startLongitude` double DEFAULT NULL,
  `destinationLatitude` double DEFAULT NULL,
  `destinationLongitude` double DEFAULT NULL,
  `vehicleId` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `creationDate` date DEFAULT NULL,
  `timeStamp` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CurrentDispatchOrderEntity`
--

LOCK TABLES `CurrentDispatchOrderEntity` WRITE;
/*!40000 ALTER TABLE `CurrentDispatchOrderEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `CurrentDispatchOrderEntity` ENABLE KEYS */;
UNLOCK TABLES;

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
  `timeStamp` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `CurrentLocationEntitycol` point DEFAULT NULL,
  `geographicalAreaId` int(11) DEFAULT NULL,
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
  `checkOutDate` datetime DEFAULT NULL,
  `checkInDate` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `notes` varchar(45) DEFAULT NULL,
  `timeStamp` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  KEY `fk_CurrentStatusEntity_1_idx` (`vehicleId`),
  CONSTRAINT `fk_CurrentStatusEntity_1` FOREIGN KEY (`vehicleId`) REFERENCES `VehicleEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
-- Table structure for table `DispatcherEntity`
--

DROP TABLE IF EXISTS `DispatcherEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DispatcherEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `firstName` varchar(45) DEFAULT NULL,
  `lastName` varchar(45) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `phoneNumber` varchar(20) DEFAULT NULL,
  `geographicalAreaId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DispatcherEntity`
--

LOCK TABLES `DispatcherEntity` WRITE;
/*!40000 ALTER TABLE `DispatcherEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `DispatcherEntity` ENABLE KEYS */;
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
  `timeStamp` varchar(45) DEFAULT 'CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP',
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
-- Table structure for table `GeographicalAreaEntity`
--

DROP TABLE IF EXISTS `GeographicalAreaEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GeographicalAreaEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) DEFAULT NULL,
  `polygon` geometry DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GeographicalAreaEntity`
--

LOCK TABLES `GeographicalAreaEntity` WRITE;
/*!40000 ALTER TABLE `GeographicalAreaEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `GeographicalAreaEntity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HistoricalDispatchOrderEntity`
--

DROP TABLE IF EXISTS `HistoricalDispatchOrderEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HistoricalDispatchOrderEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `startLatitude` double DEFAULT NULL,
  `startLongitude` double DEFAULT NULL,
  `destinationLatitude` double DEFAULT NULL,
  `destinationLongitude` double DEFAULT NULL,
  `vehicleId` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `completionDate` datetime DEFAULT NULL,
  `timeStamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HistoricalDispatchOrderEntity`
--

LOCK TABLES `HistoricalDispatchOrderEntity` WRITE;
/*!40000 ALTER TABLE `HistoricalDispatchOrderEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `HistoricalDispatchOrderEntity` ENABLE KEYS */;
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
  `timeStamp` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_HistoricalStatusEntity_1_idx` (`vehicleId`),
  CONSTRAINT `fk_HistoricalStatusEntity_1` FOREIGN KEY (`vehicleId`) REFERENCES `VehicleEntity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
-- Table structure for table `NotificationEntity`
--

DROP TABLE IF EXISTS `NotificationEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NotificationEntity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `apiUser` int(11) DEFAULT NULL,
  `dispatcherId` int(11) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `wasSeen` bit(1) DEFAULT NULL,
  `wasHandled` bit(1) DEFAULT NULL,
  `seenTimestamp` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `handledTimestamp` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `NotificationEntity`
--

LOCK TABLES `NotificationEntity` WRITE;
/*!40000 ALTER TABLE `NotificationEntity` DISABLE KEYS */;
/*!40000 ALTER TABLE `NotificationEntity` ENABLE KEYS */;
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
  `timeStamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserEntity`
--

LOCK TABLES `UserEntity` WRITE;
/*!40000 ALTER TABLE `UserEntity` DISABLE KEYS */;
INSERT INTO `UserEntity` VALUES (1,'admin','admin',NULL,4,'2018-05-03 14:15:36');
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
  `timeStamp` timestamp NULL DEFAULT NULL,
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

-- Dump completed on 2018-05-04 11:17:41
