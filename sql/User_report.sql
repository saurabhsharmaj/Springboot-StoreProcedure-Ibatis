DROP FUNCTION IF EXISTS public.get_users_by_status_and_dates(varchar, date, date);

CREATE OR REPLACE FUNCTION public.get_users_by_status_and_dates(
    p_status varchar,
    p_start_date date,
    p_end_date date
)
RETURNS TABLE (
    id integer,
    username varchar,
    email varchar,
    status varchar,
    start_date date,
    end_date date
)
LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN QUERY
    SELECT u.id, u.username, u.email, u.status, u.start_date, u.end_date
    FROM users u
    WHERE u.status = p_status
      AND u.start_date >= p_start_date
      AND (u.end_date <= p_end_date OR u.end_date IS NULL)
    ORDER BY u.start_date;
END;
$function$;
