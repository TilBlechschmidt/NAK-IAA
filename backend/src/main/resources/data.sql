---------------------
-- ID Ranges
-- User:                00 - 09
-- Survey:              10 - 19
-- Participations:
--        from Creator: 20 - 29
--       with Response: 30 - 39
--    without Response: 40 - 49
-- Responses:
--        from Creator: 50 - 59
--               other: 60 - 69
-- Timeslots:           70 - 99
-- ResponseTimeslots:
--        from Creator: 100 - 139
--               other: 140 - ? (make sure to update HIBERNATE_SEQUENCE START)

-----------
-- USERS --
-----------

-- PW = password0
INSERT INTO USER (ID, EMAIL, FULL_NAME, PASSWORD_HASH)
VALUES (0, 'Name0@example.com', 'Name0', '$2a$10$.U2xt8HHisWlSGDdUfBO4e4RpgZ41CYVb/YnC/F/XWHT1.gBepqDW');

-- PW = password1
INSERT INTO USER (ID, EMAIL, FULL_NAME, PASSWORD_HASH)
VALUES (1, 'Name1@example.com', 'Name1', '$2a$10$Ls0kE/AydHiuaHFdZXJXBuPpp4O51XC2vt.qfk4RlfVLn8OCry4aG');

-- PW = password2
INSERT INTO USER (ID, EMAIL, FULL_NAME, PASSWORD_HASH)
VALUES (2, 'Name2@example.com', 'Name2', '$2a$10$VxrZo8ixzU09.72TdMVew.CYbou0XwCN5c6Wza7fYPYKa4pdJLx7i');

-- PW = password3
INSERT INTO USER (ID, EMAIL, FULL_NAME, PASSWORD_HASH)
VALUES (3, 'Name3@example.com', 'Name3', '$2a$10$6mAknVUkgduqjCOmUePP7uO.Bgibgbbd9dpv.bJdGDUu11sXB6gz.');

-- PW = password4
INSERT INTO USER (ID, EMAIL, FULL_NAME, PASSWORD_HASH)
VALUES (4, 'Name4@examle.com', 'Name4', '$2a$10$upLmX5blX639STzhEuLwZ.PiFLHEBJXeglUPuKiYLpxvTvyyI1ppm');

-------------
-- SURVEYS --
-------------

-- Survey without participations
INSERT INTO SURVEY (ID, TITLE, DESCRIPTION, CREATOR_ID, SELECTED_TIMESLOT_ID)
VALUES (10, 'Example Survey 1', 'This example survey does not have any participations.', 0, null);

-- Survey with participations
INSERT INTO SURVEY (ID, TITLE, DESCRIPTION, CREATOR_ID, SELECTED_TIMESLOT_ID)
VALUES (11, 'Example Survey 2', 'This example survey has two responses (User 2 & 3).', 1, null);

-- Survey with changes
INSERT INTO SURVEY (ID, TITLE, DESCRIPTION, CREATOR_ID, SELECTED_TIMESLOT_ID)
VALUES (12, 'Example Survey 3', 'This example survey had two responses (User 2 & 3), but was modified.', 1, null);

-- Closed Survey
INSERT INTO SURVEY (ID, TITLE, DESCRIPTION, CREATOR_ID, SELECTED_TIMESLOT_ID)
VALUES (13, 'Example Survey 4', 'This example survey is closed (Participants 0 & 1).', 2, null);

--------------------
-- PARTICIPATIONS --
--------------------

-- Creator
INSERT INTO PARTICIPATION (ID, SURVEY_ID, PARTICIPANT_ID)
VALUES (20, 10, 0);
INSERT INTO PARTICIPATION (ID, SURVEY_ID, PARTICIPANT_ID)
VALUES (21, 11, 1);
INSERT INTO PARTICIPATION (ID, SURVEY_ID, PARTICIPANT_ID)
VALUES (22, 12, 1);
INSERT INTO PARTICIPATION (ID, SURVEY_ID, PARTICIPANT_ID)
VALUES (23, 13, 2);

