import React from 'react';
import contextStore from "../stores/ContextStore";
import {observer} from "mobx-react";
import './BoardContainer.css'
import {Board} from "../components/Board";
import {Radio, Modal, Button} from "antd";
import {MOVE, BUILD, PASS, BUILD_DOME} from '../constants';

export default observer(class BoardContainer extends React.Component {
    state = {
      showModalWhenWin: true,
    }
    onSelectWorker = (x, y) => {
      let cell = contextStore.board[x][y];
      console.log(`select ${x}, ${y}, ${cell.workerId ? cell.workerId : ''}`)
      if (cell.workerId && contextStore.belongsToCurrentPlayer(cell.workerId)) {
        contextStore.setCurrentWorker(cell.workerId);
      }
    }

    onMoveWorker = (x, y) => {
      if (!contextStore.currentWorkerId) {
        return;
      }
      contextStore.moveWorker(x, y);
      contextStore.setCurrentWorker(0);
    }

    onBuild = (x, y) => {
      if (!contextStore.currentWorkerId) {
        return;
      }
      contextStore.build(x, y);
      contextStore.setCurrentWorker(0);
    }

    onBuildDome = (x, y) => {
      if (!contextStore.currentWorkerId) {
        return;
      }
      contextStore.buildDome(x, y);
      contextStore.setCurrentWorker(0);
    }

    onPass = () => {
      contextStore.pass();
      contextStore.setCurrentWorker(0);
    }

    getOnCellClickFunc = (action) => {
      if (!(contextStore.player1HasTwoWorker && contextStore.player2HasTwoWorker)) {
        return contextStore.initWorker;
      } else if (!contextStore.currentWorkerId) {
        return this.onSelectWorker;
      } else if (action === MOVE) {
        return this.onMoveWorker;
      } else if (action === BUILD) {
        return this.onBuild;
      } else if (action === BUILD_DOME) {
        return this.onBuildDome;
      }
      return (x, y) => console.log(`${x},${y}`);
    }


    render() {
      let options = [{
        label: MOVE,
        value: MOVE,
        disabled: contextStore.context.nextAvailActions.slice().indexOf(MOVE) < 0
      }, {
        label: BUILD,
        value: BUILD,
        disabled: contextStore.context.nextAvailActions.slice().indexOf(BUILD) < 0
      }];
      if (contextStore.context.nextAvailActions.slice().indexOf(BUILD_DOME) >= 0) {
        options.push({
          label: BUILD_DOME,
          value: BUILD_DOME,
        });
      }
      let onClickCell = this.getOnCellClickFunc(contextStore.selectedAction || contextStore.defaultAction);
      return (
        <div className="santorini-board-container">
          <Board board={contextStore.board}
                 onClickCell={onClickCell}
                 currentWorker={contextStore.currentWorkerId}/>
          <div className="santorini-board-container-info">
            {contextStore.info}
          </div>
          <Radio.Group
            options={options}
            onChange={e => contextStore.setSelectedAction(e.target.value)}
            value={contextStore.selectedAction || contextStore.defaultAction}
            optionType="button"
            buttonStyle="solid"
          />
          {(contextStore.context.nextAvailActions.slice().indexOf(PASS) >= 0) && (
            <Button onClick={this.onPass}>
              Pass
            </Button>)}

          <Modal
            title={"We got a winner!"}
            visible={this.state.showModalWhenWin && contextStore.winPlayer !== null}
            onOk={() => window.location.reload()}
            onCancel={() => this.setState({showModalWhenWin: false})}
            okText="Play again"
            cancelText="Cancel"
          >
            Player{contextStore.winPlayer} win!
          </Modal>
        </div>
      );
    }
  }
)