-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cukit
-- ------------------------------------------------------
-- Server version	8.0.23
CREATE DATABASE cukit;
USE cukit;


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
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `idcategoria` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idcategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=10  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comentarios`
--

DROP TABLE IF EXISTS `comentarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comentarios` (
  `idcomentarios` int NOT NULL AUTO_INCREMENT,
  `idReceta` int NOT NULL,
  `usuario` varchar(45) NOT NULL,
  `comentario` text NOT NULL,
  `fecha` date NOT NULL,
  `idPerfil` int DEFAULT NULL,
  PRIMARY KEY (`idcomentarios`),
  KEY `idReceta_idx` (`idReceta`),
  KEY `idPerfil_idx` (`idPerfil`),
  CONSTRAINT `idReceta` FOREIGN KEY (`idReceta`) REFERENCES `receta` (`idreceta`)
) ENGINE=InnoDB AUTO_INCREMENT=25  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `perfil`
--

DROP TABLE IF EXISTS `perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `perfil` (
  `idperfil` int NOT NULL AUTO_INCREMENT,
  `idUsuario` int NOT NULL,
  `usuario` varchar(45) NOT NULL,
  `url_foto` text,
  `pais` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idperfil`),
  KEY `idUsuario_idx` (`idUsuario`),
  CONSTRAINT `idUsuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=5  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `puntuacion`
--

DROP TABLE IF EXISTS `puntuacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `puntuacion` (
  `idpuntuacion` int NOT NULL AUTO_INCREMENT,
  `idReceta` int NOT NULL,
  `usuario` varchar(45) NOT NULL,
  `puntuacion` float NOT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`idpuntuacion`),
  KEY `idReceta_idx` (`idReceta`),
  CONSTRAINT `idRecetaPuntuacion` FOREIGN KEY (`idReceta`) REFERENCES `receta` (`idreceta`)
) ENGINE=InnoDB AUTO_INCREMENT=18  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `receta`
--

DROP TABLE IF EXISTS `receta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receta` (
  `idreceta` int NOT NULL AUTO_INCREMENT,
  `idPerfil` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `descripcion` text NOT NULL,
  `ingredientes` text NOT NULL,
  `instrucciones` text NOT NULL,
  `url_fotos` text NOT NULL,
  `idCategoria` int NOT NULL,
  `tiempo` int NOT NULL,
  `status` tinyint NOT NULL,
  PRIMARY KEY (`idreceta`),
  KEY `idCategoria_idx` (`idCategoria`),
  KEY `idPerfil_idx` (`idPerfil`),
  CONSTRAINT `idCategoria` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idcategoria`),
  CONSTRAINT `idPerfil` FOREIGN KEY (`idPerfil`) REFERENCES `perfil` (`idperfil`)
) ENGINE=InnoDB AUTO_INCREMENT=20  ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `apellido_p` varchar(45) DEFAULT NULL,
  `apellido_m` varchar(45) DEFAULT NULL,
  `pass` varchar(45) NOT NULL,
  `correo` varchar(45) NOT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2  ;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

ALTER TABLE usuario AUTO_INCREMENT = 0;
ALTER TABLE comentarios AUTO_INCREMENT = 0;
ALTER TABLE puntuacion AUTO_INCREMENT = 0;
ALTER TABLE perfil AUTO_INCREMENT = 0;
ALTER TABLE receta AUTO_INCREMENT = 0;
ALTER TABLE categoria AUTO_INCREMENT = 0;

INSERT INTO usuario(nombre, apellido_p, apellido_m, pass, correo) VALUES('team.dev', 'dev', 'dev', '1234', 'dev@team.com');
INSERT INTO perfil(idusuario, usuario, url_foto, pais) VALUES(1, 'teamDev', 'user_default.png', null);
INSERT INTO categoria(nombre) VALUES('Mariscos');
INSERT INTO categoria(nombre) VALUES('Comida');
INSERT INTO categoria(nombre) VALUES('Desayunos');
INSERT INTO categoria(nombre) VALUES('Saludable');
INSERT INTO categoria(nombre) VALUES('Desayunos');
INSERT INTO categoria(nombre) VALUES('Comida Rápida');
INSERT INTO categoria(nombre) VALUES('Postres');

-- Dump completed on 2023-03-20 20:17:05