import React from 'react';
import './css/ArticleQuestion.css';

const ArticleQuestion = ({ article }) => {
  // 임시 더미 문제
  const question = {
    id: 1,
    text: `${article.title} 관련 문제입니다. 언제 발표되었나요?`,
    options: [
      '2025년 6월 15일',
      '2025년 6월 14일',
      '2025년 6월 13일',
      '2025년 6월 12일',
    ],
    correct: article.publishedAt,
  };

  return (
    <div className="article-question">
      <h3>문제 {question.id}</h3>
      <p className="question-text">{question.text}</p>
      <ul className="options-list">
        {question.options.map((opt, idx) => (
          <li key={idx} className={`option ${opt === question.correct ? 'correct' : ''}`}>
            {opt}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ArticleQuestion;