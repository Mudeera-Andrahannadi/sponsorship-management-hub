import { useState } from 'react';
import { post } from '../../api.js';
import { useLoad } from '../../hooks.js';
import Field from '../Field.jsx';
import FormShell from '../FormShell.jsx';
import Modal from '../Modal.jsx';

function UserFields({ on_close, on_done })
{
    const [form, set_form] = useState({ name: '', reg_no: '', email: '', password: '', role: 'Student' });
    const bind = (key) => ({ value: form[key], onChange: (evt) => set_form({ ...form, [key]: evt.target.value }) });

    const run = async () =>
    {
        await post('/admin/users', form);
        return form.name + ' can sign in now.';
    };

    return (
        <FormShell run={run} go="Add user" on_close={on_close} on_done={on_done}>
            <Field label="Full name" required data-autofocus="" {...bind('name')} />
            <Field label="Registration or staff number" required {...bind('reg_no')} />
            <Field label="Email" type="email" required {...bind('email')} />
            <Field label="First password" type="password" minLength={8} hint="At least 8 characters. They can keep it or ask you to change it." required {...bind('password')} />
            <Field as="select" label="Role" {...bind('role')}>
                <option>Student</option>
                <option>Admin</option>
            </Field>
        </FormShell>
    );
}

export default function Users()
{
    const { data, error, loading, reload } = useLoad('/admin/users');
    const [open, set_open] = useState(false);
    const close = () => set_open(false);

    return (
        <div>
            <div className="mt-4 flex flex-wrap items-center justify-between gap-3">
                <p className="text-mute">Students register themselves. Add admins here.</p>
                <button className="btn btn-primary" onClick={() => set_open(true)}>Add user</button>
            </div>
            {error && <p role="alert" className="mt-4 text-lock">{error}</p>}
            {loading && <p className="mt-6 text-mute">Loading…</p>}
            <ul className="mt-4 overflow-hidden rounded-lg border border-line bg-panel">
                {(data || []).map((user) => (
                    <li key={user.user_id} className="flex flex-wrap items-center justify-between gap-x-4 gap-y-1 border-t border-line px-3 py-2.5 first:border-t-0">
                        <div className="min-w-0">
                            <p className="font-medium">{user.name}</p>
                            <p className="truncate text-[0.8125rem] text-mute">{user.reg_no} · {user.email}</p>
                        </div>
                        <span className={user.role === 'Admin' ? 'font-medium' : 'text-mute'}>{user.role}</span>
                    </li>
                ))}
            </ul>
            <Modal open={open} title="Add a user" on_close={close}>
                <UserFields on_close={close} on_done={reload} />
            </Modal>
        </div>
    );
}
