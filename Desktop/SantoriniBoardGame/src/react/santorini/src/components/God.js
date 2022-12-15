import React from 'react';
import PropTypes from 'prop-types';
import './God.css';
export default class God extends React.Component {
  static propTypes = {
    name: PropTypes.string.isRequired,
    title: PropTypes.string.isRequired,
    powerDetail: PropTypes.string.isRequired,
    url: PropTypes.string.isRequired,
    handleChooseGod: PropTypes.func.isRequired,
  }

  render() {
    return (
      <div className="santorini-god" onClick={() => this.props.handleChooseGod(this.props.name)}>
        <img className="santorini-god-img" src={this.props.url}/>
        <p className="santorini-god-name">{this.props.name}</p>
        <p className="santorini-god-title">{this.props.title}</p>
      </div>
    );
  }
}