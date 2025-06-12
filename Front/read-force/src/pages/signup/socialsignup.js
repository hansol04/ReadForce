import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Socialsignup() {
  const navigate = useNavigate();
  const [tempToken, setTempToken] = useState('');
  const [nickname, setNickname] = useState('');
  const [birthday, setBirthday] = useState('');
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get('token');
    if (!token) {
      alert('잘못된 접근입니다.');
      navigate('/login');
    }
    setTempToken(token);
  }, [navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');

    const birthdayRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!birthdayRegex.test(birthday)) {
      setError('생년월일은 YYYY-MM-DD 형식이어야 합니다.');
      return;
    }

    try {
      const response = await fetch('/member/social-sign-up', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          token: tempToken,
          nickname,
          birthday,
        }),
      });

      const data = await response.json();
      if (response.ok) {
        setMessage('🎉 소셜 회원가입이 완료되었습니다!');
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
      <h2 className="signup-title">추가 정보 입력</h2>
      <form className="signup-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>닉네임</label>
          <input
            type="text"
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
        <button className="submit-btn">회원가입 완료</button>
        {error && <p className="error-message">{error}</p>}
        {message && <p className="success-message">{message}</p>}
      </form>
    </div>
  );
}
