// src/components/QuizModal.js
import React from 'react';

const QuizModal = ({ quiz, onClose }) => {
  if (!quiz) return null;

  return (
    <div style={{
      border: '2px solid black',
      padding: '20px',
      marginTop: '20px',
      backgroundColor: '#fff'
    }}>
      <h3>문제: {quiz.question}</h3>
      <ul>
        {quiz.options.map((opt, i) => (
          <li key={i}>
            {i === quiz.answerIndex ? <b>{opt}</b> : opt}
          </li>
        ))}
      </ul>
      <p><strong>정답:</strong> {quiz.options[quiz.answerIndex]}</p>
      <p><strong>해설:</strong> {quiz.reason}</p>
      <button onClick={onClose}>닫기</button>
    </div>
  );
};

export default QuizModal;
