SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE MEMBER;
ALTER TABLE MEMBER
    AUTO_INCREMENT = 1;

TRUNCATE TABLE MEMBER_SECURITY;
ALTER TABLE MEMBER_SECURITY
    AUTO_INCREMENT = 1;

TRUNCATE TABLE ADDRESS;
ALTER TABLE ADDRESS
    AUTO_INCREMENT = 1;

TRUNCATE TABLE ADMIN;
ALTER TABLE ADMIN
    AUTO_INCREMENT = 1;

TRUNCATE TABLE COUPON;
ALTER TABLE COUPON
    AUTO_INCREMENT = 1;

TRUNCATE TABLE ISSUED_COUPON;
ALTER TABLE ISSUED_COUPON
    AUTO_INCREMENT = 1;

TRUNCATE TABLE NOTIFICATION;
ALTER TABLE NOTIFICATION
    AUTO_INCREMENT = 1;

TRUNCATE TABLE STORE_MANAGER;
ALTER TABLE STORE_MANAGER
    AUTO_INCREMENT = 1;

TRUNCATE TABLE STORE_MANAGER_SECURITY;
ALTER TABLE STORE_MANAGER_SECURITY
    AUTO_INCREMENT = 1;

TRUNCATE TABLE STORE;
ALTER TABLE STORE
    AUTO_INCREMENT = 1;

TRUNCATE TABLE PRODUCT;
ALTER TABLE PRODUCT
    AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;
