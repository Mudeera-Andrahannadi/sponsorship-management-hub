import { useState } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../auth.jsx';
import Directory from '../components/admin/Directory.jsx';
import Overview from '../components/admin/Overview.jsx';
import Teams from '../components/admin/Teams.jsx';
import Users from '../components/admin/Users.jsx';

const PARTS = [
    ['overview', 'Overview', Overview],
    ['directory', 'Directory', Directory],
    ['committees', 'Committees', Teams],
    ['users', 'Users', Users]
];

export default function Admin()
{
    const { is_admin } = useAuth();
    const [part, set_part] = useState('overview');

    if (!is_admin)
    {
        return <Navigate to="/" replace />;
    }

    const Body = PARTS.find(([key]) => key === part)[2];

    return (
        <section>
            <h1 className="text-xl font-semibold">Admin</h1>
            <div role="group" aria-label="Admin sections" className="mt-3 flex flex-wrap gap-x-5 border-b border-line">
                {PARTS.map(([key, label]) => (
                    <button
                        key={key}
                        aria-pressed={part === key}
                        onClick={() => set_part(key)}
                        className={'-mb-px whitespace-nowrap border-b-2 pb-2 text-sm transition-colors ' + (part === key ? 'border-accent font-medium' : 'border-transparent text-mute')}
                    >
                        {label}
                    </button>
                ))}
            </div>
            <Body />
        </section>
    );
}
