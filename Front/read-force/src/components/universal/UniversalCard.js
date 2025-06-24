import './css/UniversalCard.css';
import React, { useEffect } from 'react';

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
  ETC: '기타',
  
  MYSTERY: '추리',
  HISTORY: '역사',
  CLASSIC: '고전',
  MODERN: '근대',
  CHILDREN: '동화',
};



const UniversalCard = React.memo(({ data, onSolve }) => {

  useEffect(() => {
  console.log("받은 데이터 확인:", data);
}, [data]);


  const koreanLevel = levelMap[data.level] || data.level;
  const koreanCategory = categoryMap[data.category] || data.category;

  return (
    <div className="news-card">
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <h3 className="news-title">{data.title}<span style={{ fontWeight: 'normal', fontSize: '0.9rem', color: '#888' }}> - {data.literature_paragraph_no}</span></h3>
        <span className={`news-badge ${koreanLevel}`}>{koreanLevel}</span>
      </div>
      
      <p className="news-content">{data.content}</p>

      <div className="news-footer">
        <div>
          <p className="news-category"># {koreanCategory}</p>
        </div>

        <button onClick={() => onSolve && onSolve(data)} className="news-button">문제 풀기</button>
      </div>
    </div>
  );
});

export default UniversalCard;
