 �EXPORT:V11.02.00
DSHARKARA
RTABLES
2048
0
72
0
 � ��          .                                     Fri Mar 1 12:54:21 2024D:\3WD_Software\dbbackup\pandurangmasterdb.sql                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  d    `$	
#G#G#A�G � ��                                     d    `$	
#G#G#A�G � � �                                      +00:00   BYTE UNUSED 2 INTERPRETED DISABLE:ALL  
METRICST
TABLE "APP_M_APP_ROLE"
CREATE TABLE "APP_M_APP_ROLE" ("NAPP_ID" NUMBER NOT NULL ENABLE, "NUSER_ROLE_ID" NUMBER NOT NULL ENABLE)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING NOCOMPRESS
INSERT INTO "APP_M_APP_ROLE" ("NAPP_ID", "NUSER_ROLE_ID") VALUES (:1, :2)
          � �   � �   � �   � �   � �	   � �
   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �  ��
CREATE UNIQUE INDEX "NAPPID_ROLID" ON "APP_M_APP_ROLE" ("NAPP_ID" , "NUSER_ROLE_ID" )  PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING
ANALSTATS IS "APP_M_APP_ROLE"
_ BEGIN  DBMS_STATS.SET_INDEX_STATS(NULL,'"NAPPID_ROLID"',NULL,NULL,NULL,15,1,15,1,1,1,0,6); END;
ALTER TABLE "APP_M_APP_ROLE" ADD  CONSTRAINT "NAPPID_ROLID" PRIMARY KEY ("NAPP_ID", "NUSER_ROLE_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING ENABLE 
ANALSTATS TS "APP_M_APP_ROLE"
X BEGIN  DBMS_STATS.SET_TABLE_STATS(NULL,'"APP_M_APP_ROLE"',NULL,NULL,NULL,15,5,7,6); END;
ANALSTATS TS "APP_M_APP_ROLE"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C104'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,3& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_APP_ROLE"','"NAPP_ID"', NULL ,NULL,NULL,3,.333333333333333,0,srec,3,6); END;  
ANALSTATS TS "APP_M_APP_ROLE"
�� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C20205'; SREC.MAXVAL := 'C20210'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 104,115& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_APP_ROLE"','"NUSER_ROLE_ID"', NULL ,NULL,NULL,11,.0909090909090909,0,srec,4,6); END;  
TABLE "APP_M_CANEYARD"
CREATE TABLE "APP_M_CANEYARD" ("NYARD_ID" NUMBER, "VYARD_NAME_MAR" VARCHAR2(30), "VYARD_NAME_ENG" VARCHAR2(30), "VTRACTOR_TRUCK" VARCHAR2(1), "VBAJAT" VARCHAR2(1))  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING NOCOMPRESS
INSERT INTO "APP_M_CANEYARD" ("NYARD_ID", "VYARD_NAME_MAR", "VYARD_NAME_ENG", "VTRACTOR_TRUCK", "VBAJAT") VALUES (:1, :2, :3, :4, :5)
     �    �    �    �       � ��������� ��֛�� Tractor Yard Y N   � ����ֳ�և� ��֛�� Nurabhai Yard Y N   � ��ӯ� ��֛��	 Pump Yard Y Y   � ����֮�� ��֛�� Colony Yard Y N  ��
