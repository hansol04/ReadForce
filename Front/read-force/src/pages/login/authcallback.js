import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Authcallback() {
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

  
    if (token) {
      localStorage.setItem('token', token); // JWT 저장
      navigate('/');
    } else {
      alert('로그인에 실패했습니다.');
      navigate('/login');
    }
  }, [navigate]);

  return <p>로그인 처리 중입니다...</p>;
}
