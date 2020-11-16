
import React, { useState, useEffect, useCallback } from 'react';
import '../../styles/index.scss';
import '../../styles/community.scss';
import '../../styles/profile.scss';
import { useTimer } from '../../hooks/timer.hook';
import { useHttpRequest } from '../../api/request/httpRequest.hook';
import { CommunityElement } from '../../components/community/CommunityElement';
import {M_COMMUNITY} from '../../constants/mappings';
import {CommunityPageMode} from './CommunityPageMode';
import { useAuth } from '../../hooks/auth.hook';

export const CommunityContent = ({ mode = CommunityPageMode.FRIENDS }) => {
    const filterRef = React.createRef();

    const [elements, setElements] = useState(null);
    const { httpRequest } = useHttpRequest();
    const {logout} = useAuth();

    const loadElements = useCallback(async (page, howMuch, filters) => {
        try {
            page = page || 0;
            howMuch = howMuch || 10;
            filters = filters || [];


            const url = `${M_COMMUNITY}/${mode.toLowerCase()}?page=${page}&howMuch=${howMuch}&filters=${filters}`;
            console.log(url);

            const response = await httpRequest({
                url: url,
                method: 'GET',
            });

            setElements(response);
        } catch (e) {
            console.log(e);
            logout();
            history.go();
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
            case CommunityPageMode.COMMUNITIES:
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
        <div className="content-block-main col-12 col-md-8 mx-auto p-0 community">



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
}