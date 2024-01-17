DROP SCHEMA IF EXISTS `Reading-List-App-10`;

CREATE SCHEMA `Reading-List-App-11`;

use `Reading-List-App-11`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `author`;

CREATE TABLE `author` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `first_name` varchar(255) DEFAULT NULL,
    `last_name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `author_detail`;

CREATE TABLE `author_detail` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `citizenship` varchar(255) DEFAULT NULL,
    `author_id` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_AUTHOR` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `author_id` int(11) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_AUTHOR_idx` (`author_id`),
  CONSTRAINT `FK_AUTHOR` FOREIGN KEY (`author_id`)
  REFERENCES `author` (`id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;