import React from 'react';
import PropTypes from 'prop-types';
import './Cell.css';

export class Cell extends React.Component {
  static propTypes = {
    x: PropTypes.number.isRequired,
    y: PropTypes.number.isRequired,
    height: PropTypes.number.isRequired,
    workerId: PropTypes.number.isRequired,
    handleClick: PropTypes.func.isRequired,
  }

  render() {
    const {x, y, handleClick, height, workerId, isActive} = this.props;
    return (
      <div className="santorini-cell" onClick={() => handleClick(x, y)}>
        <p className="santorini-cell-height">{height >= 0 ? height : '✖️'}</p>
        {workerId && (
          <div className={
            `santorini-cell-worker santorini-cell-worker-${workerId <= 2 ? 'white' : 'blue'} ${isActive ? 'santorini-cell-worker-active' : ''}`}/>
        )}
      </div>
    );
  }
}