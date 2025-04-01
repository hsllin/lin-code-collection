
-- stock.stock_sentivement_index definition

CREATE TABLE `stock_volume_trend` (
                                      `id` int(11) NOT NULL AUTO_INCREMENT,
                                      `date` date DEFAULT NULL,
                                      `volume` varchar(60) DEFAULT NULL,
                                      `input_up` int(11) DEFAULT NULL COMMENT '上涨家数',
                                      `input_down` int(11) DEFAULT NULL COMMENT '下跌家数',
                                      `limit_up` int(11) DEFAULT NULL COMMENT '涨停数',
                                      `limit_down` int(11) DEFAULT NULL COMMENT '跌停数',
                                      `limit_count` int(11) DEFAULT NULL COMMENT '连板高度',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
INSERT INTO stock.stock_volume_trend
(id, `date`, volume, input_up, input_down, limit_up, limit_down, limit_count)
VALUES(1, '2025-02-24', '2115400000000', '2778', '2468', '91', '18', '6');

INSERT INTO stock.stock_volume_trend
(id, `date`, volume, input_up, input_down, limit_up, limit_down, limit_count)
VALUES(2, '2025-02-21', '1791400000000', '3377', '1842', '107', '1', '13');

INSERT INTO stock.stock_volume_trend
(id, `date`, volume, input_up, input_down, limit_up, limit_down, limit_count)
VALUES(3, '2025-02-20', '1711800000000', '3560', '1568', '95', '0', '10');

ALTER TABLE stock_volume_trend ADD COLUMN `limit1` VARCHAR(6) DEFAULT '0' COMMENT  '1连板数量';


ALTER TABLE stock_volume_trend ADD COLUMN `limit2` VARCHAR(6) DEFAULT '0' COMMENT  '2连板数量';


ALTER TABLE stock_volume_trend ADD COLUMN `limit3` VARCHAR(6) DEFAULT '0' COMMENT  '3连板数量';


ALTER TABLE stock_volume_trend ADD COLUMN `limit4` VARCHAR(6) DEFAULT '0' COMMENT  '4连板数量';

ALTER TABLE stock_volume_trend ADD COLUMN `limit5` VARCHAR(6) DEFAULT '0' COMMENT  '5连板数量';