CREATE UNIQUE INDEX "YARD_PK" ON "APP_M_CANEYARD" ("NYARD_ID" )  PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING
ANALSTATS IS "APP_M_CANEYARD"
X BEGIN  DBMS_STATS.SET_INDEX_STATS(NULL,'"YARD_PK"',NULL,NULL,NULL,4,1,4,1,1,1,0,6); END;
ALTER TABLE "APP_M_CANEYARD" ADD  CONSTRAINT "YARD_PK" PRIMARY KEY ("NYARD_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING ENABLE 
ANALSTATS TS "APP_M_CANEYARD"
X BEGIN  DBMS_STATS.SET_TABLE_STATS(NULL,'"APP_M_CANEYARD"',NULL,NULL,NULL,4,5,36,6); END;
ANALSTATS TS "APP_M_CANEYARD"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C105'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,4& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1{ ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_CANEYARD"','"NYARD_ID"', NULL ,NULL,NULL,4,.25,0,srec,3,6); END;  
ANALSTATS TS "APP_M_CANEYARD"
��� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '99D2F2FC8C99FCB8FC20B5D6D69BD4FC'; SREC.MAXVAL := 'DBFAD6F2BBD6AED6DF20B5D6D69BD4FC'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(J 798699976748123000000000000000000000,1142200644369270000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_CANEYARD"','"VYARD_NAME_MAR"', NULL ,NULL,NULL,4,.25,0,srec,17,6); END;  
ANALSTATS TS "APP_M_CANEYARD"
��� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '436F6C6F6E792059617264'; SREC.MAXVAL := '54726163746F722059617264'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(I 350143828115780000000000000000000000,438472846723283000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_CANEYARD"','"VYARD_NAME_ENG"', NULL ,NULL,NULL,4,.25,0,srec,13,6); END;  
ANALSTATS TS "APP_M_CANEYARD"
��w DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '59'; SREC.MAXVAL := '59'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(I 462114420409600000000000000000000000,462114420409600000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1 ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_CANEYARD"','"VTRACTOR_TRUCK"', NULL ,NULL,NULL,1,1,0,srec,2,6); END;  
ANALSTATS TS "APP_M_CANEYARD"
��w DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '4E'; SREC.MAXVAL := '59'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(I 404999154965717000000000000000000000,462114420409600000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1x ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_CANEYARD"','"VBAJAT"', NULL ,NULL,NULL,2,.5,0,srec,2,6); END;  
COMMENT ON COLUMN "APP_M_CANEYARD"."VTRACTOR_TRUCK" IS 
 'Y: Yes, N: No'
COMMENT ON COLUMN "APP_M_CANEYARD"."VBAJAT" IS 
 'Y: Yes, N: No'
TABLE "APP_M_DESIGNATION"
CREATE TABLE "APP_M_DESIGNATION" ("NDES_ID" NUMBER(5, 0) NOT NULL ENABLE, "VDES_NAME" NVARCHAR2(150), "NDISPLAY_ID" VARCHAR2(1), "VACTIVE" VARCHAR2(1))  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING NOCOMPRESS
ALTER TABLE "APP_M_DESIGNATION" MODIFY DEFAULT
? ALTER TABLE "APP_M_DESIGNATION" MODIFY ("VACTIVE" DEFAULT 'Y'
)
INSERT INTO "APP_M_DESIGNATION" ("NDES_ID", "VDES_NAME", "NDISPLAY_ID", "VACTIVE") VALUES (:1, :2, :3, :4)
    ,�   �    �       �  � � � � � � � � � � � 1 Y   �   � �   � � � � � � 1 Y   �  � � � � .  � �   � � � � � � 1 Y   �  � � �  � � � � � � 1 Y   �  � � �  � � � � � � � � 1 Y   �.  � � � � � � � � � � � �   � � �  � � � � � � 1 Y  ��
CREATE UNIQUE INDEX "DES_PK" ON "APP_M_DESIGNATION" ("NDES_ID" )  PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING
ANALSTATS IS "APP_M_DESIGNATION"
W BEGIN  DBMS_STATS.SET_INDEX_STATS(NULL,'"DES_PK"',NULL,NULL,NULL,6,1,6,1,1,1,0,6); END;
ALTER TABLE "APP_M_DESIGNATION" ADD  CONSTRAINT "DES_PK" PRIMARY KEY ("NDES_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING ENABLE 
ANALSTATS TS "APP_M_DESIGNATION"
[ BEGIN  DBMS_STATS.SET_TABLE_STATS(NULL,'"APP_M_DESIGNATION"',NULL,NULL,NULL,6,5,35,6); END;
ANALSTATS TS "APP_M_DESIGNATION"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C107'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,6& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_DESIGNATION"','"NDES_ID"', NULL ,NULL,NULL,6,.166666666666667,0,srec,3,6); END;  
ANALSTATS TS "APP_M_DESIGNATION"
��� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '00BE00C600FC00D6002E201C00D600EA202000B800FC00B400D600AE00D6'; SREC.MAXVAL := '201C00D600EA202000B800FC00B400D600AE00D6'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(G 3853719103915810000000000000000000,166721473172914000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_DESIGNATION"','"VDES_NAME"', NULL ,NULL,NULL,6,.166666666666667,0,srec,28,6); END;  
ANALSTATS TS "APP_M_DESIGNATION"
��w DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '31'; SREC.MAXVAL := '31'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY($ 254422546068207000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 6� ); SREC.EPC := 1; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_DESIGNATION"','"NDISPLAY_ID"', NULL ,NULL,NULL,1,.0833333333333333,0,srec,2,6); END;  
ANALSTATS TS "APP_M_DESIGNATION"
��w DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '59'; SREC.MAXVAL := '59'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(I 462114420409600000000000000000000000,462114420409600000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1{ ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_DESIGNATION"','"VACTIVE"', NULL ,NULL,NULL,1,1,0,srec,2,6); END;  
COMMENT ON COLUMN "APP_M_DESIGNATION"."NDISPLAY_ID" IS 
 '1 : Directors 2 : Officers'
TABLE "APP_M_LIST"
CREATE TABLE "APP_M_LIST" ("NAPP_ID" NUMBER NOT NULL ENABLE, "VAPP_NAME" VARCHAR2(100))  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING NOCOMPRESS
INSERT INTO "APP_M_LIST" ("NAPP_ID", "VAPP_NAME") VALUES (:1, :2)
    d �       � SPSSKL   �2 Management, Farmer, Transpoter, Mukadam, Todanidar   � Management Web  ��
CREATE UNIQUE INDEX "APP_PK" ON "APP_M_LIST" ("NAPP_ID" )  PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING
ANALSTATS IS "APP_M_LIST"
W BEGIN  DBMS_STATS.SET_INDEX_STATS(NULL,'"APP_PK"',NULL,NULL,NULL,3,1,3,1,1,1,0,6); END;
ALTER TABLE "APP_M_LIST" ADD  CONSTRAINT "APP_PK" PRIMARY KEY ("NAPP_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING ENABLE 
ANALSTATS TS "APP_M_LIST"
T BEGIN  DBMS_STATS.SET_TABLE_STATS(NULL,'"APP_M_LIST"',NULL,NULL,NULL,3,5,27,6); END;
ANALSTATS TS "APP_M_LIST"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C104'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,3& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_LIST"','"NAPP_ID"', NULL ,NULL,NULL,3,.333333333333333,0,srec,3,6); END;  
ANALSTATS TS "APP_M_LIST"
��� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '4D616E6167656D656E7420576562'; SREC.MAXVAL := '535053534B4C'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(I 401782997081657000000000000000000000,432589833742456000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_LIST"','"VAPP_NAME"', NULL ,NULL,NULL,3,.333333333333333,0,srec,25,6); END;  
TABLE "APP_M_MENU"
CREATE TABLE "APP_M_MENU" ("NGROUPID" NUMBER, "VGROUPNAME" NVARCHAR2(100), "VSTATUS_ACTIVE" VARCHAR2(1), "NANDROID_ORDER" NUMBER)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING NOCOMPRESS
ALTER TABLE "APP_M_MENU" MODIFY DEFAULT
< ALTER TABLE "APP_M_MENU" MODIFY ("VSTATUS_ACTIVE" DEFAULT 1)
INSERT INTO "APP_M_MENU" ("NGROUPID", "VGROUPNAME", "VSTATUS_ACTIVE", "NANDROID_ORDER") VALUES (:1, :2, :3, :4)
    � �   �         �
.  � � � : � �   � � � � � � �  !" � � � � � � � 1 �	   �  0 � � �   � � � � � � � 1 �   �  � � � - � � � � " � 1 �   �  � � � � �   � � � � � � � 1 �   �* x � � � : � � � �   � � � � � � � � � � � 1 �   �4  � � � � � � " � � � �!" �   � � � � � � �!" � � � � 1 �   �"  � � � � � � " � � � �!" �   A P P 1��   �  � � � � � � �   � � �!" � � � 1 �   �	  � �x �   � � �!" � � � 1 �  ��
ANALSTATS TS "APP_M_MENU"
T BEGIN  DBMS_STATS.SET_TABLE_STATS(NULL,'"APP_M_MENU"',NULL,NULL,NULL,9,5,42,6); END;
ANALSTATS TS "APP_M_MENU"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C10A'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,9& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_MENU"','"NGROUPID"', NULL ,NULL,NULL,9,.111111111111111,0,srec,3,6); END;  
ANALSTATS TS "APP_M_MENU"
��� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '00B400D600F200AE00D600EA202200D600B400D600EB212200FC002000410050'; SREC.MAXVAL := '203000FA00C300D6002000AE00D600D600EB00A400FC00DF'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(G 3650899959592230000000000000000000,167127132506263000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_MENU"','"VGROUPNAME"', NULL ,NULL,NULL,9,.111111111111111,0,srec,35,6); END;  
ANALSTATS TS "APP_M_MENU"
��w DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '31'; SREC.MAXVAL := '31'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY($ 254422546068207000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 9� ); SREC.EPC := 1; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_MENU"','"VSTATUS_ACTIVE"', NULL ,NULL,NULL,1,.0555555555555556,0,srec,2,6); END;  
ANALSTATS TS "APP_M_MENU"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C109'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,8& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1~ ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_MENU"','"NANDROID_ORDER"', NULL ,NULL,NULL,8,.125,1,srec,3,6); END;  
COMMENT ON COLUMN "APP_M_MENU"."VSTATUS_ACTIVE" IS 
 '1: Active, 2: Deactive'
TABLE "APP_M_REASON"
CREATE TABLE "APP_M_REASON" ("NREASON_ID" NUMBER NOT NULL ENABLE, "VREASON_NAME" VARCHAR2(150), "NREASON_GROUP_ID" NUMBER, "NREASON_BLOCK" VARCHAR2(1), "VREASON_SHORT" VARCHAR2(25), "VREASON_ID" VARCHAR2(1))  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "SYSTEM" LOGGING NOCOMPRESS
ALTER TABLE "APP_M_REASON" MODIFY DEFAULT
? ALTER TABLE "APP_M_REASON" MODIFY ("NREASON_BLOCK" DEFAULT 'N')
INSERT INTO "APP_M_REASON" ("NREASON_ID", "VREASON_NAME", "NREASON_GROUP_ID", "NREASON_BLOCK", "VREASON_SHORT", "VREASON_ID") VALUES (:1, :2, :3, :4, :5, :6)
    � �      �    �    �       �	. ��ß����������� ������ �������� �ָ���֮���� � N����   �- ��֤�� ������ ������ �������� �ָ���֮���� � N����   � ��ָ� ��ָ���֮�� ���������� � N �.���.���������   � ���-�����׸���� �������� � N	 ���-�������   �$ ���� ������-���ӛ������� ��ָ���֮�� � N ������-���ӛ���������   � ���� ������-��ָ��� � N ������-��ָ�����   �: ׾ֳ���֯�ϴ������������ �����߮�� ������ ��Ӥ� ��ֻ���� � N����   �
- ����� �����ָ����� ���ӓ�� ���֮������ָ� � N����   � ��ß��֓��� �������ߴ����� � N����   � ����ߴ����� ����ߟ� � N����   � ���޵�ֆ��־�� ����ߟ� � N����   � ��־��Ù��� ��׿֮� ��׸���� � N����   �% ������ׯֵ֮� ��ָ���֮�� ���������� � N ������.���������   � �������� ���֤�����Ծ�ִ����� � N����   � ��ָ� ��ָ���ִ����� � N����   � ������ ������ ���������֚�� � N����   � ����֟�߾ָ� � N����   �L %2$s ���ӓ���ָ�� פ�.%1$s ������ %3$s �֙�֟��� ����ָ� ������� �����. � N�� Y   �I ��֛�� ����׾ֻ���� פ�.%1$s ������ �֯��� ����ָ� ������ ������ �����. � N�� L   �X פ�.%1$s ������ �ֻ֯�� ��֛�� ���؛��� ����� ������ ׾ֳ����ֿ�� ��ӯ���� ��֬�־��. � N�� P   � ���� ���ָ�� �֙� � N ���ָ�� �֙���   �  ��������� �������� ���������� � N����   � þ֟�: ��ֻ�������� � N����  ��
CREATE UNIQUE INDEX "REASON_KEY" ON "APP_M_REASON" ("NREASON_ID" )  PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING
ANALSTATS IS "APP_M_REASON"
] BEGIN  DBMS_STATS.SET_INDEX_STATS(NULL,'"REASON_KEY"',NULL,NULL,NULL,22,1,22,1,1,1,0,6); END;
ALTER TABLE "APP_M_REASON" ADD  CONSTRAINT "REASON_KEY" PRIMARY KEY ("NREASON_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING ENABLE 
ANALSTATS TS "APP_M_REASON"
W BEGIN  DBMS_STATS.SET_TABLE_STATS(NULL,'"APP_M_REASON"',NULL,NULL,NULL,22,1,52,6); END;
ANALSTATS TS "APP_M_REASON"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C117'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,22& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_REASON"','"NREASON_ID"', NULL ,NULL,NULL,22,.0454545454545455,0,srec,3,6); END;  
ANALSTATS TS "APP_M_REASON"
��� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '2532247320B5D6D6D393D6EAABFCD6B8EAFC20D7A4FC2E2531247320B8FCD6EA'; SREC.MAXVAL := 'DDD6EA99FCDBEAFAAED6209FD6D6EA9BFCDED6DF20AFD6CFD6EADDD6CFF2B4D6'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(J 193131992090142000000000000000000000,1151856628438300000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_REASON"','"VREASON_NAME"', NULL ,NULL,NULL,22,.0454545454545455,0,srec,38,6); END;  
ANALSTATS TS "APP_M_REASON"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C105'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,2,3,4& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 12,13,19,22� ); SREC.EPC := 4; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_REASON"','"NREASON_GROUP_ID"', NULL ,NULL,NULL,4,.0227272727272727,0,srec,3,6); END;  
ANALSTATS TS "APP_M_REASON"
��w DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '4E'; SREC.MAXVAL := '4E'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY($ 404999154965717000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 22� ); SREC.EPC := 1; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_REASON"','"NREASON_BLOCK"', NULL ,NULL,NULL,1,.0227272727272727,0,srec,2,6); END;  
ANALSTATS TS "APP_M_REASON"
��� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '872EDBFAD62EDDD6D6F4FBAFD6'; SREC.MAXVAL := 'B5D6E399FCD6EA2EDDD6D6F4FBAFD6'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(I 701910495341744000000000000000000000,944164199499747000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_REASON"','"VREASON_SHORT"', NULL ,NULL,NULL,6,.166666666666667,16,srec,5,6); END;  
ANALSTATS TS "APP_M_REASON"
��w DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '4C'; SREC.MAXVAL := '59'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(I 394614561248647000000000000000000000,462114420409600000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_REASON"','"VREASON_ID"', NULL ,NULL,NULL,3,.333333333333333,19,srec,2,6); END;  
COMMENT ON COLUMN "APP_M_REASON"."NREASON_BLOCK" IS 
 'N-Open, Y-Block'
TABLE "APP_M_REASON_GROUP"
CREATE TABLE "APP_M_REASON_GROUP" ("NREASON_GROUP_ID" NUMBER NOT NULL ENABLE, "VREASON_GROUP_NAME" NVARCHAR2(100))  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "SYSTEM" LOGGING NOCOMPRESS
INSERT INTO "APP_M_REASON_GROUP" ("NREASON_GROUP_ID", "VREASON_GROUP_NAME") VALUES (:1, :2)
    � �      �D  � � � � � �Rx � � �  x � � � : � � � �   � � � � � � � � � � � �   �6  " � � � � �  !" � � � � " �   � � � � � � � � � � � �   �"  0 � � �   � � � � � � � � � �!" �   �2  � � � � � � � � �   � � � � � � �   � � �!" � � �  ��
CREATE UNIQUE INDEX "REASON_GRP_KEY" ON "APP_M_REASON_GROUP" ("NREASON_GROUP_ID" )  PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING
ANALSTATS IS "APP_M_REASON_GROUP"
_ BEGIN  DBMS_STATS.SET_INDEX_STATS(NULL,'"REASON_GRP_KEY"',NULL,NULL,NULL,4,1,4,1,1,1,0,6); END;
ALTER TABLE "APP_M_REASON_GROUP" ADD  CONSTRAINT "REASON_GRP_KEY" PRIMARY KEY ("NREASON_GROUP_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING ENABLE 
ANALSTATS TS "APP_M_REASON_GROUP"
\ BEGIN  DBMS_STATS.SET_TABLE_STATS(NULL,'"APP_M_REASON_GROUP"',NULL,NULL,NULL,4,1,56,6); END;
ANALSTATS TS "APP_M_REASON_GROUP"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C105'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,4& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_REASON_GROUP"','"NREASON_GROUP_ID"', NULL ,NULL,NULL,4,.25,0,srec,3,6); END;  
ANALSTATS TS "APP_M_REASON_GROUP"
��� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '00BE00D600EE00B500D600D70152017800D600DB00FA0020017800D600D600EA'; SREC.MAXVAL := '203000FA00C300D6002000D700BE00D600BB00C600EA00FC00BE00D600D62122'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(G 3853724055609860000000000000000000,167127132506263000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_REASON_GROUP"','"VREASON_GROUP_NAME"', NULL ,NULL,NULL,4,.25,0,srec,53,6); END;  
TABLE "APP_M_SUBMENU"
CREATE TABLE "APP_M_SUBMENU" ("NID" NUMBER NOT NULL ENABLE, "VNAME" NVARCHAR2(60), "VURL" VARCHAR2(50), "NPARENTID" NUMBER, "NINNER" NUMBER, "NGROUPID" NUMBER, "NORDER_ANDROID" NUMBER, "VSTATUS_ACTIVE" VARCHAR2(2), "VIMG_NAME" NVARCHAR2(5), "NCALLER_ID" NUMBER, "NYEAR_TYPE" NUMBER(2, 0), "VNAME_DETAIL" NVARCHAR2(200))  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING NOCOMPRESS
ALTER TABLE "APP_M_SUBMENU" MODIFY DEFAULT
? ALTER TABLE "APP_M_SUBMENU" MODIFY ("VSTATUS_ACTIVE" DEFAULT 1)
INSERT INTO "APP_M_SUBMENU" ("NID", "VNAME", "VURL", "NPARENTID", "NINNER", "NGROUPID", "NORDER_ANDROID", "VSTATUS_ACTIVE", "VIMG_NAME", "NCALLER_ID", "NYEAR_TYPE", "VNAME_DETAIL") VALUES (:1, :2, :3, :4, :5, :6, :7, :8, :9, :10, :11, :12)
    x �  2 �            �   
 �      ��      �D(  � � � � � � � � � �!" �   � � � � � � � vilhangamrpt���� � � 1�� ����  � � � � � � � � � � � � �   � � � � � � � � � � � � � � � �   0 � � �   � � � � � � � � � �!" �   � � � � � ,     � .!" � � � � " �   �B6  � �!" � � � � � �   " � �x � � � � �   � � � � � � � varietysummaryreport���� � � 1�� ���V  � �!" � � � � � �   " � �x � � � � �   � � � � � � �     � " � ,   � " �   � � � � �   �EN  � �!" � � � � � �   0 � � �   � � � � � � � � � � � � � � � �   � � � � � � � canetypetonnage���� � � 1�� ���j  � �!" � � � � � �   0 � � �   � � � � � � � � � � � � � � � �   � � � � � � � ,  !" �R � � � � � � � � �   �  H o m e menu?url=home�� ����� 1��������   �  M a s t e r #�� ����� 1��������   �  U s e r   D e t a i l s menu?url=systemuser � ����� 1��������   �.  � � �x � � � � � �   � � � � � � � � � � � ������� � � 1  1 0������   �  � � � � � � � �!" � � ������� � � 1��������   �#"  � �!" � � � � � �   � � � � � � � sectionsummaryreport���� � � 1�� ���B  � �!" � � � � � �   � � � � � � �     � " � ,   � " �   � � � � �   �$"  � � � � � � �   � � � � � � � � � crushingreport���� � � 1�� ���"  � � � � � � �   � � � � � � � � �   �%X  � �!" � � � � � �  x � �!" � � � � � � �   � � � � � , � � � � � � � �  !" � � � � " �	 cutshetra���� � �	 1�� � ��  � �!" � � � � � �   � � � � � � � � � � � � ,x � �!" � � � � � � � , � � � � � � � � � �!" �   � �   � � � � � � � �   � � � � � ,!" � � � � " �   �&P  � �!" � � � � �   � � � � � � � � � � � � �  x � �!" � � � � � � �   � � � � � harvarea���� � �
 1�� � �x  � �!" � � � � �   � � � � � � � � � � � � �  x � �!" � � � � � � �   � � � � � ,x � �!" � � � � � � �  !" � � � � " �   �'6  � �!" � � � � �   � � � � � � � � � �!" �   � � � � � vilarea���� � � 1�� � �z  � �!" � � � � �   � � � � � � � � � � � � � � � �   0 � � �   � � � � � � � � � �!" �   � � � � � ,     � .!" � � � � " �   �(F  � �!" � � � � � �   � � � � � � � � � � � � �   0 � � �   � � � � � � uslaganreport���� � � 1�� � �R  � �!" � � � � � �   � � � � � � � � � � � � �   � � � � � � ,   � .!" � � � � " �   �)H  � � � � � � � � � � � � �   " � �x � � � � � �   0 � � �   � � � � � � usjatwarreport���� � � 1�� � �T  � � � � � � � � � � � � �   " � �x � � � � � �   � � � � � � ,   � .!" � � � � " �   �*@  � �!" � � � � � �   " � �x � � � � � �   0 � � �   � � � � � � usgatjatreport���� � � 1�� � �L  � �!" � � � � � �   " � �x � � � � � �   � � � � � � ,   � .!" � � � � " �   �+,  0 � � �   � � � � � � � �   � � � � � � � � usrawanareport���� � � 1�� ���,  0 � � �   � � � � � � � �   � � � � � � � �   �,*  � � � � � � � � : � �   � � � � � �x � � caneyardbalance���� � � 1�� ���*  � � � � � � � � : � �   � � � � � �x � �   �-2  � � � � � � � � �   � � � � � � �   � � � � � � � sugarreport���� � � 1�� ���2  � � � � � � � � �   � � � � � � �   � � � � � � �   �.2  � : �  � � � �   � � � � � � �     � � � � � � � dieselreport���� � � 2�� � �2  � : �  � � � �   � � � � � � �     � � � � � � �   �"(  � � �x � � � � � �   � � � � � �x � � farmerreport���� � � 1�� ���(  � � �x � � � � � �   � � � � � �x � �   �6(  � � � � � � � � � � �   � � � � � � � ������� � � 1��������   �&  � � � � � �   � � � � � � � � � � � ������� � � 1  4������   �"  � � � � � � �   � � � � � �x � ������� � � 1  5������   �   � � � � � �   � � " � � � �x ������� � � 1  8������   �"  � � � � � �   � � � � � � �!" � ������� � �	 1  7������   �  0 � � � �   � � � � �x � ������� � � 1  1 3������   �	.  0 � � �   � � � � �x � �   � � � � � �x � ������� � � 1  1 4������   �
0  0 � � � �   � � � � �x � � �   � � � � � � � ������� � � 1  1 5������   �$  0 � � � �   � � � � � � � � � �!" ������� � � 1  1 1������   �.  0 � � �     � � � � �   � � � � � � � � � � ������� � � 1  3 6������   �  � � � � �   � � � � � � ������� � � 1  5 0������   �.  � � � � �   � � � � � � �   � � � � � �x � ������� � � 1  5 2������   �B  0 � � �x � � � : �   � � � � � � � � � � �   � � � � � � � �x ������� � � 1��������   �4  � �!" � � � � : � ������� � � 1��������   �5"  � � � � � � � �   � � � � � � � ������� � � 1��������   �8*  P l a n t a t i o n   M a p   R e p o r t menu?url=plantationMapReport � ����� 1��������   �>\  � �!" � � � � � �   � � � � � � � � � � � � �   " � �x � � � � � �   0 � � �   � � � � � � hangamjatsectreport���� � � 1�� � �h  � �!" � � � � � �   � � � � � � � � � � � � �   " � �x � � � � � �   � � � � � � ,   � .!" � � � � " �   �(  � � �x � � � � � �   � � � � � �x � ������� � � 1  9������   �  0 � � �   � � � � � ������� � � 1  3������   �9  � � � � � � �     � � � � ������� � � 1��������   �:   � � � � � � �   � � � � �x � ������� � � 1��������   �;&  � � � � �x � �   � � � - � � � �!" ������� � � 1��������   �<&  � � � � � � � � � � �   " � � � � � ������� � � 1��������   �=  � � �!" � � �   � � � � � � ������� � � 1��������   �?  � �x �   � � �!" � � ������� �	 � 1��������   �,  0 � � �x � � � : �   � � � � � � � � � � ������� � � 1��������   �D  0 � � �x � � � : �   � � � � � � � � � � �   � � � �Rx � � �x ������� � � 1��������   �D  " � � � � �  !" � � � � " �  x � � � : �   � � � � � � � � � � � ������� � � 1��������   �H x � � � : � - � � � � � � ,   � � �S � � � � - � � � � �   � � � � � ������� � �	 1  1 4������   �@. x � �!" � � � � � � �   � �   � � � � � � � ������� � � 1  7������   �  v i e w   d a t a������ � �
 1  5 2������   �$  � � � � � �x � �   � � �a � � � ������� � � 1  5 2������   �  v i e w   s e n d   d a t a������ � � 1  5 2������   �  � � � � � � � ������� � � 1��������   �  � � �x � ������� � � 1��������   �
  � � � � ������� � � 1��������   �  � � � � � � � � � � ������� � � 1��������   �   � � � � � � � � � � � ������� � � 1��������   �!&  �!" � � �   � � - � � � � � �!" � � ������� � � 1��������   �/  E m p l o y e e   a t t employeereport���� � � 2�� � �  E m p l o y e e   a t t   �0,  � � � � � �   � � � � � � �   � � � � � � � tendersugarF���� � � 2�� ���,  � � � � � �   � � � � � � �   � � � � � � �   �12  � � � � � � �x �   � � � � � � �   � � � � � � � tendersugarE���� � � 2�� ���2  � � � � � � �x �   � � � � � � �   � � � � � � �   �22  m e m b e r   m e e t i n g   a t t a n d a n c e memattreport���� � � 2�� ���2  m e m b e r   m e e t i n g   a t t a n d a n c e   �3  B i r t h d a y   w i s h e s birthdaywishes���� � � 2�� ���  B i r t h d a y   w i s h e s   �7$  � �!" � � � � � �   � � � � � � � ������� � � 1��������   �A$  � � �x � � � � � �  !" � � � � " ������� � �
 1��������   �F   � � � : � �   " � � � : � � � ������� �
 � 1��������   �C   � � � � � �   " � � � : � � � ������� � � 1��������  ��
CREATE UNIQUE INDEX "PERMISSION_PK" ON "APP_M_SUBMENU" ("NID" )  PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING
ANALSTATS IS "APP_M_SUBMENU"
` BEGIN  DBMS_STATS.SET_INDEX_STATS(NULL,'"PERMISSION_PK"',NULL,NULL,NULL,68,1,68,1,1,5,0,6); END;
ALTER TABLE "APP_M_SUBMENU" ADD  CONSTRAINT "PERMISSION_PK" PRIMARY KEY ("NID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING ENABLE 
ANALSTATS TS "APP_M_SUBMENU"
X BEGIN  DBMS_STATS.SET_TABLE_STATS(NULL,'"APP_M_SUBMENU"',NULL,NULL,NULL,68,5,90,6); END;
ANALSTATS TS "APP_M_SUBMENU"
��� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '004200690072007400680064006100790020007700690073006800650073'; SREC.MAXVAL := '203000FA00C300D6002000B800FC00BE00D600D600AE00D600D6002000BE00D6'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(G 1338671530305400000000000000000000,167127132506263000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"VNAME_DETAIL"', NULL ,NULL,NULL,22,.0454545454545455,46,srec,25,6); END;  
ANALSTATS TS "APP_M_SUBMENU"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C145'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,68& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"NID"', NULL ,NULL,NULL,68,.0147058823529412,0,srec,3,6); END;  
ANALSTATS TS "APP_M_SUBMENU"
��� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '004200690072007400680064006100790020007700690073006800650073'; SREC.MAXVAL := '203000FA00C300D6017800D600D600EA203A00FC002000AF00D600CF00D600EA'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(G 1338671530305400000000000000000000,167127132506263000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"VNAME"', NULL ,NULL,NULL,67,.0149253731343284,0,srec,42,6); END;  
ANALSTATS TS "APP_M_SUBMENU"
��� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '23'; SREC.MAXVAL := '76696C68616E67616D727074'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(I 181730390048719000000000000000000000,614829271261273000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1x ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"VURL"', NULL ,NULL,NULL,25,.04,43,srec,6,6); END;  
ANALSTATS TS "APP_M_SUBMENU"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C110'; SREC.MAXVAL := 'C110'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 15,15& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1z ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"NPARENTID"', NULL ,NULL,NULL,1,1,66,srec,2,6); END;  
ANALSTATS TS "APP_M_SUBMENU"
��y DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '80'; SREC.MAXVAL := 'C102'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 0,1& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1x ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"NINNER"', NULL ,NULL,NULL,2,.5,64,srec,2,6); END;  
ANALSTATS TS "APP_M_SUBMENU"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C109'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,2,3,4,5,6,7,8& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 11,22,24,29,36,58,63,64 ); SREC.EPC := 8; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"NGROUPID"', NULL ,NULL,NULL,8,.0078125,4,srec,3,6); END;  
ANALSTATS TS "APP_M_SUBMENU"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C117'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,22& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"NORDER_ANDROID"', NULL ,NULL,NULL,22,.0454545454545455,4,srec,3,6); END;  
ANALSTATS TS "APP_M_SUBMENU"
��w DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '31'; SREC.MAXVAL := '32'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(I 254422546068207000000000000000000000,259614842926741000000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 62,68� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"VSTATUS_ACTIVE"', NULL ,NULL,NULL,2,.00735294117647059,0,srec,2,6); END;  
ANALSTATS TS "APP_M_SUBMENU"
�� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := '00310030'; SREC.MAXVAL := '0039'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(D 993852925859403000000000000000000,1156097347408150000000000000000000& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1� ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"VIMG_NAME"', NULL ,NULL,NULL,14,.0714285714285714,49,srec,2,6); END;  
ANALSTATS TS "APP_M_SUBMENU"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C106'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,5& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1| ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"NCALLER_ID"', NULL ,NULL,NULL,5,.2,47,srec,2,6); END;  
ANALSTATS TS "APP_M_SUBMENU"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C103'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY( 1,2& ); SREC.BKVALS := DBMS_STATS.NUMARRAY( 0,1| ); SREC.EPC := 2; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU"','"NYEAR_TYPE"', NULL ,NULL,NULL,2,.5,59,srec,2,6); END;  
COMMENT ON COLUMN "APP_M_SUBMENU"."VSTATUS_ACTIVE" IS 
 '1: Active, 2: Deactive'
COMMENT ON COLUMN "APP_M_SUBMENU"."NYEAR_TYPE" IS 
< '1 : last 3 year, 2 :last 2 ignore next year, 3: sugar year'
TABLE "APP_M_SUBMENU_ROLE"
CREATE TABLE "APP_M_SUBMENU_ROLE" ("NID" NUMBER NOT NULL ENABLE, "NUSER_ROLE_ID" NUMBER NOT NULL ENABLE)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING NOCOMPRESS
INSERT INTO "APP_M_SUBMENU_ROLE" ("NID", "NUSER_ROLE_ID") VALUES (:1, :2)
          � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   �	 �   �	 �   �
 �   �
 �   �
 �   �
 �   � �   � �   � �   � �   � �
   � �   � �
   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   � �   �" �   �" �   �# �   �# �   �$ �   �$ �   �% �   �% �   �& �   �& �   �' �   �' �   �( �   �( �   �) �   �) �   �* �   �* �   �+ �   �+ �   �, �   �, �   �- �   �- �   �. �   �. �   �/ �   �/ �   �0 �   �0 �   �1 �   �1 �   �2 �   �2 �   �3 �   �3 �   �4 �   �4 �   �5 �   �5 �   �6 �	   �6 �   �6 �   �6 �   �7 �   �7 �   �7 �   �8 �   �8 �   �9 �   �9 �   �: �   �: �   �; �   �; �   �< �   �< �   �= �   �= �   �> �   �> �   �? �	   �? �   �@ �   �@ �   �A �   �A �   �B �   �B �   �C �   �C �   �D �   �D �   �E �   �F �  ��
CREATE UNIQUE INDEX "NID_ROLID" ON "APP_M_SUBMENU_ROLE" ("NID" , "NUSER_ROLE_ID" )  PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING
ANALSTATS IS "APP_M_SUBMENU_ROLE"
_ BEGIN  DBMS_STATS.SET_INDEX_STATS(NULL,'"NID_ROLID"',NULL,NULL,NULL,129,1,129,1,1,12,0,6); END;
ALTER TABLE "APP_M_SUBMENU_ROLE" ADD  CONSTRAINT "NID_ROLID" PRIMARY KEY ("NID", "NUSER_ROLE_ID") USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT) TABLESPACE "USERS" LOGGING ENABLE 
ANALSTATS TS "APP_M_SUBMENU_ROLE"
] BEGIN  DBMS_STATS.SET_TABLE_STATS(NULL,'"APP_M_SUBMENU_ROLE"',NULL,NULL,NULL,129,5,7,6); END;
ANALSTATS TS "APP_M_SUBMENU_ROLE"
��{ DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C102'; SREC.MAXVAL := 'C144'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(� 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,18,20,21,22,23,24,25,26,27,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67& ); SREC.BKVALS := DBMS_STATS.NUMARRAY(� 2,4,6,10,12,14,17,19,23,25,27,29,31,33,35,37,39,41,43,45,47,49,51,54,56,58,60,62,64,66,68,70,72,74,76,78,80,82,84,86,88,90,92,94,96,100,103,105,107,109,111,113,115,117,119,121,123,125,127,129� ); SREC.EPC := 60; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU_ROLE"','"NID"', NULL ,NULL,NULL,60,.00387596899224806,0,srec,3,6); END;  
ANALSTATS TS "APP_M_SUBMENU_ROLE"
�� DECLARE  SREC DBMS_STATS.STATREC; BEGIN SREC.MINVAL := 'C20205'; SREC.MAXVAL := 'C20211'; SREC.EAVS := 0; SREC.CHVALS := NULL; # SREC.NOVALS := DBMS_STATS.NUMARRAY(' 104,105,108,109,110,111,113,114,115,116& ); SREC.BKVALS := DBMS_STATS.NUMARRAY(! 16,20,22,24,29,51,111,121,128,129� ); SREC.EPC := 10; DBMS_STATS.SET_COLUMN_STATS(NULL,'"APP_M_SUBMENU_ROLE"','"NUSER_ROLE_ID"', NULL ,NULL,NULL,10,.00387596899224806,0,srec,4,6); END;  
METRICSTreferential integrity constraints
METRICET 131
METRICSTtriggers
METRICET 131
METRICSTbitmap, functional and extensible indexes
METRICET 131
METRICSTposttables actions
METRICET 131
METRICSTPost-inst procedural actions 
METRICET 131
METRICSTreferential integrity constraints
METRICET 131
METRICSTtriggers
METRICET 131
METRICSTbitmap, functional and extensible indexes
METRICET 131
METRICSTposttables actions
METRICET 131
METRICSTPost-inst procedural actions 
METRICET 131
METRICSTreferential integrity constraints
METRICET 131
METRICSTtriggers
METRICET 131
METRICSTbitmap, functional and extensible indexes
METRICET 131
METRICSTposttables actions
METRICET 131
METRICSTPost-inst procedural actions 
METRICET 131
METRICSTreferential integrity constraints
METRICET 131
METRICSTtriggers
METRICET 131
METRICSTbitmap, functional and extensible indexes
METRICET 131
METRICSTposttables actions
METRICET 131
METRICSTPost-inst procedural actions 
METRICET 131
METRICSTreferential integrity constraints
METRICET 131
METRICSTtriggers
METRICET 131
METRICSTbitmap, functional and extensible indexes
METRICET 131
METRICSTposttables actions
METRICET 131
METRICSTPost-inst procedural actions 
METRICET 131
METRICSTreferential integrity constraints
METRICET 131
METRICSTtriggers
METRICET 131
METRICSTbitmap, functional and extensible indexes
METRICET 131
METRICSTposttables actions
METRICET 131
METRICSTPost-inst procedural actions 
METRICET 131
METRICSTreferential integrity constraints
METRICET 131
METRICSTtriggers
METRICET 131
METRICSTbitmap, functional and extensible indexes
METRICET 131
METRICSTposttables actions
METRICET 131
METRICSTPost-inst procedural actions 
METRICET 131
METRICSTreferential integrity constraints
METRICET 131
METRICSTtriggers
METRICET 131
METRICSTbitmap, functional and extensible indexes
METRICET 131
METRICSTposttables actions
METRICET 131
METRICSTPost-inst procedural actions 
METRICET 131
METRICSTreferential integrity constraints
METRICET 131
METRICSTtriggers
METRICET 131
METRICSTbitmap, functional and extensible indexes
METRICET 131
METRICSTposttables actions
METRICET 131
METRICSTPost-inst procedural actions 
METRICET 131
METRICSTDeferred analyze commands 
TABLE "APP_M_APP_ROLE"
ANALCOMPUTE TS "APP_M_APP_ROLE" ANALYZE  TABLE "APP_M_APP_ROLE"  ESTIMATE STATISTICS 
TABLE "APP_M_CANEYARD"
ANALCOMPUTE TS "APP_M_CANEYARD" ANALYZE  TABLE "APP_M_CANEYARD"  ESTIMATE STATISTICS 
TABLE "APP_M_DESIGNATION"
ANALCOMPUTE TS "APP_M_DESIGNATION" ANALYZE  TABLE "APP_M_DESIGNATION"  ESTIMATE STATISTICS 
TABLE "APP_M_LIST"
ANALCOMPUTE TS "APP_M_LIST" ANALYZE  TABLE "APP_M_LIST"  ESTIMATE STATISTICS 
TABLE "APP_M_MENU"
ANALCOMPUTE TS "APP_M_MENU" ANALYZE  TABLE "APP_M_MENU"  ESTIMATE STATISTICS 
TABLE "APP_M_REASON"
ANALCOMPUTE TS "APP_M_REASON" ANALYZE  TABLE "APP_M_REASON"  ESTIMATE STATISTICS 
TABLE "APP_M_REASON_GROUP"
ANALCOMPUTE TS "APP_M_REASON_GROUP" ANALYZE  TABLE "APP_M_REASON_GROUP"  ESTIMATE STATISTICS 
TABLE "APP_M_SUBMENU"
ANALCOMPUTE TS "APP_M_SUBMENU" ANALYZE  TABLE "APP_M_SUBMENU"  ESTIMATE STATISTICS 
TABLE "APP_M_SUBMENU_ROLE"
ANALCOMPUTE TS "APP_M_SUBMENU_ROLE" ANALYZE  TABLE "APP_M_SUBMENU_ROLE"  ESTIMATE STATISTICS 
ENDTABLE
METRICET 140
METRICETG0
EXIT
EXIT
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                