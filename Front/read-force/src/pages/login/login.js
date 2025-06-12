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
        body: JSON.stringify({ email: id, password: password }),
      });
      const data = await response.json();
      const MESSAGE_CODE = data.MESSAGE_CODE;
      console.log(MESSAGE_CODE)

      console.log("로그인 응답 데이터", data); // 👈 여기에 nickname이 포함돼야 함

      if (response.ok) {
        const token = data.TOKEN;
        const nickname = data.nickname || data.NICKNAME;
        // const messageCode = data.MESSAGE_CODE;

        localStorage.setItem('token', token);
        localStorage.setItem('nickname', nickname);

        navigate('/');
      } else {
        setError(data.message || '로그인에 실패했습니다.');
      }
    } catch (err) {
      console.error('로그인 요청 실패:', err);
      setError('서버 오류가 발생했습니다.');
    }
  };

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
            <button type="button" className="kakao" aria-label="카카오 로그인" onClick={() => window.location.href = "http://localhost:8080/oauth2/authorization/kakao"} />
            <button type="button" className="naver" aria-label="네이버 로그인" onClick={() => window.location.href = "http://localhost:8080/oauth2/authorization/naver"} />
            <button type="button" className="google" aria-label="구글 로그인" onClick={() => window.location.href = "http://localhost:8080/oauth2/authorization/google"} />
          </div>

          {error && <p className="error-message">{error}</p>}

          <button type="submit" className="login-btn">로그인</button>
        </form>
      </div>
    </div>
  );
}
