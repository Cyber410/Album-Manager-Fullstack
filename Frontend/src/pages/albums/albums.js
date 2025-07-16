import React, { useState, useEffect } from 'react';
import { Grid, Card, CardContent } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { fetchGetDataWithAuth } from 'client/client';
import { Link } from '../../../node_modules/react-router-dom/dist/index';

const brightPopColors = [
  '#FF3E4D', '#FF5635', '#FFAB00', '#36837E', '#008809', '#0052CC', '#253858', '#006781',
  '#004B8D', '#9C2780', '#E91E63', '#673A87', '#3F5185', '#2196F3', '#03A9F4', '#00BCD4',
  '#009688', '#4CAF50', '#8BC34A', '#CDDC39', '#FFEB3B', '#FFC107', '#FF9800', '#FF5722',
  '#795548', '#9E9E9E', '#607D8B', '#F44336', '#E57373', '#FFCDD2', '#64B5F6', '#4FC3F7',
  '#BBDEFB', '#81C784', '#C8E6C9', '#DCE775', '#FFF176', '#FFD54F', '#FFB74D', '#FF8A65',
  '#A1887F', '#90A4AE', '#FF4081', '#FF80AB', '#F50057', '#651FFF', '#305AFE',
  '#2979FF', '#0080FF', '#00E5FF'
];

const getRandomColor = () => {
  const randomIndex = Math.floor(Math.random() * brightPopColors.length);
  return brightPopColors[randomIndex];
};

const DynamicGridPage = () => {
  const [dataArray, setDataArray] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const isLoggedIn = localStorage.getItem('token');
    if (!isLoggedIn) {
      navigate('/login');
      window.location.reload();
    }

    fetchGetDataWithAuth('/albums')
      .then(res => {
        setDataArray(res.data);
      })
      .catch(err => {
        console.error("Error fetching album data:", err);
      });
  }, []);

  return (
    <Grid container spacing={2}>
      {dataArray.map((data, index) => (
        <Grid item key={index} xs={12} sm={6} md={4} lg={3}>
          <Link to={`/album/show?id=${data.id}`}>
          <Card
            sx={{
              backgroundColor: getRandomColor(),
              textAlign: 'center',
              p: 3,
              borderRadius: 2,
              height: '250px',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
            }}
          >
            <CardContent>
              <h1 style={{ fontSize: '2rem', margin: 0, color: 'white' }}>{data.name}</h1>
            </CardContent>
          </Card>
          </Link>
        </Grid>
      ))}
    </Grid>
  );
};

export default DynamicGridPage;
