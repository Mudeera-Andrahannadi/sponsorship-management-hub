import { useLoad } from '../../hooks.js';
import { money, until_text } from '../../format.js';
import Badge from '../Badge.jsx';

function Ranked({ title, rows, empty })
{
    return (
        <section aria-label={title} className="mt-8">
            <h2 className="mb-1.5 font-semibold">{title}</h2>
            {rows.length === 0 && <p className="text-mute">{empty}</p>}
            {rows.length > 0 && (
                <ol className="overflow-hidden rounded-lg border border-line bg-panel">
                    {rows.map((row) => (
                        <li key={row.name} className="flex items-center justify-between gap-4 border-t border-line px-3 py-2.5 first:border-t-0">
                            <span className="min-w-0 truncate">{row.name}</span>
                            <span className="num font-medium">{money(row.sum)}</span>
                        </li>
                    ))}
                </ol>
            )}
        </section>
    );
}

function Ending({ rows })
{
    return (
        <section aria-label="Ending soon" className="mt-8">
            <h2 className="mb-1.5 font-semibold">Ending in the next 48 hours</h2>
            {rows.length === 0 && <p className="text-mute">No hold or cooldown ends in the next two days.</p>}
            {rows.length > 0 && (
                <ul className="overflow-hidden rounded-lg border border-line bg-panel">
                    {rows.map((row) => (
                        <li key={row.comp_id} className="flex flex-wrap items-center justify-between gap-x-4 gap-y-1 border-t border-line px-3 py-2.5 first:border-t-0">
                            <span className="flex items-center gap-3"><Badge status={row.status} /><span className="font-medium">{row.name}</span></span>
                            <span className="text-[0.8125rem] text-mute">{row.held_by ? row.held_by + ' · ' : ''}{until_text(row.lock_until)}</span>
                        </li>
                    ))}
                </ul>
            )}
        </section>
    );
}

export default function Overview()
{
    const { data, error, loading } = useLoad('/admin/analytics');

    if (loading)
    {
        return <p className="mt-6 text-mute">Loading…</p>;
    }
    if (error || !data)
    {
        return <p role="alert" className="mt-6 text-lock">{error}</p>;
    }
    return (
        <div>
            <p className="mt-4">{money(data.raised)} approved so far.</p>
            <ul className="mt-3 flex flex-wrap gap-x-6 gap-y-2">
                {Object.entries(data.lights).map(([label, count]) => (
                    <li key={label} className="flex items-center gap-2">
                        <Badge status={label} />
                        <span className="num font-medium">{count}</span>
                    </li>
                ))}
            </ul>
            <Ending rows={data.ending} />
            <Ranked title="Approved money by event" rows={data.by_event} empty="Nothing has been approved yet." />
            <Ranked title="Top sponsors" rows={data.top_sponsors} empty="No sponsor has paid yet." />
        </div>
    );
}
