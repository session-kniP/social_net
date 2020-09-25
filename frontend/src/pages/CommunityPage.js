import React, { useState, useEffect, useCallback } from 'react';
import '../styles/index.css';
import '../styles/community.css';
import '../styles/profile.css';
import { useTimer } from '../hooks/timer.hook';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { CommunityElement } from '../components/community/CommunityElement';

export const CommunityPageMode = Object.freeze({
    SUBSCRIPTIONS: 'Subscriptions',
    SUBSCRIBERS: 'Subscribers',
    FRIENDS: 'Friends',
    ALL: 'Users',
});

export const CommunityPage = ({ mode = CommunityPageMode.FRIENDS }) => {
    const communityMapping = '/api/community';

    const filterRef = React.createRef();

    const [elements, setElements] = useState(null);
    const { httpRequest } = useHttpRequest();

    const loadElements = useCallback(async (page, howMuch, filters) => {
        try {
            page = page || 0;
            howMuch = howMuch || 10;
            filters = filters || [];

            const response = await httpRequest({
                url: `${communityMapping}/${mode.toLowerCase()}?page=${page}&howMuch=${howMuch}&filters=${filters}`,
                method: 'GET',
            });
            console.log(response);

            setElements(response);
        } catch (e) {
            console.log(e);
        }
    }, []);

    useEffect(() => {
        loadElements();
    }, [loadElements]);


    const removeElement = (id) => {
        const newState = elements;
        newState.splice(elements.find(el => el.id = id), 1);
        
        setElements(newState);
    }


    const { onTimeIsOver } = useTimer();

    const filterChangeHandler = (e) => {
        const value = e.target.value;
        onTimeIsOver(() => loadElements(0, 10, value), 1000);
    };

    const elementOfMode = (mode, { id, firstName, lastName, title }) => {
        switch (mode) {
            case CommunityPageMode.SUBSCRIPTIONS:
            case CommunityPageMode.SUBSCRIBERS:
            case CommunityPageMode.FRIENDS:
            case CommunityPageMode.ALL:
                return (
                    <CommunityElement
                        id={id}
                        firstName={firstName}
                        lastName={lastName}
                        title={title}
                        key={id}
                        mode={mode}
                        onDelete={removeElement}
                    />
                );
        }
    };

    return (
        <div className="content-block-main community">
            {!elements || elements.length == 0 ? (
                <div className="no-community-block">
                    <h3 className="no-community-msg">{`No ${mode.toLowerCase()} yet`}</h3>
                    {mode == CommunityPageMode.FRIENDS && (
                        <a className="community link" href="/users">
                            {`Add a ${mode.toLowerCase().substr(0, mode.length - 1)}`}
                        </a>
                    )}
                </div>
            ) : (
                <React.Fragment>
                    <div className="community-filter">
                        <input
                            className="filter-field profile text-input"
                            ref={filterRef}
                            onChange={(e) => filterChangeHandler(e)}
                            placeholder={`Search for ${mode.toLowerCase()}...`}
                        />
                    </div>

                    <div className="community-list">
                        {elements.map((el) => {
                            return elementOfMode(mode, {
                                id: el.id,
                                firstName: el.userInfo.firstName,
                                lastName: el.userInfo.lastName,
                                title: el.username,
                            });
                        })}
                    </div>
                </React.Fragment>
            )}
        </div>
    );
};
