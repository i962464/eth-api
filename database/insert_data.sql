INSERT INTO `currency_info`(`id`, `network_id`, `asset`, `sf_asset_id`, `fee_asset`, `state`, `contract_address`, `decimals`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (1, 60, 'ETH', 'ETH_1', 'ETH', 1, '', 18, 0, 0, NULL, '2024-05-11 09:39:23', '2024-05-11 09:16:45');
INSERT INTO `currency_info`(`id`, `network_id`, `asset`, `sf_asset_id`, `fee_asset`, `state`, `contract_address`, `decimals`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (2, 60, 'aDAI', 'aDAI', 'ETH', 1, '0x67550Df3290415611F6C140c81Cd770Ff1742cb9', 6, 0, 0, NULL, '2024-05-13 11:16:07', '2024-05-11 09:39:14');

INSERT INTO `user_address`(`id`, `uid`, `wallet_name`, `network_id`, `asset`, `address`, `private_key`, `tag`, `state`, `note`, `version`, `deleted`, `deleted_at`, `updated_at`, `created_at`) VALUES (1000, 7, '钱包测试1', 60, 'ETH', '0x8ce4092e890c5e21d1596156edc73ab00242b20d', '11e807ffd2af91ad19a093c9613f116139848b6bf10f9fb8f2f0f138f7b44ec4', '', 1, '', 0, 0, NULL, '2024-05-11 18:26:32', '2024-05-11 18:26:32');
INSERT INTO `eth_scan_block`(`id`, `network_id`, `end_block`) VALUES (1, 60, NULL);