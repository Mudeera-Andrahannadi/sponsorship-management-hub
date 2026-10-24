import { useState } from 'react';
import { post } from '../api.js';
import { useAuth } from '../auth.jsx';
import { useFresh, useLoad, useRefresh } from '../hooks.js';
import Toolbar from '../components/Toolbar.jsx';
import Confirm from '../components/Confirm.jsx';
import CompanyRow, { COLS } from '../components/CompanyRow.jsx';
import CompanyDetail from '../components/CompanyDetail.jsx';

const GROUPS = [
    { key: 'Available', title: 'Available now' },
    { key: 'In Progress', title: 'In progress' },
    { key: 'Locked', title: 'Locked' }
];

const FILTERS = [
    ['all', 'All'],
    ['Available', 'Available'],
    ['In Progress', 'In progress'],
    ['Locked', 'Locked']
];

function tally(list)
{
    const counts = { all: list.length, Available: 0, 'In Progress': 0, Locked: 0 };
    list.forEach((item) => counts[item.status]++);
    return counts;
}

function match(term, filter)
{
    const needle = term.trim().toLowerCase();
    return (item) =>
    {
        if (filter !== 'all' && item.status !== filter)
        {
            return false;
        }
        return !needle || [item.name, item.field, item.contact].some((text) => (text || '').toLowerCase().includes(needle));
    };
}

function Group({ group, items, is_admin, fresh, on_pick })
{
    if (items.length === 0)
    {
        return null;
    }
    return (
        <section aria-label={group.title} className="mt-6">
            <h2 className="mb-1.5 flex items-baseline gap-2 font-semibold">
                {group.title}
                <span className="num font-normal text-mute">{items.length}</span>
            </h2>
            <ul className="overflow-hidden rounded-lg border border-line bg-panel">
                {items.map((item) => (
                    <CompanyRow key={item.comp_id} item={item} is_admin={is_admin} fresh={fresh.has(item.comp_id)} on_pick={on_pick} />
                ))}
            </ul>
        </section>
    );
}

function Skeleton()
{
    return (
        <div className="mt-6 overflow-hidden rounded-lg border border-line bg-panel" aria-busy="true" aria-label="Loading companies">
            {[0, 1, 2, 3, 4].map((key) => (
                <div key={key} className="flex items-center gap-4 border-t border-line px-3 py-4 first:border-t-0">
                    <div className="h-5 w-24 animate-pulse rounded-full bg-sunk motion-reduce:animate-none" />
                    <div className="h-4 w-48 animate-pulse rounded bg-sunk motion-reduce:animate-none" />
                </div>
            ))}
        </div>
    );
}

function Nothing({ term, on_clear, total })
{
    if (total === 0)
    {
        return <p className="mt-8 text-mute">The directory is empty. An admin can add companies from the Admin tab.</p>;
    }
    return (
        <div className="mt-8">
            <p>No company matches {term ? '“' + term + '”' : 'that filter'}.</p>
            <button className="btn mt-3" onClick={on_clear}>Clear search and filter</button>
        </div>
    );
}

async function claim(item, reload)
{
    const view = await post('/companies/' + item.comp_id + '/lock');
    await reload();
    return view.name + ' is yours for ' + view.days_left + ' days.';
}

async function release(item, reload)
{
    await post('/companies/' + item.comp_id + '/release');
    await reload();
    return item.name + ' is back on the board.';
}

export default function Companies()
{
    const { is_admin } = useAuth();
    const { data, error, loading, reload } = useLoad('/companies');
    const [term, set_term] = useState('');
    const [filter, set_filter] = useState('all');
    const [pick, set_pick] = useState({ kind: '', item: null, open: false });
    const fresh = useFresh(data);
    useRefresh(reload);

    const list = data || [];
    const counts = tally(list);
    const shown = list.filter(match(term, filter));
    const mine = list.filter((item) => item.mine && item.status === 'In Progress').length;
    const item = pick.item;
    const on_pick = (kind, target) => set_pick({ kind, item: target, open: true });
    const close = () => set_pick((old) => ({ ...old, open: false }));
    const clear = () =>
    {
        set_term('');
        set_filter('all');
    };

    return (
        <section>
            <h1 className="text-xl font-semibold">Companies</h1>
            <p className="mt-1 text-mute">
                {counts.Available} of {counts.all} companies are available right now.{mine > 0 ? ' You are holding ' + mine + '.' : ''}
            </p>
            <Toolbar term={term} on_term={set_term} hint="Search name, field or contact" filters={FILTERS} filter={filter} on_filter={set_filter} counts={counts} />
            {error && (
                <p role="alert" className="mt-4 flex flex-wrap items-center gap-3 text-lock">
                    {error}
                    <button className="btn" onClick={reload}>Try again</button>
                </p>
            )}
            {loading && <Skeleton />}
            {!loading && shown.length === 0 && !error && <Nothing term={term} on_clear={clear} total={list.length} />}
            {shown.length > 0 && (
                <div aria-hidden="true" className={'mt-4 -mb-4 hidden gap-x-4 px-3 pb-1 text-xs text-mute md:grid ' + COLS}>
                    <span>Status</span><span>Company</span><span>Contact</span><span>Hold</span><span />
                </div>
            )}
            {GROUPS.map((group) => (
                <Group key={group.key} group={group} items={shown.filter((row) => row.status === group.key)} is_admin={is_admin} fresh={fresh} on_pick={on_pick} />
            ))}
            <Confirm
                open={pick.open && pick.kind === 'claim'}
                title={item ? 'Claim ' + item.name + '?' : ''}
                confirm="Claim it"
                run={() => claim(item, reload)}
                on_close={close}
            >
                <p>You will hold it for {item ? item.hold_days : ''} days. Log a pitch to restart the clock. If nothing happens, it goes back on the board.</p>
            </Confirm>
            <Confirm
                open={pick.open && pick.kind === 'release'}
                title={item ? 'Release ' + item.name + '?' : ''}
                confirm="Release"
                run={() => release(item, reload)}
                on_close={close}
            >
                <p>Other societies can claim it straight away.</p>
            </Confirm>
            <CompanyDetail open={pick.open && pick.kind === 'detail'} item={item} is_admin={is_admin} on_close={close} on_pick={on_pick} />
        </section>
    );
}
