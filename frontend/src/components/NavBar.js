import 'bootstrap/dist/js/bootstrap.min';
import React from 'react';
import PropTypes from 'prop-types';
import '../styles/index.scss';
import '../styles/navBar.scss';

const NavBarType = Object.freeze({
    MAPPING: 0,
    AUTH: 1,
});

const NavBar = ({
    navClassName = null,
    mappingLinks = null,
    authLinks = null,
    additionalComps = null,
}) => {
    // ${
    //     type === NavBarType.MAPPING ? 'float-left' : 'float-right'
    // }
    const spreadLinks = (links, type) => {
        return links.map((link) => {
            return (
                <a
                    key={Math.random()}
                    className={`nav-item nav-link active ml-0 mr-0 ml-md-2 mr-md-2 p-3 d-inline-block text-center h-100`}
                    href={link.href ? link.href : './'}
                    onClick={link.onClick ? link.onClick : () => {}}
                >
                    {link.title}
                </a>
            );
        });
    };

    return (
        <nav className="navbar sticky-top navbar-dark bg-dark navbar-toggleable navbar-expand-sm p-0 pl-lg-5 pr-lg-5 pl-sm-1 pr-sm-1">
            <div className="container">
                <a className="navbar-brand" href="./">
                    Pink.net
                </a>
                <button
                    className="navbar-toggler"
                    type="button"
                    data-toggle="collapse"
                    data-target="#navbarNavAltMarkup"
                    aria-controls="navbarNavAltMarkup"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div
                    className="collapse navbar-collapse w-50"
                    id="navbarNavAltMarkup"
                >
                    <div className="navbar-nav w-100">
                        {mappingLinks &&
                            spreadLinks(mappingLinks, NavBarType.MAPPING)}
                    </div>
                    <div  className="navbar-nav w-100 justify-content-end">
                    {authLinks && spreadLinks(authLinks, NavBarType.AUTH)}

                    </div>
                </div>
            </div>
        </nav>
    );
};

NavBar.propTypes = {
    mappingLinks: PropTypes.arrayOf(PropTypes.object),
    authLinks: PropTypes.arrayOf(PropTypes.object),
    additionalComps: PropTypes.arrayOf(PropTypes.object),
};

export default NavBar;
