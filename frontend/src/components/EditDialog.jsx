import { useState } from 'react';
import { toast } from 'sonner';
import { error_text, patch } from '../api.js';
import Field from './Field.jsx';
import Modal from './Modal.jsx';

function EditForm({ row, on_close, on_done })
{
    const [amount, set_amount] = useState(String(row.amount));
    const [notes, set_notes] = useState(row.notes || '');
    const [busy, set_busy] = useState(false);
    const [problem, set_problem] = useState('');

    async function submit(evt)
    {
        evt.preventDefault();
        set_busy(true);
        set_problem('');
        try
        {
            await patch('/sponsorships/' + row.deal_id + '/status', { amount: Number(amount), notes });
            toast.success('Saved.');
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
            <Field label="Amount (LKR)" type="number" min="1" step="any" inputMode="decimal" value={amount} onChange={(evt) => set_amount(evt.target.value)} required data-autofocus="" />
            <Field as="textarea" rows={3} label="Notes" value={notes} onChange={(evt) => set_notes(evt.target.value)} />
            <p role="alert" className="min-h-5 text-sm text-lock">{problem}</p>
            <div className="flex justify-end gap-2">
                <button type="button" className="btn" onClick={on_close}>Cancel</button>
                <button className="btn btn-primary" disabled={busy}>{busy ? 'One moment…' : 'Save'}</button>
            </div>
        </form>
    );
}

export default function EditDialog({ open, row, on_close, on_done })
{
    return (
        <Modal open={open} title={row ? 'Edit pitch to ' + row.company : ''} on_close={on_close}>
            {row && <EditForm row={row} on_close={on_close} on_done={on_done} />}
        </Modal>
    );
}
