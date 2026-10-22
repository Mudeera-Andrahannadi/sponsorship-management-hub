export default function Field({ label, hint, ...props })
{
    return (
        <label className="block">
            <span className="mb-1 block text-[0.8125rem] font-medium">{label}</span>
            <input className="field" {...props} />
            {hint && <span className="mt-1 block text-xs text-mute">{hint}</span>}
        </label>
    );
}
