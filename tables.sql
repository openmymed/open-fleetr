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
-- Table structure for table `ApiUser`
--

DROP TABLE IF EXISTS `ApiUser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ApiUser` (
  `userId` int(11) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `applicationName` varchar(100) DEFAULT NULL,
  `maintainerEmail` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ApiUser_User_FK` (`userId`),
  CONSTRAINT `ApiUser_User_FK` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiUser`
--

LOCK TABLES `ApiUser` WRITE;
/*!40000 ALTER TABLE `ApiUser` DISABLE KEYS */;
/*!40000 ALTER TABLE `ApiUser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DispatchOrder`
--

DROP TABLE IF EXISTS `DispatchOrder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DispatchOrder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `startLatitude` double DEFAULT NULL,
  `startLongitude` double DEFAULT NULL,
  `creationDate` varchar(100) DEFAULT NULL,
  `startDate` varchar(100) DEFAULT NULL,
  `completionDate` varchar(100) DEFAULT NULL,
  `vehicleId` int(11) NOT NULL,
  `firstName` varchar(100) DEFAULT NULL,
  `lastName` varchar(100) DEFAULT NULL,
  `phoneNumber` varchar(100) DEFAULT NULL,
  `notes` varchar(100) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `destinationHospitalId` int(11) NOT NULL,
  `timeStamp` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `DispatchOrder_Vehicle_FK` (`vehicleId`),
  KEY `DispatchOrder_Hospital_FK` (`destinationHospitalId`),
  KEY `DispatchOrder_User_FK` (`userId`),
  CONSTRAINT `DispatchOrder_Hospital_FK` FOREIGN KEY (`destinationHospitalId`) REFERENCES `Hospital` (`id`),
  CONSTRAINT `DispatchOrder_User_FK` FOREIGN KEY (`userId`) REFERENCES `User` (`id`),
  CONSTRAINT `DispatchOrder_Vehicle_FK` FOREIGN KEY (`vehicleId`) REFERENCES `Vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DispatchOrder`
--

LOCK TABLES `DispatchOrder` WRITE;
/*!40000 ALTER TABLE `DispatchOrder` DISABLE KEYS */;
/*!40000 ALTER TABLE `DispatchOrder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Dispatcher`
--

DROP TABLE IF EXISTS `Dispatcher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Dispatcher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(100) DEFAULT NULL,
  `lastName` varchar(100) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `birthDate` varchar(100) DEFAULT NULL,
  `phoneNumber` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Dispatcher_User_FK` (`userId`),
  CONSTRAINT `Dispatcher_User_FK` FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Dispatcher`
--

LOCK TABLES `Dispatcher` WRITE;
/*!40000 ALTER TABLE `Dispatcher` DISABLE KEYS */;
/*!40000 ALTER TABLE `Dispatcher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Driver`
--

DROP TABLE IF EXISTS `Driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Driver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(100) DEFAULT NULL,
  `lastName` varchar(100) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `birthDate` varchar(100) DEFAULT NULL,
  `phoneNumber` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Driver_User_FK` (`userId`),
  CONSTRAINT `Driver_User_FK` FOREIGN KEY (`userId`) REFERENCES `User` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Driver`
--

LOCK TABLES `Driver` WRITE;
/*!40000 ALTER TABLE `Driver` DISABLE KEYS */;
/*!40000 ALTER TABLE `Driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Hospital`
--

DROP TABLE IF EXISTS `Hospital`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Hospital` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `latitude` varchar(100) DEFAULT NULL,
  `longitude` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Hospital`
--

LOCK TABLES `Hospital` WRITE;
/*!40000 ALTER TABLE `Hospital` DISABLE KEYS */;
/*!40000 ALTER TABLE `Hospital` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LocationHistory`
--

DROP TABLE IF EXISTS `LocationHistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LocationHistory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleId` int(11) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `timeStamp` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  KEY `LocationHistory_Vehicle_FK` (`vehicleId`),
  CONSTRAINT `LocationHistory_Vehicle_FK` FOREIGN KEY (`vehicleId`) REFERENCES `Vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LocationHistory`
--

LOCK TABLES `LocationHistory` WRITE;
/*!40000 ALTER TABLE `LocationHistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `LocationHistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `StatusHistory`
--

DROP TABLE IF EXISTS `StatusHistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `StatusHistory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `from` int(11) NOT NULL,
  `to` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `StatusHistory_User_FK` (`userId`),
  KEY `StatusHistory_Vehicle_FK` (`vehicleId`),
  CONSTRAINT `StatusHistory_User_FK` FOREIGN KEY (`userId`) REFERENCES `User` (`id`),
  CONSTRAINT `StatusHistory_Vehicle_FK` FOREIGN KEY (`vehicleId`) REFERENCES `Vehicle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `StatusHistory`
--

LOCK TABLES `StatusHistory` WRITE;
/*!40000 ALTER TABLE `StatusHistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `StatusHistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `token` varchar(100) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'admin','admin','e440d585-6e87-4608-9a31-101a4c89e678',4),(2,'dispatcher','dispatcher',NULL,3);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Vehicle`
--

DROP TABLE IF EXISTS `Vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Vehicle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timeStamp` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `vehicleDescription` varchar(100) DEFAULT NULL,
  `vehicleLicensePlate` varchar(100) DEFAULT NULL,
  `driver` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `latitude` int(11) DEFAULT NULL,
  `longitude` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Vehicle_UN` (`driver`),
  KEY `Vehicle_driver_IDX` (`driver`) USING BTREE,
  CONSTRAINT `Vehicle_Driver_FK` FOREIGN KEY (`driver`) REFERENCES `Driver` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Vehicle`
--

LOCK TABLES `Vehicle` WRITE;
/*!40000 ALTER TABLE `Vehicle` DISABLE KEYS */;
/*!40000 ALTER TABLE `Vehicle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'OpenFleetr'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-14 15:51:44
