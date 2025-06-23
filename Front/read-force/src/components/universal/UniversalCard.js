import './css/UniversalCard.css';
import React from 'react';

const levelMap = {
  BEGINNER: '초급',
  INTERMEDIATE: '중급',
  ADVANCED: '고급',
};

const categoryMap = {
  POLITICS: '정치',
  ECONOMY: '경제',
  SOCIETY: '사회',
  CULTURE: '생활/문화',
  SCIENCE: 'IT/과학',
  ETC: '기타'
};

const UniversalCard = React.memo(({ data, onSolve }) => {
  const koreanLevel = levelMap[data.level] || data.level;
  const koreanCategory = categoryMap[data.category] || data.category;

  return (
    <div className="news-card">
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <h3 className="news-title">{data.title}</h3>
        <span className={`news-badge ${koreanLevel}`}>{koreanLevel}</span>
      </div>
      
      <p className="news-content">{data.content}</p>

      <div className="news-footer">
        <div>
          <p className="news-category"># {koreanCategory}</p>
        </div>

        <button onClick={() => onSolve(data)} className="news-button">
          문제 풀기
        </button>
      </div>
    </div>
  );
});

export default UniversalCard;
