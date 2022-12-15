import React from 'react';
import godStore from "../stores/GodStore";
import contextStore from "../stores/ContextStore";
import God from "../components/God";
import {observer} from "mobx-react";
import './GodSelectionPanel.css'

export default observer(class GodSelectionPanel extends React.Component {
    componentDidMount() {
      godStore.listGods();
    }

    render() {
      return (
        <div className="santorini-god-selection-panel">
          <div className="santorini-god-selection-grid">
          {godStore.allGods ? godStore.allGods.slice().map((god) => (
            <God
              key={god.name}
              name={god.name}
              title={god.title}
              powerDetail={god.powerDetail}
              url={god.url}
              handleChooseGod={contextStore.chooseGod}
            />
          )) : null}
          </div>
        </div>
      );
    }
  }
)