import React from 'react';
import {Sex} from '../../components/profile/Sex';

const AdditionalInfo = ({ baseClass = '', isOpened = false, info = {} }) => {
    return (
        <div className={baseClass + ' additional-info'}>
            {!info ? (
                <label className={baseClass + ' no-info'}>No info</label>
            ) : (
                <table>
                    <tbody>
                        {info.email && (
                            <tr className={baseClass + ' table-row'}>
                                <td className={baseClass + ' table-key'}>Email</td>
                                <td className={baseClass + ' table-value'}>{info.email}</td>
                            </tr>
                        )}
                        {info.sex && Sex[info.sex] != Sex.UNDEFINED && (
                            <tr className={baseClass + ' table-row'}>
                                <td className={baseClass + ' table-key'}>Sex</td>
                                <td className={baseClass + ' table-value'}>{Sex[info.sex]}</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default AdditionalInfo;
