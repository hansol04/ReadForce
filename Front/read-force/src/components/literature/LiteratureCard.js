import React from 'react';

const LiteratureCard = ({ data, onSolve }) => {
  const { title, summary, author, difficulty, publishedAt } = data;

  return (
    <div style={{
      border: '1px solid #ddd',
      borderRadius: '8px',
      padding: '16px',
      marginBottom: '16px',
      backgroundColor: '#f9f9f9'
    }}>
      <h3>{title}</h3>
      <p><strong>작자:</strong> {author || '미상'}</p>
      <p><strong>요약:</strong> {summary}</p>
      <p><strong>난이도:</strong> {difficulty}</p>
      <p><small>{publishedAt ? `등록일: ${new Date(publishedAt).toLocaleDateString()}` : ''}</small></p>
      <button
        onClick={() => onSolve(data)}
        style={{
          marginTop: '10px',
          padding: '8px 16px',
          backgroundColor: '#4CAF50',
          color: '#fff',
          border: 'none',
          borderRadius: '4px',
          cursor: 'pointer'
        }}
      >
        문제 풀기
      </button>
    </div>
  );
};

export default LiteratureCard;
