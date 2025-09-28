-- docker pull gvenzl/oracle-xe
-- docker run --name oracle-xe  -p 1521:1521  -e ORACLE_RANDOM_PASSWORD=yes  -d gvenzl/oracle-xe
-- ORACLE PASSWORD FOR SYS AND SYSTEM: YzZiMTkw
CREATE TABLE postgres.users (
	id int NOT NULL,
	username varchar(50) NOT NULL,
	email varchar(100) NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	status varchar(20) NOT NULL,
	start_date date NULL,
	end_date date NULL,
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);



INSERT ALL
  INTO postgres.users (id, username, email, status, start_date, end_date)
    VALUES (1, 'alice', 'alice@example.com', 'active', DATE '2023-01-01', DATE '2025-09-30')
  INTO postgres.users (id, username, email, status, start_date, end_date)
    VALUES (2, 'bob', 'bob@example.com', 'active', DATE '2023-01-01', DATE '2025-09-30')
  INTO postgres.users (id, username, email, status, start_date, end_date)
    VALUES (3, 'charlie', 'charlie@example.com', 'active', DATE '2023-01-01', DATE '2025-09-30')
  INTO postgres.users (id, username, email, status, start_date, end_date)
    VALUES (4, 'david', 'david@example.com', 'inactive', DATE '2023-01-01', DATE '2025-09-30')
  INTO postgres.users (id, username, email, status, start_date, end_date)
    VALUES (5, 'emma', 'emma@example.com', 'inactive', DATE '2023-01-01', DATE '2025-09-30')
  INTO postgres.users (id, username, email, status, start_date, end_date)
    VALUES (6, 'frank', 'frank@example.com', 'inactive', DATE '2023-01-01', DATE '2025-09-30')
  INTO postgres.users (id, username, email, status, start_date, end_date)
    VALUES (7, 'grace', 'grace@example.com', 'inactive', DATE '2023-01-01', DATE '2025-09-30')
  INTO postgres.users (id, username, email, status, start_date, end_date)
    VALUES (8, 'henry', 'henry@example.com', 'inactive', DATE '2023-01-01', DATE '2025-09-30')
  INTO postgres.users (id, username, email, status, start_date, end_date)
    VALUES (9, 'irene', 'irene@example.com', 'inactive', DATE '2023-01-01', DATE '2025-09-30')
  INTO postgres.users (id, username, email, status, start_date, end_date)
    VALUES (10, 'jack', 'jack@example.com', 'inactive', DATE '2023-01-01', DATE '2025-09-30')
SELECT 1 FROM dual;


--
CREATE OR REPLACE PROCEDURE postgres.get_users_by_status_and_dates (
    p_status      IN  VARCHAR2,
    p_start_date  IN  DATE,
    p_end_date    IN  DATE,
    p_recordset   OUT SYS_REFCURSOR
)
AS
BEGIN
    OPEN p_recordset FOR
        SELECT u.id,
               u.username,
               u.email,
               u.status,
               u.start_date,
               u.end_date
        FROM   postgres.users u
        WHERE  u.status = p_status
          AND  u.start_date >= p_start_date
          AND  (u.end_date <= p_end_date OR u.end_date IS NULL)
        ORDER BY u.start_date;
END;

--Test your storeproc:
DECLARE
    rc SYS_REFCURSOR;
    v_id        users.id%TYPE;
    v_username  users.username%TYPE;
    v_email     users.email%TYPE;
    v_status    users.status%TYPE;
    v_start     users.start_date%TYPE;
    v_end       users.end_date%TYPE;
BEGIN
    -- Call the procedure
    postgres.get_users_by_status_and_dates(
        p_status     => 'inactive',
        p_start_date => DATE '2023-01-01',
        p_end_date   => DATE '2025-09-30',
        p_recordset  => rc
    );

    -- Loop through the cursor and print each row
    LOOP
        FETCH rc INTO v_id, v_username, v_email, v_status, v_start, v_end;
        EXIT WHEN rc%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE(
            v_id || ' | ' || v_username || ' | ' || v_email || ' | ' ||
            v_status || ' | ' || TO_CHAR(v_start, 'YYYY-MM-DD') || ' | ' ||
            NVL(TO_CHAR(v_end, 'YYYY-MM-DD'), 'NULL')
        );
    END LOOP;

    CLOSE rc;
END;

