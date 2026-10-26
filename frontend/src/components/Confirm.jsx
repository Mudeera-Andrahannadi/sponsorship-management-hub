import { useEffect } from 'react';
import { toast } from 'sonner';
import { useAction } from '../hooks.js';
import Modal from './Modal.jsx';

export default function Confirm({ open, title, confirm, danger, run, on_close, children })
{
    const { busy, problem, fire, clear } = useAction(async () =>
    {
        toast.success(await run());
        on_close();
    });

    useEffect(() => (open ? clear() : undefined), [open]);

    return (
        <Modal open={open} title={title} on_close={on_close}>
            <div className="space-y-4">
                <div>{children}</div>
                <p role="alert" className="min-h-5 text-sm text-lock">{problem}</p>
                <div className="flex justify-end gap-2">
                    <button type="button" className="btn" onClick={on_close} data-autofocus={danger ? '' : undefined}>Cancel</button>
                    <button type="button" className={'btn ' + (danger ? 'btn-danger' : 'btn-primary')} onClick={fire} disabled={busy} data-autofocus={danger ? undefined : ''}>
                        {busy ? 'One moment…' : confirm}
                    </button>
                </div>
            </div>
        </Modal>
    );
}
