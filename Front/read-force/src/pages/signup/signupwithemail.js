import './signupwithemail.css';
import { useNavigate } from "react-router-dom";
import { useState } from "react";

const SignupWithEmail = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');

    try {
      const response = await fetch('/email/send-verification-code-sign-up', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || '인증 이메일 전송 실패');
      }

      navigate(`/signup/emailverifypage?email=${encodeURIComponent(email)}`);
    } catch (error) {
      setMessage(`❌ 오류: ${error.message}`);
    }
  };

  return (
    <div className="signup-wrapper">
      <h2 className="signup-title">E-Mail 입력</h2>
      <form className="signup-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <input
            type="email"
            placeholder="example@email.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <button className="submit-btn">E-Mail 인증</button>
      </form>
      {message && <p className="error-message">{message}</p>}
    </div>
  );
};

export default SignupWithEmail;
