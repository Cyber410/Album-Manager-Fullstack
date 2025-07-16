import React, { useState } from 'react';
import Header from './header';
import {
  Box,
  Button,
  Container,
  Grid,
  Paper,
  Typography,
  IconButton,
  CircularProgress
} from '@mui/material';
import { AddCircleOutline, Close } from '@mui/icons-material';
import { useDropzone } from 'react-dropzone';
import { useLocation, useNavigate } from 'react-router-dom';
import { fetchPostFileUploadWithAuth } from 'client/client';

const FileUploadPage = () => {
  const [files, setFiles] = useState([]);
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const id = queryParams.get('id');
  const navigate = useNavigate();
  const [processing, setProcessing] = useState(false);

  const onDrop = (acceptedFiles) => {
    setFiles((prevFiles) => [...prevFiles, ...acceptedFiles]);
  };

  const { getRootProps, getInputProps } = useDropzone({ onDrop });

  const removeFile = (index) => {
    setFiles((prevFiles) => prevFiles.filter((_, i) => i !== index));
  };

  const handleUpload = async () => {
    try {
      setProcessing(true);
      const formData = new FormData();
      files.forEach((file) => {
        formData.append('files', file);
      });

      fetchPostFileUploadWithAuth('/albums/' + id + '/upload-photos', formData).then((res) => {
        console.log(res.data);
        navigate('/album/show?id=' + id);
      });
    } catch (error) {
      console.error('Upload failed:', error);
    }
  };

  return (
    <div>
      <Header />
      <Container maxWidth="md" sx={{ mt: 4, p: 2 }}>
        <Paper
          elevation={3}
          {...getRootProps()}
          sx={{
            border: '2px dashed',
            borderColor: 'primary.main',
            borderRadius: 2,
            p: 4,
            textAlign: 'center',
            cursor: 'pointer',
          }}
        >
          <input {...getInputProps()} />
          <AddCircleOutline fontSize="large" />
          <Typography variant="h6">Drag & drop files here, or click to select</Typography>
        </Paper>

        <Box mt={2}>
          {files.map((file, index) => (
            <Paper
              key={index}
              elevation={3}
              sx={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                p: 2,
                mt: 2,
                border: 1,
                borderColor: 'secondary.main',
                borderRadius: 1,
              }}
            >
              <Typography>{file.name}</Typography>
              <IconButton onClick={() => removeFile(index)} color="secondary">
                <Close />
              </IconButton>
            </Paper>
          ))}
        </Box>

        <Grid container spacing={2} mt={3}>
          <Grid item xs={12}>
            {processing ? (
              <Box textAlign="center">
                <CircularProgress />
                <Typography variant="body2" color="textSecondary" mt={1}>
                  Uploading...
                </Typography>
              </Box>
            ) : (
              <Button
                variant="contained"
                color="primary"
                onClick={handleUpload}
                disabled={files.length === 0}
              >
                Upload Photos
              </Button>
            )}
          </Grid>
        </Grid>
      </Container>
    </div>
  );
};

export default FileUploadPage;
