import React from 'react';
import {observer} from "mobx-react";
import contextStore from '../stores/ContextStore';
import './GameHeader.css'
import {Button, Row, Col, Popconfirm} from "antd";

export default observer(class GameHeader extends React.Component {
    componentDidMount() {
    }

    render() {
      return (
        <div className="santorini-game-header">
          <Row>
            <Col span={2}>
              <Popconfirm title={"Sure?"} onConfirm={() => contextStore.init()} okText="Yes"
                          cancelText="No">
                <Button className="santorini-game-header-restart" type="danger">Restart</Button>
              </Popconfirm>
            </Col>
            <Col span={22}>
              <p className="santorini-game-header-title">Santorini</p>
            </Col>
          </Row>
        </div>
      );
    }
  }
)