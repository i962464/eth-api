-- ⽤户表
create table user
(
    id   int          not null auto_increment,
    name varchar(100) not null comment '⽤户名',
    primary key (id),
    unique key uq_name (name) using hash
) engine = innodb
  default charset = utf8mb4 comment '⽤户表';
ALTER TABLE `user`
    ADD (
        `version` INT UNSIGNED NULL DEFAULT 0 COMMENT '乐观锁预留',
        `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除，冗余字段',
        `deleted_at` DATETIME NULL,
        `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        );

CREATE TABLE `currency_info`
(
    `id`               int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `network_id`       int(10) unsigned NOT NULL COMMENT '区块链网络ID',
    `symbol`           varchar(32)      NOT NULL COMMENT 'token简称',
    `name`             varchar(32)      NOT NULL COMMENT 'token名称',
    `state`            tinyint(4)       NOT NULL DEFAULT '1' COMMENT '1 可用 0 冻结',
    `contract_address` varchar(64)               DEFAULT '' COMMENT 'erc20合约地址',
    `decimals`         int(10) unsigned NOT NULL COMMENT '币种精度',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_network_id_asset` (`network_id`, `symbol`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 100
  DEFAULT CHARSET = utf8mb4 COMMENT ='币种表';
ALTER TABLE `currency_info`
    ADD (
        `version` INT UNSIGNED NULL DEFAULT 0 COMMENT '乐观锁预留',
        `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除，冗余字段',
        `deleted_at` DATETIME NULL,
        `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        );

-- 钱包地址列表
CREATE TABLE `user_address`
(
    `id`          BIGINT(64) UNSIGNED unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `uid`         int(64)          NOT NULL COMMENT '用户ID',
    `type`        tinyint(4)       NOT NULL COMMENT '钱包类型0-随机钱包，2-hd分层钱包',
    `hd_index`    int(64) COMMENT 'hd分层钱包推导索引',
    `wallet_name` varchar(32)      NOT NULL COMMENT '钱包账户名称',
    `network_id`  int(10) unsigned NOT NULL COMMENT '币种网络ID ，对应 BIP44 分配的 ID',
    `asset`       varchar(32)      NOT NULL COMMENT '对应资产(币种),如: ETH',
    `address`     varchar(64)      NOT NULL COMMENT '资产地址',
    `private_key` varchar(255) COMMENT '地址私钥，暂时写死，后期HD优化',
    `tag`         varchar(256) DEFAULT '' COMMENT 'Tag/MEMO',
    `state`       tinyint(4)   DEFAULT '1' COMMENT '状态 1: 正常 0: 禁用',
    `note`        varchar(256) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uni_uid_name_network_id_asset` (`uid`, `wallet_name`, `network_id`, `asset`) USING BTREE,
    UNIQUE KEY `uni_network_uid_address` (`network_id`, `uid`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8mb4
  ROW_FORMAT = COMPACT COMMENT ='钱包充值地址表';
ALTER TABLE `user_address`
    ADD (
        `version` INT UNSIGNED NULL DEFAULT 0 COMMENT '乐观锁预留',
        `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除，冗余字段',
        `deleted_at` DATETIME NULL,
        `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        );

-- 链上扫描最新区块 可redis缓存
CREATE TABLE `eth_scan_block`
(
    `id`         bigint NOT NULL,
    `network_id` int    DEFAULT NULL COMMENT '扩展网络预留字段',
    `end_block`  bigint DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- 本地交易记录
CREATE TABLE user_transactions
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    `uid`            int(64)     NOT NULL COMMENT '用户ID',
    sender_address   VARCHAR(42) NOT NULL,
    receiver_address VARCHAR(42) NOT NULL,
    amount           decimal(32,18)      NOT NULL,
    token_name       VARCHAR(42) NOT NULL,
    token_address    VARCHAR(64) NOT NULL,
    transaction_hash VARCHAR(66) NOT NULL
);
ALTER TABLE `user_transactions`
    ADD (
        `version` INT UNSIGNED NULL DEFAULT 0 COMMENT '乐观锁预留',
        `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除，冗余字段',
        `deleted_at` DATETIME NULL,
        `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        );


-- 线上交易记录
CREATE TABLE ether_scan_record
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    block_num        BIGINT(42)   NOT NULL,
    block_hash       VARCHAR(128) NOT NULL,
    sender_address   VARCHAR(64)  NOT NULL,
    receiver_address VARCHAR(64)  NOT NULL,
    amount           decimal(32, 18)       NOT NULL,
    token_name       VARCHAR(32)  NOT NULL,
    token_address    VARCHAR(64) default null,
    transaction_hash VARCHAR(128) NOT NULL
);
ALTER TABLE `ether_scan_record`
    ADD (
        `version` INT UNSIGNED NULL DEFAULT 0 COMMENT '乐观锁预留',
        `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除，冗余字段',
        `deleted_at` DATETIME NULL,
        `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        );
-- 充值记录（归集需求）
CREATE TABLE deposit_tx_record
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    uid               BIGINT(42)   NOT NULL,
    block_num         BIGINT(42)   NOT NULL,
    block_hash        VARCHAR(128) NOT NULL,
    sender_address    VARCHAR(64)  NOT NULL,
    receiver_address  VARCHAR(64)  NOT NULL,
    amount            decimal(32,18)       NOT NULL,
    token_name        VARCHAR(32)  NOT NULL,
    token_address     VARCHAR(64)           default null,
    transaction_hash  VARCHAR(128) NOT NULL,
    collection_status tinyint(4)   NOT NULL default 0 comment '0-未归集， 1-归集中， 2-已归集'
);
ALTER TABLE `deposit_tx_record`
    ADD (
        `version` INT UNSIGNED NULL DEFAULT 0 COMMENT '乐观锁预留',
        `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除，冗余字段',
        `deleted_at` DATETIME NULL,
        `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        );



