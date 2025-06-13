import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './login.css';
import kakaoIcon from '../../assets/image/kakao.png';
import naverIcon from '../../assets/image/naver.png';
import googleIcon from '../../assets/image/google.png';

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
        const token = data.ACCESS_TOKEN;
        const refreshToken = data.REFRESH_TOKEN; // ✅ 추가
        const nickname = data.NICK_NAME || data.nickname;

        localStorage.setItem('token', token);
        localStorage.setItem('refresh_token', refreshToken); // ✅ 추가
        localStorage.setItem('nickname', nickname); // ✅ 이게 없으면 Header에서 못 뜸

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
            <button
              type="button"
              className="social-btn"
              onClick={() => window.location.href = "http://localhost:8080/oauth2/authorization/kakao"}
            >
              <img src={kakaoIcon} alt="카카오" />
            </button>
            <button
              type="button"
              className="social-btn"
              onClick={() => window.location.href = "http://localhost:8080/oauth2/authorization/naver"}
            >
              <img src={naverIcon} alt="네이버" />
            </button>
            <button
              type="button"
              className="social-btn"
              onClick={() => window.location.href = "http://localhost:8080/oauth2/authorization/google"}
            >
              <img src={googleIcon} alt="구글" />
            </button>
          </div>


          {error && <p className="error-message">{error}</p>}

          <button type="submit" className="login-btn">로그인</button>
        </form>
      </div>
    </div>
  );
}
