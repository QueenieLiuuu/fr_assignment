import React from 'react';
import {observer} from "mobx-react";
import contextStore from '../stores/ContextStore';
import './Info.css'
import {Button} from "antd";

export default observer(class Info extends React.Component {
    UndoRedoBtns = () => (
      <div className="santorini-sider-info-undo-redo-btns">
        <Button type="primary"
                onClick={() => contextStore.fetchFormerContext(contextStore.context.id)}>Undo</Button>
        <Button type="primary"
                onClick={() => contextStore.fetchLatterContext(contextStore.context.id)}>Redo</Button>
      </div>
    )

    render() {
      return (
        <div className="santorini-sider-info">
          <div className="santorini-sider-info-god">
            {this.UndoRedoBtns()}
            {contextStore.player1HasGod && (<p>
              Player1: {contextStore.context.player1.god.name} - {contextStore.context.player1.god.powerDetail}
            </p>)}
            {contextStore.player2HasGod && (<p>
              Player2: {contextStore.context.player2.god.name} - {contextStore.context.player2.god.powerDetail}
            </p>)}
          </div>
          <div className="santorini-sider-info-general">
            {contextStore.info}
          </div>
        </div>
      );
    }
  }
)