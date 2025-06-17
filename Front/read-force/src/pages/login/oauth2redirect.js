// ✅ 공통 레이아웃 .page-container 반영됨
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Oauth2RedirectHandler() {
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');
    const isNew = params.get('isNew'); // 백엔드에서 신규 회원 여부 전달

    if (!token) {
      alert('로그인에 실패했습니다.');
      navigate('/login');
      return;
    }

    if (isNew === 'true') {
      // 신규 회원: 토큰만 전달하고 페이지 전환
      navigate(`/social-sign-up?token=${token}`);
    } else {
      // 기존 회원: 바로 로그인 처리
      localStorage.setItem('token', token);
      navigate('/');
    }
  }, [navigate]);

  return (
    <div className="page-container">
      <p>로그인 처리 중입니다...</p>
    </div>
  );
}
