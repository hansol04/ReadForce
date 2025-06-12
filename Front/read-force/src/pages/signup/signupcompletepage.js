import './signupwithemail.css';
import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

const SignupCompletePage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const email = queryParams.get('email');

  const [nickname, setNickname] = useState('');
  const [birthday, setBirthday] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');

    // 유효성 검사
    if (!email) {
      const msg = '이메일 인증이 누락되었습니다.';
      setError(msg);
      alert(msg);
      return;
    }

    if (password !== confirmPassword) {
      const msg = '비밀번호가 일치하지 않습니다.';
      setError(msg);
      alert(msg);
      return;
    }

    // 닉네임 형식 확인
    const isValidNickname = (name) => {
      const onlyKorean = /^[가-힣]+$/.test(name);
      const onlyEnglish = /^[a-zA-Z]+$/.test(name);
      const mixed = /^[가-힣a-zA-Z0-9]+$/.test(name);

      if (!mixed) return false;
      if (onlyKorean && name.length <= 8) return true;
      if (onlyEnglish && name.length <= 20) return true;
      if (mixed && name.length <= 12) return true;

      return false;
    };

    if (!isValidNickname(nickname)) {
      const msg = '닉네임은 한글 8자, 영문 20자 이하로 입력해주세요.';
      setError(msg);
      alert(msg);
      return;
    }

    // 생년월일 형식 확인
    const birthdayRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!birthdayRegex.test(birthday)) {
      const msg = '생년월일은 YYYY-MM-DD 형식이어야 합니다.';
      setError(msg);
      alert(msg);
      return;
    }

    try {
      const response = await fetch('/member/sign-up', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password, nickname, birthday }),
      });

      const data = await response.json();

      if (response.ok) {
        setMessage('🎉 회원가입이 완료되었습니다!');
        setTimeout(() => {
          navigate('/login');
        }, 1500);
      } else {
        setError(data.message || '회원가입 실패');
      }
    } catch (err) {
      console.error(err);
      setError('서버 오류가 발생했습니다.');
    }
  };

  return (
    <div className="signup-wrapper">
      <h2 className="signup-title">회원정보 입력</h2>
      <form className="signup-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>닉네임</label>
          <input
            type="text"
            placeholder='한글 8글자, 영문 20글자 이내로 작성해주세요'
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>생년월일</label>
          <input
            type="text"
            placeholder="YYYY-MM-DD"
            value={birthday}
            onChange={(e) => setBirthday(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>비밀번호</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>비밀번호 확인</label>
          <input
            type="password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
        </div>
        <button className="submit-btn">회원가입 완료</button>
        {error && <p className="error-message">{error}</p>}
        {message && <p className="success-message">{message}</p>}
      </form>
    </div>
  );
};

export default SignupCompletePage;
