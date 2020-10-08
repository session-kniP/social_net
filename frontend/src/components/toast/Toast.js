import React, { useEffect } from 'react';
import $ from 'jquery';
import '../../styles/toast.scss';
import { useTimer } from '../../hooks/timer.hook';

export const Toast = ({ type, header, body }) => {
    const {onTimeIsOver} = useTimer();

    const close = () => {
        onTimeIsOver(() => {$(`.${type}`).toast('hide')}, 5000);
    }

    useEffect(() => {
        close();
    }, []);

    return (
        <div
            className={`toast ${type}`}
            role="alert"
            aria-live="assertive"
            aria-atomic="true"
            data-autohide="false"
            onMouseOver={() => close()}
            // data-delay="5000"
        >
            <div className="toast-header bgcolor-inherit">
                <img
                    src={`../../../resources/images/${type}.png`}
                    width="20"
                    height="20"
                    className="rounded mr-2"
                    alt="..."
                />
                <strong className="mr-auto">{header}</strong>
                {/* <small className="text-muted">11 mins ago</small> */}
                <button
                    type="button"
                    className="ml-2 mb-1 close"
                    data-dismiss="toast"
                    aria-label="Close"
                >
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div className="toast-body">{body}</div>
        </div>
    );
};
