-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 21 Des 2024 pada 07.13
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `laundryyuk`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admin`
--

CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL,
  `date_created` datetime(6) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `last_updated` datetime(6) NOT NULL,
  `nama` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','CUSTOMER') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `admin`
--

INSERT INTO `admin` (`id`, `date_created`, `email`, `last_updated`, `nama`, `role`) VALUES
(1, '2024-12-15 09:58:06.000000', 'jawa@admin.com', '2024-12-15 09:58:06.000000', 'jawa', 'ADMIN');

-- --------------------------------------------------------

--
-- Struktur dari tabel `customer`
--

CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL,
  `date_created` datetime(6) NOT NULL,
  `last_updated` datetime(6) NOT NULL,
  `nama` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','CUSTOMER') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `customer`
--

INSERT INTO `customer` (`id`, `date_created`, `last_updated`, `nama`, `email`, `role`) VALUES
(6, '2024-12-15 09:57:46.000000', '2024-12-15 09:57:46.000000', 'Dimas', 'dimas@gmail.com', 'CUSTOMER'),
(7, '2024-12-19 05:06:02.000000', '2024-12-19 05:06:02.000000', 'steven', 'steven@gmail.com', 'CUSTOMER');

-- --------------------------------------------------------

--
-- Struktur dari tabel `order`
--

CREATE TABLE `order` (
  `id` bigint(20) NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `date_created` datetime(6) NOT NULL,
  `last_updated` datetime(6) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `order_status` enum('DIPROSES','MENUNGGU','SELESAI') DEFAULT NULL,
  `price` double DEFAULT NULL,
  `weight` double DEFAULT NULL,
  `payment_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `order`
--

