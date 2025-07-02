import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './AdaptiveResultPage.css';

const AdaptiveResultPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [showExplanation, setShowExplanation] = useState(false);

  const isCorrect = location.state?.isCorrect;
  const explanation = location.state?.explanation || '해설이 제공되지 않았습니다.';
  const next = location.state?.next || '/adaptive-learning/start'; // 다음 문제로 되돌아가기

  const resultMessage = isCorrect ? '정답입니다!' : '오답입니다.';
  const resultEmoji = isCorrect ? '🎉' : '❌';
  const resultSubText = isCorrect
    ? '👏 정확히 파악했어요! 멋져요.'
    : '😢 괜찮아요! 다음에는 더 잘할 수 있어요.';

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
            onClick={() => navigate(next)}
          >
            다음 문제
          </button>
          <button
            className="LiteratureResultPage-button gray"
            onClick={() => navigate('/adaptive-learning')}
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

export default AdaptiveResultPage;