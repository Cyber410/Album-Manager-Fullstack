import axios from 'axios';
const API_VERSION = "/api/v1";
// GET request function
const fetchGetData = (uri) => {
    const url = `${API_VERSION}${uri}`;
  return axios.get(url)
    .catch(error => {
      // Handle exceptions/errors
      console.error('Error fetching data for URL:', url, 'Error:', error.message);
      throw error; // rethrow if you want to handle it elsewhere
    });
};

// POST request function
const fetchPostData = (uri, payload) => {
  const url = `${API_VERSION}${uri}`;

  return axios.post(url, payload)
    .catch(error => {
      // Handle exceptions/errors
      console.error('Error posting data to URL:', url, 'Error:', error.message);
      throw error;
    });
};

const fetchPostDataWithAuth = async (uri, payload) => {
  const url = `${API_VERSION}${uri}`;
  try {
    const token = localStorage.getItem('token'); // Get token from localStorage

    return await axios.post(url, payload, {
      headers: {
        Accept: '*/*',
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error('Error fetching data for URL:', url, 'Error:', error.message);
    throw error; // Optional: throw it again to handle elsewhere
  }
};

const fetchPutDataWithAuth = async (uri, payload) => {
  const url = `${API_VERSION}${uri}`;
  try {
    const token = localStorage.getItem('token'); // Get token from localStorage

    return await axios.put(url, payload, {
      headers: {
        Accept: '*/*',
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });
  } catch (error) {
    console.error('Error fetching data for URL:', url, 'Error:', error.message);
    throw error; // Optional: throw it again to handle elsewhere
  }
};

const fetchGetDataWithAuth = async (uri) => {
  const url = `${API_VERSION}${uri}`;
  try {
    const token = localStorage.getItem('token'); // Get token from localStorage

   const response= await axios.get(url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response;
  } catch (error) {
    console.error('Error fetching data for URL:', url, 'Error:', error.message);
    throw error; 
  }
};
const fetchDeleteDataWithAuth = async (uri) => {
  const token = localStorage.getItem('token');
  const url = `${API_VERSION}${uri}`;

  try {
    const response = await axios.delete(url, {
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });
    return response;
  } catch (error) {
    console.error('Error deleting data:', error);
    throw error;
  }
};

const fetchPostFileUploadWithAuth = async (uri,formData) => {
  const url = `${API_VERSION}${uri}`;
  try {
    const token = localStorage.getItem('token'); // Get token from localStorage

    const response = await axios.post(
        url,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${token}`,
          },
        }
      );
    return response;
  } catch (error) {
    console.error('Error fetching data for URL:', url, 'Error:', error.message);
    throw error; 
  }
};
const fetchGetBlobDataWithAuth = async (uri) => {
  const token = localStorage.getItem('token');
  const url = `${API_VERSION}${uri}`;

  try {
    const response = await axios.get(url, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      responseType: 'blob', // Important for downloading files
    });
    return response;
  } catch (error) {
    console.error('Error fetching blob data:', error);
    throw error;
  }
};


const fetchGetDataWithAuthArrayBuffer = async (uri) => {
  const token = localStorage.getItem('token');
  const url = `${API_VERSION}${uri}`;

  try {
    const response = await axios.get(url, {
      headers: {
        Authorization: `Bearer ${token}`
      },
      responseType: 'arraybuffer'
    });

    return response;
  } catch (error) {
    console.error('Error fetching data:', error);
    throw error; 
  }
};

export { fetchPostData,fetchPostDataWithAuth,fetchGetDataWithAuth,fetchPostFileUploadWithAuth,fetchGetDataWithAuthArrayBuffer,fetchPutDataWithAuth,fetchDeleteDataWithAuth,fetchGetBlobDataWithAuth };
export default fetchGetData;
