import React, { useContext, useState, useCallback, useEffect } from 'react';
import { NewsContext } from '../dev/DevContext';
import { Publication } from '../components/Publication';
import PublicationForm from '../components/PublicationForm';
import { useHttpRequest } from '../api/request/httpRequest.hook';
import { useHistory } from 'react-router-dom';
import { useAuth } from '../hooks/auth.hook';
import { usePublications } from '../hooks/api/publications.hook';
import '../styles/index.scss';
import '../styles/feedPage.scss';

export const FeedPage = () => {
    const { httpRequest } = useHttpRequest();

    const [publications, setPublications] = useState(null);
    const { getPublications, postPublication } = usePublications();

    const history = useHistory();
    const { logout } = useAuth();

    const loadPublications = useCallback(async (page, howMuch) => {
        page = page || 0;
        howMuch = howMuch || 10;
        try {
            const response = await getPublications({ page, howMuch });
            setPublications(response);
        } catch (e) {
            console.error(e);
            logout();
            history.go();
        }
    }, []);

    // const loadPublications = () => {
    //     return useContext(NewsContext);
    // }

    useEffect(() => {
        loadPublications();
    }, [loadPublications]);



    const makePublication = async ({ theme, text }) => {
        try {
            const response = await postPublication({ theme, text });
            history.go();
        } catch (e) {
            console.log(e);
        }
    };

    return (
        <div className="feedPage">
            <div className="content-block-main">
                <div className="form-block-new-publication mx-auto col-8">
                    
                    <PublicationForm onSubmit={makePublication} />
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
