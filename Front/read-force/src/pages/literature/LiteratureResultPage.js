import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

const LiteratureResultPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [showExplanation, setShowExplanation] = useState(false);

  const isCorrect = location.state?.isCorrect;
  const explanation = location.state?.explanation || '해설이 제공되지 않았습니다.';
  const language = location.state?.language || '한국어';
  const category = location.state?.category || '';

  const getBackPath = () => {
  switch (category.trim()) {
    case '소설':
      return '/literature/novel';
    case '동화':
      return '/literature/fairytale';
    default:
      return '/literature'; // 예외 처리
  }
};

  const resultMessage = isCorrect ? '정답입니다!' : '오답입니다.';
  const resultEmoji = isCorrect ? '🎉' : '❌';
  const resultSubText = isCorrect
    ? '👏 대단해요! 문맥을 잘 파악하셨네요.'
    : '😢 조금만 더 집중해볼까요? 누구나 틀릴 수 있어요!';

    const handleClose = () => {
    const cat = category.trim().toUpperCase(); // 영어로 바꿔서 판단

    switch (cat) {
        case 'NOVEL':
        navigate('/literature/novel');
        break;
        case 'FAIRYTALE':
        navigate('/literature/fairytale');
        break;
        default:
        navigate('/');
    }
    };

  return (
    <div className="result-wrapper">
      <div className="result-card">
        <h2>{resultEmoji} {resultMessage}</h2>
        <p className="result-subtext">{resultSubText}</p>

        <div className="button-group">
          <button onClick={() => setShowExplanation(!showExplanation)}>해설보기</button>
          <button onClick={() => navigate(-1)}>다시 도전하기</button>
          <button onClick={handleClose}>닫기</button>
        </div>

        {showExplanation && (
          <div className="explanation-box">
            <h3>📝 해설</h3>
            <p>{explanation}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default LiteratureResultPage;
