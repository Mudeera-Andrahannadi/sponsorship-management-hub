import { createContext, useContext, useState } from 'react';
import { keep_login, post, saved_login } from './api.js';

const AuthContext = createContext(null);

export function AuthProvider({ children })
{
    const [me, set_me] = useState(saved_login);

    async function enter(path, body)
    {
        const data = await post(path, body);
        keep_login(data);
        set_me(data);
    }

    function leave()
    {
        keep_login(null);
        set_me(null);
    }

    const value =
    {
        me,
        is_admin: !!me && me.role === 'Admin',
        sign_in: (body) => enter('/auth/login', body),
        sign_up: (body) => enter('/auth/register', body),
        leave
    };
    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth()
{
    return useContext(AuthContext);
}
