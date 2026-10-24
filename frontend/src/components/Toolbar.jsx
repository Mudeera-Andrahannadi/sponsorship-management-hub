import Icon from './Icon.jsx';

export default function Toolbar({ term, on_term, hint, filters, filter, on_filter, counts })
{
    return (
        <div className="mt-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            <div className="relative sm:w-80">
                <Icon name="search" className="pointer-events-none absolute top-1/2 left-2.5 -translate-y-1/2 text-mute" />
                <input
                    type="search"
                    className="field pl-8"
                    aria-label={hint}
                    placeholder={hint}
                    value={term}
                    onChange={(evt) => on_term(evt.target.value)}
                />
            </div>
            <div role="group" aria-label="Filter by status" className="flex flex-wrap gap-1">
                {filters.map(([key, label]) => (
                    <button key={key} aria-pressed={filter === key} onClick={() => on_filter(key)} className={'btn ' + (filter === key ? 'btn-on' : 'btn-quiet')}>
                        {label} <span className="num text-mute">{counts[key]}</span>
                    </button>
                ))}
            </div>
        </div>
    );
}
