import React from 'react';
import contextStore from "../stores/ContextStore";
import {observer} from "mobx-react";
import './Game.css';
import GodSelectionPanel from "./GodSelectionPanel";
import BoardContainer from "./BoardContainer";

export default observer(
  class Game extends React.Component {
    componentDidMount() {
      contextStore.init();
    }

    corePanel = () => {
      if (!(contextStore.player2HasGod && contextStore.player2HasGod)) {
        return (<GodSelectionPanel/>);
      } else {
        return (<BoardContainer/>);
      }
    }

    render() {
      return (
        <div className="santorini-game">
          {contextStore.context && this.corePanel()}
        </div>
      );
    }
  }
)