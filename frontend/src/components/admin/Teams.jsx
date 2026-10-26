import { useState } from 'react';
import { toast } from 'sonner';
import { error_text, patch, post } from '../../api.js';
import { useLoad } from '../../hooks.js';
import Field from '../Field.jsx';
import FormShell from '../FormShell.jsx';
import Modal from '../Modal.jsx';

function AssignFields({ on_close, on_done })
{
    const people = useLoad('/admin/users?role=Student');
    const events = useLoad('/events');
    const [form, set_form] = useState({ user_id: '', event_id: '', post: '' });
    const bind = (key) => ({ value: form[key], onChange: (evt) => set_form({ ...form, [key]: evt.target.value }) });

    const run = async () =>
    {
        await post('/committees/assign', { user_id: Number(form.user_id), event_id: Number(form.event_id), post: form.post.trim() });
        return 'Student added to the committee.';
    };

    return (
        <FormShell run={run} go="Add to committee" on_close={on_close} on_done={on_done}>
            <Field as="select" label="Student" required data-autofocus="" {...bind('user_id')}>
                <option value="" disabled>Choose a student</option>
                {(people.data || []).map((user) => <option key={user.user_id} value={user.user_id}>{user.name} ({user.reg_no})</option>)}
            </Field>
            <Field as="select" label="Event" required {...bind('event_id')}>
                <option value="" disabled>Choose an event</option>
                {(events.data || []).map((event) => <option key={event.event_id} value={event.event_id}>{event.name}</option>)}
            </Field>
            <Field label="Role on the committee" placeholder="Treasurer" required {...bind('post')} />
        </FormShell>
    );
}

function TeamRow({ row, on_toggle })
{
    const label = (row.active ? 'Switch off ' : 'Switch on ') + row.name + ' for ' + row.event;
    return (
        <li className="flex flex-wrap items-center justify-between gap-x-4 gap-y-1 border-t border-line px-3 py-2.5 first:border-t-0">
            <div className="min-w-0">
                <p className="font-medium">{row.name}</p>
                <p className="text-[0.8125rem] text-mute">{row.event} · {row.post}</p>
            </div>
            <div className="flex items-center gap-3">
                <span className={row.active ? 'text-free' : 'text-mute'}>{row.active ? 'Active' : 'Inactive'}</span>
                <button className="btn btn-quiet" onClick={() => on_toggle(row)} aria-label={label}>{row.active ? 'Switch off' : 'Switch on'}</button>
            </div>
        </li>
    );
}

function useToggle(reload)
{
    return async (row) =>
    {
        try
        {
            await patch('/committees/' + row.team_id, { active: !row.active });
            await reload();
            toast.success(row.name + (row.active ? ' is off ' : ' is back on ') + row.event + '.');
        }
        catch (err)
        {
            toast.error(error_text(err));
        }
    };
}

export default function Teams()
{
    const { data, error, loading, reload } = useLoad('/committees');
    const [open, set_open] = useState(false);
    const toggle = useToggle(reload);
    const close = () => set_open(false);

    return (
        <div>
            <div className="mt-4 flex flex-wrap items-center justify-between gap-3">
                <p className="text-mute">Only active members can claim companies and log pitches.</p>
                <button className="btn btn-primary" onClick={() => set_open(true)}>Add student to an event</button>
            </div>
            {error && <p role="alert" className="mt-4 text-lock">{error}</p>}
            {loading && <p className="mt-6 text-mute">Loading…</p>}
            <ul className="mt-4 overflow-hidden rounded-lg border border-line bg-panel">
                {(data || []).map((row) => <TeamRow key={row.team_id} row={row} on_toggle={toggle} />)}
            </ul>
            <Modal open={open} title="Add a student to an event" on_close={close}>
                <AssignFields on_close={close} on_done={reload} />
            </Modal>
        </div>
    );
}
