import React, { useState } from 'react';
import '../styles/modal.scss';
import { Modal as Mod } from 'react-bootstrap';

export const Modal = ({ isOpened, children, onClose }) => {
    return (
        //<React.Fragment>
        //    {isOpened && (
        //        <div className="modal" onLoad={(e) => e.target.focus()}>
        //            <div className="modal-content">
        //                <div className="modal-header">
        //                    <button
        //                        className="close-icon"
        //                        onClick={() => onClose()}
        //                    >
        //                        &times;
        //                    </button>
        //                </div>
        //                <div className="modal-body p-0 h-100">
        //                    <div className="d-block">{children}</div>
        //                </div>

        //                <div className="modal-footer"></div>
        //            </div>
        //        </div>
        //    )}
        //</React.Fragment>

        <Mod show={isOpened} onHide={onClose}>
            <Mod.Header className="m-1" closeButton />
            <Mod.Body className="modal-body">
                {children}
            </Mod.Body>
            <Mod.Footer />
        </Mod>
    );
};
