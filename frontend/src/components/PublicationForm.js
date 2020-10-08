import React from 'react';
import '../styles/publicationForm.scss';
import '../styles/index.scss';
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
                <div className="publication-form col-12 m-0">
                    <h2 className="publication-form-title">What's new?</h2>
                    <div className="row w-100">
                        <input
                            className="publication-form-theme col-8 m-2 p-1"
                            ref={themeInput}
                            maxLength="255"
                            placeholder="Theme..."
                        />
                        <div className="col-12 m-1 p-1 justify-content-left">
                            <TextInput ref={textInput} />
                        </div>
                    </div>

                    <div className="col-12">
                        <div className="row justify-content-end p-1 m-1">
                            <div className="col-2" align="right">
                                <button
                                    className="publication-form-button send"
                                    onClick={sendPublication}
                                >
                                    Send
                                </button>
                            </div>
                        </div>

                        <div className="row justify-content-end p-1 m-1">
                            <div className="col-2" align="right">
                                <button
                                    className="publication-form-button decline-rotate px-2"
                                    onClick={() => onClose()}
                                >
                                    &times;
                                </button>
                            </div>
                        </div>
                    </div>

                    <label
                        className="error-msg"
                        ref={errorLabel}
                        hidden={true}
                    />
                </div>
            )}
        </React.Fragment>
    );
};

export default PublicationForm;
