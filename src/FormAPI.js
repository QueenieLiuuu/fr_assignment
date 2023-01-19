import axios from 'axios';
const api = 'https://frontend-take-home.fetchrewards.com/form'

export const getData = () => 
    fetch(`${api}`)
      .then(res => {
        return res.json()
      })


export const postFormtest = (formData) =>
    fetch(`${api}`, {
      method: 'POST',
      body: formData
    })
        

export const postForm = (formData) =>
    axios
    .post('https://frontend-take-home.fetchrewards.com/form', formData)
    .then(response => {
        console.log(response)
    })
