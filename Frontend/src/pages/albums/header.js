import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Button, AppBar, Toolbar, Typography } from '@mui/material';
import { fetchDeleteDataWithAuth } from 'client/client';

const Header = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const id = queryParams.get('id');

  const handleDelete = () => {
  const isConfirmed = window.confirm('Are you sure you want to delete the Album?');

  if (isConfirmed) {
    fetchDeleteDataWithAuth(`/albums/${id}/delete`)
      .then(res => {
        console.log('Item deleted!', res);
        window.location.href='/'; 
      })
      .catch(err => {
        console.error('Failed to delete photo:', err);
      });
  } else {
    console.log('Delete operation canceled');
  }
};

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Photo Gallery
        </Typography>

        <Button
          component={Link}
          to={`/album/edit?id=${id}`}
          color="inherit"
          variant="contained"
          sx={{
            mr: 2,
            backgroundColor: '#5799ed',
            '&:hover': { backgroundColor: '#2f6ade' },
          }}
        >
          Edit Album
        </Button>

        <Button
          component={Link}
          to={`/album/upload?id=${id}`}
          color="inherit"
          variant="contained"
          sx={{
            mr: 2,
            backgroundColor: '#4CAF50',
            '&:hover': { backgroundColor: '#388E3C' },
          }}
        >
          Upload Photos
        </Button>

        <Button
          onClick= {handleDelete}
          color="inherit"
          variant="contained"
          sx={{
            backgroundColor: '#F44336',
            '&:hover': { backgroundColor: '#D32F2F' },
          }}
        >
          Delete Album
        </Button>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
