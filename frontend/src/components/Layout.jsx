import { NavLink, Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../auth.jsx';

const TABS = [
    { to: '/', label: 'Companies', end: true },
    { to: '/events', label: 'Events' },
    { to: '/history', label: 'History' }
];

function Tab({ to, label, end })
{
    const pick = ({ isActive }) => 'relative whitespace-nowrap px-3 py-2.5 text-sm transition-colors '
        + (isActive ? 'font-medium text-ink after:absolute after:inset-x-3 after:bottom-0 after:h-0.5 after:rounded-full after:bg-accent' : 'text-mute hover:text-ink');
    return <NavLink to={to} end={end} className={pick}>{label}</NavLink>;
}

export default function Layout()
{
    const { me, is_admin, leave } = useAuth();

    if (!me)
    {
        return <Navigate to="/login" replace />;
    }

    const tabs = is_admin ? [...TABS, { to: '/admin', label: 'Admin' }] : TABS;

    return (
        <div className="min-h-dvh">
            <a href="#main" className="sr-only rounded bg-panel px-3 py-2 focus:not-sr-only focus:absolute focus:left-3 focus:top-3">Skip to content</a>
            <header className="border-b border-line bg-panel">
                <div className="mx-auto flex max-w-6xl items-center justify-between gap-4 px-4 pt-3 sm:px-6">
                    <span className="font-semibold">Sponsorship Hub</span>
                    <div className="flex items-center gap-2">
                        <span className="hidden text-mute sm:inline">{me.name}{is_admin ? ' · Admin' : ''}</span>
                        <button className="btn btn-quiet" onClick={leave}>Sign out</button>
                    </div>
                </div>
                <nav aria-label="Main" className="mx-auto flex max-w-6xl gap-1 overflow-x-auto px-2 sm:px-4">
                    {tabs.map((tab) => <Tab key={tab.to} {...tab} />)}
                </nav>
            </header>
            <main id="main" className="mx-auto max-w-6xl px-4 py-6 sm:px-6">
                <Outlet />
            </main>
        </div>
    );
}
