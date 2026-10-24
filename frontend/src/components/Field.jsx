export default function Field({ label, hint, as: Tag = 'input', ...props })
{
    return (
        <label className="block">
            <span className="mb-1 block text-[0.8125rem] font-medium">{label}</span>
            <Tag className="field" {...props} />
            {hint && <span className="mt-1 block text-xs text-mute">{hint}</span>}
        </label>
    );
}
