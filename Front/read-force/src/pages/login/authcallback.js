import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Authcallback() {
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const nickname = urlParams.get('NICK_NAME');
    const temporalToken = urlParams.get('TEMPORAL_TOKEN');
    const provider = urlParams.get('PROVIDER');

    const handleNewUser = (temporalToken, nickname, provider) => {
      if (temporalToken && nickname && provider) {
        localStorage.setItem('temporal_token', temporalToken);
        localStorage.setItem('nickname', nickname);
        localStorage.setItem('provider', provider);
        window.location.replace('/signup/social');
      } else {
        console.error('[❌ 신규 회원 처리 실패] 파라미터 누락');
        alert('회원가입에 실패했습니다.');
        navigate('/login');
      }
    };
    if (temporalToken && nickname) {
      fetch('http://localhost:3000/auth/get-tokens', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({ temporal_token: temporalToken }),
      })
        .then(async res => {
          const data = await res.json();
          const { ACCESS_TOKEN, REFRESH_TOKEN } = data;

          if (ACCESS_TOKEN && REFRESH_TOKEN) {
            localStorage.setItem('token', ACCESS_TOKEN);
            localStorage.setItem('refresh_token', REFRESH_TOKEN);
            localStorage.setItem('nickname', nickname);
            if (provider) localStorage.setItem('provider', provider);
            window.location.replace('/');
          } else {
            console.warn('[ℹ️ 기존 회원 아님] → 소셜 가입 이동');
            handleNewUser(temporalToken, nickname, provider);
          }
        })
        .catch(err => {
          console.error('[❌ 토큰 요청 실패]', err);
          console.warn('[ℹ️ 기존 회원 처리 실패] → 소셜 가입 이동');
          handleNewUser(temporalToken, nickname, provider);
        });

      return;
    }

    console.error('[❌ 로그인 실패 이유] 필요한 파라미터가 없습니다.');
    alert('로그인에 실패했습니다.');
    navigate('/login');
  }, [navigate]);

  return <p>로그인 처리 중입니다...</p>;
}
