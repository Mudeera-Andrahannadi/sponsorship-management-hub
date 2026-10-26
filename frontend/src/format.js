export function days_text(count)
{
    return count === 1 ? '1 day left' : count + ' days left';
}

export function money(amount)
{
    return 'LKR ' + Number(amount).toLocaleString('en-LK', { minimumFractionDigits: 0, maximumFractionDigits: 2 });
}

export function day_text(text)
{
    if (!text)
    {
        return '';
    }
    const stamp = text.length === 10 ? text + 'T00:00:00' : text + 'Z';
    return new Date(stamp).toLocaleDateString('en-GB', { day: 'numeric', month: 'short', year: 'numeric' });
}

export function until_text(text)
{
    return new Date(text + 'Z').toLocaleString('en-GB', { day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit' });
}
