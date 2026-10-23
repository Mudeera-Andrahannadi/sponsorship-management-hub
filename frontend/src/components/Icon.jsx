const SHAPES =
{
    dot: <circle cx="8" cy="8" r="5" fill="currentColor" stroke="none" />,
    half: (
        <>
            <circle cx="8" cy="8" r="5" />
            <path d="M8 3a5 5 0 0 1 0 10z" fill="currentColor" stroke="none" />
        </>
    ),
    lock: (
        <>
            <rect x="3.5" y="7" width="9" height="6.5" rx="1.5" />
            <path d="M5.5 7V5.5a2.5 2.5 0 0 1 5 0V7" />
        </>
    ),
    search: (
        <>
            <circle cx="7" cy="7" r="4.25" />
            <path d="M10.2 10.2 13 13" />
        </>
    ),
    clock: (
        <>
            <circle cx="8" cy="8" r="5.5" />
            <path d="M8 5v3.2l2 1.3" />
        </>
    ),
    x: <path d="M4 4l8 8M12 4l-8 8" />,
    check: <path d="M3.5 8.5l3 3 6-7" />,
    plus: <path d="M8 3.5v9M3.5 8h9" />
};

export default function Icon({ name, size = 16, className })
{
    return (
        <svg
            width={size}
            height={size}
            viewBox="0 0 16 16"
            fill="none"
            stroke="currentColor"
            strokeWidth="1.5"
            strokeLinecap="round"
            strokeLinejoin="round"
            aria-hidden="true"
            className={className}
        >
            {SHAPES[name]}
        </svg>
    );
}
