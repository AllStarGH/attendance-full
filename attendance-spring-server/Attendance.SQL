+-----------+---------------------------------------------------------------------------------------------------------------------------
---------------------------------------------+
| Table     | Create Table                                                                                                              
                                             |
+-----------+---------------------------------------------------------------------------------------------------------------------------
---------------------------------------------+
| t_account | CREATE TABLE `t_account` (                                                                                                
                                             |
|           |   `id` int(22) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',                                                           
                                             |
|           |   `employee_name` varchar(18) NOT NULL COMMENT '雇员的姓名',                                                              
                                             |
|           |   `born_time` timestamp NOT NULL COMMENT '出生日期(年月日)',                                                                                                           |
|           |   `phone` varchar(48) NOT NULL COMMENT '电话号码',                                                                                                                     |
|           |   `mailbox` varchar(99) NOT NULL COMMENT '邮箱地址',                                                                                                                   |
|           |   `portrait` varchar(252) NOT NULL COMMENT '头像(地址)',                                                                                                               |
|           |   `region` int(9) NOT NULL COMMENT '员工所在地区,0-安淮,1-南徽,2-古丰,3-方千,4-播星,5-角港,6-米园,7-湖地,8-寿韫,\n9-天玺台,10-苏崧,11-南威,12-都张集,13-企塘,14-中缇', |
|           |   `department` int(3) NOT NULL COMMENT '所属部门,0-研发,1-财务,2-人事,3-销售,4-公关,5-法务,6-安全',                                                                    |
|           |   `role` int(1) NOT NULL COMMENT '系统内角色:0-管理员,1-系统普通用户',                                                                                                 |
|           |   `employee_num` int(25) unsigned NOT NULL COMMENT '员工工号',                                                                                                         |
|           |   `password` char(55) NOT NULL COMMENT '密码密文',                                                                                                                     |
|           |   `salt` char(25) NOT NULL COMMENT '盐密钥',
|           |   `reg_time` timestamp NOT NULL COMMENT '账号注册日期',                                                                                                                  |
|           |   PRIMARY KEY (`id`),                                                                                                                                                  |
|           |   UNIQUE KEY `employee_num` (`employee_num`),
|           |   UNIQUE KEY `phone` (`phone`),                                                                                                                                        |
|           |   UNIQUE KEY `mailbox` (`mailbox`)                                                                                                                                                                                                                    |
|           | ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账号表'                                                                                    |
+-----------+------------------------------------------------------------------------------------------------------------------------------------------------------------------------+

-- -----------------------------------------------------
-- -----------------------------------------------------


