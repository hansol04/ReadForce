import './signupwithemail.css';
import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

const SignupCompletePage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const email = queryParams.get('email');

  const [nickname, setNickname] = useState('');
  const [nicknameMessage, setNicknameMessage] = useState('');
  const [isNicknameValid, setIsNicknameValid] = useState(null);
  const [birthday, setBirthday] = useState('');
  const [birthdayMessage, setBirthdayMessage] = useState('');
  const [isBirthdayValid, setIsBirthdayValid] = useState(null);
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [passwordMessage, setPasswordMessage] = useState('');
  const [isPasswordValid, setIsPasswordValid] = useState(null);
  const [confirmPasswordMessage, setConfirmPasswordMessage] = useState('');
  const [isConfirmPasswordValid, setIsConfirmPasswordValid] = useState(null);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

    // 닉네임 중복 검사 함수
    const checkNicknameDuplicate = async (nickname) => {
      try {
        const res = await fetch(`/member/nickname-check?nickname=${nickname}`);
  
        if (res.ok) {
          const data = await res.json();
          console.log(data.message); // "닉네임은 사용 가능합니다."
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
  
    // 닉네임 형식 검사 함수
    const validateNickname = async (value) => {
      const onlyKorean = /^[가-힣]+$/.test(value);
      const onlyEnglish = /^[a-zA-Z]+$/.test(value);
  
      if (
        (onlyKorean && value.length <= 8) ||
        (onlyEnglish && value.length <= 20)
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
        setNicknameMessage('한글 8자, 영문 20자 이하로 입력해주세요');
        setIsNicknameValid(false);
      }
    };
  
      // 생연월일 형식 검사
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

    // - 자동 추가
    const handleBirthdayChange = (value) => {
      // 숫자만 남기기
      const numeric = value.replace(/\D/g, '').slice(0, 8);
  
      // 자동으로 YYYY-MM-DD 형태로 변환
      let formatted = numeric;
      if (numeric.length >= 5) {
        formatted = `${numeric.slice(0, 4)}-${numeric.slice(4, 6)}`;
        if (numeric.length >= 7) {
          formatted += `-${numeric.slice(6, 8)}`;
        }
      }
  
      setBirthday(formatted);
      validateBirthday(formatted); // 유효성 검사 호출
    };
    // 비밀번호 유효성 검사
    const validatePassword = (value) => {
      const lengthValid = value.length >= 8 && value.length <= 16;
      const hasLetter = /[a-zA-Z]/.test(value);
      const hasNumber = /[0-9]/.test(value);
      const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(value);
  
      if (lengthValid && hasLetter && hasNumber && hasSpecialChar) {
        setPasswordMessage('사용 가능한 비밀번호입니다');
        setIsPasswordValid(true);
      } else {
        setPasswordMessage(
          '영문/숫자/특수문자를 모두 포함하여 8~16자로 설정해주세요'
        );
        setIsPasswordValid(false);
      }
    };
  
    // 비밀번호 확인
    const validateConfirmPassword = (value) => {
      if (value === password) {
        setConfirmPasswordMessage('비밀번호가 일치합니다');
        setIsConfirmPasswordValid(true);
      } else {
        setConfirmPasswordMessage('비밀번호가 일치하지 않습니다');
        setIsConfirmPasswordValid(false);
      }
    };
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');

    // 유효성 검사
    if (!email) {
      const msg = '이메일 인증이 누락되었습니다.';
      setError(msg);

      return;
    }

    if (password !== confirmPassword) {
      const msg = '비밀번호가 일치하지 않습니다.';
      setError(msg);
      alert(msg);
      return;
    }

    // 생년월일 형식 확인 (간단한 정규표현식)
    const birthdayRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!birthdayRegex.test(birthday)) {
      setError('생년월일은 YYYY-MM-DD 형식이어야 합니다.');
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
              placeholder="한글 8자, 영문 20자 이내로 작성해주세요"
              value={nickname}
              onChange={async (e) => {
                const value = e.target.value;
                setNickname(value);
                await validateNickname(value);
              }}
              required
            />
            <span
              className="validation-message"
              style={{
                color: isNicknameValid === null ? 'inherit' : isNicknameValid ? 'green' : 'red',
                fontSize: '0.85rem',
                // marginTop: '4px',
              }}
            >
              {nicknameMessage}
            </span>
        </div>
        <div className="form-group">
          <label>생년월일</label>
          <div className='input-with-message'>
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
        <div className="form-group">
          <label>비밀번호</label>
          <div className='input-with-message'>
            <input
              type="password"
              placeholder="8~16자, 영문/숫자/특수문자 포함"
              value={password}
              onChange={(e) => {
                const value = e.target.value;
                setPassword(value);
                validatePassword(value);
              }} required
            />
            <span
              className="validation-message"
              style={{
                color: isPasswordValid === null ? 'inherit' : isPasswordValid ? 'green' : 'red',
                fontSize: '0.85rem',
              }}
            >
              {passwordMessage}
            </span>
          </div>

        </div>
        <div className="form-group">
          <label>비밀번호 확인</label>
          <div className='input-with-message'>
            <input
              type="password"
              value={confirmPassword}
              onChange={(e) => {
                const value = e.target.value;
                setConfirmPassword(value);
                validateConfirmPassword(value);
              }}
              required
            />
            <span
              className="validation-message"
              style={{
                color: isConfirmPasswordValid === null
                  ? 'inherit'
                  : isConfirmPasswordValid
                    ? 'green'
                    : 'red',
                fontSize: '0.85rem',
              }}
            >
              {confirmPasswordMessage}
            </span>
          </div>

        </div>
        <button className="submit-btn">회원가입 완료</button>
        {error && <p className="error-message">{error}</p>}
        {message && <p className="success-message">{message}</p>}
      </form>
    </div>
  );
};

export default SignupCompletePage;
