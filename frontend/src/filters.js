export function tally(list, keys)
{
    const counts = { all: list.length };
    for (const key of keys)
    {
        counts[key] = 0;
    }
    for (const item of list)
    {
        if (item.status in counts)
        {
            counts[item.status]++;
        }
    }
    return counts;
}

export function match(term, filter, fields)
{
    const needle = term.trim().toLowerCase();
    return (item) =>
    {
        if (filter !== 'all' && item.status !== filter)
        {
            return false;
        }
        return !needle || fields.some((key) => (item[key] || '').toLowerCase().includes(needle));
    };
}
