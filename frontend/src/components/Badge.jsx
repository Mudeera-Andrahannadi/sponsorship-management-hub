import Icon from './Icon.jsx';

const LOOK =
{
    'Available': { icon: 'dot', tone: 'bg-free-bg text-free' },
    'In Progress': { icon: 'half', tone: 'bg-busy-bg text-busy' },
    'Locked': { icon: 'lock', tone: 'bg-lock-bg text-lock' }
};

export default function Badge({ status })
{
    const look = LOOK[status];
    return (
        <span className={'inline-flex items-center rounded-full py-0.5 pr-2.5 pl-2 text-xs font-medium transition-colors duration-200 ' + look.tone}>
            <span key={status} className="swap inline-flex items-center gap-1.5">
                <Icon name={look.icon} size={14} />
                {status}
            </span>
        </span>
    );
}
