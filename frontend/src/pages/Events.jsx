import { useState } from 'react';
import { useAuth } from '../auth.jsx';
import { useLoad } from '../hooks.js';
import { day_text, money } from '../format.js';
import EventDialog from '../components/EventDialog.jsx';
import PitchDialog from '../components/PitchDialog.jsx';

function summary(rows)
{
    if (rows.length === 0)
    {
        return 'No pitches yet';
    }
    const total = rows.filter((row) => row.status === 'Approved').reduce((sum, row) => sum + Number(row.amount), 0);
    return rows.length + (rows.length === 1 ? ' pitch' : ' pitches') + ' · ' + money(total) + ' approved';
}

function EventRow({ event, mine, rows, on_log })
{
    return (
        <li className="grid gap-x-4 gap-y-1 border-t border-line px-3 py-3 first:border-t-0 md:grid-cols-[minmax(0,1.5fr)_8rem_minmax(0,1.3fr)_8rem] md:items-center">
            <div className="min-w-0">
                <p className="font-medium">{event.name}</p>
                <p className="text-[0.8125rem] text-mute">{event.society}</p>
            </div>
            <p className="num text-mute md:text-ink">{day_text(event.held_on)}</p>
            <p className="text-[0.8125rem] text-mute">{mine ? 'On your committee · ' + summary(rows) : ''}</p>
            <div className="mt-1 md:mt-0 md:justify-self-end">
                {mine && <button className="btn btn-primary" onClick={() => on_log(event)}>Log a pitch</button>}
            </div>
        </li>
    );
}

function Section({ title, events, mine_ids, history, on_log })
{
    if (events.length === 0)
    {
        return null;
    }
    return (
        <section aria-label={title} className="mt-6">
            <h2 className="mb-1.5 flex items-baseline gap-2 font-semibold">
                {title}
                <span className="num font-normal text-mute">{events.length}</span>
            </h2>
            <ul className="overflow-hidden rounded-lg border border-line bg-panel">
                {events.map((event) => (
                    <EventRow
                        key={event.event_id}
                        event={event}
                        mine={mine_ids.has(event.event_id)}
                        rows={history.filter((row) => row.event_id === event.event_id)}
                        on_log={on_log}
                    />
                ))}
            </ul>
        </section>
    );
}

function Lead({ is_admin, count })
{
    if (is_admin)
    {
        return <p className="mt-1 text-mute">Every event the societies are raising money for.</p>;
    }
    return <p className="mt-1 text-mute">You are on {count} {count === 1 ? 'committee.' : 'committees.'}</p>;
}

function useEvents(is_admin)
{
    const events = useLoad('/events');
    const mine = useLoad('/committees/mine');
    const history = useLoad('/history');
    const companies = useLoad(is_admin ? null : '/companies');
    const held = (companies.data || []).filter((item) => item.mine && item.status === 'In Progress');
    const refresh = () => Promise.all([history.reload(), companies.reload()]);
    return { events, mine, held, refresh, history: history.data || [], mine_ids: new Set((mine.data || []).map((event) => event.event_id)) };
}

export default function Events()
{
    const { is_admin } = useAuth();
    const { events, mine, held, refresh, history, mine_ids } = useEvents(is_admin);
    const [dialog, set_dialog] = useState({ kind: '', event: null, open: false });
    const list = events.data || [];
    const today = new Date().toISOString().slice(0, 10);
    const shared = { mine_ids, history, on_log: (event) => set_dialog({ kind: 'pitch', event, open: true }) };
    const close = () => set_dialog((old) => ({ ...old, open: false }));

    return (
        <section>
            <div className="flex flex-wrap items-start justify-between gap-3">
                <div>
                    <h1 className="text-xl font-semibold">Events</h1>
                    <Lead is_admin={is_admin} count={mine_ids.size} />
                </div>
                {is_admin && <button className="btn btn-primary" onClick={() => set_dialog({ kind: 'event', event: null, open: true })}>New event</button>}
            </div>
            {events.error && <p role="alert" className="mt-4 text-lock">{events.error}</p>}
            {!events.loading && list.length === 0 && <p className="mt-8 text-mute">No events yet.</p>}
            {!is_admin && !mine.loading && mine_ids.size === 0 && (
                <p className="mt-4 text-mute">Ask the faculty admin to add you to an event committee before you start claiming companies.</p>
            )}
            <Section title="Upcoming" events={list.filter((event) => event.held_on >= today)} {...shared} />
            <Section title="Past" events={list.filter((event) => event.held_on < today).reverse()} {...shared} />
            <PitchDialog open={dialog.open && dialog.kind === 'pitch'} event={dialog.event} held={held} on_close={close} on_done={refresh} />
            <EventDialog open={dialog.open && dialog.kind === 'event'} on_close={close} on_done={events.reload} />
        </section>
    );
}
