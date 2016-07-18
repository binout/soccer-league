import React from 'react';
import { Navbar,Nav,NavItem } from 'react-bootstrap';
import {browserHistory} from 'react-router';

import SeasonSelector from './SeasonSelector.jsx';

const SoccerNavBar = React.createClass({

    render() {
        return (
            <Navbar>
                <Navbar.Header>
                    <Navbar.Brand>
                        <a href="/">Planning Equipe Soccer 5</a>
                    </Navbar.Brand>
                </Navbar.Header>
                <Nav>
                    <NavItem eventKey={1}>
                        Season
                        <SeasonSelector list={this.props.seasons} onClick={() => browserHistory.push('/')}/>
                    </NavItem>
                    <NavItem eventKey={2} onClick={() => browserHistory.push('/')}>Agenda</NavItem>
                    <NavItem eventKey={3} onClick={() => browserHistory.push('players')}>Players</NavItem>
                </Nav>
            </Navbar>
        );
    }

});

export default SoccerNavBar;

