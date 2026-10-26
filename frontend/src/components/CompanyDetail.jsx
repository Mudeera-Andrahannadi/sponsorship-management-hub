import { useEffect } from 'react';
import { useLoad } from '../hooks.js';
import { day_text, money } from '../format.js';
import Badge from './Badge.jsx';
import { holds } from './CompanyRow.jsx';
import Modal from './Modal.jsx';
import OutcomeTag from './OutcomeTag.jsx';

function Line({ label, children })
{
    if (!children)
    {
        return null;
    }
    return (
        <>
            <dt className="text-mute">{label}</dt>
            <dd className="min-w-0 break-words">{children}</dd>
        </>
    );
}

function Past({ rows, loading })
{
    if (loading)
    {
        return <p className="text-mute">Loading…</p>;
    }
    if (!rows || rows.length === 0)
    {
        return <p className="text-mute">Nobody has logged a pitch to this company yet.</p>;
    }
    return (
        <ul className="divide-y divide-line rounded-md border border-line">
            {rows.map((row) => (
                <li key={row.deal_id} className="flex flex-wrap items-center justify-between gap-x-4 gap-y-1 px-3 py-2">
                    <span className="min-w-0">{row.event}<span className="text-mute"> · {day_text(row.asked_on)}</span></span>
                    <span className="num flex items-center gap-3">{money(row.amount)}<OutcomeTag status={row.status} /></span>
                </li>
            ))}
        </ul>
    );
}

function Facts({ item })
{
    return (
        <dl className="grid grid-cols-[6rem_1fr] gap-x-3 gap-y-2">
            <Line label="Status"><Badge status={item.status} /></Line>
            <Line label="Contact">{[item.contact, item.job].filter(Boolean).join(', ')}</Line>
            <Line label="Email">{item.email && <a className="text-accent underline" href={'mailto:' + item.email}>{item.email}</a>}</Line>
            <Line label="Phone">{item.phone && <a className="text-accent underline" href={'tel:' + item.phone.replace(/\s/g, '')}>{item.phone}</a>}</Line>
            <Line label="Industry">{item.field}</Line>
        </dl>
    );
}

export default function CompanyDetail({ open, item, is_admin, on_close, on_pick })
{
    const { data, loading, reload } = useLoad(item ? '/history/company/' + item.comp_id : null);

    useEffect(() =>
    {
        if (open)
        {
            reload();
        }
    }, [open, reload]);

    if (!item)
    {
        return <Modal open={false} title="" on_close={on_close} />;
    }

    const can_claim = !is_admin && item.status === 'Available';
    return (
        <Modal open={open} title={item.name} on_close={on_close}>
            <Facts item={item} />
            <h3 className="mt-5 mb-2 font-semibold">Past sponsorships</h3>
            <Past rows={data} loading={loading} />
            <div className="mt-5 flex justify-end gap-2">
                {holds(item) && <button className="btn" onClick={() => on_pick('release', item)}>Release</button>}
                {can_claim && <button className="btn btn-primary" onClick={() => on_pick('claim', item)}>Claim</button>}
                <button className="btn btn-quiet" onClick={on_close}>Close</button>
            </div>
        </Modal>
    );
}
