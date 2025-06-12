
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

alter  table stock_volume_trend modify  column id  varchar(100);


CREATE TABLE `stock_market` (
                                `code` int(11) NOT NULL AUTO_INCREMENT,
                                `name` VARCHAR(50) DEFAULT NULL COMMENT '名字',
                                `create_date` date DEFAULT NULL,
                                `update_date` date DEFAULT NULL,
                                `description` VARCHAR(500) DEFAULT NULL COMMENT '说明',
                                `main_business` VARCHAR(600) DEFAULT NULL COMMENT '主营业务',
                                `location` VARCHAR(30) DEFAULT NULL COMMENT '所在城市',
                                `profit_loss` VARCHAR(10) DEFAULT NULL COMMENT '盈亏状况',
                                `delisting_risk` VARCHAR(20) DEFAULT NULL COMMENT '退市风险',
                                `volume` int(11) DEFAULT NULL COMMENT '市值',
                                PRIMARY KEY (`code`),
                                UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `stock_concept` (
                                 `id` int(11) NOT NULL AUTO_INCREMENT,
                                 `create_date` date DEFAULT NULL,
                                 `update_date` date DEFAULT NULL,
                                 `name` VARCHAR(50) DEFAULT NULL COMMENT '名字',
                                 `description` VARCHAR(500) DEFAULT NULL COMMENT '说明',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `stock_board` (
                               `id` int(11) NOT NULL AUTO_INCREMENT,
                               `create_date` date DEFAULT NULL,
                               `update_date` date DEFAULT NULL,
                               `name` VARCHAR(50) DEFAULT NULL COMMENT '名字',
                               `description` VARCHAR(500) DEFAULT NULL COMMENT '说明',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `stock_market_concept` (
                                        `id` int(11) NOT NULL AUTO_INCREMENT,
                                        `create_date` datetime DEFAULT NULL,
                                        `update_date` datetime DEFAULT NULL,
                                        `code` VARCHAR(50) DEFAULT NULL COMMENT '股票编码',
                                        `concept_id` VARCHAR(500) DEFAULT NULL COMMENT '概念id',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `stock_market_board` (
                                      `id` int(11) NOT NULL AUTO_INCREMENT,
                                      `create_date` date DEFAULT NULL,
                                      `update_date` date DEFAULT NULL,
                                      `code` VARCHAR(50) DEFAULT NULL COMMENT '股票编码',
                                      `board_id` VARCHAR(500) DEFAULT NULL COMMENT '板块id',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;


alter  table stock_concept modify  column id  varchar(100);
alter  table stock_board modify  column id  varchar(100);
alter  table stock_market_concept modify  column id  varchar(100);
alter  table stock_market_board modify  column id  varchar(100);

CREATE TABLE `stock` (
                         `code` VARCHAR(100) NOT NULL,
                         `create_date` datetime DEFAULT NULL,
                         `update_date` datetime DEFAULT NULL,
                         `name` VARCHAR(500) DEFAULT NULL COMMENT '股票名',
                         PRIMARY KEY (`code`),
                         UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

ALTER TABLE stock ADD COLUMN `concepts` VARCHAR(1000) DEFAULT '' COMMENT  '概念';

ALTER TABLE stock ADD COLUMN `industry` VARCHAR(1000) DEFAULT '' COMMENT  '行业';

ALTER TABLE stock ADD COLUMN `main_business` VARCHAR(1000) DEFAULT '' COMMENT  '主营业务';

ALTER TABLE stock ADD COLUMN `company_introduce` VARCHAR(4000) DEFAULT '' COMMENT  '公司介绍';


ALTER TABLE stock ADD COLUMN `study_time` date DEFAULT null COMMENT  '学习时间';


ALTER TABLE stock ADD COLUMN `study_status` VARCHAR(1000) DEFAULT '0' COMMENT  '学习状态';

ALTER TABLE stock ADD COLUMN `market_cap` VARCHAR(32) DEFAULT '0' COMMENT  '市值';
ALTER TABLE stock ADD COLUMN `market_value` VARCHAR(32) DEFAULT '0' COMMENT  '流通市值';
ALTER TABLE stock ADD COLUMN `profit_loss` VARCHAR(2) DEFAULT '0' COMMENT  '盈亏状况，1是盈利，2是亏损';
ALTER TABLE stock ADD COLUMN `price` VARCHAR(12) DEFAULT '0' COMMENT  '股价';

ALTER TABLE stock ADD COLUMN `company_location` VARCHAR(200) DEFAULT '' COMMENT  '公司地址';
ALTER TABLE stock ADD COLUMN `pe` VARCHAR(200) DEFAULT '' COMMENT  'PE';
ALTER TABLE stock ADD COLUMN `description` VARCHAR(2000) DEFAULT '' COMMENT  '备注';
ALTER TABLE stock ADD COLUMN `rate` VARCHAR(10) DEFAULT '' COMMENT  '涨跌幅';

CREATE TABLE `stock_today` (
                               `code` VARCHAR(24) NOT NULL COMMENT '股票编码',
                               `create_date` date DEFAULT NULL,
                               `update_date` date DEFAULT NULL,
                               PRIMARY KEY (`code`),
                               UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

