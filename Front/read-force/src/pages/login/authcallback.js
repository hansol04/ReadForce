import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Authcallback() {
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('ACCESS_TOKEN');

    console.log('[✅ 콜백 URL 전체]', window.location.href);
    console.log('[✅ ACCESS_TOKEN]', token);

    if (token) {
      localStorage.setItem('token', token);
      window.location.href = '/';  // ✅ 새로고침 포함되어 확실하게 동작
      return;
    }

    console.error('[❌ 로그인 실패 이유] ACCESS_TOKEN이 없습니다.');
    alert('로그인에 실패했습니다.');
    navigate('/login');
  }, [navigate]);

  return <p>로그인 처리 중입니다...</p>;
}
