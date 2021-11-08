drop table if exists notice;
drop table if exists inquire_answer;
drop table if exists inquire;
drop table if exists delivery;
drop table if exists funded;
drop table if exists likes;
drop table if exists project_funding;
drop table if exists seller;
drop table if exists question;
drop table if exists userdto;

CREATE TABLE userdto
(
    `user_aiid`     INT            AUTO_INCREMENT    PRIMARY KEY,
    `user_email`    VARCHAR(50)    NOT NULL UNIQUE, 
    `user_name`     VARCHAR(50)    NOT NULL, 
    `user_phone`    VARCHAR(15), 
    `user_addr`     VARCHAR(100), 
    `user_pw`       VARCHAR(256), 
    `user_role`     VARCHAR(20),
    `user_regdate`  DATETIME       DEFAULT now(),
    `user_cash`     INT            DEFAULT 0,
    `user_cardnum`  VARCHAR(20),
    `user_picture`  VARCHAR(100)
);


CREATE TABLE seller
(
	`sel_selid`     VARCHAR(70)    NOT NULL, 
    `sel_useremail` VARCHAR(50)    NOT NULL,
    `sel_name`      VARCHAR(50)    NOT NULL,
    `sel_jumin`     VARCHAR(16)    NOT NULL, 
    `sel_business`  VARCHAR(10)    NOT NULL,
    CONSTRAINT PK_seller PRIMARY KEY (sel_selid)
);

ALTER TABLE seller
    ADD CONSTRAINT FK_seller_sel_useremail_userdto_user_email FOREIGN KEY (sel_useremail)
        REFERENCES userdto (user_email) ON DELETE CASCADE ON UPDATE CASCADE;

       
CREATE TABLE project_funding
(
    `pf_prnum`      INT            NOT NULL    AUTO_INCREMENT, 
    `pf_selid`      VARCHAR(15)    NOT NULL, 
    `pf_title`      VARCHAR(50)    NOT NULL, 
    `pf_content`    TEXT           NOT NULL, 
    `pf_itemprice`  INT            NOT NULL, 
    `pf_price`      INT            NOT NULL, 
    `pf_category`   VARCHAR(10)    NOT NULL, 
    `pf_viewcnt`    INT            NOT NULL    DEFAULT 0, 
    `pf_regdate`    DATETIME       NOT NULL    DEFAULT (current_date),
    `pf_startdate`  DATE           NOT NULL, 
    `pf_enddate`    DATE           NOT NULL,
    `pf_filepath`   VARCHAR(700)   NULL,
    `pf_newctn`     TEXT           NULL,
    CONSTRAINT PK_project_funding PRIMARY KEY (pf_prnum)
);

ALTER TABLE project_funding
    ADD CONSTRAINT FK_project_funding_pf_selid_seller_sel_selid FOREIGN KEY (pf_selid)
        REFERENCES seller (sel_selid) ON DELETE CASCADE ON UPDATE CASCADE;


CREATE TABLE inquire
(
    `in_num`        INT          NOT NULL  AUTO_INCREMENT, 
    `in_prnum`      INT          NOT NULL, 
    `in_title`      VARCHAR(20)  NOT NULL,
    `in_content`    TEXT         NOT NULL, 
    `in_regdate`    DATETIME     NOT NULL  DEFAULT  now(), 
    `in_useremail`  VARCHAR(50)  NOT NULL, 
    CONSTRAINT PK_inquire PRIMARY KEY (in_num)
);

ALTER TABLE inquire
    ADD CONSTRAINT FK_inquire_in_useremail_userdto_user_email FOREIGN KEY (in_useremail)
        REFERENCES userdto (user_email) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE inquire
    ADD CONSTRAINT FK_inquire_in_prnum_project_funding_pf_prnum FOREIGN KEY (in_prnum)
        REFERENCES project_funding (pf_prnum) ON DELETE CASCADE ON UPDATE CASCADE;


