import axios from 'axios';

axios.defaults.baseURL = "http://localhost:8080/"
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=utf-8';
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

export function getGods() {
  return axios.get('/god/');
}

export function postInitGame() {
  return axios.post('/game/init/');
}

export function getChooseGod(playerId, godName) {
  return axios.get(`/game/choose_god/?playerId=${playerId}&godName=${godName}`);
}

export function getInitWorker(playerId, x, y) {
  return axios.get(`/game/init_worker/?playerId=${playerId}&x=${x}&y=${y}`);
}

export function getMoveWorker(workerId, x, y) {
  return axios.get(`/game/move/?workerId=${workerId}&x=${x}&y=${y}`);
}

export function getBuild(workerId, x, y, buildDome) {
  return axios.get(`/game/build/?workerId=${workerId}&x=${x}&y=${y}&buildDome=${buildDome}`);
}

export function getPass() {
  return axios.get(`/game/pass/`);
}

export function getFormerContext(id) {
  return axios.get(`/context/former/${id}/`);
}
export function getLatterContext(id) {
  return axios.get(`/context/latter/${id}/`);
}