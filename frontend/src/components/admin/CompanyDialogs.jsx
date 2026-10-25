import { useState } from 'react';
import { post, put } from '../../api.js';
import Field from '../Field.jsx';
import FormShell from '../FormShell.jsx';
import Modal from '../Modal.jsx';

const KEYS = ['name', 'contact', 'job', 'field', 'email', 'phone'];

function blank_to_null(form)
{
    return Object.fromEntries(Object.entries(form).map(([key, text]) => [key, text.trim() || null]));
}

function CompanyFields({ item, on_close, on_done })
{
    const [form, set_form] = useState(Object.fromEntries(KEYS.map((key) => [key, (item && item[key]) || ''])));
    const bind = (key) => ({ value: form[key], onChange: (evt) => set_form({ ...form, [key]: evt.target.value }) });

    async function run()
    {
        const body = blank_to_null(form);
        await (item ? put('/companies/' + item.comp_id, body) : post('/companies', body));
        return body.name + (item ? ' saved.' : ' added to the directory.');
    }

    return (
        <FormShell run={run} go={item ? 'Save' : 'Add company'} on_close={on_close} on_done={on_done}>
            <Field label="Company name" required data-autofocus="" {...bind('name')} />
            <div className="grid gap-4 sm:grid-cols-2">
                <Field label="Contact person" {...bind('contact')} />
                <Field label="Position" {...bind('job')} />
                <Field label="Industry" {...bind('field')} />
                <Field label="Phone" type="tel" {...bind('phone')} />
            </div>
            <Field label="Email" type="email" {...bind('email')} />
        </FormShell>
    );
}

export function CompanyDialog({ open, item, on_close, on_done })
{
    return (
        <Modal open={open} title={item ? 'Edit ' + item.name : 'Add a company'} on_close={on_close}>
            <CompanyFields item={item} on_close={on_close} on_done={on_done} />
        </Modal>
    );
}

function LockFields({ item, on_close, on_done })
{
    const [days, set_days] = useState('');
    const run = async () =>
    {
        await post('/admin/companies/' + item.comp_id + '/lock', days ? { days: Number(days) } : {});
        return item.name + ' is locked.';
    };

    return (
        <FormShell run={run} go="Lock it" on_close={on_close} on_done={on_done}>
            <p>This takes the company off the board. Students cannot claim it until the lock ends or you unlock it.</p>
            <Field label="Days to lock" type="number" min="1" max="1095" placeholder="Its normal cooldown" hint="Leave blank to use the company's cooldown length." value={days} onChange={(evt) => set_days(evt.target.value)} data-autofocus="" />
        </FormShell>
    );
}

export function LockDialog({ open, item, on_close, on_done })
{
    return (
        <Modal open={open} title={item ? 'Lock ' + item.name : ''} on_close={on_close}>
            {item && <LockFields item={item} on_close={on_close} on_done={on_done} />}
        </Modal>
    );
}

function LengthFields({ item, on_close, on_done })
{
    const [hold, set_hold] = useState(item.lock_days == null ? '' : String(item.lock_days));
    const [cool, set_cool] = useState(item.cool_days == null ? '' : String(item.cool_days));
    const num = (text) => (text ? Number(text) : null);
    const run = async () =>
    {
        await put('/admin/companies/' + item.comp_id + '/lock-days', { lock_days: num(hold), cool_days: num(cool) });
        return 'Lengths saved for ' + item.name + '.';
    };

    return (
        <FormShell run={run} go="Save lengths" on_close={on_close} on_done={on_done}>
            <p>Leave a box empty to use the standard length. New lengths apply from the next claim or approval.</p>
            <Field label="Hold (days)" type="number" min="1" max="365" placeholder="Standard" value={hold} onChange={(evt) => set_hold(evt.target.value)} data-autofocus="" />
            <Field label="Cooldown after a sponsorship (days)" type="number" min="1" max="1095" placeholder="Standard" value={cool} onChange={(evt) => set_cool(evt.target.value)} />
        </FormShell>
    );
}

export function LengthDialog({ open, item, on_close, on_done })
{
    return (
        <Modal open={open} title={item ? 'Lock lengths for ' + item.name : ''} on_close={on_close}>
            {item && <LengthFields item={item} on_close={on_close} on_done={on_done} />}
        </Modal>
    );
}
