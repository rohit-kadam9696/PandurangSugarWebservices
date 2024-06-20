prompt PL/SQL Developer Export User Objects for user SHARKARA@PANDURANG
prompt Created by administrator on 01 March 2024
set define off
spool pandurangdb.log

prompt
prompt Creating table APP_CR_T_PLANTATION
prompt ==================================
prompt
create table SHARKARA.APP_CR_T_PLANTATION
(
  vyear_id              VARCHAR2(100) not null,
  nentity_uni_id        VARCHAR2(8) not null,
  nplot_no              NUMBER(7) not null,
  vform_no              VARCHAR2(15),
  ncrop_id              NUMBER(1),
  nhangam_id            NUMBER(1),
  vsurve_no             VARCHAR2(50) default 0,
  dentry_date           DATE,
  dplantation_date      DATE,
  nirrigation_id        NUMBER(1),
  nvariety_id           NUMBER(3),
  narea                 NUMBER(6,2),
  nexpected_yield       NUMBER(9,3) default 0,
  dexp_harvested_date   DATE,
  nirrigation_method_id NUMBER(1),
  nlagan_type_id        NUMBER(1),
  nvillage_id           NUMBER(4),
  ndistance             NUMBER(7,2),
  dcreate_date          DATE default sysdate,
  dupdate_date          DATE default sysdate,
  ncreate_user_id       NUMBER,
  nupdate_user_id       NUMBER,
  ncomputer             VARCHAR2(25),
  nsection_id           NUMBER,
  change_falg           NUMBER default 0,
  dlate_entry_date      DATE,
  nfarmer_type_id       NUMBER,
  nfact_id              NUMBER default 1,
  ngps_area             NUMBER,
  ngps_distance         NUMBER,
  nconfirm_flag         NUMBER default 0,
  vphoto_path           VARCHAR2(2000),
  vconfirmphoto_path    VARCHAR2(2000),
  ntentative_area       NUMBER,
  ncropwater_condition  NUMBER,
  njune_flag            NUMBER default 0,
  naugust_flag          NUMBER default 0,
  vsoil_test            VARCHAR2(1),
  vgreen_fert           VARCHAR2(1),
  nlane_type_id         NUMBER,
  vbene_treat           VARCHAR2(1),
  vbesal_dose           VARCHAR2(1),
  vdrip_used            VARCHAR2(1),
  nharvest_type_id      NUMBER,
  v_ferti_vatap_flag    VARCHAR2(1),
  nplot_no_offline      VARCHAR2(20),
  nreg_flag_offline     NUMBER(1),
  nconf_flag_offline    NUMBER(1),
  nharvested_flag       NUMBER default 0,
  nregn_gps_flag        NUMBER,
  vstanding_latitue     VARCHAR2(30),
  vstanding_longitude   VARCHAR2(30),
  nplot_no_prev_year    NUMBER(7)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 30M
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_CR_T_PLANTATION.nconfirm_flag
  is '0 Non Confirm, 1 Confirm';
comment on column SHARKARA.APP_CR_T_PLANTATION.nharvested_flag
  is '0 Non Complete, 1 Complete';
comment on column SHARKARA.APP_CR_T_PLANTATION.nregn_gps_flag
  is '1 : WALKING 2: FINGER';
alter table SHARKARA.APP_CR_T_PLANTATION
  add constraint PK_PLOT_APP primary key (VYEAR_ID, NPLOT_NO)
  using index 
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 5M
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_IN_T_ISSUE_HEADER
prompt ====================================
prompt
create table SHARKARA.APP_IN_T_ISSUE_HEADER
(
  vyear_id           VARCHAR2(9) not null,
  nissue_id          NUMBER(5) not null,
  dissue_date        DATE,
  nhod_id            NUMBER(2),
  nissue_type_id     NUMBER(2),
  vmat_issue_to      VARCHAR2(200),
  nentity_uni_id     VARCHAR2(8),
  vvou_number        VARCHAR2(20),
  dvou_date          DATE,
  dcreate_date       DATE default SYSDATE,
  dupdate_date       DATE default SYSDATE,
  ncreate_user_id    NUMBER,
  napproval_id       NUMBER,
  ncharge_type       NUMBER,
  vget_pass          VARCHAR2(1) default 'N',
  nget_pass_no       NUMBER,
  ddate_gate_pass    DATE,
  nsubdept_id        NUMBER,
  ngroup_id          NUMBER,
  cissue_closed      VARCHAR2(1) default 'N',
  nbill_id           NUMBER,
  nuser_role_id      NUMBER,
  nfert_guarantor_1  VARCHAR2(8),
  nfert_guarantor_2  VARCHAR2(8),
  nlocation_id       NUMBER,
  n_ferti_vatap_area NUMBER,
  vsale_type         VARCHAR2(2),
  vseason_year       VARCHAR2(9),
  nplot_no           NUMBER(7)
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 6M
    next 1M
    minextents 1
    maxextents unlimited
  );
create unique index SHARKARA.APP_ISSUE_ID_PK on SHARKARA.APP_IN_T_ISSUE_HEADER (VYEAR_ID, NISSUE_ID)
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 2M
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_IN_T_ISSUE_HEADER
  add constraint APP_ISSUE_REG_PK primary key (VYEAR_ID, NISSUE_ID);

prompt
prompt Creating table APP_IN_T_ISSUE_DETAIL
prompt ====================================
prompt
create table SHARKARA.APP_IN_T_ISSUE_DETAIL
(
  vyear_id       VARCHAR2(9) not null,
  nissue_id      NUMBER(5) not null,
  vitem_id       VARCHAR2(8) not null,
  nindent_qty    NUMBER(10,3),
  nissued_qty    NUMBER(10,3),
  nentity_uni_id VARCHAR2(8),
  nsr_no         NUMBER(3),
  vmake_name     VARCHAR2(250),
  nunit_id       NUMBER,
  navg_rate      NUMBER(12,2),
  namount        NUMBER(12,2),
  nsale_rate     NUMBER,
  nsale_amount   NUMBER,
  ngroup_id      NUMBER,
  ncost_id       NUMBER,
  ncgst_rate     NUMBER,
  ncgst_amt      NUMBER,
  nsgst_rate     NUMBER,
  nsgst_amt      NUMBER,
  nigst_rate     NUMBER,
  nigst_amt      NUMBER
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 8M
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_IN_T_ISSUE_DETAIL
  add constraint APP_ISSUE_REG_FK foreign key (VYEAR_ID, NISSUE_ID)
  references SHARKARA.APP_IN_T_ISSUE_HEADER (VYEAR_ID, NISSUE_ID) on delete cascade;

prompt
prompt Creating table APP_MS_T_SUG_CARD_HEADER
prompt =======================================
prompt
create table SHARKARA.APP_MS_T_SUG_CARD_HEADER
(
  vyear_id           VARCHAR2(9) not null,
  nentity_uni_id     VARCHAR2(8) not null,
  nno_of_shares      NUMBER(3),
  nshare_amt         NUMBER(12,2),
  nsugar_qty         NUMBER(3),
  nrate              NUMBER(7,2),
  namount            NUMBER(12,2),
  nsabasad_anamat    NUMBER(1),
  nsugar_for         NUMBER(2) not null,
  dsug_date          DATE,
  nlocation          NUMBER,
  nlocked            VARCHAR2(1) default 'N',
  nemp_count_id      NUMBER,
  nsms_flag          NUMBER(1) default 0,
  ventity_name_local VARCHAR2(254),
  village_name_local VARCHAR2(100),
  vfinyear_id        VARCHAR2(9),
  ddelivery_date     TIMESTAMP(6),
  vlatitue_vatap     VARCHAR2(30),
  vlongitude_vatap   VARCHAR2(30),
  nvatap_flag        NUMBER default 0,
  vphoto_path        VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 23M
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_MS_T_SUG_CARD_HEADER.nlocked
  is 'N-Open, Y-Locked, L-Lost, P-Pending';
comment on column SHARKARA.APP_MS_T_SUG_CARD_HEADER.nvatap_flag
  is '1-Code Entry, 2-QR scan';
alter table SHARKARA.APP_MS_T_SUG_CARD_HEADER
  add constraint APP_PK_SUG_CARD primary key (VYEAR_ID, NENTITY_UNI_ID, NSUGAR_FOR)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 10M
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_APP_ROLE
prompt =============================
prompt
create table SHARKARA.APP_M_APP_ROLE
(
  napp_id       NUMBER not null,
  nuser_role_id NUMBER not null
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_M_APP_ROLE
  add constraint NAPPID_ROLID primary key (NAPP_ID, NUSER_ROLE_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_CANEYARD
prompt =============================
prompt
create table SHARKARA.APP_M_CANEYARD
(
  nyard_id       NUMBER not null,
  vyard_name_mar VARCHAR2(30),
  vyard_name_eng VARCHAR2(30),
  vtractor_truck VARCHAR2(1),
  vbajat         VARCHAR2(1)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_M_CANEYARD.vtractor_truck
  is 'Y: Yes, N: No';
comment on column SHARKARA.APP_M_CANEYARD.vbajat
  is 'Y: Yes, N: No';
alter table SHARKARA.APP_M_CANEYARD
  add constraint YARD_PK primary key (NYARD_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_DESIGNATION
prompt ================================
prompt
create table SHARKARA.APP_M_DESIGNATION
(
  ndes_id     NUMBER(5) not null,
  vdes_name   NVARCHAR2(150),
  ndisplay_id VARCHAR2(1),
  vactive     VARCHAR2(1) default 'Y'
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_M_DESIGNATION.ndisplay_id
  is '1 : Directors 2 : Officers';
alter table SHARKARA.APP_M_DESIGNATION
  add constraint DES_PK primary key (NDES_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_EMPLOYEE
prompt =============================
prompt
create table SHARKARA.APP_M_EMPLOYEE
(
  nemp_id         NUMBER(5) not null,
  vemp_name       NVARCHAR2(150),
  ndes_id         NUMBER(5),
  vemp_img        VARCHAR2(100),
  nandroid_order  NUMBER(3),
  nmobile_no      VARCHAR2(10),
  vactive         VARCHAR2(1) default 'Y',
  nentry_user_id  NUMBER(5),
  nentry_date     DATE default Sysdate,
  nupdate_user_id NUMBER(5),
  nupdate_date    DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_M_EMPLOYEE
  add constraint APP_EMP_PK primary key (NEMP_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_EXPECTED_TON_VEHICLE
prompt =========================================
prompt
create table SHARKARA.APP_M_EXPECTED_TON_VEHICLE
(
  nvehicle_type_id        NUMBER not null,
  nwirerope_no            NUMBER(2) not null,
  ntailor_front           NUMBER(2) not null,
  ntailor_back            NUMBER(2) not null,
  vcard_combination_local VARCHAR2(60),
  nexp_harv_tonnage       NUMBER(8,3)
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_M_EXPECTED_TON_VEHICLE
  add constraint VEHICLE_REMARK primary key (NVEHICLE_TYPE_ID, NWIREROPE_NO, NTAILOR_FRONT, NTAILOR_BACK)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_GUTKHADE
prompt =============================
prompt
create table SHARKARA.APP_M_GUTKHADE
(
  vyear_id         VARCHAR2(9) not null,
  ntrans_id        NUMBER not null,
  nvehicle_type_id NUMBER(2),
  dfrom_date       DATE,
  vtime_start      DATE,
  dto_date         DATE,
  vtime_end        DATE,
  nsection_id      NUMBER,
  nentry_user_id   NUMBER,
  dentry_date      DATE,
  vactive          VARCHAR2(1) default 'A',
  nupdate_user_id  NUMBER,
  dupdate_date     DATE
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_M_GUTKHADE.vactive
  is 'A-ACTIVE D-DACTIVE';
alter table SHARKARA.APP_M_GUTKHADE
  add constraint GUT_YEAR_TRANSID primary key (VYEAR_ID, NTRANS_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_HARVESTING_PROGRAM
prompt =======================================
prompt
create table SHARKARA.APP_M_HARVESTING_PROGRAM
(
  vyear_id         VARCHAR2(9) not null,
  dplant_from_date DATE not null,
  dplant_to_date   DATE not null,
  nhangam          NUMBER not null,
  nvariety         NUMBER not null,
  nentry_user_id   NUMBER not null,
  dentry_date_time DATE default sysdate not null
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_M_HARVESTING_PROGRAM
  add constraint EXACT_DUP_ENTRY_1 primary key (VYEAR_ID, DPLANT_FROM_DATE, DPLANT_TO_DATE, NHANGAM, NVARIETY)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_HARVESTING_PROGRAM_SPL
prompt ===========================================
prompt
create table SHARKARA.APP_M_HARVESTING_PROGRAM_SPL
(
  vyear_id         VARCHAR2(9) not null,
  nentity_uni_id   VARCHAR2(8) not null,
  nplot_no         NUMBER not null,
  nreason_id       NUMBER not null,
  nentry_user_id   NUMBER not null,
  dentry_date_time DATE default sysdate not null
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_M_HARVESTING_PROGRAM_SPL
  add constraint EXACT_DUP_ENTRY_2 primary key (VYEAR_ID, NENTITY_UNI_ID, NPLOT_NO, NREASON_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_HARVESTING_PROGRAM_START
prompt =============================================
prompt
create table SHARKARA.APP_M_HARVESTING_PROGRAM_START
(
  vyear_id             VARCHAR2(9),
  dentry_date          DATE,
  ndays_permit         NUMBER,
  nlimit_tonnage       NUMBER,
  nlimit_tonnage_extra NUMBER
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_LIST
prompt =========================
prompt
create table SHARKARA.APP_M_LIST
(
  napp_id   NUMBER not null,
  vapp_name VARCHAR2(100)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_M_LIST
  add constraint APP_PK primary key (NAPP_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_MENU
prompt =========================
prompt
create table SHARKARA.APP_M_MENU
(
  ngroupid       NUMBER,
  vgroupname     NVARCHAR2(100),
  vstatus_active VARCHAR2(1) default 1,
  nandroid_order NUMBER
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_M_MENU.vstatus_active
  is '1: Active, 2: Deactive';

prompt
prompt Creating table APP_M_REASON
prompt ===========================
prompt
create table SHARKARA.APP_M_REASON
(
  nreason_id       NUMBER not null,
  vreason_name     VARCHAR2(150),
  nreason_group_id NUMBER,
  nreason_block    VARCHAR2(1) default 'N',
  vreason_short    VARCHAR2(25),
  vreason_id       VARCHAR2(1)
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_M_REASON.nreason_block
  is 'N-Open, Y-Block';
alter table SHARKARA.APP_M_REASON
  add constraint REASON_KEY primary key (NREASON_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_REASON_GROUP
prompt =================================
prompt
create table SHARKARA.APP_M_REASON_GROUP
(
  nreason_group_id   NUMBER not null,
  vreason_group_name NVARCHAR2(100)
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_M_REASON_GROUP
  add constraint REASON_GRP_KEY primary key (NREASON_GROUP_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_SLIP_RSTRN_TIME
prompt ====================================
prompt
create table SHARKARA.APP_M_SLIP_RSTRN_TIME
(
  nvehicle_type_id NUMBER(2) not null,
  nminutes         NUMBER(4)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_M_SLIP_RSTRN_TIME
  add constraint VEHICAL_TIME primary key (NVEHICLE_TYPE_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_SUBMENU
prompt ============================
prompt
create table SHARKARA.APP_M_SUBMENU
(
  nid            NUMBER not null,
  vname          NVARCHAR2(60),
  vurl           VARCHAR2(50),
  nparentid      NUMBER,
  ninner         NUMBER,
  ngroupid       NUMBER,
  norder_android NUMBER,
  vstatus_active VARCHAR2(2) default 1,
  vimg_name      NVARCHAR2(5),
  ncaller_id     NUMBER,
  nyear_type     NUMBER(2),
  vname_detail   NVARCHAR2(200)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_M_SUBMENU.vstatus_active
  is '1: Active, 2: Deactive';
comment on column SHARKARA.APP_M_SUBMENU.nyear_type
  is '1 : last 3 year, 2 :last 2 ignore next year, 3: sugar year';
alter table SHARKARA.APP_M_SUBMENU
  add constraint PERMISSION_PK primary key (NID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_SUBMENU_ROLE
prompt =================================
prompt
create table SHARKARA.APP_M_SUBMENU_ROLE
(
  nid           NUMBER not null,
  nuser_role_id NUMBER not null
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_M_SUBMENU_ROLE
  add constraint NID_ROLID primary key (NID, NUSER_ROLE_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_USER_MASTER
prompt ================================
prompt
create table SHARKARA.APP_M_USER_MASTER
(
  nuser_name       NUMBER not null,
  vuser_name       VARCHAR2(100),
  vfull_name       VARCHAR2(250),
  npassword        VARCHAR2(20),
  vactive          VARCHAR2(1) default 'Y',
  ndept_id         NUMBER,
  vfull_name_local VARCHAR2(250),
  nuser_role_id    NUMBER,
  nlocation_id     NUMBER,
  nmobile_no       NUMBER(10),
  nsug_type_id     NUMBER(2),
  vadmin           VARCHAR2(1) default 'N',
  nyard_id         NUMBER
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_M_USER_MASTER.vadmin
  is 'Y : Yes N: No';
alter table SHARKARA.APP_M_USER_MASTER
  add constraint NUN primary key (NUSER_NAME)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_M_USER_MASTER
  add constraint NMB unique (NMOBILE_NO)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_M_USER_ROLE_MASTER
prompt =====================================
prompt
create table SHARKARA.APP_M_USER_ROLE_MASTER
(
  nuser_role_id NUMBER not null,
  vuser_role    VARCHAR2(50)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_M_USER_ROLE_MASTER
  add constraint APP_U_ID primary key (NUSER_ROLE_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_T_ENTITY_MODIFIED
prompt ====================================
prompt
create table SHARKARA.APP_T_ENTITY_MODIFIED
(
  nentity_uni_id      VARCHAR2(8),
  nupdate_user_id     NUMBER,
  dupdate_date        DATE,
  vmobile_no_old      VARCHAR2(10),
  vmobile_no_new      VARCHAR2(10),
  vaadhaar_no_old     VARCHAR2(12),
  vaadhaar_no_new     VARCHAR2(12),
  vaadhaar_photo_old  VARCHAR2(500),
  vaadhaar_photo_new  VARCHAR2(500),
  nbank_id_old        NUMBER,
  nbank_id_new        NUMBER,
  vbank_ac_no_old     VARCHAR2(25),
  vbank_ac_no_new     VARCHAR2(25),
  vpassbook_photo_old VARCHAR2(500),
  vpassbook_photo_new VARCHAR2(500),
  nupdate_mode        NUMBER
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_T_ENTITY_MODIFIED.nupdate_mode
  is '1-Desktop, 2-App';

prompt
prompt Creating table APP_T_EXCESS_TON_PLOT_REQ
prompt ========================================
prompt
create table SHARKARA.APP_T_EXCESS_TON_PLOT_REQ
(
  vyear_id           VARCHAR2(9),
  nentity_uni_id     VARCHAR2(8),
  nplot_no           NUMBER(7),
  ntentative_area    NUMBER,
  nexpected_yield    NUMBER(9,3),
  nlimit_yield       NUMBER(9,3),
  nharvested_tonnage NUMBER(8,3),
  nreason_id         NUMBER,
  narea_remaining    NUMBER(6,2),
  nrequested_tonnage NUMBER(8,3),
  nentry_user_id     NUMBER,
  dentry_date_time   DATE default sysdate,
  nallowed_tonnage   NUMBER(8,3),
  naction_user_id    NUMBER,
  daction_date_time  DATE,
  nstatus_id         NUMBER default 1
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_T_EXCESS_TON_PLOT_REQ.nstatus_id
  is '1-REQUESTED,2-APPROVED,3-REJECT';

prompt
prompt Creating table APP_T_MESSAGE_NEWS
prompt =================================
prompt
create table SHARKARA.APP_T_MESSAGE_NEWS
(
  nmsg_news_id    NUMBER(5) not null,
  vtitle          VARCHAR2(100),
  vmessage        VARCHAR2(250),
  vtype           VARCHAR2(1),
  vimage_path     VARCHAR2(100),
  vpdfname_path   VARCHAR2(100),
  dexpire_date    DATE,
  nentry_user_id  NUMBER(5),
  dentry_date     DATE,
  nupdate_user_id NUMBER(5),
  dupdate_date    DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
comment on column SHARKARA.APP_T_MESSAGE_NEWS.vtype
  is '0: News 1: all 2: Farmer 3 : transporter 4: bulluckcart 5: tondanidar';
alter table SHARKARA.APP_T_MESSAGE_NEWS
  add constraint NEWS_MSG_PK primary key (NMSG_NEWS_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;

prompt
prompt Creating table APP_T_NUMBERTAKER
prompt ================================
prompt
create table SHARKARA.APP_T_NUMBERTAKER
(
  nnumber_id      NUMBER not null,
  vyear_id        VARCHAR2(9),
  dnumber_date    DATE,
  ntransportor_id VARCHAR2(8),
  ntoken_no       NUMBER,
  nlot_no         NUMBER,
  shift_no        VARCHAR2(1),
  dshift_date     DATE,
  vehicle_no      VARCHAR2(20),
  vactive_status  VARCHAR2(1) default 1,
  nvehicle_type   NUMBER,
  nvillage_id     NUMBER,
  nslip_no        VARCHAR2(50),
  ndeleted_user   NUMBER,
  deleted_date    DATE,
  dunloadtime     DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1M
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_T_NUMBERTAKER.vactive_status
  is '1: Number in Queue, 2: Number Release, 3: Number Deleted';
alter table SHARKARA.APP_T_NUMBERTAKER
  add constraint NUMBER_PK primary key (NNUMBER_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255;

prompt
prompt Creating table APP_T_OTP
prompt ========================
prompt
create table SHARKARA.APP_T_OTP
(
  notp_id        NUMBER(8) not null,
  votp           VARCHAR2(20),
  nmobile_no     VARCHAR2(10),
  dgenrated_date DATE,
  dexprie_date   DATE,
  vstatus        VARCHAR2(1),
  napp_id        NUMBER default 1
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_T_OTP.vstatus
  is '1:  Created 2:Used 3:Expired';
alter table SHARKARA.APP_T_OTP
  add constraint OTPID primary key (NOTP_ID)
  using index 
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_T_REGISTRATION_LATLNGS
prompt =========================================
prompt
create table SHARKARA.APP_T_REGISTRATION_LATLNGS
(
  nplot_no   NUMBER,
  vyear_id   VARCHAR2(255),
  vlatitude  VARCHAR2(255),
  vlongitude VARCHAR2(255),
  nacc       NUMBER,
  ntrans_no  NUMBER
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 21M
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_T_USER_TRACKER
prompt =================================
prompt
create table SHARKARA.APP_T_USER_TRACKER
(
  nuserid        NUMBER,
  vimei          VARCHAR2(25),
  vuniquestring  NVARCHAR2(25),
  vuniquestring2 NVARCHAR2(25),
  napp_id        NUMBER default 1,
  nmobileno      NUMBER
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_T_USER_TRACKER
  add constraint LOGIN_UK unique (NUSERID, NAPP_ID, NMOBILENO)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_T_US_VIKAS
prompt =============================
prompt
create table SHARKARA.APP_T_US_VIKAS
(
  uv_id           NUMBER not null,
  uvtitle         NVARCHAR2(150),
  uvdescription   NVARCHAR2(1500),
  vvideo_id       VARCHAR2(30),
  vporimg         VARCHAR2(100),
  vtype           VARCHAR2(1),
  vpdfname        VARCHAR2(100),
  vuvfor          VARCHAR2(1),
  vactive         VARCHAR2(1),
  nentry_user_id  VARCHAR2(5),
  dentry_date     DATE,
  nupdate_user_id VARCHAR2(5),
  dupdate_date    DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255;
comment on column SHARKARA.APP_T_US_VIKAS.vvideo_id
  is 'You tube video ID';
comment on column SHARKARA.APP_T_US_VIKAS.vporimg
  is 'video place holder or image or pdf';
comment on column SHARKARA.APP_T_US_VIKAS.vtype
  is '1: text 2: Video 3: image 4: pdf';
comment on column SHARKARA.APP_T_US_VIKAS.vuvfor
  is '1: News 2 : Cane Guideline 3 : expert advicce 4 :Success story 5: Invitations 6 : Live Program 7 : Photo 8: Contact';
comment on column SHARKARA.APP_T_US_VIKAS.vactive
  is '1: Active 2: Deactive';

prompt
prompt Creating table APP_T_VEHICLE_TRANSIT
prompt ====================================
prompt
create table SHARKARA.APP_T_VEHICLE_TRANSIT
(
  ddate              DATE not null,
  nentry_user_id     NUMBER not null,
  nvillage_id        NUMBER not null,
  nop_vehicle        NUMBER,
  ntransit_vehicle_1 NUMBER,
  ntransit_vehicle_2 NUMBER,
  nop_bajat          NUMBER,
  ntransit_bajat_1   NUMBER,
  ntransit_bajat_2   NUMBER,
  nop_machine        NUMBER,
  ntransit_machine_1 NUMBER,
  ntransit_machine_2 NUMBER,
  dentry_date        DATE default SYSDATE,
  dupdate_date       DATE,
  vyear_id           VARCHAR2(9)
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_T_VEHICLE_TRANSIT.ntransit_vehicle_1
  is '1 - Befor 4 PM';
comment on column SHARKARA.APP_T_VEHICLE_TRANSIT.ntransit_vehicle_2
  is '2 - After 4 PM';
comment on column SHARKARA.APP_T_VEHICLE_TRANSIT.ntransit_bajat_1
  is '1 - Befor 4 PM';
comment on column SHARKARA.APP_T_VEHICLE_TRANSIT.ntransit_bajat_2
  is '2 - After 4 PM';
comment on column SHARKARA.APP_T_VEHICLE_TRANSIT.ntransit_machine_1
  is '1 - Befor 4 PM';
comment on column SHARKARA.APP_T_VEHICLE_TRANSIT.ntransit_machine_2
  is '2 - After 4 PM';
alter table SHARKARA.APP_T_VEHICLE_TRANSIT
  add constraint USER_VILL_DATE primary key (DDATE, NENTRY_USER_ID, NVILLAGE_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_T_VERSION
prompt ============================
prompt
create table SHARKARA.APP_T_VERSION
(
  nversionid   NUMBER not null,
  vversionname VARCHAR2(15),
  vupdate      VARCHAR2(1),
  vcomplusory  VARCHAR2(1),
  vhead        NVARCHAR2(50),
  vmessage     NVARCHAR2(250),
  dcreate_date DATE default sysdate,
  napp_id      NUMBER default 1 not null
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
comment on column SHARKARA.APP_T_VERSION.vupdate
  is 'Y: Yes N : No';
comment on column SHARKARA.APP_T_VERSION.vcomplusory
  is 'Y: Yes N : No';
alter table SHARKARA.APP_T_VERSION
  add constraint CANEAPPVERSION primary key (NVERSIONID, NAPP_ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table APP_WB_T_WEIGHT_SLIP
prompt ===================================
prompt
create table SHARKARA.APP_WB_T_WEIGHT_SLIP
(
  vyear_id              VARCHAR2(9) not null,
  nslip_no              NUMBER(12) not null,
  nshift_id             NUMBER(2) not null,
  dslip_date            DATE default sysdate,
  nplot_no              NUMBER(7),
  nentity_uni_id        VARCHAR2(8),
  nfarmer_type_id       NUMBER(2),
  nsection_id           NUMBER(2),
  nvillage_id           NUMBER(4),
  ncrop_id              NUMBER(2),
  nvariety_id           NUMBER(3),
  ndistance             NUMBER(7,3),
  nvehicle_type_id      NUMBER(2),
  ngadi_doki_id         NUMBER(1),
  nharvestor_id         VARCHAR2(8),
  ntransportor_id       VARCHAR2(8),
  vvehicle_no           VARCHAR2(200),
  ntailor_front         NUMBER(2),
  ntailor_back          NUMBER(2),
  nwirerope_no          NUMBER(2),
  nbulluckcart_id       VARCHAR2(8),
  nbulluckcart_main_id  VARCHAR2(8),
  vin_time              DATE default sysdate,
  vout_time             DATE default sysdate,
  ngross_weight         NUMBER(8,3),
  nempty_weight         NUMBER(8,3),
  ncane_weight          NUMBER(8,3),
  nbinding_weight       NUMBER(8,3),
  nexcess_wt            NUMBER(12,3),
  nnet_weight           NUMBER(8,3),
  ndiesel               NUMBER(5,2) default 0,
  noil                  NUMBER(5,2) default 0,
  nkata_id              NUMBER(2),
  nslipboy_id           NUMBER(5),
  vremark               VARCHAR2(100),
  nfact_id              NUMBER(2),
  vcreate_user_id       NUMBER,
  dcreate_date          DATE default sysdate,
  vupdate_user_id       VARCHAR2(10),
  dupdate_date          DATE default sysdate,
  nhangam_id            NUMBER,
  nload_user_id         NUMBER,
  nempty_user_id        NUMBER,
  nentity_transfer_id   VARCHAR2(8),
  nfarmer_sms_flag      NUMBER default 0,
  ntransporter_sms_flag NUMBER default 0,
  nkata_operator        NUMBER,
  nflag_out             NUMBER default 0,
  dslip_date_slipboy    DATE,
  nslip_no_offline      VARCHAR2(30),
  nremark_id            NUMBER,
  nuws_card_id          VARCHAR2(16),
  vfar_oth_fact_id      VARCHAR2(12),
  ngross_ml_flag        NUMBER default 0,
  nempty_ml_flag        NUMBER default 0,
  nadg_flag             NUMBER default 0,
  vactive_dactive       VARCHAR2(1) default 'A',
  nslip_seq             NUMBER,
  ngross_actual         NUMBER(8,3),
  nstr_catch_flag       NUMBER,
  nseq_weight           NUMBER,
  vslip_verify          VARCHAR2(1) default 'N',
  nper_flag             NUMBER,
  ngps_distance         NUMBER,
  ngps_bill_flag        VARCHAR2(1) default 'N',
  nuser_deactive        NUMBER(5),
  ddate_deactive        DATE,
  cqr_image_wt_slip     BLOB,
  vqr_photo_path        VARCHAR2(100),
  vqr_string            VARCHAR2(250),
  vqr_string_gross      VARCHAR2(250),
  vqr_string_empty      VARCHAR2(250),
  nharvestor_sms_flag   NUMBER default 0
)
tablespace SYSTEM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 128M
    next 1M
    minextents 1
    maxextents unlimited
  );
create index SHARKARA.APP_EMPETY on SHARKARA.APP_WB_T_WEIGHT_SLIP (NEMPTY_WEIGHT)
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 16M
    next 1M
    minextents 1
    maxextents unlimited
  );
create index SHARKARA.APP_GROSS on SHARKARA.APP_WB_T_WEIGHT_SLIP (NGROSS_WEIGHT)
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 16M
    next 1M
    minextents 1
    maxextents unlimited
  );
create index SHARKARA.APP_NENTITY_UNI_ID on SHARKARA.APP_WB_T_WEIGHT_SLIP (NENTITY_UNI_ID)
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 16M
    next 1M
    minextents 1
    maxextents unlimited
  );
create index SHARKARA.APP_SECTION_ID on SHARKARA.APP_WB_T_WEIGHT_SLIP (NSECTION_ID)
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 20M
    next 1M
    minextents 1
    maxextents unlimited
  );
create index SHARKARA.APP_SLIP_DATE on SHARKARA.APP_WB_T_WEIGHT_SLIP (DSLIP_DATE)
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 24M
    next 1M
    minextents 1
    maxextents unlimited
  );
create index SHARKARA.APP_VEHICLE_TYPE_ID on SHARKARA.APP_WB_T_WEIGHT_SLIP (NVEHICLE_TYPE_ID)
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 19M
    next 1M
    minextents 1
    maxextents unlimited
  );
create index SHARKARA.APP_VILLAGE on SHARKARA.APP_WB_T_WEIGHT_SLIP (NVILLAGE_ID)
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 12M
    next 1M
    minextents 1
    maxextents unlimited
  );
create index SHARKARA.APP_WIREROPE on SHARKARA.APP_WB_T_WEIGHT_SLIP (NWIREROPE_NO)
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 19M
    next 1M
    minextents 1
    maxextents unlimited
  );
create index SHARKARA.APP_YEAR_ID on SHARKARA.APP_WB_T_WEIGHT_SLIP (VYEAR_ID)
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 17M
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table SHARKARA.APP_WB_T_WEIGHT_SLIP
  add constraint APP_CABWTSPRKEY primary key (VYEAR_ID, NSLIP_NO)
  using index 
  tablespace SYSTEM
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 19M
    next 1M
    minextents 1
    maxextents unlimited
  );


prompt Done
spool off
set define on