CREATE TABLE inquire_answer
(
    `ina_num`      INT         NOT NULL  AUTO_INCREMENT,
    `ina_innum`    INT         NOT NULL, 
    `ina_content`  TEXT        NOT NULL, 
    `ina_regdate`  DATETIME    NOT NULL  DEFAULT now(), 
    CONSTRAINT PK_inquire_answer PRIMARY KEY (ina_num)
);

ALTER TABLE inquire_answer
    ADD CONSTRAINT FK_inquire_answer_ina_innum_inquire_in_num FOREIGN KEY (ina_innum)
        REFERENCES inquire (in_num) ON DELETE CASCADE ON UPDATE CASCADE;


CREATE TABLE Likes
(
    `lk_prnum`      INT          NOT NULL, 
    `lk_useremail`  VARCHAR(50)  NOT NULL, 
    `lk_likecnt`    INT          NOT NULL  CHECK (lk_likecnt IN (0, 1))  DEFAULT 0, 
    CONSTRAINT PK_Like PRIMARY KEY (lk_prnum, lk_useremail)
);

ALTER TABLE Likes
    ADD CONSTRAINT FK_Like_lk_prnum_project_funding_pf_prnum FOREIGN KEY (lk_prnum)
        REFERENCES project_funding (pf_prnum) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Likes
    ADD CONSTRAINT FK_Like_lk_useremail_userdto_user_email FOREIGN KEY (lk_useremail)
        REFERENCES userdto (user_email) ON DELETE CASCADE ON UPDATE CASCADE;


CREATE TABLE funded
(
    `f_prnum`        INT            NOT NULL, 
    `f_useremail`    VARCHAR(50)    NOT NULL, 
    `f_fundingdate`  DATETIME       NOT NULL  DEFAULT now(), 
    CONSTRAINT PK_funded PRIMARY KEY (f_prnum, f_useremail, f_fundingdate)
);

ALTER TABLE funded
    ADD CONSTRAINT FK_funded_f_useremail_userdto_user_email FOREIGN KEY (f_useremail)
        REFERENCES userdto (user_email) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE funded
    ADD CONSTRAINT FK_funded_f_prnum_project_funding_pf_prnum FOREIGN KEY (f_prnum)
        REFERENCES project_funding (pf_prnum) ON DELETE CASCADE ON UPDATE CASCADE;
       

CREATE TABLE Delivery
(
	`dv_num`        VARCHAR(15)    NOT NULL,
    `dv_prnum`      INT            NOT NULL, 
    `dv_useremail`  VARCHAR(50)    NOT NULL,
    `dv_addr`       VARCHAR(100)   NOT NULL, 
    `dv_content`    VARCHAR(50)    NULL, 
    `dv_check`      INT            NOT NULL  CHECK (dv_check IN (0, 1, 2)), 
    CONSTRAINT PK_Delivery PRIMARY KEY (dv_num)
);

ALTER TABLE Delivery
    ADD CONSTRAINT FK_Delivery_dv_prnum_funded_f_prnum FOREIGN KEY (dv_prnum)
        REFERENCES funded (f_prnum) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Delivery
    ADD CONSTRAINT FK_Delivery_dv_useremail_funded_f_useremail FOREIGN KEY (dv_useremail)
        REFERENCES funded (f_useremail) ON DELETE CASCADE ON UPDATE CASCADE;

       
CREATE TABLE notice
(
    `nt_num`        INT          NOT NULL  AUTO_INCREMENT,
    `nt_useremail`  VARCHAR(50)  NOT NULL,
    `nt_category`   VARCHAR(10)  NOT NULL  CHECK (nt_category IN ('notice', 'event')),
    `nt_title`      VARCHAR(30)  NOT NULL,
    `nt_content`    TEXT         NOT NULL,
    `nt_regdate`    DATE         NOT NULL  DEFAULT (current_date),
    CONSTRAINT PK_notice PRIMARY KEY(nt_num)
);

ALTER TABLE Notice
    ADD CONSTRAINT FK_Notice_nt_useremail_userdto_user_email FOREIGN KEY (nt_useremail)
        REFERENCES userdto (user_email) ON DELETE CASCADE ON UPDATE CASCADE;