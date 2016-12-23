-- 楽観的排他制御用
CREATE TABLE MEMO_FOR_OPTIMISTIC (
    ID              BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    TITLE           VARCHAR(30)   NOT NULL,
    CONTENT         VARCHAR(1000) NOT NULL,
    UPDATE_DATETIME DATETIME NOT NULL
);

-- 悲観的排他制御用
CREATE TABLE MEMO_FOR_PESSIMISTIC (
    ID          BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    TITLE       VARCHAR(30)   NOT NULL,
    CONTENT     VARCHAR(1024) NOT NULL
);

CREATE TABLE PESSIMISTIC_LOCK (
    ID              BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    TARGET_CODE     VARCHAR(128) NOT NULL,
    TARGET_ID       BIGINT       NOT NULL,
    LOGIN_ID        VARCHAR(32)  NOT NULL,
    UPDATE_DATETIME DATETIME     NOT NULL,
    CONSTRAINT PESSIMISTIC_LOCK_UK1 UNIQUE (TARGET_CODE, TARGET_ID)
);