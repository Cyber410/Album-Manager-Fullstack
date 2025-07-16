import React, { useState } from "react";
import { Button, Container, TextField } from "../../../../node_modules/@mui/material/index";
import { fetchPostData } from "client/client";
import { useNavigate } from "../../../../node_modules/react-router-dom/dist/index";
import { useEffect } from "react";


// material-ui


// ============================|| FIREBASE - LOGIN ||============================ //

const AuthLogin = () => {
  
  const [email,setEmail]=useState("");
  const [password,setPassword]=useState("");
  const [errors,setErrors]=useState({email:'', password:''});
  const [loginError,setLoginError]=useState('');
  const navigate=useNavigate();

  useEffect(()=>{
    const isLoggedIn=localStorage.getItem('token');
    if(isLoggedIn){
      navigate('/');
      window.location.reload()
    }
  },[])

    const validateEmail = (email) => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailRegex.test(email);
    };

    const validatePassword = (password) => {
      return password.length>=6 && password.length<=15;
    };

    const handleLogin = async() => {
     
      setErrors({email:'', password:''});

      if (!validateEmail(email)) {
        setErrors((prevErrors)=>({...prevErrors,email:"Invalid Email Format"}));
        return;
      }

      if (!validatePassword(password)) {
        setErrors((prevErrors)=>({...prevErrors,password:"Password length must be between 6 and 15"}));
        return;
      }

     fetchPostData("/auth/token",{email,password})
     .then((response)=>{
      const {token}= response.data;
      setLoginError('');
      localStorage.setItem('token',token);
      console.log(token);
      console.log(response);
      navigate('/');
      window.location.reload()
     }).catch((error)=>{
      console.log("Error:", error );
      setLoginError('Error Occured while Login');
     })

    };


  return(

    <Container component ="main" maxWidth="xs">

      <TextField
      variant="outlined"
      margin="normal"
      fullWidth
      label="email"
      value={email}
      onChange={(e)=>setEmail(e.target.value)}
      error={!!errors.email}
      helperText={errors.email}
      />

      <TextField
      variant="outlined"
      margin="normal"
      fullWidth
      label="password"
      type="password"
      value={password}
      onChange={(e)=>setPassword(e.target.value)}
      error={!!errors.password}
      helperText={errors.password}
      />

      <Button variant="contained" color="primary" fullWidth onClick={handleLogin}>Login</Button>
      {loginError && <p style={{color:'red'}}>{loginError}</p>}
    </Container>

  );

};

export default AuthLogin;
