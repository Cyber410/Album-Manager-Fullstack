import React, { useState } from "react";
import { Button,TextField } from "../../../node_modules/@mui/material/index";
import { fetchPostDataWithAuth } from "client/client";
import { useNavigate } from "../../../node_modules/react-router-dom/dist/index"; 
import { useEffect } from "react";


// material-ui


// ============================|| FIREBASE - LOGIN ||============================ //

const AddAlbum = () => {
  
  const [formData,setFormData]=useState({name:'',description:''});
  const [errors,setErrors]=useState({name:'',description:''});
  const navigate= useNavigate();
  useEffect(()=>{
    const isLoggedIn=localStorage.getItem('token');
    if(!isLoggedIn){
      navigate('/login');
      window.location.reload()
    }
  },[])

  const handelInputChange=(e)=>{

    const{name,value}=e.target;

    setFormData((prevData)=>({
        ...prevData,
        [name]:value,
    }))

  }

    const handleSubmit = async (e) => {
    e.preventDefault();

    // Validation
    let isValid = true;
    const newErrors = { name: '', description: '' };

    if (!formData.name.trim()) {
      newErrors.name = 'Name is required';
      isValid = false;
    }

    if (!formData.description.trim()) {
      newErrors.description = 'Description is required';
      isValid = false;
    }

    setErrors(newErrors);

    // If form is valid, you can proceed with further actions
    if (isValid) {
      const payload = {
        name: formData.name,
        description: formData.description,
      };

      fetchPostDataWithAuth("/albums/add", payload)
        .then((response) => {
          console.log(response);
        })
        .catch((error) => {
          console.error('Login error:', error);
        });

        navigate('/login');

      console.log('Form submitted:', payload);
    }
  };



  return(

  <form onSubmit={handleSubmit}>

      <TextField
      variant="outlined"
      margin="normal"
      fullWidth
      label="Name"
      name="name"
      value={formData.name}
      onChange={handelInputChange}
      error={!!errors.name}
      helperText={errors.name}
      />

      <TextField
      variant="outlined"
      margin="normal"
      fullWidth
      name="description"
      label="Description"
      value={formData.description}
      onChange={handelInputChange}
      error={!!errors.description}
      helperText={errors.description}
      rows={4}
      />

      <Button type='submit'variant='contained' color='primary'>Add Album</Button>
    </form>

  );

};

export default AddAlbum;
