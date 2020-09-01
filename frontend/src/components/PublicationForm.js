import React from 'react';
import '../styles/publicationForm.css';
import '../styles/index.css';
import { TextInput } from './TextInput';

const PublicationForm = ({ isOpened, onClose, onSubmit }) => {
    let themeInput = React.createRef();
    let textInput = React.createRef();
    let errorLabel = React.createRef();

    const textInputKeyDown = (e) => {
        const el = e.target;
        setTimeout(() => {
            el.style.cssText = 'height:auto; padding:0';
            el.style.cssText = 'height:' + el.scrollHeight + 'px';
        }, 0);
    };

    const sendPublication = (e) => {
        const theme = themeInput.current.value;
        const text = textInput.current.value;

        if (!theme || !text) {
            errorLabel.current.innerText = 'All fields should be filled';
            errorLabel.current.hidden = false;
            return;
        }

        onSubmit({ theme, text });

        errorLabel.current.innerText = '';
        themeInput.current.innerText = '';
        textInput.current.innerText = '';

        onClose();
    };

    return (
        <React.Fragment>
            {isOpened && (
                <div className="publication-form">
                    <h2 className="publication-form-title">What's new?</h2>
                    <input className="publication-form-theme" ref={themeInput} maxLength="255" placeholder="Theme..." />
                    <br />
                    <TextInput ref={textInput} />
                    <br />
                    <button className="publication-form-button send" onClick={sendPublication}>
                        Send
                    </button>
                    <br />
                    <br />
                    <button className="publication-form-button decline-rotate" onClick={() => onClose()}>
                        &times;
                    </button>
                    <label className="error-msg" ref={errorLabel} hidden={true} />
                </div>
            )}
        </React.Fragment>
    );
};

export default PublicationForm;