-- Participations with responses
INSERT INTO PARTICIPATION (ID, SURVEY_ID, PARTICIPANT_ID)
VALUES (30, 11, 2);
INSERT INTO PARTICIPATION (ID, SURVEY_ID, PARTICIPANT_ID)
VALUES (31, 11, 3);
INSERT INTO PARTICIPATION (ID, SURVEY_ID, PARTICIPANT_ID)
VALUES (32, 13, 0);
INSERT INTO PARTICIPATION (ID, SURVEY_ID, PARTICIPANT_ID)
VALUES (33, 13, 1);

-- Participations without responses
INSERT INTO PARTICIPATION (ID, SURVEY_ID, PARTICIPANT_ID)
VALUES (40, 12, 2);
INSERT INTO PARTICIPATION (ID, SURVEY_ID, PARTICIPANT_ID)
VALUES (41, 12, 3);

---------------
-- RESPONSES --
---------------

-- Creator
INSERT INTO RESPONSE (ID, PARTICIPATION_ID)
VALUES (50, 20);
INSERT INTO RESPONSE (ID, PARTICIPATION_ID)
VALUES (51, 21);
INSERT INTO RESPONSE (ID, PARTICIPATION_ID)
VALUES (52, 22);
INSERT INTO RESPONSE (ID, PARTICIPATION_ID)
VALUES (53, 23);

-- Other
INSERT INTO RESPONSE (ID, PARTICIPATION_ID)
VALUES (60, 30);
INSERT INTO RESPONSE (ID, PARTICIPATION_ID)
VALUES (61, 31);
INSERT INTO RESPONSE (ID, PARTICIPATION_ID)
VALUES (62, 32);
INSERT INTO RESPONSE (ID, PARTICIPATION_ID)
VALUES (63, 33);

---------------
-- TIMESLOTS --
---------------

-- Survey 1
INSERT INTO TIMESLOT (ID, SURVEY_ID, START, END)
VALUES (70, 10, '2020-02-20 02:20:20Z', null);
INSERT INTO TIMESLOT (ID, SURVEY_ID, START, END)
VALUES (71, 10, '2020-02-21 02:20:20Z', null);

-- Survey 2
INSERT INTO TIMESLOT (ID, SURVEY_ID, START, END)
VALUES (72, 11, '2020-02-20 02:20:20Z', '2020-02-20 03:20:20Z');
INSERT INTO TIMESLOT (ID, SURVEY_ID, START, END)
VALUES (73, 11, '2020-02-21 02:20:20Z', '2020-02-21 03:20:20Z');

-- Survey 3
INSERT INTO TIMESLOT (ID, SURVEY_ID, START, END)
VALUES (74, 12, '2020-02-20 02:20:20Z', null);
INSERT INTO TIMESLOT (ID, SURVEY_ID, START, END)
VALUES (75, 12, '2020-02-21 02:20:20Z', null);

-- Survey 4
INSERT INTO TIMESLOT (ID, SURVEY_ID, START, END)
VALUES (76, 13, CURRENT_TIMESTAMP() + 365, CURRENT_TIMESTAMP() + 366);
INSERT INTO TIMESLOT (ID, SURVEY_ID, START, END)
VALUES (77, 13, CURRENT_TIMESTAMP() + 366, CURRENT_TIMESTAMP() + 367);
UPDATE SURVEY SET SELECTED_TIMESLOT_ID=77 WHERE ID = 13;

------------------------
-- RESPONSE-TIMESLOTS --
------------------------

-- Creator
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (100, 50, 70, 'YES');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (101, 50, 71, 'YES');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (102, 51, 72, 'YES');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (103, 51, 73, 'YES');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (104, 52, 74, 'YES');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (105, 52, 75, 'YES');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (106, 53, 76, 'YES');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (107, 53, 77, 'YES');

-- Other
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (140, 60, 72, 'YES');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (141, 60, 73, 'NO');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (142, 61, 72, 'YES');

INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (143, 62, 76, 'NO');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (144, 62, 77, 'YES');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (145, 63, 76, 'NO');
INSERT INTO RESPONSE_TIMESLOT (ID, RESPONSE_ID, TIMESLOT_ID, RESPONSE_TYPE)
VALUES (146, 63, 77, 'YES');

ALTER SEQUENCE HIBERNATE_SEQUENCE RESTART WITH 200;
