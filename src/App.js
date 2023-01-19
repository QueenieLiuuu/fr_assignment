import { useState, useEffect } from "react";
import "./App.css";
import { getData, postForm} from "./FormAPI";
import FormInput from "./components/FormInput";
import axios from 'axios';

const App = () => {
  const [values, setValues] = useState({
    username: "",
    email: "",
    password: "",
  });
  const initialValue = {occupation: "", state: ""};
  const [formValues, setFormValue] = useState(initialValue);
  const [formErrors, setFormErros] = useState({});
  const [occupations, setOccupcations] = useState();
  const [states, setStates] = useState();
  const [chooseOccupations, setChooseOccupations] = useState();
  const [chooseStates, setChooseState] = useState();
  const [validate, setValidate] = useState(false)


  const inputs = [
    {
      id: 1,
      name: "name",
      type: "text",
      placeholder: "Username",
      errorMessage:
        "Username should be 3-16 characters and shouldn't include any special character!",
      label: "Username",
      pattern: "^[A-Za-z0-9]{3,16}$",
      required: true,
    },
    {
      id: 2,
      name: "email",
      type: "email",
      placeholder: "Email",
      errorMessage: "It should be a valid email address!",
      label: "Email",
      required: true,
    },
    {
      id: 3,
      name: "password",
      type: "password",
      placeholder: "Password",
      errorMessage:
        "Password should be 8-20 characters and include at least 1 letter, 1 number and 1 special character!",
      label: "Password",
      pattern: `^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$`,
      required: true,
    },
  ];

  const validation = (values) => {
    console.log('validation')
    console.log(values.occupation)
    const errors = {};
    if (!values.occupation) {
      errors.occupation = "Occupation is required!";
    }
    if (!values.state) {
      errors.state = "State is required!";
    }
    else {
      setValidate(true)
    }
    return errors;
  };

  const handleOccupcations = (e) => {
    setChooseOccupations(e.target.value)
    const { name, value } = e.target;
    console.log(name, value)
    setFormValue({...formValues, [name]: value});
    console.log(formValues)
 }

  const handleStates = (e) => {
    setChooseState(e.target.value)
    const { name, value } = e.target;
    console.log(name, value)
    setFormValue({...formValues, [name]: value});
    console.log(formValues)
  }


  const handleSubmit = (e) => {
    console.log('click')
    e.preventDefault();
    setFormErros(validation(formValues))
    if (validate === true){
      axios
      .post('https://frontend-take-home.fetchrewards.com/form', formValues)
      .then(response => {
        if (response.status === 201){
          alert("submit successful")
          setFormValue(initialValue)
          setValues({
            username: "",
            email: "",
            password: ""})
        } else {
          alert("error, please try again")
        }
        console.log(response)})
      .catch(error => {
        console.log(error)
      })}
    }

  const onChange = (e) => {
    setValues({ ...values, [e.target.name]: e.target.value });
    console.log(e.target);
    const { name, value } = e.target;
    console.log(name, value)
    setFormValue({...formValues, [name]: value});
    console.log(formValues)
  };

  useEffect(() => {
    getData()
      .then((data) => {
        setStates(data.states)
        console.log(data.states)
        setOccupcations(data.occupations)
        console.log(data.occupations)
      })
  }, [])


  return (
    <div className="app">
      <div className="form">
        <h1>Register</h1>
        <form onSubmit={handleSubmit}>
          {inputs.map((input) => (
            <FormInput
              key={input.id}
              {...input}
              value={values[input.name]}
              onChange={onChange}
            />
          ))}
          <div className="formInput">
            <label className='label'>Occupation</label>
            <select 
                className="select"
                name="occupation"
                value={ formValues.occupation } 
                onChange={handleOccupcations}
                >
                {occupations && occupations.map((occupation) => (
                  <option value={occupation} key={occupation}>
                    {occupation}
                  </option>
                ))}
              </select>
          </div>
          <error>{ formErrors.occupation }</error>

          <div className="formInput">
            <label className='label'>States</label>
            <select 
                className="select"
                name="state" 
                value={ formValues.state } 
                onChange={handleStates}>
                {states && states.map((state) => (
                  <option value={state.name} key={state.name}>
                    {state.abbreviation}
                  </option>
                ))}
              </select>
          </div>
          <error>{ formErrors.state }</error>
          <button>Submit</button>
        </form>
      </div>
      
    </div>
  );
};

export default App;