import './signupchoice.css';
import { useNavigate } from "react-router-dom";

const SignupChoice = () => {
    const navigate = useNavigate();

    return (
        <div>
            <h2 className='title33'>Read Force 시작하기</h2>
            <div className="choice-container">
                <button className="choice-btn" onClick={() => navigate("/signup")}>E-Mail로 시작하기</button>
                <button className="choice-btn">Google</button>
                <button className="choice-btn">Naver</button>
                <button className="choice-btn">Kakao</button>
            </div>
        </div>
    );
};

export default SignupChoice;