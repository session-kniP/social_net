import React from 'react';
import {TextInput} from '../../components/TextInput';

export const ChatForm = React.forwardRef((props, ref) => {
    return (
        
        <div>
            <TextInput ref={ref} />
            <button className="publication-form-button accept" onClick={props.sendBtnHandler}>
                Send
            </button>
        </div>
    );
});
