import React from 'react';

export const TextInput = React.forwardRef((props, ref) => {
    const textInputKeyDown = (e) => {
        const el = e.target;
        setTimeout(() => {
            el.style.cssText = 'height:auto; padding:0';
            el.style.cssText = 'height:' + el.scrollHeight + 'px';
        }, 0);
    };

    return (
        <textarea
            className="publication-form-text"
            ref={ref}
            placeholder="Your text here..."
            onKeyDown={(e) => textInputKeyDown(e)}
        ></textarea>
    );
});
