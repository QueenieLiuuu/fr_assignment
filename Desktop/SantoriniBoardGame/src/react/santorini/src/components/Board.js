import React from 'react';
import './Board.css'
import PropTypes from "prop-types";
import {Cell} from "./Cell";

export class Board extends React.Component {
  static propTypes = {
    board: PropTypes.array.isRequired,
    onClickCell: PropTypes.func.isRequired,
    currentWorker: PropTypes.number.isRequired,
  }


  render() {
    console.log(this.props.board)
    const {board, currentWorker, onClickCell, onSelectAction} = this.props;
    return (
      <div className="santorini-board">
        {board ? board.slice().map((row, x) => (
          <div className="santorini-board-row">
            {row.slice().map((cell, y) => (
              <Cell
                x={x}
                y={y}
                height={cell.height}
                workerId={cell.workerId}
                isActive={cell.workerId === currentWorker}
                handleClick={onClickCell}
                handleSelectAction={onSelectAction}
              />
            ))}
          </div>
        )) : null}
      </div>
    );
  }
}