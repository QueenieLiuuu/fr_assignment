import {action, computed, makeObservable, observable} from "mobx";
import {
  getBuild,
  getChooseGod, getFormerContext,
  getInitWorker, getLatterContext,
  getMoveWorker,
  getPass,
  postInitGame
} from "../api";
import {message} from 'antd';
import {BUILD, MOVE} from "../constants";

class ContextStore {
  context = {};
  currentWorkerId = 0;
  selectedAction = '';
  nextAvailActions = [];

  constructor() {
    makeObservable(this, {
      context: observable,
      currentWorkerId: observable,
      selectedAction: observable,
      nextAvailActions: observable,
      init: action,
      setCurrentWorker: action,
      setSelectedAction: action,
      setDefaultAction: action,
      board: computed,
      player1HasGod: computed,
      player2HasGod: computed,
      player1HasTwoWorker: computed,
      player2HasTwoWorker: computed,
      info: computed,
      winPlayer: computed,
    })
  }

  getParentPlayerByWorker(workerId) {
    if (workerId >= 1 && workerId <= 2) {
      return 1;
    } else if (workerId >= 3 && workerId <= 4) {
      return 2;
    } else {
      return null;
    }
  }

  belongsToCurrentPlayer(workerId) {
    return this.getParentPlayerByWorker(workerId) === (this.context.activePlayerId || 1);
  }

  setCurrentWorker(workerId) {
    this.currentWorkerId = workerId;
  }

  setSelectedAction(selectedAction) {
    this.selectedAction = selectedAction;
  }

  get board() {
    if (!this.context || JSON.stringify(this.context) === '{}') {
      return false;
    }
    let board = [];
    for (let i = 0; i < this.context.board.length; i++) {
      let row = this.context.board[i];
      let newRow = []
      for (let j = 0; j < row.length; j++) {
        const height = row[j];
        let workerId = null;
        const workers = this.context.player1.workers.concat(this.context.player2.workers);
        for (let worker of workers.slice()) {
          if (worker.x === i && worker.y === j) {
            workerId = worker.id;
          }
        }
        newRow.push({x: i, y: j, height, workerId})
      }
      board.push(newRow);
    }
    return board;
  }

  get player1HasGod() {
    if (!this.context || JSON.stringify(this.context) === '{}') {
      return false;
    }
    return this.context.player1 && this.context.player1.god;
  }

  get player1HasTwoWorker() {
    if (!this.context || JSON.stringify(this.context) === '{}') {
      return false;
    }
    return this.context.player1 && this.context.player1.workers && this.context.player1.workers.length === 2;
  }

  get player2HasTwoWorker() {
    if (!this.context || JSON.stringify(this.context) === '{}') {
      return false;
    }
    return this.context.player2 && this.context.player2.workers && this.context.player2.workers.length === 2;
  }

  get player2HasGod() {
    if (!this.context || JSON.stringify(this.context) === '{}') {
      return false;
    }
    return this.context.player2 && this.context.player2.god;
  }

  get winPlayer() {
    if (!this.context || JSON.stringify(this.context) === '{}') {
      return null;
    }
    if (this.context.state === 'Win') {
      return this.context.activePlayerId;
    } else {
      return null;
    }
  }

  get info() {
    if (!this.context || JSON.stringify(this.context) === '{}') {
      return '';
    }
    if (!this.player1HasGod) {
      return 'Player 1 please choose your god.';
    } else if (!this.player2HasGod) {
      return 'Player 2 please choose your god.';
    }
    if (!this.player1HasTwoWorker) {
      return 'Player 1 please set worker.';
    } else if (!this.player2HasTwoWorker) {
      return 'Player 2 please set worker.';
    }
    return `Player ${this.context.activePlayerId}'s turn`
  }


  setDefaultAction() {
    if (!this.context || JSON.stringify(this.context) === '{}') {
      return '';
    }
    if (this.context.nextAvailActions.slice().indexOf(MOVE) >= 0) {
      this.setSelectedAction(MOVE);
    } else if (this.context.nextAvailActions.slice().indexOf(BUILD) >= 0) {
      this.setSelectedAction(BUILD);
    }
  }

  init() {
    postInitGame().then(response => {
      this.context = response.data;
      this.setDefaultAction();
    });
  };

  fetchFormerContext(id) {
    getFormerContext(id).then(response => {
      this.context = response.data;
    }).catch(err => {
      message.error('no more', 5);
    });
  };

  fetchLatterContext(id) {
    getLatterContext(id).then(response => {
      this.context = response.data;
    }).catch(err => {
      message.error('no more', 5);
    });
  };

  chooseGod = (godName) => {
    getChooseGod(this.context.activePlayerId || 1, godName).then(response => {
      this.context = response.data;
      this.setDefaultAction();
    }).catch(err => {
      message.error('failed to choose god', 5);
    })
  }

  initWorker = (x, y) => {
    getInitWorker(this.context.activePlayerId || 1, x, y).then(response => {
      this.context = response.data;
      this.setDefaultAction();
    }).catch(err => {
      message.error('fail to init worker', 5);
    })
  }

  moveWorker = (x, y) => {
    getMoveWorker(this.currentWorkerId || 1, x, y).then(response => {
      this.context = response.data;
      this.setDefaultAction();
    }).catch(err => {
      message.error('fail to move', 5);
    })
  }

  build = (x, y) => {
    getBuild(this.currentWorkerId || 1, x, y, false).then(response => {
      this.context = response.data;
      this.setDefaultAction();
    }).catch(err => {
      message.error('fail to build', 5);
    })
  }

  buildDome = (x, y) => {
    getBuild(this.currentWorkerId || 1, x, y, true).then(response => {
      this.context = response.data;
      this.setDefaultAction();
    }).catch(err => {
      message.error('fail to build dome', 5);
    })
  }

  pass = () => {
    getPass().then(response => {
      this.context = response.data;
      this.setDefaultAction();
    }).catch(err => {
      message.error('fail to pass', 5);
    })
  }
}

export default new ContextStore();
