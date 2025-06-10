import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './login.css';

export default function Login() {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch('/member/sign-in', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: id, password : password}),
      });
      const data = await response.json(); 
      const MESSAGE_CODE = data.MESSAGE_CODE;
      console.log(MESSAGE_CODE)

      if (response.ok) {
        const token = data.TOKEN; 
        // const messageCode = data.MESSAGE_CODE;

        localStorage.setItem('token', token);

        navigate('/');
      } else {
        setError(data.message || '로그인에 실패했습니다.');
      }
    } catch (err) {
      console.error('로그인 요청 실패:', err);
      setError('서버 오류가 발생했습니다.');
    }
  };

  const kakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${process.env.REACT_APP_KAKAO_REST_API_KEY}&redirect_uri=${process.env.REACT_APP_KAKAO_REDIRECT_URI}&response_type=code`;
  const naverURL = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${process.env.REACT_APP_NAVER_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_NAVER_REDIRECT_URI}&state=naver_login`;
  const googleURL = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${process.env.REACT_APP_GOOGLE_CLIENT_ID}&redirect_uri=${process.env.REACT_APP_GOOGLE_REDIRECT_URI}&response_type=code&scope=email profile openid&access_type=offline`;

  return (
    <div>
      <div className="login-wrapper">
        <h2 className="login-title">로그인</h2>
        <form className="login-form" onSubmit={handleLogin}>
          <div className="form-group">
            <label htmlFor="userId">ID</label>
            <input
              id="userId"
              type="text"
              value={id}
              onChange={(e) => setId(e.target.value)}
              placeholder="이메일"
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="userPw">PW</label>
            <input
              id="userPw"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="비밀번호"
              required
            />
          </div>

          <div className="login-links">
            <span onClick={() => navigate('/findpassword')}>비밀번호 재설정</span>
          </div>

          <div className="social-login">
            <button type="button" className="kakao" aria-label="카카오 로그인" onClick={() => window.location.href = kakaoURL} />
            <button type="button" className="naver" aria-label="네이버 로그인" onClick={() => window.location.href = naverURL} />
            <button type="button" className="google" aria-label="구글 로그인" onClick={() => window.location.href = googleURL} />
          </div>

          {error && <p className="error-message">{error}</p>}

          <button type="submit" className="login-btn">로그인</button>
        </form>
      </div>
    </div>
  );
}
