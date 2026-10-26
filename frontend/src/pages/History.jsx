import { useState } from 'react';
import { patch } from '../api.js';
import { useAuth } from '../auth.jsx';
import { useLoad } from '../hooks.js';
import { match, tally } from '../filters.js';
import { money } from '../format.js';
import Confirm from '../components/Confirm.jsx';
import DealRow, { DEAL_COLS } from '../components/DealRow.jsx';
import EditDialog from '../components/EditDialog.jsx';
import Toolbar from '../components/Toolbar.jsx';

const FILTERS = [
    ['all', 'All'],
    ['Pending', 'Pending'],
    ['Approved', 'Approved'],
    ['Rejected', 'Rejected']
];

const KEYS = FILTERS.slice(1).map(([key]) => key);
const FIELDS = ['company', 'event', 'handler', 'notes'];

async function decide(row, status, reload)
{
    await patch('/sponsorships/' + row.deal_id + '/status', { status });
    await reload();
    return status === 'Approved' ? row.company + ' is locked for its cooldown.' : 'Rejected the pitch to ' + row.company + '.';
}

function Ledger({ rows, me, is_admin, on_act })
{
    return (
        <div className="mt-6">
            <div aria-hidden="true" className={'hidden gap-x-4 px-3 pb-1 text-xs text-mute lg:grid ' + DEAL_COLS}>
                <span>Company</span><span>Event</span><span className="text-right">Amount</span><span>Status</span><span />
            </div>
            <ul className="overflow-hidden rounded-lg border border-line bg-panel">
                {rows.map((item) => <DealRow key={item.deal_id} row={item} me={me} is_admin={is_admin} on_act={on_act} />)}
            </ul>
        </div>
    );
}

function Nothing({ total, on_clear })
{
    if (total === 0)
    {
        return <p className="mt-8 text-mute">No pitch has been logged yet. They show up here as soon as a society logs one.</p>;
    }
    return (
        <div className="mt-8">
            <p>No pitch matches that search.</p>
            <button className="btn mt-3" onClick={on_clear}>Clear search and filter</button>
        </div>
    );
}

function Dialogs({ act, close, reload })
{
    const row = act.row;
    const is = (kind) => act.open && act.kind === kind;
    return (
        <>
            <Confirm open={is('approve')} title={row ? 'Approve the pitch to ' + row.company + '?' : ''} confirm="Approve" run={() => decide(row, 'Approved', reload)} on_close={close}>
                <p>{row ? money(row.amount) + ' for ' + row.event + '. ' : ''}The company goes into its cooldown and the pitch is no longer editable by the society.</p>
            </Confirm>
            <Confirm open={is('reject')} title={row ? 'Reject the pitch to ' + row.company + '?' : ''} confirm="Reject" danger run={() => decide(row, 'Rejected', reload)} on_close={close}>
                <p>{row ? row.handler + ' will see it as rejected. ' : ''}If they still hold the company, it goes back on the board.</p>
            </Confirm>
            <EditDialog open={is('edit')} row={row} on_close={close} on_done={reload} />
        </>
    );
}

export default function History()
{
    const { me, is_admin } = useAuth();
    const { data, error, loading, reload } = useLoad('/history');
    const [term, set_term] = useState('');
    const [filter, set_filter] = useState('all');
    const [act, set_act] = useState({ kind: '', row: null, open: false });

    const all = data || [];
    const rows = all.filter(match(term, filter, FIELDS));
    const raised = all.filter((row) => row.status === 'Approved').reduce((sum, row) => sum + Number(row.amount), 0);
    const clear = () =>
    {
        set_term('');
        set_filter('all');
    };

    return (
        <section>
            <h1 className="text-xl font-semibold">History</h1>
            <p className="mt-1 text-mute">
                {all.length} {all.length === 1 ? 'pitch' : 'pitches'}, {money(raised)} approved. Look here before you call a company.
            </p>
            <Toolbar term={term} on_term={set_term} hint="Search company, event or person" filters={FILTERS} filter={filter} on_filter={set_filter} counts={tally(all, KEYS)} />
            {error && <p role="alert" className="mt-4 text-lock">{error}</p>}
            {loading && <p className="mt-8 text-mute">Loading…</p>}
            {!loading && rows.length === 0 && !error && <Nothing total={all.length} on_clear={clear} />}
            {rows.length > 0 && <Ledger rows={rows} me={me} is_admin={is_admin} on_act={(kind, row) => set_act({ kind, row, open: true })} />}
            <Dialogs act={act} close={() => set_act((old) => ({ ...old, open: false }))} reload={reload} />
        </section>
    );
}
