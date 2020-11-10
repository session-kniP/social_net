import React from 'react';
import { Tab, Row, Col, Nav } from 'react-bootstrap';
import { CommunityContent } from './CommunityContent';
import { CommunityPageMode } from './CommunityPageMode';
import '../../styles/navBar.scss';

export const CommunitySwitch = ({ modes }) => {
    return (
        <Tab.Container id="left-tabs-example" defaultActiveKey={modes[0]}>
            <Row className="d-flex m-0 justify-content-center">
                <Nav variant="pills" className="flex-row col-8 p-0">
                    {console.log(modes)}
                    {modes && modes.filter((el) => el == CommunityPageMode.FRIENDS).length > 0 && (
                        <Col sm={3} className="p-0 text-center">
                            <Nav.Item className="switch">
                                <Nav.Link eventKey={CommunityPageMode.FRIENDS}>Friends</Nav.Link>
                            </Nav.Item>
                        </Col>
                    )}

                    {modes && modes.filter((el) => el == CommunityPageMode.SUBSCRIBERS).length > 0 && (
                        <Col sm={3} className="p-0 text-center">
                            <Nav.Item className="switch">
                                <Nav.Link eventKey={CommunityPageMode.SUBSCRIBERS}>Subscribers</Nav.Link>
                            </Nav.Item>
                        </Col>
                    )}

                    {modes && modes.filter((el) => el == CommunityPageMode.SUBSCRIPTIONS).length > 0 && (
                        <Col sm={3} className="p-0 text-center">
                            <Nav.Item className="switch">
                                <Nav.Link eventKey={CommunityPageMode.SUBSCRIPTIONS}>Subscriptions</Nav.Link>
                            </Nav.Item>
                        </Col>
                    )}

                    {modes && modes.filter((el) => el == CommunityPageMode.COMMUNITIES).length > 0 && (
                        <Col sm={3} className="p-0 text-center">
                            <Nav.Item className="switch">
                                <Nav.Link eventKey={CommunityPageMode.COMMUNITIES}>Communities</Nav.Link>
                            </Nav.Item>
                        </Col>
                    )}

                    
                </Nav>
            </Row>

            <Row>
                <Col sm={12}>
                    <Tab.Content>
                        {modes &&
                            modes.map((el, idx) => {
                                console.log('In map', el);
                                return (
                                    <Tab.Pane eventKey={el} key={idx}>
                                        <CommunityContent mode={el} />
                                    </Tab.Pane>
                                );
                            })}
                    </Tab.Content>
                </Col>
            </Row>
        </Tab.Container>
    );
};
