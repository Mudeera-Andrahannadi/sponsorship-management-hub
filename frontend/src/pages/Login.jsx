import { useState } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth.jsx';
import { error_text } from '../api.js';
import Field from '../components/Field.jsx';

const BLANK = { name: '', reg_no: '', email: '', password: '' };

const COPY =
{
    in: { title: 'Sign in', lead: 'Check which companies are free before you contact them.', go: 'Sign in' },
    up: { title: 'Create your account', lead: 'For students on a society committee. The faculty office adds admins.', go: 'Create account' }
};

function Switch({ mode, on_pick })
{
    return (
        <div role="tablist" aria-label="Sign in or create an account" className="mb-6 flex gap-5 border-b border-line">
            {[['in', 'Sign in'], ['up', 'Create account']].map(([key, label]) => (
                <button
                    key={key}
                    role="tab"
                    type="button"
                    aria-selected={mode === key}
                    onClick={() => on_pick(key)}
                    className={'-mb-px border-b-2 pb-2 text-sm transition-colors ' + (mode === key ? 'border-accent font-medium' : 'border-transparent text-mute')}
                >
                    {label}
                </button>
            ))}
        </div>
    );
}

export default function Login()
{
    const { me, sign_in, sign_up } = useAuth();
    const [mode, set_mode] = useState('in');
    const [form, set_form] = useState(BLANK);
    const [busy, set_busy] = useState(false);
    const [problem, set_problem] = useState('');
    const nav = useNavigate();

    if (me)
    {
        return <Navigate to="/" replace />;
    }

    const bind = (key) => ({ value: form[key], onChange: (evt) => set_form({ ...form, [key]: evt.target.value }) });

    async function submit(evt)
    {
        evt.preventDefault();
        set_busy(true);
        set_problem('');
        try
        {
            await (mode === 'in' ? sign_in({ email: form.email, password: form.password }) : sign_up(form));
            nav('/', { replace: true });
        }
        catch (err)
        {
            set_problem(error_text(err));
        }
        finally
        {
            set_busy(false);
        }
    }

    return (
        <div className="min-h-dvh">
            <header className="border-b border-line bg-panel">
                <div className="mx-auto max-w-6xl px-4 py-3 font-semibold sm:px-6">Sponsorship Hub</div>
            </header>
            <main className="mx-auto mt-10 max-w-sm px-4 sm:mt-16">
                <Switch mode={mode} on_pick={set_mode} />
                <h1 className="text-xl font-semibold">{COPY[mode].title}</h1>
                <p className="mt-1 mb-6 text-mute">{COPY[mode].lead}</p>
                <form onSubmit={submit} className="space-y-4">
                    {mode === 'up' && <Field label="Full name" autoComplete="name" required {...bind('name')} />}
                    {mode === 'up' && <Field label="Registration number" placeholder="EE/22/014" required {...bind('reg_no')} />}
                    <Field label="Email" type="email" autoComplete="email" required {...bind('email')} />
                    <Field
                        label="Password"
                        type="password"
                        autoComplete={mode === 'in' ? 'current-password' : 'new-password'}
                        minLength={mode === 'up' ? 8 : undefined}
                        hint={mode === 'up' ? 'At least 8 characters.' : undefined}
                        required
                        {...bind('password')}
                    />
                    <p role="alert" className="min-h-5 text-sm text-lock">{problem}</p>
                    <button className="btn btn-primary w-full" disabled={busy}>{busy ? 'One moment…' : COPY[mode].go}</button>
                </form>
            </main>
        </div>
    );
}
