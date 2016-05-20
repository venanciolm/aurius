-- MySQL dump 10.13  Distrib 5.5.25, for Win64 (x86)
--
-- Host: localhost    Database: dic
-- ------------------------------------------------------
-- Server version	5.5.25

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
-- Dumping data for table `usuario_usr`
--

LOCK TABLES `usuario_usr` WRITE;
/*!40000 ALTER TABLE `usuario_usr` DISABLE KEYS */;
INSERT INTO `usuario_usr` VALUES ('00000000000000000000000000000000','system','S','*','Adminstrador','del','Sistema','ES','es',NULL);
/*!40000 ALTER TABLE `usuario_usr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `grupo_grp`
--

LOCK TABLES `grupo_grp` WRITE;
/*!40000 ALTER TABLE `grupo_grp` DISABLE KEYS */;
INSERT INTO `grupo_grp` VALUES ('00000000000000000000000000000000','Administradores');
/*!40000 ALTER TABLE `grupo_grp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `role_rol`
--

LOCK TABLES `role_rol` WRITE;
/*!40000 ALTER TABLE `role_rol` DISABLE KEYS */;
INSERT INTO `role_rol` VALUES ('00000000000000000000000000000000','ADMIN','Administrador');
/*!40000 ALTER TABLE `role_rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `grp_grp`
--

LOCK TABLES `grp_grp` WRITE;
/*!40000 ALTER TABLE `grp_grp` DISABLE KEYS */;
/*!40000 ALTER TABLE `grp_grp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `grp_rol`
--

LOCK TABLES `grp_rol` WRITE;
/*!40000 ALTER TABLE `grp_rol` DISABLE KEYS */;
/*!40000 ALTER TABLE `grp_rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `usr_grp`
--

LOCK TABLES `usr_grp` WRITE;
/*!40000 ALTER TABLE `usr_grp` DISABLE KEYS */;
/*!40000 ALTER TABLE `usr_grp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `usr_rol`
--

LOCK TABLES `usr_rol` WRITE;
/*!40000 ALTER TABLE `usr_rol` DISABLE KEYS */;
/*!40000 ALTER TABLE `usr_rol` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-07-22  8:46:31
