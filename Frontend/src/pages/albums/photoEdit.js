import React, { useState } from "react";
import { Button,TextField } from "../../../node_modules/@mui/material/index";
import { fetchPutDataWithAuth } from "client/client";
import { useNavigate } from "../../../node_modules/react-router-dom/dist/index"; 
import { useEffect } from "react";
import { useLocation } from "../../../node_modules/react-router-dom/dist/index";


// material-ui


// ============================|| FIREBASE - LOGIN ||============================ //

const EditPhoto = () => {

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const album_id = queryParams.get('album_id');
    const photo_id = queryParams.get('photo_id');
    const photo_name = queryParams.get('photo_name');
    let photo_des = queryParams.get('photo_des');
  
  const [formData,setFormData]=useState({name:'',description:''});
  const [errors,setErrors]=useState({name:'',description:''});
  const navigate= useNavigate();
  useEffect(()=>{
    const isLoggedIn=localStorage.getItem('token');
    if(!isLoggedIn){
      navigate('/login');
      window.location.reload()
    }

    if(photo_des=='null') photo_des="";

     setFormData((prevData)=>({
        ...prevData,
        name:photo_name,
        des:photo_des
    }))
  },[navigate])

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

      fetchPutDataWithAuth("/albums/"+album_id+"/photos/"+photo_id+"/update", payload)
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

      <Button type='submit'variant='contained' color='primary'>Edit Photo</Button>
    </form>

  );

};

export default EditPhoto;
