import React from 'react';
import { useParams } from 'react-router-dom';

export const ErrorPage = () => {
    const num = useParams().num;

    return (
        <div className="container justify-content-center">
            <div className="row justify-content-center py-2">
                <div><span className="h3">We've got some troubles: </span><span className="h2" style={{color: "RED"}}>{num}</span></div>
            </div>
            <div className="row h-50 justify-content-center">
                <div className="col-6">
                    <img
                        style={{ width: '100%', height: '100%' }}
                        src="../../resources/images/f5.png"
                    />
                </div>
            </div>
        </div>
    );
};
