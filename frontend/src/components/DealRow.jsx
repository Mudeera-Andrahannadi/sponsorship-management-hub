import { day_text, money } from '../format.js';
import OutcomeTag from './OutcomeTag.jsx';

export const DEAL_COLS = 'lg:grid-cols-[minmax(0,1.5fr)_minmax(0,1.1fr)_7.5rem_7rem_13rem]';

function Actions({ row, me, is_admin, on_act })
{
    const pending = row.status === 'Pending';
    const can_edit = is_admin || (pending && row.handler_id === me.user_id);
    return (
        <div className="mt-1 flex flex-wrap gap-2 lg:mt-0 lg:justify-end">
            {is_admin && pending && <button className="btn btn-primary" onClick={() => on_act('approve', row)} aria-label={'Approve ' + row.company}>Approve</button>}
            {is_admin && pending && <button className="btn" onClick={() => on_act('reject', row)} aria-label={'Reject ' + row.company}>Reject</button>}
            {can_edit && <button className="btn btn-quiet" onClick={() => on_act('edit', row)} aria-label={'Edit pitch to ' + row.company}>Edit</button>}
        </div>
    );
}

export default function DealRow({ row, me, is_admin, on_act })
{
    return (
        <li className={'grid gap-x-4 gap-y-1 border-t border-line px-3 py-3 first:border-t-0 lg:items-start ' + DEAL_COLS}>
            <div className="min-w-0">
                <p className="font-medium">{row.company}</p>
                {row.notes && <p className="line-clamp-2 text-[0.8125rem] text-mute" title={row.notes}>{row.notes}</p>}
            </div>
            <div className="min-w-0">
                <p>{row.event}</p>
                <p className="text-[0.8125rem] text-mute">{row.handler}</p>
            </div>
            <p className="num font-medium lg:text-right">{money(row.amount)}</p>
            <div>
                <OutcomeTag status={row.status} />
                <p className="num text-[0.8125rem] text-mute">{day_text(row.asked_on)}</p>
            </div>
            <Actions row={row} me={me} is_admin={is_admin} on_act={on_act} />
        </li>
    );
}