INSERT INTO `order` (`id`, `date`, `date_created`, `last_updated`, `status`, `customer_id`, `order_status`, `price`, `weight`, `payment_id`) VALUES
(1, NULL, '2024-12-15 10:00:02.000000', '2024-12-15 10:00:02.000000', NULL, 6, 'DIPROSES', 20000, 2, 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `payment`
--

CREATE TABLE `payment` (
  `id` bigint(20) NOT NULL,
  `date_created` datetime(6) NOT NULL,
  `last_updated` datetime(6) NOT NULL,
  `nomimal` double DEFAULT NULL,
  `status_payment` enum('BELUM','SUDAH') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `payment`
--

INSERT INTO `payment` (`id`, `date_created`, `last_updated`, `nomimal`, `status_payment`) VALUES
(1, '2024-12-15 09:59:38.000000', '2024-12-15 10:03:18.000000', 20000, 'SUDAH'),
(2, '2024-12-15 10:00:40.000000', '2024-12-15 10:00:40.000000', 50000, 'BELUM');

-- --------------------------------------------------------

--
-- Struktur dari tabel `report`
--

CREATE TABLE `report` (
  `id` bigint(20) NOT NULL,
  `date_created` datetime(6) NOT NULL,
  `last_updated` datetime(6) NOT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `report_income`
--

CREATE TABLE `report_income` (
  `id` bigint(20) NOT NULL,
  `date_created` datetime(6) NOT NULL,
  `last_updated` datetime(6) NOT NULL,
  `total_income` double DEFAULT NULL,
  `admin_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `report_income`
--

INSERT INTO `report_income` (`id`, `date_created`, `last_updated`, `total_income`, `admin_id`) VALUES
(1, '2024-12-15 10:00:59.000000', '2024-12-15 10:00:59.000000', NULL, 1),
(2, '2024-12-19 05:08:11.000000', '2024-12-19 05:08:11.000000', 244444, 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `report_incomes`
--

CREATE TABLE `report_incomes` (
  `report_income_id` bigint(20) NOT NULL,
  `payment_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `report_incomes`
--

INSERT INTO `report_incomes` (`report_income_id`, `payment_id`) VALUES
(1, 1),
(1, 2);

-- --------------------------------------------------------

--
-- Struktur dari tabel `report_order`
--

CREATE TABLE `report_order` (
  `id` bigint(20) NOT NULL,
  `date_created` datetime(6) NOT NULL,
  `last_updated` datetime(6) NOT NULL,
  `admin_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `report_orders`
--

CREATE TABLE `report_orders` (
  `report_order_id` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `report_review`
--

CREATE TABLE `report_review` (
  `id` bigint(20) NOT NULL,
  `date_created` datetime(6) NOT NULL,
  `last_updated` datetime(6) NOT NULL,
  `admin_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `report_reviews`
--

CREATE TABLE `report_reviews` (
  `report_review_id` bigint(20) NOT NULL,
  `review_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `review`
--

CREATE TABLE `review` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `date_created` datetime(6) NOT NULL,
  `last_updated` datetime(6) NOT NULL,
  `customer_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `konten` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKlfiqju7cjguy7ug64ay1sspjq` (`payment_id`),
  ADD KEY `FK1oduxyuuo3n2g98l3j7754vym` (`customer_id`);

--
-- Indeks untuk tabel `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `report_income`
--
ALTER TABLE `report_income`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK7xd8ttpj1plvatxgdpufufpqw` (`admin_id`);

--
-- Indeks untuk tabel `report_incomes`
--
ALTER TABLE `report_incomes`
  ADD PRIMARY KEY (`report_income_id`,`payment_id`),
  ADD KEY `FKecr7lmam3erey4ed93096nag1` (`payment_id`);

--
-- Indeks untuk tabel `report_order`
--
ALTER TABLE `report_order`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKmmf6ddpafejv1t8jsoj0cntg3` (`admin_id`);

--
-- Indeks untuk tabel `report_orders`
--
ALTER TABLE `report_orders`
  ADD PRIMARY KEY (`report_order_id`,`order_id`),
  ADD KEY `FK72mesyrxqy5cl9276bfipyd4b` (`order_id`);

--
-- Indeks untuk tabel `report_review`
--
ALTER TABLE `report_review`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5c82qnwg1q8ld70ekfcrrs0va` (`admin_id`);

--
-- Indeks untuk tabel `report_reviews`
--
ALTER TABLE `report_reviews`
  ADD PRIMARY KEY (`report_review_id`,`review_id`),
  ADD KEY `FKj7dul3vvni27d2ut8jyjijmm5` (`review_id`);

--
-- Indeks untuk tabel `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKgce54o0p6uugoc2tev4awewly` (`customer_id`),
  ADD KEY `FK80acgchiskxpcqegik62mf1jg` (`order_id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `admin`
--
ALTER TABLE `admin`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `customer`
--
ALTER TABLE `customer`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT untuk tabel `order`
--
ALTER TABLE `order`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `payment`
--
ALTER TABLE `payment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `report`
--
ALTER TABLE `report`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `report_income`
--
ALTER TABLE `report_income`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `report_order`
--
ALTER TABLE `report_order`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `report_review`
--
ALTER TABLE `report_review`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `review`
--
ALTER TABLE `review`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `FK1oduxyuuo3n2g98l3j7754vym` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  ADD CONSTRAINT `FKhgqyhl4wnj9use215racmlveo` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`);

--
-- Ketidakleluasaan untuk tabel `report_income`
--
ALTER TABLE `report_income`
  ADD CONSTRAINT `FK7xd8ttpj1plvatxgdpufufpqw` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`);

--
-- Ketidakleluasaan untuk tabel `report_incomes`
--
ALTER TABLE `report_incomes`
  ADD CONSTRAINT `FKecr7lmam3erey4ed93096nag1` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`),
  ADD CONSTRAINT `FKgjn5jt3yl9itsq86o6q57u858` FOREIGN KEY (`report_income_id`) REFERENCES `report_income` (`id`);

--
-- Ketidakleluasaan untuk tabel `report_order`
--
ALTER TABLE `report_order`
  ADD CONSTRAINT `FKmmf6ddpafejv1t8jsoj0cntg3` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`);

--
-- Ketidakleluasaan untuk tabel `report_orders`
--
ALTER TABLE `report_orders`
  ADD CONSTRAINT `FK72mesyrxqy5cl9276bfipyd4b` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`),
  ADD CONSTRAINT `FKa1k8ob0o5yjee5r60hke8gt1f` FOREIGN KEY (`report_order_id`) REFERENCES `report_order` (`id`);

--
-- Ketidakleluasaan untuk tabel `report_review`
--
ALTER TABLE `report_review`
  ADD CONSTRAINT `FK5c82qnwg1q8ld70ekfcrrs0va` FOREIGN KEY (`admin_id`) REFERENCES `admin` (`id`);

--
-- Ketidakleluasaan untuk tabel `report_reviews`
--
ALTER TABLE `report_reviews`
  ADD CONSTRAINT `FKhm6ucevqbh6ffxxpw6tkga9v4` FOREIGN KEY (`report_review_id`) REFERENCES `report_review` (`id`),
  ADD CONSTRAINT `FKj7dul3vvni27d2ut8jyjijmm5` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`);

--
-- Ketidakleluasaan untuk tabel `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `FK80acgchiskxpcqegik62mf1jg` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`),
  ADD CONSTRAINT `FKgce54o0p6uugoc2tev4awewly` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
