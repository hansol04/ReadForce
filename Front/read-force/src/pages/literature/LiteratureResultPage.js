import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './css/LiteratureResultPage.css'; // 독립적인 CSS 선언

const LiteratureResultPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [showExplanation, setShowExplanation] = useState(false);

  const isCorrect = location.state?.isCorrect;
  const explanation = location.state?.explanation || '해설이 제공되지 않았습니다.';
  const category = location.state?.category || '';

  const resultMessage = isCorrect ? '정답입니다!' : '오답입니다.';
  const resultEmoji = isCorrect ? '🎉' : '❌';
  const resultSubText = isCorrect
    ? '👏 대단해요! 문맥을 잘 파악하셨네요.'
    : '😢 조금만 더 집중해볼까요? 누구나 틀릴 수 있어요!';

  const handleClose = () => {
    const cat = category.trim().toUpperCase();
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
    <div className="LiteratureResultPage-wrapper">
      <div className="LiteratureResultPage-card">
        <h2>{resultEmoji} {resultMessage}</h2>
        <p className="LiteratureResultPage-subtext">{resultSubText}</p>

        <div className="LiteratureResultPage-buttons">
          <button
            className="LiteratureResultPage-button green"
            onClick={() => setShowExplanation(!showExplanation)}
          >
            해설보기
          </button>
          <button
            className="LiteratureResultPage-button yellow"
            onClick={() => navigate(-1)}
          >
            다시 도전하기
          </button>
          <button
            className="LiteratureResultPage-button gray"
            onClick={handleClose}
          >
            닫기
          </button>
        </div>

        {showExplanation && (
          <div className="LiteratureResultPage-explanation">
            <h3>📝 해설</h3>
            <p>{explanation}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default LiteratureResultPage;
