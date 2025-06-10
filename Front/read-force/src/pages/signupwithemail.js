import './signupwithemail.css';

const SignupWithEmail = () => {
    return (
    <div className="signup-wrapper">
      <h2 className="signup-title">오늘의 문해력 회원가입</h2>
      <form className="signup-form">
        {/* 이메일 */}
        <div className="form-group">
          <label>이메일</label>
          <input type="email" placeholder="example@email.com" />
        </div>

        {/* 비밀번호 */}
        <div className="form-group">
          <label>비밀번호</label>
          <input type="password" placeholder="8~16자, 특수문자 포함" />
        </div>

        {/* 비밀번호 확인 */}
        <div className="form-group">
          <label>비밀번호 확인</label>
          <input type="password" placeholder="비밀번호 재입력" />
        </div>

        {/* 회원가입 버튼 */}
        <button type="submit" className="submit-btn">회원가입</button>
      </form>
    </div>
  );
}

export default SignupWithEmail;