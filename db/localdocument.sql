-- MySQL dump 10.13  Distrib 5.1.51-MariaDB, for suse-linux-gnu (i686)
--
-- Host: localhost    Database: workflowmgr
-- ------------------------------------------------------
-- Server version	5.1.51

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
-- Dumping data for table `localdocument`
--

LOCK TABLES `localdocument` WRITE;
/*!40000 ALTER TABLE `localdocument` DISABLE KEYS */;
INSERT INTO `localdocument` (id, description, documentDate,partner,attachmentid,subject,type,value,priority,created,createdBy,updated,updatedBy,status) VALUES (1,'Licence and support Payment for the ERP - Payment to be made in USD','2013-06-27 00:00:00','SAP Africa',NULL,'Inv/Fin/023/2013','INVOICE','10M Ksh',NULL,'2013-06-27 08:00:00',NULL,'2013-07-01 19:35:10',NULL,'INPROGRESS'),(2,'Contract for the construction of Hall 7. Needs to be approved within the month due to legal requirements','2013-06-27 00:00:00','Wonderland Construction Campany',NULL,'CNT/B&C/08/13','CONTRACT','300Mil',NULL,'2013-06-27 08:00:00',NULL,'2013-06-30 22:55:08',NULL,'DRAFTED'),(3,'Purchase order for the the purchase of daily cows','2013-06-14 00:00:00','Brookside Ltd',NULL,'LPO/JK/0106/13','LPO','556,000Ksh',NULL,'2013-06-27 08:00:00',NULL,'2013-06-30 22:55:16',NULL,'DRAFTED'),(4,'Contract for quarterly supply of stationery for JKUAT University. ','2013-06-28 00:00:00','Luyo Stationery',NULL,'CNT/ST/01/13','CONTRACT','5.3Mil',NULL,'2013-06-27 08:00:00',NULL,NULL,NULL,'DRAFTED'),(5,'Purchase order for the delivery of milk to the Mess 2013','2013-06-27 00:00:00','Baya Dairy',NULL,'LPO/MS/09/13','LPO','200,000Ksh',NULL,'2013-06-27 08:00:00',NULL,NULL,NULL,'DRAFTED'),(6,'Ref LPO/MS/09/13 - Invoice for Milk delivered by Baya Traders','2013-06-29 00:00:00','Baya Traders',NULL,'Inv/BD/87/13','INVOICE','200,000Ksh',NULL,'2013-06-27 08:00:00',NULL,NULL,NULL,'DRAFTED'),(7,'Contract for the constrution of Hall6',NULL,'B&C Contactors',NULL,'CNT/B&C/01/2013','CONTRACT','5.5Mil',NULL,'2013-06-30 21:27:13',NULL,NULL,NULL,'DRAFTED');
/*!40000 ALTER TABLE `localdocument` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-07-01 19:40:17
