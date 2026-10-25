import { useState } from 'react';
import { del, post } from '../../api.js';
import { useLoad } from '../../hooks.js';
import Badge from '../Badge.jsx';
import Confirm from '../Confirm.jsx';
import { Hold } from '../CompanyRow.jsx';
import { CompanyDialog, LengthDialog, LockDialog } from './CompanyDialogs.jsx';

const COLS = 'lg:grid-cols-[9.5rem_minmax(0,1.3fr)_minmax(0,1fr)_auto]';

function Row({ item, on_act })
{
    const custom = item.lock_days != null || item.cool_days != null;
    const locked = item.status !== 'Available';
    return (
        <li className={'grid items-center gap-x-4 gap-y-1 border-t border-line px-3 py-3 first:border-t-0 ' + COLS}>
            <div><Badge status={item.status} /></div>
            <div className="min-w-0">
                <p className="font-medium">{item.name}</p>
                <p className="text-[0.8125rem] text-mute">{[item.field, item.contact].filter(Boolean).join(' · ')}</p>
            </div>
            <div>
                <Hold item={item} />
                {custom && <p className="text-[0.8125rem] text-mute">Custom lengths</p>}
            </div>
            <div className="mt-1 flex flex-wrap gap-1 lg:mt-0 lg:justify-end">
                <button className="btn btn-quiet" onClick={() => on_act('edit', item)} aria-label={'Edit ' + item.name}>Edit</button>
                <button className="btn btn-quiet" onClick={() => on_act('lengths', item)} aria-label={'Lock lengths for ' + item.name}>Lengths</button>
                <button className="btn btn-quiet" onClick={() => on_act(locked ? 'unlock' : 'lock', item)} aria-label={(locked ? 'Unlock ' : 'Lock ') + item.name}>{locked ? 'Unlock' : 'Lock'}</button>
                <button className="btn btn-quiet btn-danger" onClick={() => on_act('delete', item)} aria-label={'Delete ' + item.name}>Delete</button>
            </div>
        </li>
    );
}

export default function Directory()
{
    const { data, error, loading, reload } = useLoad('/companies');
    const [act, set_act] = useState({ kind: '', item: null, open: false });
    const item = act.item;
    const on_act = (kind, target) => set_act({ kind, item: target, open: true });
    const close = () => set_act((old) => ({ ...old, open: false }));
    const is = (kind) => act.open && act.kind === kind;

    const unlock = async () =>
    {
        await post('/admin/companies/' + item.comp_id + '/unlock');
        await reload();
        return item.name + ' is available again.';
    };
    const remove = async () =>
    {
        await del('/companies/' + item.comp_id);
        await reload();
        return item.name + ' deleted.';
    };

    return (
        <div>
            <div className="mt-4 flex flex-wrap items-center justify-between gap-3">
                <p className="text-mute">{(data || []).length} companies in the directory.</p>
                <button className="btn btn-primary" onClick={() => on_act('edit', null)}>Add company</button>
            </div>
            {error && <p role="alert" className="mt-4 text-lock">{error}</p>}
            {loading && <p className="mt-6 text-mute">Loading…</p>}
            <ul className="mt-4 overflow-hidden rounded-lg border border-line bg-panel">
                {(data || []).map((row) => <Row key={row.comp_id} item={row} on_act={on_act} />)}
            </ul>
            <CompanyDialog open={is('edit')} item={item} on_close={close} on_done={reload} />
            <LockDialog open={is('lock')} item={item} on_close={close} on_done={reload} />
            <LengthDialog open={is('lengths')} item={item} on_close={close} on_done={reload} />
            <Confirm open={is('unlock')} title={item ? 'Unlock ' + item.name + '?' : ''} confirm="Unlock" run={unlock} on_close={close}>
                <p>It goes back on the board right away, whoever was holding it.</p>
            </Confirm>
            <Confirm open={is('delete')} title={item ? 'Delete ' + item.name + '?' : ''} confirm="Delete" danger run={remove} on_close={close}>
                <p>This cannot be undone. A company that already has sponsorship history cannot be deleted.</p>
            </Confirm>
        </div>
    );
}
