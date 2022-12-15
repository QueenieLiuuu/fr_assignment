import {action, makeObservable, observable} from "mobx";
import {getGods} from "../api";

class GodStore {
  allGods = []

  constructor() {
    makeObservable(this, {
      allGods: observable,
      listGods: action,
    })
  }

  listGods = () => {
    getGods().then(response => {
      this.allGods = response.data;
    });
  };

}

export default new GodStore();