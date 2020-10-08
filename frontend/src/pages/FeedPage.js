import React, { useContext, useState, useCallback, useEffect } from 'react';
import { NewsContext } from '../dev/DevContext';
import { Publication } from '../components/Publication';
import PublicationForm from '../components/PublicationForm';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { useHistory } from 'react-router-dom';
import '../styles/index.scss';
import '../styles/feedPage.scss';
import { M_PUBLICATIONS } from '../constants/mappings';


export const FeedPage = () => {
    const {httpRequest} = useHttpRequest();
    const [publications, setPublications] = useState(null);
    const history = useHistory();

    const loadPublications = useCallback(async (page, howMuch) => {
        page = page || 0;
        howMuch = howMuch || 10;
        
        const response = await httpRequest({
            url: `${M_PUBLICATIONS}/news?page=${page}&howMuch=${howMuch}`,
            method: 'GET',
        });
        setPublications(response);
    }, []);

    // const loadPublications = () => {
    //     return useContext(NewsContext);
    // }

    useEffect(() => {
        loadPublications();
    }, [loadPublications]);

    const [isOpened, setIsOpened] = useState(false);

    const closeForm = () => {
        setIsOpened(false);
    };

    const openForm = () => {
        setIsOpened(true);
    };

    const makePublicaion = ({ theme, text }) => {
        try {
            const response = httpRequest({
                url: `${M_PUBLICATIONS}/makePublication`,
                method: 'POST',
                body: {
                    theme: theme,
                    text: text,
                },
            });
            history.go();
        } catch (e) {
            console.log(e);
        }
        
    };

    return (
        <div className="feedPage">
            <div className="content-block-main">
                <div className="form-block-new-publication mx-auto col-8">
                    {!isOpened && (
                        <button className="feed-open-form-btn m-0" onClick={() => openForm()}>
                            New publication
                        </button>
                    )}
                    <PublicationForm isOpened={isOpened} onClose={closeForm} onSubmit={makePublicaion} />
                </div>
                <div className="content-block-publications mx-auto col-12 col-md-8 col-lg-8">
                    {publications &&
                        publications.map((p) => {
                            return <Publication props={p} key={Math.random()} />;
                        })}
                </div>
            </div>
        </div>
    );
};
