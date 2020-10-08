import React from 'react';

export const useTimer = () => {
    let delayTimer;

    const onTimeIsOver = (callback, millis) => {
        clearTimeout(delayTimer);

        delayTimer = setTimeout(() => {
            callback();
        }, millis);
    };

    return { onTimeIsOver };
};
