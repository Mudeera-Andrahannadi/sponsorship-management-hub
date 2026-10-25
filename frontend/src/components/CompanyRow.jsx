import Badge from './Badge.jsx';
import { days_text, until_text } from '../format.js';

export const COLS = 'md:grid-cols-[9.5rem_minmax(0,1.4fr)_minmax(0,1fr)_minmax(0,1fr)_7rem]';

export function holds(item)
{
    return item.mine && item.status === 'In Progress';
}

export function Hold({ item })
{
    if (item.status === 'Available')
    {
        return <p className="text-mute md:hidden">Free to claim</p>;
    }
    const own = holds(item);
    const who = item.status === 'Locked' ? 'Cooldown' : (own ? 'You' : item.held_by);
    const left = item.days_left == null ? '' : ' · ' + days_text(item.days_left);
    return (
        <p title={'Until ' + until_text(item.lock_until)}>
            <span className={own ? 'font-medium' : ''}>{who}</span>
            <span className="num text-mute">{left}</span>
        </p>
    );
}

function Action({ item, is_admin, on_pick })
{
    if (is_admin)
    {
        return <button className="btn" onClick={() => on_pick('detail', item)} aria-label={'Details for ' + item.name}>Details</button>;
    }
    if (holds(item))
    {
        return <button className="btn" onClick={() => on_pick('release', item)} aria-label={'Release ' + item.name}>Release</button>;
    }
    if (item.status === 'Available')
    {
        return <button className="btn btn-primary" onClick={() => on_pick('claim', item)} aria-label={'Claim ' + item.name}>Claim</button>;
    }
    return <span className="text-xs text-mute">Not available</span>;
}

export default function CompanyRow({ item, is_admin, fresh, on_pick })
{
    const meta = [item.field, item.contact].filter(Boolean).join(' · ');
    return (
        <li data-fresh={fresh} className={'row-line grid grid-cols-2 items-center gap-x-4 gap-y-1 border-t border-line px-3 py-3 first:border-t-0 ' + COLS}>
            <div className="col-start-1 row-start-1 md:col-auto md:row-auto">
                <Badge status={item.status} />
            </div>
            <div className="col-span-2 row-start-2 min-w-0 md:col-span-1 md:row-auto">
                <button className="text-left font-medium hover:underline" onClick={() => on_pick('detail', item)}>{item.name}</button>
                <p className="truncate text-[0.8125rem] text-mute md:hidden">{meta}</p>
                <p className="hidden truncate text-[0.8125rem] text-mute md:block">{item.field}</p>
            </div>
            <div className="hidden min-w-0 md:block">
                <p className="truncate">{item.contact}</p>
                <p className="truncate text-[0.8125rem] text-mute">{item.job}</p>
            </div>
            <div className="col-span-2 row-start-3 md:col-span-1 md:row-auto">
                <Hold item={item} />
            </div>
            <div className="col-start-2 row-start-1 justify-self-end md:col-auto md:row-auto">
                <Action item={item} is_admin={is_admin} on_pick={on_pick} />
            </div>
        </li>
    );
}
