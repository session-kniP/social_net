import React from 'react';
import { useRoutes } from './routes';
import { BrowserRouter } from 'react-router-dom';

export default () => {
    const routes = useRoutes(false);
    return (
        <div>
            <BrowserRouter>
                <p>Hi, I'm application, but you are pidor!!!!</p>
                <div>{routes}</div>
            </BrowserRouter>
        </div>
    );
}

