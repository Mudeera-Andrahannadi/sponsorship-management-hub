import Icon from './Icon.jsx';

const LOOK =
{
    Approved: { icon: 'check', tone: 'text-free' },
    Rejected: { icon: 'x', tone: 'text-lock' },
    Pending: { icon: 'clock', tone: 'text-busy' }
};

export default function OutcomeTag({ status })
{
    const look = LOOK[status];
    return (
        <span className={'inline-flex items-center gap-1 text-[0.8125rem] font-medium ' + look.tone}>
            <Icon name={look.icon} size={14} />
            {status}
        </span>
    );
}
