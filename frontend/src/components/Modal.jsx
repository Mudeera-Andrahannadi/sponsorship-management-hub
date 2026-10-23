import { useEffect, useId, useRef, useState } from 'react';
import Icon from './Icon.jsx';

export default function Modal({ open, title, on_close, children })
{
    const box = useRef(null);
    const head = useId();
    const [shown, set_shown] = useState(open);

    useEffect(() =>
    {
        const node = box.current;
        if (open)
        {
            set_shown(true);
            if (!node.open)
            {
                node.showModal();
            }
            const first = node.querySelector('[data-autofocus]');
            if (first)
            {
                first.focus();
            }
            return undefined;
        }
        if (node.open)
        {
            node.close();
        }
        const timer = setTimeout(() => set_shown(false), 220);
        return () => clearTimeout(timer);
    }, [open]);

    const on_click = (evt) => evt.target === box.current && on_close();

    return (
        <dialog ref={box} className="modal" aria-labelledby={head} onClose={on_close} onClick={on_click}>
            {(open || shown) && (
                <div className="max-h-[calc(100dvh-2rem)] overflow-y-auto">
                    <div className="flex items-start justify-between gap-4 border-b border-line px-5 py-3">
                        <h2 id={head} className="pt-1 text-base font-semibold">{title}</h2>
                        <button type="button" className="btn btn-quiet -mr-2" onClick={on_close} aria-label="Close">
                            <Icon name="x" />
                        </button>
                    </div>
                    <div className="px-5 py-4">{children}</div>
                </div>
            )}
        </dialog>
    );
}
