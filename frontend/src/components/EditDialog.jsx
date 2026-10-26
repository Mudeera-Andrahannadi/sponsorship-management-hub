import { useState } from 'react';
import { patch } from '../api.js';
import Field from './Field.jsx';
import FormShell from './FormShell.jsx';
import Modal from './Modal.jsx';

function EditForm({ row, on_close, on_done })
{
    const [amount, set_amount] = useState(String(row.amount));
    const [notes, set_notes] = useState(row.notes || '');

    const run = async () =>
    {
        await patch('/sponsorships/' + row.deal_id + '/status', { amount: Number(amount), notes });
        return 'Saved.';
    };

    return (
        <FormShell run={run} go="Save" on_close={on_close} on_done={on_done}>
            <Field label="Amount (LKR)" type="number" min="1" step="any" inputMode="decimal" value={amount} onChange={(evt) => set_amount(evt.target.value)} required data-autofocus="" />
            <Field as="textarea" rows={3} label="Notes" value={notes} onChange={(evt) => set_notes(evt.target.value)} />
        </FormShell>
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
