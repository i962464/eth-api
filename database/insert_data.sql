INSERT INTO `currency_info`(`id`, `network_id`, `asset`, `sf_asset_id`, `fee_asset`, `state`, `contract_address`, `decimals`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (1, 60, 'ETH', 'ETH_1', 'ETH', 1, '', 18, 0, 0, NULL, '2024-05-11 09:39:23', '2024-05-11 09:16:45');
INSERT INTO `currency_info`(`id`, `network_id`, `asset`, `sf_asset_id`, `fee_asset`, `state`, `contract_address`, `decimals`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (2, 60, 'aDAI', 'aDAI', 'ETH', 1, '0x67550Df3290415611F6C140c81Cd770Ff1742cb9', 6, 0, 0, NULL, '2024-05-13 11:16:07', '2024-05-11 09:39:14');

INSERT INTO `pundi`.`user_address`(`id`, `uid`, `type`, `hd_index`, `wallet_name`, `network_id`, `asset`, `address`, `private_key`, `tag`, `state`, `note`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (1000, 7, 0, NULL, '钱包测试1', 60, 'ETH', '0x8ce4092e890c5e21d1596156edc73ab00242b20d', '11e807ffd2af91ad19a093c9613f116139848b6bf10f9fb8f2f0f138f7b44ec4', '', 1, '', 0, 0, NULL, '2024-05-15 14:24:48', '2024-05-11 18:26:32');
INSERT INTO `pundi`.`user_address`(`id`, `uid`, `type`, `hd_index`, `wallet_name`, `network_id`, `asset`, `address`, `private_key`, `tag`, `state`, `note`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (1002, 9, 0, NULL, 'string', 60, 'ETH', '0x3f280ad375aec7f1cd6d943984f4f81c2f3e5871', '7b29a189fc90cec6cd58c4c12196273d3c7408da1c47a638a4c73c42ff328d60', '', 1, '', 0, 0, NULL, '2024-05-15 14:24:48', '2024-05-14 11:24:52');
INSERT INTO `pundi`.`user_address`(`id`, `uid`, `type`, `hd_index`, `wallet_name`, `network_id`, `asset`, `address`, `private_key`, `tag`, `state`, `note`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (1003, 8, 1, 0, 'HD钱包1', 60, 'ETH', '0x907fd0693e3ca1343b4ba4bf5e63361e8b6a2a85', '', '', 1, '', 0, 0, NULL, '2024-05-15 14:31:11', '2024-05-15 14:29:52');
INSERT INTO `pundi`.`user_address`(`id`, `uid`, `type`, `hd_index`, `wallet_name`, `network_id`, `asset`, `address`, `private_key`, `tag`, `state`, `note`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (1006, 10, 1, 1, 'HD钱包测试2', 60, 'ETH', '0x8092c7aae282144daa017e93d83fe30b66870daa', '', '', 1, '', 0, 0, NULL, '2024-05-15 14:38:05', '2024-05-15 14:38:05');

INSERT INTO `pundi`.`user`(`id`, `name`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (7, '普通用户1', 0, 0, NULL, '2024-05-17 14:23:03', '2024-05-10 18:34:05');
INSERT INTO `pundi`.`user`(`id`, `name`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (8, 'HD用户1', 0, 0, NULL, '2024-05-14 10:58:54', '2024-05-14 10:58:54');
INSERT INTO `pundi`.`user`(`id`, `name`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (9, '普通用户2', 0, 0, NULL, '2024-05-17 14:23:09', '2024-05-14 10:58:54');
INSERT INTO `pundi`.`user`(`id`, `name`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (10, 'HD用户2', 0, 0, NULL, '2024-05-15 14:32:21', '2024-05-15 14:32:21');

INSERT INTO `eth_scan_block`(`id`, `network_id`, `end_block`) VALUES (1, 60, NULL);