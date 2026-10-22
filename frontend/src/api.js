import axios from 'axios';

const KEY = 'hub_login';

export function saved_login()
{
    try
    {
        return JSON.parse(localStorage.getItem(KEY));
    }
    catch (err)
    {
        return null;
    }
}

export function keep_login(me)
{
    try
    {
        if (me)
        {
            localStorage.setItem(KEY, JSON.stringify(me));
            return;
        }
        localStorage.removeItem(KEY);
    }
    catch (err)
    {
        return;
    }
}

const client = axios.create({ baseURL: '/api' });

client.interceptors.request.use((conf) =>
{
    const me = saved_login();
    if (me)
    {
        conf.headers.Authorization = 'Bearer ' + me.token;
    }
    return conf;
});

client.interceptors.response.use((res) => res, (err) =>
{
    const on_auth = err.config && err.config.url.startsWith('/auth/');
    if (err.response && err.response.status === 401 && !on_auth)
    {
        keep_login(null);
        window.location.assign('/login');
    }
    return Promise.reject(err);
});

export function error_text(err)
{
    const data = err.response && err.response.data;
    if (data && data.message)
    {
        return data.message;
    }
    if (err.response)
    {
        return 'Something went wrong on our side. Try again in a moment.';
    }
    return 'Could not reach the server. Check your connection and try again.';
}

export const get = (url, params) => client.get(url, { params }).then((res) => res.data);
export const post = (url, body) => client.post(url, body).then((res) => res.data);
export const put = (url, body) => client.put(url, body).then((res) => res.data);
export const patch = (url, body) => client.patch(url, body).then((res) => res.data);
export const del = (url) => client.delete(url).then(() => null);
