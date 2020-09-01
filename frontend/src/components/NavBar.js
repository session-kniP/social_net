import React from 'react';
import { NavLink as a } from 'react-router-dom';
import PropTypes from 'prop-types';
import '../styles/index.css';
import '../styles/navBar.css';

const NavBar = ({ navClassName = null, mappingLinks = null, authLinks = null, additionalComps = null }) => {
    console.log(additionalComps);

    const spreadLinks = (links) => {
        return links.map((link) => {
            return (
                <a
                    key={Math.random()}
                    className={link.className ? link.className + '-nav-link' : 'nav-link'}
                    href={link.href}
                >
                    {link.title}
                </a>
            );
        });
    };

    return (
        <nav className={navClassName ? 'nav-bar-' + navClassName : 'nav-bar'}>
            <div className="nav-wrapper">
                {mappingLinks && <div className="mapping-links">{spreadLinks(mappingLinks)}</div>}
                {authLinks && <div className="auth-links">{spreadLinks(authLinks)}</div>}
                {additionalComps && (
                    <div className="additional-comps">
                        {additionalComps.map((comp) => {
                            return (
                                <a
                                    key={Math.random()}
                                    href={'/' + comp.className}
                                    onClick={comp.onClick}
                                    className={comp.className + '-link'}
                                >
                                    {comp.text}
                                </a>
                            );
                        })}
                    </div>
                )}
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
