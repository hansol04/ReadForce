import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Socialsignup() {
  const navigate = useNavigate();
  const [tempToken, setTempToken] = useState('');

  const [nickname, setNickname] = useState('');
  const [nicknameMessage, setNicknameMessage] = useState('');
  const [isNicknameValid, setIsNicknameValid] = useState(null);

  const [birthday, setBirthday] = useState('');
  const [birthdayMessage, setBirthdayMessage] = useState('');
  const [isBirthdayValid, setIsBirthdayValid] = useState(null);

  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  // 닉네임 중복 검사
  const checkNicknameDuplicate = async (nickname) => {
    try {
      const res = await fetch(`/member/nickname-check?nickname=${nickname}`);
      if (res.ok) {
        const data = await res.json();
        console.log(data.message);
        return true;
      } else {
        const data = await res.json();
        console.warn(data.message || '닉네임 중복');
        return false;
      }
    } catch (err) {
      console.error('닉네임 중복 확인 오류:', err);
      return false;
    }
  };

  // 닉네임 형식 + 중복 검사
  const validateNickname = async (value) => {
    const onlyKorean = /^[가-힣]+$/.test(value);
    const onlyEnglish = /^[a-zA-Z]+$/.test(value);

    if (
      (onlyKorean && value.length >= 2 && value.length <= 8) ||
      (onlyEnglish && value.length >= 2 && value.length <= 20)
    ) {
      const isAvailable = await checkNicknameDuplicate(value);
      if (isAvailable) {
        setNicknameMessage('사용 가능한 닉네임입니다.');
        setIsNicknameValid(true);
      } else {
        setNicknameMessage('이미 존재하는 닉네임입니다.');
        setIsNicknameValid(false);
      }
    } else {
      setNicknameMessage('한글 2~8자, 영문 2~20자만 입력해주세요');
      setIsNicknameValid(false);
    }
  };

  // 생년월일 유효성 검사
  const validateBirthday = (value) => {
    const birthdayRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (birthdayRegex.test(value)) {
      setBirthdayMessage('생년월일 입력 완료');
      setIsBirthdayValid(true);
    } else {
      setBirthdayMessage('생년월일 8자리를 입력해주세요 (예: 19971104)');
      setIsBirthdayValid(false);
    }
  };

  const handleBirthdayChange = (value) => {
    const numeric = value.replace(/\D/g, '').slice(0, 8);
    let formatted = numeric;
    if (numeric.length >= 5) {
      formatted = `${numeric.slice(0, 4)}-${numeric.slice(4, 6)}`;
      if (numeric.length >= 7) {
        formatted += `-${numeric.slice(6, 8)}`;
      }
    }
    setBirthday(formatted);
    validateBirthday(formatted);
  };

  // 토큰 검증
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const token = params.get('TEMPORAL_TOKEN');
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
          temporal_token: tempToken,
          nickname,
          birthday,
        }),
      });

      const data = await response.json();
      if (response.ok) {
        localStorage.setItem('token', data.access_token);
        localStorage.setItem('refresh_token', data.REFRESH_TOKEN);
        localStorage.setItem('nickname', data.NICK_NAME || data.nickname);
        setMessage('🎉 소셜 회원가입이 완료되었습니다!');
        setTimeout(() => navigate('/'), 1500);
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
          <div className="input-with-message">
            <input
              type="text"
              placeholder="한글 8자, 영문 20자 이내로 작성해주세요"
              value={nickname}
              onChange={async (e) => {
                const value = e.target.value;
                setNickname(value);
                if (value.length >= 2) {
                  await validateNickname(value);
                } else {
                  setNicknameMessage('2자 이상 입력해주세요');
                  setIsNicknameValid(false);
                }
              }}
              required
            />
            <span
              className="validation-message"
              style={{
                color: isNicknameValid === null ? 'inherit' : isNicknameValid ? 'green' : 'red',
                fontSize: '0.85rem',
              }}
            >
              {nicknameMessage}
            </span>
          </div>
        </div>
        <div className="form-group">
          <label>생년월일</label>
          <div className="input-with-message">
            <input
              type="text"
              placeholder="예: 19971104"
              value={birthday}
              onChange={(e) => handleBirthdayChange(e.target.value)}
              required
            />
            <span
              className="validation-message"
              style={{
                color: isBirthdayValid === null ? 'inherit' : isBirthdayValid ? 'green' : 'red',
                fontSize: '0.85rem',
              }}
            >
              {birthdayMessage}
            </span>
          </div>
        </div>

        <button className="submit-btn">회원가입 완료</button>
        {error && <p className="error-message">{error}</p>}
        {message && <p className="success-message">{message}</p>}
      </form>
    </div>
  );
}
