-- MySQL dump 10.16  Distrib 10.1.23-MariaDB, for debian-linux-gnueabihf (armv7l)
--
-- Host: localhost    Database: OpenFleetr
-- ------------------------------------------------------
-- Server version	10.1.23-MariaDB-9+deb9u1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ApiUser`
--

LOCK TABLES `ApiUser` WRITE;
/*!40000 ALTER TABLE `ApiUser` DISABLE KEYS */;
INSERT INTO `ApiUser` VALUES (3,1,'helthcareapp','hamzeh@somemai.com');
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
  `destinationHospitalId` int(11) DEFAULT NULL,
  `timeStamp` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `userId` int(11) DEFAULT NULL,
  `dispatcherId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `DispatchOrder_Vehicle_FK` (`vehicleId`),
  KEY `DispatchOrder_Hospital_FK` (`destinationHospitalId`),
  KEY `DispatchOrder_User_FK` (`userId`),
  CONSTRAINT `DispatchOrder_Hospital_FK` FOREIGN KEY (`destinationHospitalId`) REFERENCES `Hospital` (`id`),
  CONSTRAINT `DispatchOrder_User_FK` FOREIGN KEY (`userId`) REFERENCES `User` (`id`),
  CONSTRAINT `DispatchOrder_Vehicle_FK` FOREIGN KEY (`vehicleId`) REFERENCES `Vehicle` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DispatchOrder`
--

LOCK TABLES `DispatchOrder` WRITE;
/*!40000 ALTER TABLE `DispatchOrder` DISABLE KEYS */;
INSERT INTO `DispatchOrder` VALUES (1,10,15,'Sun Jun 17 20:49:15 UTC 2018',NULL,NULL,1,'test','request','059999','nothing',0,NULL,'2018-06-17 21:15:01.577',3,NULL),(2,12,15,'Sun Jun 17 20:49:50 UTC 2018',NULL,NULL,1,'test','request2','059999','nothing',0,NULL,'2018-06-17 21:15:01.577',3,NULL),(3,12,15,'Sun Jun 17 20:57:26 UTC 2018',NULL,NULL,1,'test','request3','059999','nothing',0,NULL,'2018-06-17 21:15:01.577',3,NULL),(4,12,15,'Sun Jun 17 21:24:11 UTC 2018',NULL,NULL,1,'test','request3','059999','nothing',0,NULL,'2018-06-17 21:24:11.304',NULL,NULL),(5,NULL,NULL,'Sun Jun 17 21:33:36 UTC 2018',NULL,NULL,1,NULL,NULL,NULL,NULL,0,NULL,'2018-06-17 21:33:37.114',3,NULL),(6,105,130,'Mon Jun 18 06:55:38 UTC 2018',NULL,NULL,1,'test','request6','059999','nothing',0,NULL,'2018-06-18 06:55:39.014',3,NULL),(7,0,0,'Mon Jun 18 08:10:15 UTC 2018',NULL,NULL,1,'Android','test','05999','notes',0,NULL,'2018-06-18 08:10:15.709',3,NULL),(8,0,0,'Mon Jun 18 09:47:45 UTC 2018',NULL,NULL,1,'Android','test','05999','notes',0,NULL,'2018-06-18 09:47:45.347',3,NULL),(9,0,0,'Mon Jun 18 09:47:46 UTC 2018',NULL,NULL,1,'Android','test','05999','notes',0,NULL,'2018-06-18 09:47:46.581',3,NULL),(10,0,0,'Mon Jun 18 09:47:50 UTC 2018',NULL,NULL,1,'Android','test','05999','notes',0,NULL,'2018-06-18 09:47:50.942',3,NULL),(11,0,0,'Mon Jun 18 10:23:51 UTC 2018',NULL,NULL,1,'Android','test','05999','notes',0,NULL,'2018-06-18 10:23:51.352',3,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Dispatcher`
--

LOCK TABLES `Dispatcher` WRITE;
/*!40000 ALTER TABLE `Dispatcher` DISABLE KEYS */;
INSERT INTO `Dispatcher` VALUES (1,'Tareq','Kirresh',2,'23-7-1997','0595541800');
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Driver`
--

LOCK TABLES `Driver` WRITE;
/*!40000 ALTER TABLE `Driver` DISABLE KEYS */;
INSERT INTO `Driver` VALUES (1,'Tareq','Kirresh',4,'2018-06-19','059551800');
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
  `fromValue` int(11) DEFAULT NULL,
  `toValue` int(11) DEFAULT NULL,
  `timeStamp` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  KEY `StatusHistory_User_FK` (`userId`),
  KEY `StatusHistory_Vehicle_FK` (`vehicleId`),
  CONSTRAINT `StatusHistory_User_FK` FOREIGN KEY (`userId`) REFERENCES `User` (`id`),
  CONSTRAINT `StatusHistory_Vehicle_FK` FOREIGN KEY (`vehicleId`) REFERENCES `Vehicle` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `StatusHistory`
--

LOCK TABLES `StatusHistory` WRITE;
/*!40000 ALTER TABLE `StatusHistory` DISABLE KEYS */;
INSERT INTO `StatusHistory` VALUES (1,1,4,0,1,'2018-06-18 10:09:08.814'),(2,1,4,1,0,'2018-06-18 10:09:13.925'),(3,1,4,0,1,'2018-06-18 10:09:31.576'),(4,1,4,1,0,'2018-06-18 10:13:53.606'),(5,1,4,0,1,'2018-06-18 10:16:04.690'),(6,1,4,1,0,'2018-06-18 10:18:56.273'),(7,1,4,0,1,'2018-06-18 10:19:05.923'),(8,1,4,1,0,'2018-06-18 10:26:07.981'),(9,1,4,0,1,'2018-06-18 10:28:48.682'),(10,1,4,1,0,'2018-06-18 10:31:42.087'),(11,1,4,0,1,'2018-06-18 10:33:46.267'),(12,1,4,1,0,'2018-06-18 10:37:44.200'),(13,1,4,0,1,'2018-06-18 10:38:00.519'),(14,1,4,1,0,'2018-06-18 10:46:01.102'),(15,1,4,0,1,'2018-06-18 10:47:28.671');
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'admin','admin','dacb3be7-a72b-4602-952f-d3f466b50167',4),(2,'dispatcher','dispatcher','0cae7e0f-7030-49b7-a930-b0b2189adb22',3),(3,'apiUser','apiUser','9fa6cde0-957a-49ac-bc9d-0a087046b1b6',2),(4,'c8H1XWvh','GyS3nSYBPVa9Z2I3','d60cc8bc-44f9-4a8a-99dc-a99ec241d26e',1);
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
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Vehicle_UN` (`driver`),
  KEY `Vehicle_driver_IDX` (`driver`) USING BTREE,
  CONSTRAINT `Vehicle_Driver_FK` FOREIGN KEY (`driver`) REFERENCES `Driver` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Vehicle`
--

LOCK TABLES `Vehicle` WRITE;
/*!40000 ALTER TABLE `Vehicle` DISABLE KEYS */;
INSERT INTO `Vehicle` VALUES (1,'2018-06-18 11:34:53.115',NULL,NULL,1,1,23,35.2065956),(2,'2018-06-18 11:34:53.115',NULL,NULL,NULL,0,23,0),(3,'2018-06-18 11:34:53.115',NULL,NULL,NULL,0,23,0),(4,'2018-06-18 11:34:53.115',NULL,NULL,NULL,0,23,0);
/*!40000 ALTER TABLE `Vehicle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-18 12:08:02
