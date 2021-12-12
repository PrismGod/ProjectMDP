-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 12 Des 2021 pada 11.46
-- Versi server: 10.4.20-MariaDB
-- Versi PHP: 8.0.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `projectmdp`
--
CREATE DATABASE IF NOT EXISTS `projectmdp` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `projectmdp`;

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `banned` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`username`, `password`, `banned`) VALUES
('123', '123', 0),
('123321', '123321', 0),
('asd', 'asd', 0),
('sylveon', 'ninfia', 0),
('user1', 'user1', 0),
('user2', 'user2', 0),
('user3', 'user3', 0),
('user4', 'user4', 0);

-- --------------------------------------------------------

--
-- Struktur dari tabel `user_comment`
--

DROP TABLE IF EXISTS `user_comment`;
CREATE TABLE `user_comment` (
  `username` varchar(50) DEFAULT NULL,
  `movie_id` varchar(11) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `user_comment`
--

INSERT INTO `user_comment` (`username`, `movie_id`, `comment`) VALUES
('123', '634649', 'I cannot wait for this movie to be released.'),
('asd', '580489', 'Very shocking post credit scene'),
('123', '634649', 'Will this series be related to post credit scene of venom?');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user_favorite`
--

DROP TABLE IF EXISTS `user_favorite`;
CREATE TABLE `user_favorite` (
  `username` varchar(50) DEFAULT NULL,
  `movie_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `user_favorite`
--

INSERT INTO `user_favorite` (`username`, `movie_id`) VALUES
('123', 580489),
('123', 370172),
('123', 634649);

-- --------------------------------------------------------

--
-- Struktur dari tabel `user_watchlist`
--

DROP TABLE IF EXISTS `user_watchlist`;
CREATE TABLE `user_watchlist` (
  `username` varchar(50) DEFAULT NULL,
  `movie_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `user_watchlist`
--

INSERT INTO `user_watchlist` (`username`, `movie_id`) VALUES
('asd', 843241),
('asd', 580489),
('asd', 617653),
('123', 580489);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
