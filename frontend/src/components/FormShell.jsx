import { useState } from 'react';
import { toast } from 'sonner';
import { error_text } from '../api.js';

export default function FormShell({ run, go, on_close, on_done, children })
{
    const [busy, set_busy] = useState(false);
    const [problem, set_problem] = useState('');

    async function submit(evt)
    {
        evt.preventDefault();
        set_busy(true);
        set_problem('');
        try
        {
            toast.success(await run());
            await on_done();
            on_close();
        }
        catch (err)
        {
            set_problem(error_text(err));
        }
        finally
        {
            set_busy(false);
        }
    }

    return (
        <form onSubmit={submit} className="space-y-4">
            {children}
            <p role="alert" className="min-h-5 text-sm text-lock">{problem}</p>
            <div className="flex justify-end gap-2">
                <button type="button" className="btn" onClick={on_close}>Cancel</button>
                <button className="btn btn-primary" disabled={busy}>{busy ? 'One moment…' : go}</button>
            </div>
        </form>
    );
}
