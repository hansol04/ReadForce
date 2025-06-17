// import { useEffect } from 'react';
// import { useNavigate } from 'react-router-dom';

// export default function Authcallback() {
//   const navigate = useNavigate();

//   useEffect(() => {
//     const urlParams = new URLSearchParams(window.location.search);
//     const token = urlParams.get('ACCESS_TOKEN');

//     console.log('[✅ 콜백 URL 전체]', window.location.href);
//     console.log('[✅ ACCESS_TOKEN]', token);

//     if (token) {
//       localStorage.setItem('token', token);
//       window.location.href = '/';  // ✅ 새로고침 포함되어 확실하게 동작
//       return;
//     }

//     console.error('[❌ 로그인 실패 이유] ACCESS_TOKEN이 없습니다.');
//     alert('로그인에 실패했습니다.');
//     navigate('/login');
//   }, [navigate]);

//   return <p>로그인 처리 중입니다...</p>;
// }

// ✅ 공통 레이아웃 .page-container 반영됨

import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Authcallback() {
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('ACCESS_TOKEN');
    const refreshToken = urlParams.get('REFRESH_TOKEN');     const nickname = urlParams.get('NICK_NAME');
    if (token && refreshToken && nickname) {
      localStorage.setItem('token', token);
      localStorage.setItem('refresh_token', refreshToken);
      localStorage.setItem('nickname', nickname); 

      window.location.replace('/');
      return;
    }

    console.error('[❌ 로그인 실패 이유] 필요한 파라미터가 없습니다.');
    alert('로그인에 실패했습니다.');
    navigate('/login');
  }, [navigate]);

  return (
    <div className="page-container">
      <p>로그인 처리 중입니다...</p>
    </div>
  );
}