"use strict";

import React from 'react';
import $ from 'jquery';

import { Navbar,Nav,NavItem } from 'react-bootstrap';


require('!style!css!less!./Application.less');

export class Application extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: 'Initial',
            counter: -1,
            when: ''
        };
    }
    fetchState() {
        $.ajax('/rest/hello').done((data) => this.setState(data));
    }
    render() {
        return (
            <div>
                <Navbar>
                    <Navbar.Header>
                        <Navbar.Brand>
                            <a href="#">Planning Equipe Soccer 5</a>
                        </Navbar.Brand>
                    </Navbar.Header>
                    <Nav>
                        <NavItem eventKey={1} href="#">Season</NavItem>
                        <NavItem eventKey={2} href="#">Agenda</NavItem>
                        <NavItem eventKey={3} href="#">Players</NavItem>
                    </Nav>
                </Navbar>
                <div className="app-hello-message">
                    Hello {this.state.name} #{this.state.counter} at {this.state.timestamp}
                </div>
            </div>
        );
    }
    componentDidMount() {
        this.interval = window.setInterval(this.fetchState.bind(this), 1000);
    }
    componentWillUnmount() {
        clearInterval(this.interval);
    }
}
