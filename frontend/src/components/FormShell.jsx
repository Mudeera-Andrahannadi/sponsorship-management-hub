import { toast } from 'sonner';
import { useAction } from '../hooks.js';

export default function FormShell({ run, go, on_close, on_done, children })
{
    const { busy, problem, fire } = useAction(async () =>
    {
        toast.success(await run());
        await on_done();
        on_close();
    });

    return (
        <form onSubmit={fire} className="space-y-4">
            {children}
            <p role="alert" className="min-h-5 text-sm text-lock">{problem}</p>
            <div className="flex justify-end gap-2">
                <button type="button" className="btn" onClick={on_close}>Cancel</button>
                <button className="btn btn-primary" disabled={busy}>{busy ? 'One moment…' : go}</button>
            </div>
        </form>
    );
}
