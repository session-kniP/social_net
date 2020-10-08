import React from 'react';
import '../styles/publicationForm.scss';

export const TextInput = React.forwardRef((props, ref) => {
    const textInputKeyDown = (e) => {
        const el = e.target;
        setTimeout(() => {
            el.style.cssText = 'height:auto;';
            el.style.cssText = 'height:' + el.scrollHeight + 'px;';
            el.scrollIntoView();
            props.callback && props.callback();
        }, 0);
    };

    return (
        <textarea
            className="publication-form-text px-1 col-12"
            ref={ref}
            placeholder="Your text here..."
            onKeyDown={(e) => textInputKeyDown(e)}
        />
    );
});
