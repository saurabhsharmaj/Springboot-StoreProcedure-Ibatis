-- DROP PROCEDURE public.get_users_by_status_and_dates(in varchar, in date, in date, inout refcursor);

CREATE OR REPLACE PROCEDURE public.get_users_by_status_and_dates(IN p_status character varying, IN p_start_date date, IN p_end_date date, INOUT ref refcursor)
 LANGUAGE plpgsql
AS $procedure$
BEGIN
    -- Open the cursor with the result set
    OPEN ref FOR
        SELECT id, username, email, status, start_date, end_date
        FROM users
        WHERE status = p_status
          AND start_date >= p_start_date
          AND (end_date <= p_end_date OR end_date IS NULL)
        ORDER BY start_date;
END;
$procedure$
;