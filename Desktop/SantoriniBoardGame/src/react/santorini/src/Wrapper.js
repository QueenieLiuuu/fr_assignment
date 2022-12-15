import React from 'react';
import './Wrapper.css';
import Layout, {Content, Header} from "antd/es/layout/layout";
import Sider from "antd/es/layout/Sider";
import Game from "./containers/Game";
import Info from "./containers/Info";
import GameHeader from "./containers/GameHeader";

export default class Wrapper extends React.Component {
  render() {
    return (
      <Layout className="santorini-wrapper" >
        <Header className="santorini-wrapper-header"><GameHeader/></Header>
        <Layout className="santorini-wrapper-inner">
          <Content><Game/></Content>
          <Sider theme="light"><Info/></Sider>
        </Layout>
      </Layout>
    );
  }
}