import React from 'react';
import './testresultpage.css';
import { useLocation, useNavigate } from 'react-router-dom';

const TestResultPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { score, total } = location.state || {};
  const percent = Math.round((score / total) * 100);

  return (
    <div className="test-result-wrapper">
      <div className="test-result-card">
        <h2>🎉 당신의 문해력은 <span className="highlight">
          {percent >= 80 ? '고급' : percent >= 50 ? '중급' : '초급'}
        </span>입니다!</h2>
        <p>{total}문제 중 {score}문제를 맞았습니다.</p>
        <p>상위 {100 - percent}%에 해당합니다.</p>
        <div className="test-result-actions">
          <button onClick={() => navigate('/challenge')}>해설 보기</button>
          <button onClick={() => navigate('/test')}>다시 도전</button>
        </div>
      </div>
    </div>
  );
};

export default TestResultPage;
