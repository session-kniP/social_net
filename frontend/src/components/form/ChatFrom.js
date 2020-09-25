import React from 'react';
import { TextInput } from '../../components/TextInput';

export const ChatForm = React.forwardRef((props, ref) => {
    return (
        <div className="container p-0">
            <div className="d-flex justify-content-center">
                <div className="col-11">
                    <TextInput ref={ref} />
                    <button
                        className="publication-form-button accept"
                        onClick={props.sendBtnHandler}
                    >
                        Send
                    </button>
                </div>
            </div>
        </div>
    );
});
