SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;


CREATE TABLE IF NOT EXISTS `CellData` (
  `latitude` int(11) NOT NULL,
  `longitude` int(11) NOT NULL,
  `xCoordinate` int(11) NOT NULL,
  `yCoordinate` int(11) NOT NULL,
  `temperature` varchar(100) NOT NULL,
  `SimulationInfo_id` bigint(20) NOT NULL,
  `Time` datetime NOT NULL,
  PRIMARY KEY (`latitude`,`longitude`,`SimulationInfo_id`,`Time`),
  KEY `SimulationInfo_id` (`SimulationInfo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `SimulationInfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `axialTilt` decimal(4,2) NOT NULL,
  `eccentricity` double(4,4) NOT NULL,
  `gridSpacing` int(11) NOT NULL,
  `timeStep` int(11) NOT NULL,
  `simulationLength` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


ALTER TABLE `CellData`
  ADD CONSTRAINT `CellData_ibfk_2` FOREIGN KEY (`SimulationInfo_id`) REFERENCES `SimulationInfo` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `CellData_ibfk_1` FOREIGN KEY (`SimulationInfo_id`) REFERENCES `SimulationInfo` (`id`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
