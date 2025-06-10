// src/components/NewsCard.js
import React from 'react';

const NewsCard = ({ article, onSolve }) => {
  return (
    <div style={{
      border: '1px solid #ccc',
      padding: '16px',
      marginBottom: '12px',
      borderRadius: '8px',
      backgroundColor: '#f9f9f9'
    }}>
      <h4>{article.title}</h4>
      <p>{article.description}</p>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div>
          <span style={{
            backgroundColor: '#c8e6c9',
            padding: '4px 10px',
            borderRadius: '10px',
            marginRight: '8px'
          }}>
            {article.difficulty}
          </span>
          <span style={{ color: 'gray' }}># {article.category}</span>
        </div>
        <button onClick={() => onSolve(article)}>문제풀기</button>
      </div>
    </div>
  );
};

export default NewsCard;
