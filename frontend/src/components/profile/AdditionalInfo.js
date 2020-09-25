import React from 'react';
import { Sex } from '../../components/profile/Sex';

const AdditionalInfo = ({ baseClass = '', isOpened = false, info = {} }) => {

    const isSex = (sex) => {
        sex ? true : false;
        sex == Sex.UNDEFINED ? false : true;
    }

    return (
        <div className={baseClass + ' additional-info w-100'}>
            {!(isSex(info.sex) || info.email) ? (
                <label className={baseClass + ' no-info'}>No info</label>
            ) : (
                <div className="container w-100">
                    {info.email && (
                        <div className="row">
                            <div className="col-3">Email</div>
                            <div className="col-9">{info.email}</div>
                        </div>
                    )}
                    {isSex(info.sex) != 'UNDEFINED' && (
                        <div className="row">
                            <div className="col-3">Sex</div>
                            <div className="col-9">{Sex[info.sex]}</div>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default AdditionalInfo;
