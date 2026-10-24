import { useState } from 'react';
import { toast } from 'sonner';
import { error_text, post } from '../api.js';
import Field from './Field.jsx';
import Modal from './Modal.jsx';

function EventForm({ on_close, on_done })
{
    const [form, set_form] = useState({ name: '', society: '', held_on: '' });
    const [busy, set_busy] = useState(false);
    const [problem, set_problem] = useState('');
    const bind = (key) => ({ value: form[key], onChange: (evt) => set_form({ ...form, [key]: evt.target.value }) });

    async function submit(evt)
    {
        evt.preventDefault();
        set_busy(true);
        set_problem('');
        try
        {
            const made = await post('/events', form);
            toast.success(made.name + ' added.');
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
            <Field label="Event name" placeholder="CareerXpo 2027" required data-autofocus="" {...bind('name')} />
            <Field label="Society" placeholder="CareerXpo Organizing Committee" required {...bind('society')} />
            <Field label="Date" type="date" required {...bind('held_on')} />
            <p role="alert" className="min-h-5 text-sm text-lock">{problem}</p>
            <div className="flex justify-end gap-2">
                <button type="button" className="btn" onClick={on_close}>Cancel</button>
                <button className="btn btn-primary" disabled={busy}>{busy ? 'One moment…' : 'Add event'}</button>
            </div>
        </form>
    );
}

export default function EventDialog({ open, on_close, on_done })
{
    return (
        <Modal open={open} title="New event" on_close={on_close}>
            <EventForm on_close={on_close} on_done={on_done} />
        </Modal>
    );
}
