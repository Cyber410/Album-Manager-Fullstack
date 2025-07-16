import React from 'react';
import Header from './header'; 
import PhotoGrid from './photoGrid';


const Albums = () => {
  return (
    <div>
      <Header />
      <div style={{ marginTop: '20px', padding: '20px' }}>
        <PhotoGrid />
      </div>
    </div>
  );
};

export default Albums;
