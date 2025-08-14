
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


CREATE TABLE stock_rules (
                             id INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                             content TEXT NOT NULL COMMENT '规则内容',
                             sort_order INT UNSIGNED DEFAULT 0 COMMENT '排序顺序',
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (id),
                             INDEX idx_sort_order (sort_order),
                             INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='股票交易规则表';




INSERT INTO stock_rules (content, sort_order) VALUES
                                                  ('看到不懂的票，特别是涨了6-9.5%的那种票，如果不是涨停的话不能买，因为其有可能是庄家诱多出货，要买只能等涨停了，封单慢慢增多的时候再买', 1),
                                                  ('不要怕卖飞，条件单尽量设置低一点，不要抱有幻想，强势的票不会一直在均价下面运行的，卖掉了就再找强势的股。', 2),
                                                  ('业绩预增的票，如果开盘不是强势，不要买，先观察竞价的价格，比如4.22号入了妙可蓝多，结果买入直接一直跌，亏6%，这种就是典型的借业绩出货，迫不及待了', 3),
                                                  ('如果是搞地天板模式的，只能翘跌停买，比如上涨0.01%买入，千万不要赌地天板，比如跌停开，一直拉到5%，肯定有很多获利盘忍不住砸盘的，只能等涨停封住了再挂单买入，半路接的话性价比不高的，因为大部分第二天可能跌停开。', 4),
                                                  ('开盘跌-4%的拉升先不要急着追，看看后续走势如何，大概率是骗炮的，拉升骗你接盘，如果是强势得话为啥会开-4%，肯定是主力跑路了，懒得维护了', 5),
                                                  ('对于那种人气票，如果一直维护高开在5-7%以上，可耐心留到两点50分，最后有可能有大资金封盘的，对标2025.04.25的步步高', 6),
                                                  ('缩量涨停的票不能去追，买到了就是大面一碗', 7),
                                                  ('跌停数量贼多的时候不要硬打板，容易大面一碗', 8),
                                                  ('翘地板的时候要等上穿开盘点才跟随买进，能避免骗炮的，自己手上有票的，应该先卖而不是先买', 9),
                                                  ('严守条件单的卖出，对于开板的票，给的阈值大概就是7-7.5%左右，跌破这个区间，无条件卖出', 10),
                                                  ('加速过的票鱼尾不要搞，因为风险大于收益', 11),
                                                  ('翘跌停只能用少量资金，这种模式不熟悉，往往造成重大亏损', 12),
                                                  ('跌过超3%的票拉升特别是尾盘不要追，大部分是骗炮', 13),
                                                  ('打板操作的个股，一定要先分析前面的k线，是不是两三板都吃独食的，比如成交量低于8000万的，这种竞价多强都不要去接，不然吃到天地板就是大面一碗', 14),
                                                  ('不管多看好的票都要设置一个条件单，跌破接受的价格就无条件清仓', 15),
                                                  ('翘跌停不要搞那种缩量涨停然后天地版的标的，没人会来接盘，要搞那种前一天有资金进来接盘的票', 16),
                                                  ('不在交易规划内的票不要盲目追涨，特别是那种高开有点幅度的，然后磨磨蹭蹭拉了一点又跌好多的那种，应该是庄家出货导致的', 17),
                                                  ('即使多看好的票都要设置条件单，当能跌破那个价格肯定有鬼，应该保留子弹去干最强的票，而不应把希望留在这种弱势票上面。', 18),
                                                  ('公告大利好，早盘涨停单一直撤的那种票千万不要买，风险大于机会，容易天地板大面。', 19),
                                                  ('周四周五是个情绪退潮期的分界线，特别是周四，观察一下短线亏钱效应，如果连人气股都亏钱的话，那么恐慌情绪就会蔓延，手上有的票该考虑止盈了而不是一股脑冲进去', 20),
                                                  ('条件单卖飞的股，看到拉涨，先看看什么情况，不要无脑的追的，特别是是亏1个点卖，然后涨4%-5%的时候再追来，亏的慌', 21),
                                                  ('周五一般是一个退潮的点，切忌打板，一般这时候是属于题材切换，潮水退去，应该是干新的概念', 22),
                                                  ('当自己的跑路个股都绿了一大片的时候，就知道情绪很差了，这时候先想着先跑路而不是去接盘', 23),
                                                  ('早上半路的时候，挑选个股，先看拐头，如果拐头向下的，虽然大单净流入，但是拐头向下的，不要追', 24),
                                                  ('早上追涨模式，如果是前期高位股，比如五六连板的时候，不要傻乎乎一股脑的跟着追，先看流入资金，至少出现好几手万手大单，不然就是诱多出货，比如通达电器，都6板了，首阴之后，第二天自己在涨2%的时候追，结果上套了，直接回落12%，血亏', 25),
                                                  ('专注自己的模式，之前的高位股早盘涨3%以上别追了，骗炮的居多，抵住诱惑', 26),
                                                  ('观察当前的连板池板块，当最高连板低于4板，不要追高板了，基本情绪很差', 27),
                                                  ('经常遇到一些早盘出货的票，故意拉高一两个点，装的资金大幅流入的样子，然后一直砸盘，一整天都在卖卖卖的，如何避免被这种票收割呢，就是看主动买入的，如果没有连续大手进来，而且绿单较多的话，那么宁愿放弃吧，买到骗炮的票，轻轻松松能亏5-7%', 28),
                                                  ('对于那些当天涨幅在5%以上的票，如果不好把握卖点的话，也设一个条件单，比如3.5-4.5%就卖，冲不上就是弱势的票', 29),
                                                  ('早盘低开的票先不要慌，观察一下成交额，如果低于5000万可以先观察一下，可能只是洗盘，超过1个亿就要小心了，说明出货', 30),
                                                  ('早上设置条件单的时候，不要设太近了，容易卖飞，如果是低开的，那可以设置近一点，如果是高于2%的，就设到绿盘比如-0.5%左右，如果真跌破这个价位说明卖压比较重了', 31),
                                                  ('当一个板块真的很强的时候，即使早盘打板显示不强的话也不要担心，干就对了，因为板块的强导致后排也能吃肉。', 32),
                                                  ('要翘板不要在高点接手，比如他跌-5%，涨到-1%的时候，千万不要接盘，这时候回落砸的很伤，要不就是红盘再买，如果强的话为啥不能红盘呢', 33),
                                                  ('周五不打板', 34),
                                                  ('当你真正意识到所有价格的波动都是随机的，你能控制的只有自己的止损和出入场，你就会放弃所有对市场的主观推测、分析等等妄想的行为。交易的本质是用确定性的风险，来博取不确定性的收益。失败者总在质疑和主观预测中错失良机，成功者则是用逆向思维、概率思维、风险博弈思维，专心在找市场上风险最小的机会利用人性来大胆博弈，赚取情绪的流动性溢价。你要更多考虑的是这笔交易如果失败、它会怎么失败、他会亏损多少，要如何做到少亏，而非想着如何盈利。', 35),
                                                  ('尾盘如果要低吸的话千万不要追高，比如跌5-6%的，忽然拉到-3-2%，没有量的不要跟，低吸重在低。能跌这么多，要不就是洗盘。吸的话一定要在均线以下吸。如果没吸到宁愿不买', 36),
                                                  ('当出现一个大题材前排个股买不到的时候，特别几十个一字板涨停的话，进场最好的方式就是买那种相关etf进行套利，说不定也有20左右的收益，要拿先手', 37),
                                                  ('历史性的大题材强了两三天的时候，不适合再去冲那些后排的票，往往是套利的，涨了20%再去接容易被套。', 38),
                                                  ('连续一字板涨停吃独食的票不要接，接到了就是大面一碗，翘板也不要翘这种，没有充分换手，全是获利盘，抛压太大了。', 39),
                                                  ('大题材杀跌分歧那天如果持票跌多的话比如-5.5-7.5%开的话线不要恐慌，一般都会拉升一下的，早盘这样设置条件单就会很亏。', 40);

-- 创建操作规则表
CREATE TABLE IF NOT EXISTS trade_rules (
                                           id INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                           content TEXT NOT NULL COMMENT '规则内容',
                                           sort_order INT UNSIGNED DEFAULT 0 COMMENT '排序顺序',
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           PRIMARY KEY (id),
    INDEX idx_sort_order (sort_order),
    INDEX idx_created_at (created_at)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作规则表';
----自选股
create table my_stock select * from stock where 1=2;
alter table my_stock add unique (code);
ALTER TABLE my_stock ADD COLUMN `turnover_rate` VARCHAR(10) DEFAULT '' COMMENT  '换手率';
ALTER TABLE my_stock ADD COLUMN `volality` VARCHAR(20) DEFAULT '' COMMENT  '市值';
ALTER TABLE my_stock ADD COLUMN `trading_volume` VARCHAR(20) DEFAULT '' COMMENT  '成交额';
ALTER TABLE my_stock ADD COLUMN `min_price` VARCHAR(20) DEFAULT '' COMMENT  '最低价';
ALTER TABLE my_stock ADD COLUMN `max_price` VARCHAR(20) DEFAULT '' COMMENT  '最高价成交额';
ALTER TABLE my_stock ADD COLUMN `user_id` VARCHAR(20) DEFAULT 'admin' COMMENT  '用户id';
ALTER TABLE my_stock ADD COLUMN `quantity_ratio` VARCHAR(20) DEFAULT '' COMMENT  '量比';