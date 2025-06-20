import './css/UniversalCard.css';
import React from 'react';

const UniversalCard = React.memo(({ data, onSolve }) => {
  return (
    <div className="news-card">
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <h3 className="news-title">{data.title}</h3>
        <span className={`news-badge ${data.difficulty}`}>{data.difficulty}</span>
      </div>
      
      <p className="news-summary">{data.summary}</p>

      <div className="news-footer">
        <div>
          <p className="news-category"># {data.category}</p>
        </div>

        <button onClick={() => onSolve(data)} className="news-button">
          문제 풀기
        </button>
      </div>
    </div>
  );
});

export default UniversalCard;