-- twitter 相关

CREATE TABLE `user_auth_twitter_tmp` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_twitter_id` varchar(100) NOT NULL COMMENT '用户的twitter id',
  `name` varchar(100) NOT NULL COMMENT 'twitter用户名',
  `avatar` varchar(300) NOT NULL COMMENT 'twitter头像',
  `oauth_token` varchar(200) NOT NULL COMMENT '授权token',
  `oauth_token_secret` varchar(200) NOT NULL COMMENT '授权token',
  `access_token` varchar(200) DEFAULT '' COMMENT '用户token, 授权成功后回填',
  `access_token_secret` varchar(200) DEFAULT '' COMMENT '用户token secret, 授权成功后回填',
  `token` varchar(200) NOT NULL COMMENT '登陆token, 授权成功后生成',
  `status` tinyint NOT NULL COMMENT '是否已关联用户 0:未关联用户; 1:已关联用户',
  `dt` bigint NOT NULL COMMENT '创建时间',
  `expiry_time` bigint NOT NULL COMMENT 'token过期时间',
  `refresh_token` varchar(200) NOT NULL COMMENT '刷新token',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_oauth_token` (`oauth_token`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='twitter授权信息临时表';
ALTER TABLE `user_auth_twitter_tmp`
    ADD (
        `version` INT UNSIGNED NULL DEFAULT 0 COMMENT '乐观锁预留',
        `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除，冗余字段',
        `deleted_at` DATETIME NULL,
        `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        );

CREATE TABLE `user_auth_twitter` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户id',
  `user_twitter_id` varchar(100) NOT NULL COMMENT '用户的twitter id',
  `name` varchar(100) NOT NULL COMMENT 'twitter用户名',
  `avatar` varchar(300) NOT NULL COMMENT 'twitter头像',
  `access_token` varchar(200) NOT NULL COMMENT '用户token',
  `access_token_secret` varchar(200) NOT NULL COMMENT '用户token secret',
  `dt` bigint NOT NULL COMMENT '创建时间',
  `update_dt` bigint NOT NULL COMMENT '更新时间',
  `expiry_time` bigint NOT NULL COMMENT 'token过期时间',
  `refresh_token` varchar(200) NOT NULL COMMENT '刷新token',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_id` (`user_id`),
  UNIQUE KEY `uq_user_twitter_id` (`user_twitter_id`),
  UNIQUE KEY `uq_access_token` (`access_token`),
  KEY `idx_user_id_dt` (`user_id`,`dt`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户twitter授权信息';

ALTER TABLE `user_auth_twitter`
    ADD (
        `version` INT UNSIGNED NULL DEFAULT 0 COMMENT '乐观锁预留',
        `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除，冗余字段',
        `deleted_at` DATETIME NULL,
        `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        );

CREATE TABLE `user_login_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户id',
  `token` varchar(200) NOT NULL COMMENT '登陆token',
  `ip` varchar(30) NOT NULL COMMENT 'ip',
  `dt` bigint NOT NULL COMMENT '创建时间',
  `update_dt` bigint NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_id` (`user_id`),
  UNIQUE KEY `uq_token` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户登陆token';
ALTER TABLE `user_login_token`
    ADD (
        `version` INT UNSIGNED NULL DEFAULT 0 COMMENT '乐观锁预留',
        `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除，冗余字段',
        `deleted_at` DATETIME NULL,
        `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        );