-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cukit
-- ------------------------------------------------------
-- Server version	8.0.23



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

CREATE DATABASE cukit;
USE cukit;

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `idcategoria` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idcategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'Mariscos'),(2,'Comida'),(3,'Desayunos'),(4,'Saludable'),(5,'Desayunos'),(6,'Comida Rápida'),(7,'Postres');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=6;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comentarios`
--

LOCK TABLES `comentarios` WRITE;
/*!40000 ALTER TABLE `comentarios` DISABLE KEYS */;
/*!40000 ALTER TABLE `comentarios` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfil`
--

LOCK TABLES `perfil` WRITE;
/*!40000 ALTER TABLE `perfil` DISABLE KEYS */;
INSERT INTO `perfil` VALUES (1,1,'teamDev','ffdbd8cb-ffc0-49c8-94a0-3839012006d3.jpg','undefined'),(2,2,'Efren Ortega','f6e1a297-d2b9-4799-b07a-b1904320b4b6.jpg','undefined');
/*!40000 ALTER TABLE `perfil` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `puntuacion`
--

LOCK TABLES `puntuacion` WRITE;
/*!40000 ALTER TABLE `puntuacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `puntuacion` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=10;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receta`
--

LOCK TABLES `receta` WRITE;
/*!40000 ALTER TABLE `receta` DISABLE KEYS */;
INSERT INTO `receta` VALUES (4,1,'Arroz Con Pollo','Un plato tradicional que combina arroz, pollo y verduras.','1 taza de Arroz, 2 tazas de agua, 1 pechuga de pollo cortada en cubos, 1 cebolla picada, 1 diente de ajo picada, 1 pimiento picado, 1/2 taza de guisantes, 1/4 taza de aceite, 1 cucharada de comino, Sal y pimienta al gusto,','En una olla grande, calienta el aceite a fuego medio-alto. Agrega la cebolla. Añade el pollo y saltea hasta que esté dorado. Agrega las zanahorias, el pimiento y los guisantes y saltea durante unos minutos. Agrega el arroz y revuelve para que se cubra con el aceite y las verduras. Agrega el comino, la sal y la pimienta y mezcla bien. Agrega el agua y lleva a ebullición. Reduce el fuego a bajo, tapa la olla y cocina durante 20-25 minutos o hasta que el arroz esté cocido y el agua se haya evaporado. Retira del fuego y deja reposar durante unos minutos antes de servir.','13bf218f-b981-449e-a338-2da584cc6c56.jpg,',2,45,0),(6,2,'Ensalada de quinoa y vegetales','Receta de Camarones a la Diabla Camarones a la Diabla Camarones a la Diabla Camarones a la Diabla Camarones a la Diabla Camarones a la Diabla Camarones a la Diabla  Camarones a la Diabla Camarones a la Diabla','1 taza de quinoa, 2 tazas de agua, 1 pepino cortado en cubos, 1 pimiento rojo cortado en cubos, 1 cebolla roja cortada en cubos, 1/2 taza de hojas de cilantro picado, 1/2 taza de hojas de menta picado, 1/4 taza de aceite de oliva, 2 cucharadas de jugo de limón, Sal y pimienta al gusto','Enjuaga la quinoa en un colador y luego colócala en una olla con agua. Lleva a ebullición, reduce el fuego a medio-bajo y tapa la olla. Cocina durante 15-20 minutos o hasta que la quinoa esté cocida y el agua se haya evaporado. Retira del fuego y deja enfriar. En un tazón grande, mezcla la quinoa cocida con el pepino, el pimiento y la cebolla roja. Agrega el cilantro y la menta picados y mezcla bien. Agrega el aceite de oliva y el jugo de limón y mezcla de nuevo. Agrega sal y pimienta al gusto y mezcla bien antes de servir.','76eb77fc-17f2-4a14-afcd-48e664229445.jpg,',4,30,0),(7,1,'Pollo al horno con verduras','Una deliciosa y saludable receta de pollo asado con vegetales.','4 pechugas de pollo, 2 patatas, 2 zanahorias, 1 cebolla, 1 calabacín, 4 dientes de ajo, 4 ramas de tomillo fresco, 4 ramas de romero fresco, Aceite de oliva, Sal y pimienta negra molida','Precalentar el horno a 200 °C. Pelar y cortar las patatas en rodajas gruesas. Pelar y cortar las zanahorias en trozos grandes.Pelar y cortar la cebolla en cuartos.Cortar el calabacín en rodajas gruesas.Colocar todas las verduras en una bandeja de horno y rociar con aceite de oliva.Espolvorear con sal y pimienta y mezclar bien.Colocar las pechugas de pollo encima de las verduras.Sazonar las pechugas de pollo con sal y pimienta negra molida.Sazonar las pechugas de pollo con sal y pimienta negra molida.Sazonar las pechugas de pollo con sal y pimienta negra molida.Hornear durante 40-45 minutos, o hasta que el pollo esté bien cocido y las verduras estén tiernas y doradas.Servir caliente.','3440b370-4b78-40f9-a429-ad35d2ab490d.jpg,',2,60,0),(8,1,'Pasta con salsa de tomate y albahaca','Una deliciosa y saludable receta de Pasta con salsa de tomate y albahaca.','500g de pasta (preferiblemente spaghetti),2 latas de tomates pelados,1 cebolla picada,3 dientes de ajo picados,1 taza de hojas de albahaca fresca,1/2 taza de aceite de oliva,Sal y pimienta al gusto,Queso parmesano rallado (opcional)','Cocina la pasta en una olla grande de agua con sal siguiendo las instrucciones del paquete hasta que esté al dente. Mientras tanto, en una sartén grande, calienta el aceite de oliva a fuego medio. Agrega la cebolla y el ajo y cocina hasta que estén dorados. Añade las latas de tomate pelado (incluyendo su jugo) a la sartén y mezcla bien. Cocina a fuego medio durante unos 10-15 minutos, revolviendo ocasionalmente.Agrega las hojas de albahaca fresca a la salsa de tomate y mezcla bien. Continúa cocinando a fuego medio durante otros 5 minutos.Agrega la pasta cocida a la sartén con la salsa de tomate y albahaca. Mezcla bien para que la pasta esté completamente cubierta con la salsa.Sirve caliente y agrega queso parmesano rallado si lo deseas. ¡Disfruta de tu deliciosa pasta con salsa de tomate y albahaca!','fcad6952-4440-47e3-8313-bfc5cf517071.jpg,',2,30,0),(9,2,'Sopa de pollo y verduras','Una deliciosa y saludable receta de Pasta con salsa de tomate y albahaca.','1 cebolla picada, 3 dientes de ajo picados, 2 zanahorias peladas y cortadas en rodajas, 2 tallos de apio cortados en rodajas, 1 taza de champiñones cortados en cuartos, 1 pechuga de pollo sin piel ni hueso, cortada en cubos, 8 tazas de caldo de pollo, 1 cucharada de tomillo seco, 1 cucharada de romero seco, Sal y pimienta al gusto, 1 taza de espinacas frescas, Jugo de limón fresco (opcional)','Calienta el aceite de oliva en una olla grande a fuego medio-alto. Agrega las pechugas de pollo y cocina hasta que estén doradas por ambos lados, aproximadamente 5-6 minutos. Retira el pollo de la olla y reserva.En la misma olla, agrega la cebolla, las zanahorias y el apio. Cocina durante unos 5 minutos, revolviendo ocasionalmente, hasta que las verduras estén suaves.Agrega el caldo de pollo y el agua a la olla y mezcla bien. Vuelve a poner el pollo en la olla y agrega sal y pimienta al gusto.Deja que la sopa hierva a fuego medio durante unos 20-25 minutos, hasta que el pollo esté completamente cocido.Retira las pechugas de pollo de la olla y córtalas en trozos pequeños. Agrega el arroz cocido a la sopa y mezcla bien. Vuelve a poner los trozos de pollo en la olla y cocina a fuego lento durante unos 5-10 minutos más.Sirve caliente y disfruta de tu deliciosa sopa de pollo con verduras.','4ffa0a5e-86aa-40f1-bbbd-f91560576a59.jpg,',2,45,0);
/*!40000 ALTER TABLE `receta` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'team.dev','dev','dev','1234','dev@team.com'),(2,'Efren Ortega','undefined','undefined','1234','efren@email.com');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-03-20 21:31:56