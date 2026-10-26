import { useState } from 'react';
import { Link } from 'react-router-dom';
import { post } from '../api.js';
import Field from './Field.jsx';
import FormShell from './FormShell.jsx';
import Modal from './Modal.jsx';

function PitchForm({ event, held, on_close, on_done })
{
    const [comp_id, set_comp_id] = useState(held.length === 1 ? String(held[0].comp_id) : '');
    const [amount, set_amount] = useState('');
    const [notes, set_notes] = useState('');

    const run = async () =>
    {
        await post('/sponsorships', { comp_id: Number(comp_id), event_id: event.event_id, amount: Number(amount), notes: notes.trim() || null });
        return 'Pitch logged. The clock on that company has restarted.';
    };

    return (
        <FormShell run={run} go="Log pitch" on_close={on_close} on_done={on_done}>
            <Field as="select" label="Company" value={comp_id} onChange={(evt) => set_comp_id(evt.target.value)} required data-autofocus="">
                <option value="" disabled>Choose a company you hold</option>
                {held.map((item) => <option key={item.comp_id} value={item.comp_id}>{item.name}</option>)}
            </Field>
            <Field label="Amount you asked for (LKR)" type="number" min="1" step="any" inputMode="decimal" value={amount} onChange={(evt) => set_amount(evt.target.value)} required />
            <Field as="textarea" rows={3} label="Notes" hint="Who you spoke to, what they said, what comes next." value={notes} onChange={(evt) => set_notes(evt.target.value)} />
        </FormShell>
    );
}

export default function PitchDialog({ open, event, held, on_close, on_done })
{
    return (
        <Modal open={open} title={event ? 'Log a pitch for ' + event.name : ''} on_close={on_close}>
            {event && held.length === 0 && (
                <p>
                    You are not holding any company right now. <Link to="/" className="text-accent underline">Claim one on the board</Link> first, then come back to log the pitch.
                </p>
            )}
            {event && held.length > 0 && <PitchForm event={event} held={held} on_close={on_close} on_done={on_done} />}
        </Modal>
    );
}
