import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Authcallback() {
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const temporalToken = urlParams.get('TEMPORAL_TOKEN');
    //const messageCode = urlParams.get('MESSAGE_CODE');
    const nickname = urlParams.get('NICK_NAME');
    const provider = urlParams.get('PROVIDER');
  
    const handleNewUser = (temporalToken) => {
      if (temporalToken) {
        localStorage.setItem('temporal_token', temporalToken);
        window.location.replace('/signup/social');
      } else {
        console.error('[❌ 신규 회원 처리 실패] 파라미터 누락');
        alert('회원가입에 실패했습니다.');
        navigate('/login');
      }
    };
  
    if (!temporalToken) {
      console.error('[❌ 파라미터 없음]');
      alert('로그인에 실패했습니다.');
      navigate('/login');
      return;
    }
  
    if (nickname && provider) {
      // ✅ 신규 회원
      handleNewUser(temporalToken);
      return;
    }
  
    // ✅ 기존 회원 → 반드시 get-tokens 호출
    fetch('http://localhost:3000/auth/get-tokens', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({ temporal_token: temporalToken }),
    })
      .then(async (res) => {
        const data = await res.json();
        const { ACCESS_TOKEN, REFRESH_TOKEN, NICK_NAME, PROVIDER } = data;
  
        if (ACCESS_TOKEN && REFRESH_TOKEN && NICK_NAME && PROVIDER !== undefined) {
          localStorage.setItem('token', ACCESS_TOKEN);
          localStorage.setItem('refresh_token', REFRESH_TOKEN);
          localStorage.setItem('nickname', NICK_NAME);
          localStorage.setItem('provider', PROVIDER);
          window.location.replace('/');
        } else {
          console.warn('[ℹ️ 기존 회원 아님] → 소셜 가입 이동');
          handleNewUser(temporalToken);
        }
      })
      .catch((err) => {
        console.error('[❌ 토큰 요청 실패]', err);
        handleNewUser(temporalToken);
      });
  }, [navigate]);
  

  return (
    <div className="page-container">
      <p>로그인 처리 중입니다...</p>
    </div>
  );
}