import axios from 'axios';

const api = axios.create({
  baseURL: '/',
  headers: {
    'Content-Type': 'application/json',
  },
});


api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token && token !== 'null' && token !== 'undefined') {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      const ignoredPaths = [
        '/ranking', 
      ];
      const requestUrl = error.config?.url ?? '';
      const shouldRedirect = !ignoredPaths.some(path => requestUrl.startsWith(path));

      if (shouldRedirect) {
        localStorage.clear();
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
