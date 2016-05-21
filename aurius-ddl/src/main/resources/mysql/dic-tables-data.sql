INSERT INTO `operativas_op` VALUES ('AURIUS',NULL,'Operativas de Sistema');
INSERT INTO `operativas_op` VALUES ('SAMPLE',NULL,'Ejemplo de operativa');


INSERT INTO `diccionario_dic` VALUES ('AURIUS',00001,0,NULL,'OPERATIVAS',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00002,0,NULL,'OPERATIVA',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00003,0,NULL,'PAGINACION',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00004,0,NULL,'SVR_DESC',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00005,1,NULL,'OP_GRUPO',0000000006,0000000006,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00006,1,NULL,'OP_DESC',0000000000,0000000255,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00007,1,NULL,'DICD_ID',0000000032,0000000032,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00008,1,NULL,'SVRD_DESC',0000000000,0000000255,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00009,1,NULL,'SVR_ID',0000000009,0000000009,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00010,1,NULL,'SVR_INHERIT',0000000001,0000000001,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00011,1,NULL,'SVR_PUBLIC',0000000001,0000000001,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00012,1,NULL,'SVR_VERSION',0000000002,0000000002,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00013,2,NULL,'ALL',0000000015,0000000000,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00014,2,NULL,'FIRST',0000000015,0000000000,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00015,0,NULL,'SERVICIOS',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `diccionario_dic` VALUES ('AURIUS',00016,2,NULL,'PAGE_SIZE',0000000015,0000000000,NULL,NULL,NULL,NULL,NULL,NULL);


INSERT INTO `registro_reg` VALUES ('AURIUS',00001,000,'AURIUS',00002,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00001,001,'AURIUS',00003,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00002,000,'AURIUS',00005,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00002,001,'AURIUS',00007,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00002,002,'AURIUS',00006,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00003,000,'AURIUS',00014,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00003,001,'AURIUS',00013,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00003,002,'AURIUS',00016,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00004,000,'AURIUS',00005,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00004,001,'AURIUS',00007,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00004,002,'AURIUS',00008,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00004,003,'AURIUS',00009,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00004,004,'AURIUS',00010,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00004,005,'AURIUS',00011,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00004,006,'AURIUS',00012,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00015,000,'AURIUS',00004,0,NULL,NULL);
INSERT INTO `registro_reg` VALUES ('AURIUS',00015,001,'AURIUS',00003,0,NULL,NULL);


INSERT INTO `servicio_svrd` VALUES ('SAMPLE',000,00,NULL,1,1,'Ejemplo de servicio');


INSERT INTO `servicio_svr` VALUES ('SAMPLE',000,00,'com.farmafene.aurius.svc.aurius.GetOperativasSVC','AURIUS',00001,'AURIUS',00001);
