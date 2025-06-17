import axios from 'axios';

const api = axios.create({
  baseURL: '/', // 필요 시 /api 등으로 변경
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 전: 토큰 자동 삽입
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// 응답 후: 401 발생 시 → 로그인 페이지로 이동
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.clear();
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
