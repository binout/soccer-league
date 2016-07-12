import React from 'react';
import {FormGroup,ControlLabel,FormControl} from 'react-bootstrap';

const SeasonSelector = React.createClass({

    render() {
        const options = [];
        for (var i = 0; i < this.props.list.length; i++) {
            options.push(<option key={i}>{this.props.list[i]}</option>)
        }
        return (
            <form>
               <select>
                   {options}
               </select>
            </form>);
    }
});

export default SeasonSelector;