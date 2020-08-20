CREATE TABLE `bizuserlogin` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `User_Id` int(11) DEFAULT NULL COMMENT '用户Id',
  `IP` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `DeviceType` tinyint(4) DEFAULT NULL COMMENT '设备类型',
  `DeviceId` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `LoginTime` datetime DEFAULT NULL COMMENT '登录时间',
  `DeviceOS` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `UserAgent` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ApiVersion` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `AppVersion` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `CityId` int(11) DEFAULT NULL COMMENT '城市 Id',
  PRIMARY KEY (`Id`),
  KEY `ix_CityIdAndLoginTime` (`LoginTime`,`CityId`),
  KEY `IX_UserId_LoginTime` (`User_Id`,`LoginTime`)
) ENGINE=InnoDB AUTO_INCREMENT=122746630 DEFAULT CHARSET=utf8mb4