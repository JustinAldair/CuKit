-- Host: 127.0.0.1    Database: cukit

CREATE DATABASE cukit;
USE cukit;


DROP TABLE IF EXISTS `categoria`;

CREATE TABLE `categoria` (
  `idcategoria` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idcategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=2;


DROP TABLE IF EXISTS `comentarios`;

CREATE TABLE `comentarios` (
  `idcomentarios` int NOT NULL AUTO_INCREMENT,
  `idReceta` int NOT NULL,
  `usuario` varchar(45) NOT NULL,
  `comentario` text NOT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`idcomentarios`),
  KEY `idReceta_idx` (`idReceta`),
  CONSTRAINT `idReceta` FOREIGN KEY (`idReceta`) REFERENCES `receta` (`idreceta`)
) ENGINE=InnoDB AUTO_INCREMENT=8;


DROP TABLE IF EXISTS `perfil`;

CREATE TABLE `perfil` (
  `idperfil` int NOT NULL AUTO_INCREMENT,
  `idUsuario` int NOT NULL,
  `usuario` varchar(45) NOT NULL,
  `url_foto` varchar(45) DEFAULT NULL,
  `pais` varchar(45) NOT NULL,
  PRIMARY KEY (`idperfil`),
  KEY `idUsuario_idx` (`idUsuario`),
  CONSTRAINT `idUsuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3;


DROP TABLE IF EXISTS `puntuacion`;

CREATE TABLE `puntuacion` (
  `idpuntuacion` int NOT NULL AUTO_INCREMENT,
  `idReceta` int NOT NULL,
  `usuario` varchar(45) NOT NULL,
  `puntuacion` float NOT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`idpuntuacion`),
  KEY `idReceta_idx` (`idReceta`),
  CONSTRAINT `idRecetaPuntuacion` FOREIGN KEY (`idReceta`) REFERENCES `receta` (`idreceta`)
) ENGINE=InnoDB AUTO_INCREMENT=7;


DROP TABLE IF EXISTS `receta`;

CREATE TABLE `receta` (
  `idreceta` int NOT NULL AUTO_INCREMENT,
  `idPerfil` int NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `descripcion` text NOT NULL,
  `ingredientes` text NOT NULL,
  `instrucciones` text NOT NULL,
  `url_fotos` text NOT NULL,
  `idCategoria` int NOT NULL,
  `status` tinyint NOT NULL,
  PRIMARY KEY (`idreceta`),
  KEY `idCategoria_idx` (`idCategoria`),
  KEY `idPerfil_idx` (`idPerfil`),
  CONSTRAINT `idCategoria` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idcategoria`),
  CONSTRAINT `idPerfil` FOREIGN KEY (`idPerfil`) REFERENCES `perfil` (`idperfil`)
) ENGINE=InnoDB AUTO_INCREMENT=6;


DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `apellido_p` varchar(45) NOT NULL,
  `apellido_m` varchar(45) NOT NULL,
  `pass` varchar(45) NOT NULL,
  `correo` varchar(45) NOT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=15;