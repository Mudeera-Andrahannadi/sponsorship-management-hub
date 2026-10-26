import { useState } from 'react';
import { post } from '../api.js';
import Field from './Field.jsx';
import FormShell from './FormShell.jsx';
import Modal from './Modal.jsx';

function EventForm({ on_close, on_done })
{
    const [form, set_form] = useState({ name: '', society: '', held_on: '' });
    const bind = (key) => ({ value: form[key], onChange: (evt) => set_form({ ...form, [key]: evt.target.value }) });

    const run = async () =>
    {
        const made = await post('/events', form);
        return made.name + ' added.';
    };

    return (
        <FormShell run={run} go="Add event" on_close={on_close} on_done={on_done}>
            <Field label="Event name" placeholder="CareerXpo 2027" required data-autofocus="" {...bind('name')} />
            <Field label="Society" placeholder="CareerXpo Organizing Committee" required {...bind('society')} />
            <Field label="Date" type="date" required {...bind('held_on')} />
        </FormShell>
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
