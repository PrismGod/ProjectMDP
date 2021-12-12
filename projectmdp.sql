/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 10.4.20-MariaDB : Database - projectmdp
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`projectmdp` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `projectmdp`;

/*Table structure for table `movie_rating` */

DROP TABLE IF EXISTS `movie_rating`;

CREATE TABLE `movie_rating` (
  `username` varchar(50) DEFAULT NULL,
  `movie_id` int(11) DEFAULT NULL,
  `rating` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `movie_rating` */

insert  into `movie_rating`(`username`,`movie_id`,`rating`) values 
('asd',580489,5),
('asd',512195,4),
('asd',617653,3.5),
('123',580489,5),
('123',512195,3);

/*Table structure for table `user_comment` */

DROP TABLE IF EXISTS `user_comment`;

CREATE TABLE `user_comment` (
  `username` varchar(50) DEFAULT NULL,
  `movie_id` varchar(11) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `user_comment` */

insert  into `user_comment`(`username`,`movie_id`,`comment`) values 
('123','634649','I cannot wait for this movie to be released.'),
('asd','580489','Very shocking post credit scene'),
('123','634649','Will this series be related to post credit scene of venom?'),
('asd','580489','wado mantap'),
('asd','580489','nice btw');

/*Table structure for table `user_favorite` */

DROP TABLE IF EXISTS `user_favorite`;

CREATE TABLE `user_favorite` (
  `username` varchar(50) DEFAULT NULL,
  `movie_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `user_favorite` */

insert  into `user_favorite`(`username`,`movie_id`) values 
('123',580489),
('123',370172),
('123',634649),
('asd',568124),
('asd',566525);

/*Table structure for table `user_watchlist` */

DROP TABLE IF EXISTS `user_watchlist`;

CREATE TABLE `user_watchlist` (
  `username` varchar(50) DEFAULT NULL,
  `movie_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `user_watchlist` */

insert  into `user_watchlist`(`username`,`movie_id`) values 
('asd',843241),
('asd',580489),
('asd',617653),
('123',580489);

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `banned` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `users` */

insert  into `users`(`username`,`password`,`banned`) values 
('123','123',0),
('123321','123321',0),
('asd','asd',0),
('sylveon','ninfia',0),
('user1','user1',0),
('user2','user2',0),
('user3','user3',0),
('user4','user4',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
