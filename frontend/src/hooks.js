import { useCallback, useEffect, useRef, useState } from 'react';
import { error_text, get } from './api.js';

export function useLoad(url)
{
    const [state, set_state] = useState({ data: null, error: '', loading: !!url });

    const reload = useCallback(async () =>
    {
        if (!url)
        {
            return;
        }
        try
        {
            const data = await get(url);
            set_state({ data, error: '', loading: false });
        }
        catch (err)
        {
            set_state((old) => ({ ...old, error: error_text(err), loading: false }));
        }
    }, [url]);

    useEffect(() =>
    {
        reload();
    }, [reload]);

    return { ...state, reload };
}

export function useRefresh(reload)
{
    useEffect(() =>
    {
        const timer = setInterval(reload, 60000);
        const wake = () => document.visibilityState === 'visible' && reload();
        document.addEventListener('visibilitychange', wake);
        return () =>
        {
            clearInterval(timer);
            document.removeEventListener('visibilitychange', wake);
        };
    }, [reload]);
}

export function useFresh(list)
{
    const seen = useRef(null);
    const [fresh, set_fresh] = useState(new Set());

    useEffect(() =>
    {
        if (!list)
        {
            return undefined;
        }
        const now = Object.fromEntries(list.map((item) => [item.comp_id, item.status + item.held_by]));
        const changed = seen.current ? list.filter((item) => seen.current[item.comp_id] !== now[item.comp_id]) : [];
        seen.current = now;
        if (changed.length === 0)
        {
            return undefined;
        }
        set_fresh(new Set(changed.map((item) => item.comp_id)));
        const timer = setTimeout(() => set_fresh(new Set()), 1600);
        return () => clearTimeout(timer);
    }, [list]);

    return fresh;
}
