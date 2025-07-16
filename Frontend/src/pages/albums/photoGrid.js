import React, { useEffect, useState } from 'react';
import {
  Grid, Card, CardMedia, CardContent, Typography, Tooltip, Modal, Box, Button
} from '@mui/material';
import { useLocation } from 'react-router-dom';
import {
  fetchGetDataWithAuth,
  fetchGetDataWithAuthArrayBuffer,
  fetchDeleteDataWithAuth,
  fetchGetBlobDataWithAuth
} from 'client/client';

const PhotoGrid = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const album_id = queryParams.get('id');

  const [photos, setPhotos] = useState({});
  const [albumInfo, setAlbumInfo] = useState({});

  // Modal state
  const [open, setOpen] = useState(false);
  const [photoContent, setPhotoContent] = useState(null);
  const [photoDesc, setPhotoDesc] = useState('');
  const [downloadLink, setDownloadLink] = useState(null);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleView = (download_link, description) => {
    fetchGetDataWithAuthArrayBuffer(download_link).then((response) => {
      const binary = String.fromCharCode(...new Uint8Array(response.data));
      const buffer = btoa(binary);
      setPhotoContent(buffer);
      setPhotoDesc(description);
      setDownloadLink(download_link);
      handleOpen();
    });
  };

  const handleDownload = (download_link) => {
    fetchGetBlobDataWithAuth(download_link)
      .then((response) => {
        const disposition = response.headers['content-disposition'];
        const match = /filename="(.+)"/.exec(disposition);
        const filename = match ? match[1] : 'downloadedFile';

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', filename);

        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);

        window.URL.revokeObjectURL(url);
      })
      .catch((error) => {
        console.error('Error downloading photo:', error);
      });
  };

  const handleDelete = (photo_id) => {
    const isConfirmed = window.confirm('Are you sure you want to delete the photo?');
    if (isConfirmed) {
      fetchDeleteDataWithAuth(`/albums/${album_id}/photos/${photo_id}/delete`)
        .then((res) => {
          console.log('Item deleted!', res);
          window.location.reload();
        })
        .catch((err) => {
          console.error('Failed to delete photo:', err);
        });
    }
  };

  useEffect(() => {
    if (!album_id) return;
    const fetchPhotos = async () => {
      try {
        const res = await fetchGetDataWithAuth('/albums/' + album_id);
        const photosList = res.data.photos;
        setAlbumInfo(res.data);

        photosList.forEach(async (photo) => {
          const response = await fetchGetDataWithAuthArrayBuffer(photo.download_link);
          const binary = String.fromCharCode(...new Uint8Array(response.data));
          const buffer = btoa(binary);

          const temp = {
            album_id,
            photo_id: photo.id,
            name: photo.name,
            description: photo.description,
            content: buffer,
            download_link: photo.download_link
          };

          const albumPhotoID = `album_${album_id}_photo_${photo.id}`;
          setPhotos((prevPhotos) => ({
            ...prevPhotos,
            [albumPhotoID]: temp
          }));
        });
      } catch (err) {
        console.error('Error fetching album photos:', err);
      }
    };

    fetchPhotos();
  }, [album_id]);

  return (
    <div>
      <Typography variant="h5" gutterBottom>{albumInfo.name}</Typography>
      <Typography variant="subtitle1" gutterBottom>{albumInfo.description}</Typography>

      <Grid container spacing={2}>
        {Object.keys(photos).map((key) => (
          <Grid item key={key} xs={8} sm={4} md={4} lg={2}>
            <Card>
              <Tooltip title={photos[key].description || ''}>
                <CardMedia
                  component="img"
                  height="200"
                  image={`data:image/jpeg;base64,${photos[key].content}`}
                  alt={photos[key].name}
                />
              </Tooltip>
              <CardContent>
                <Tooltip title={photos[key].description || ''}>
                  <Typography variant="subtitle1">{photos[key].name}</Typography>
                </Tooltip>

                <a href="#" onClick={() => handleView(photos[key].download_link, photos[key].description)}>View | </a>
                <a href={`/photo/edit?album_id=${album_id}&photo_id=${photos[key].photo_id}&photo_name=${encodeURIComponent(photos[key].name)}&photo_desc=${encodeURIComponent(photos[key].description)}`}>Edit | </a>
                <a href="#" onClick={() => handleDownload(photos[key].download_link)}>Download | </a>
                <a href="#" onClick={() => handleDelete(photos[key].photo_id)}>Delete</a>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Modal */}
      <Modal open={open} onClose={handleClose}>
        <Box
          sx={{
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            bgcolor: 'background.paper',
            boxShadow: 24,
            p: 4,
            maxWidth: 600,
            textAlign: 'center'
          }}
        >
          <Typography variant="h6" gutterBottom>{photoDesc}</Typography>
          {photoContent && (
            <img
              src={`data:image/jpeg;base64,${photoContent}`}
              alt="View"
              style={{ maxWidth: '100%', height: 'auto', marginBottom: '16px' }}
            />
          )}
          <div>
            <Button variant="contained" onClick={() => handleDownload(downloadLink)} sx={{ mr: 2 }}>
              Download
            </Button>
            <Button variant="outlined" onClick={handleClose}>Close</Button>
          </div>
        </Box>
      </Modal>
    </div>
  );
};

export default PhotoGrid;
