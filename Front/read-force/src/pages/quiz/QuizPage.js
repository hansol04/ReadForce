import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const QuizPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { quiz, article, from } = location.state || {};

  const [selectedIndex, setSelectedIndex] = useState(null);
  const [isSubmitted, setIsSubmitted] = useState(false);

  if (!quiz || !quiz.choices) {
    return (
      <div style={{ padding: '20px' }}>
        <h3>퀴즈 데이터를 찾을 수 없습니다.</h3>
        <button onClick={() => navigate('/')}>홈으로</button>
      </div>
    );
  }

  const answerIndex = quiz.choices.findIndex(opt => opt === quiz.answer);

  return (
    <div style={{
      maxWidth: '700px',
      margin: '40px auto',
      padding: '30px',
      backgroundColor: '#fff',
      border: '1px solid #ccc',
      borderRadius: '10px',
      boxShadow: '0 4px 10px rgba(0,0,0,0.1)'
    }}>
      <h2>📘 퀴즈 문제</h2>
      <p style={{ fontSize: '18px', fontWeight: 'bold' }}>{quiz.question}</p>

      <div style={{ marginTop: '20px' }}>
        {quiz.choices.map((choice, i) => {
          const isCorrect = i === answerIndex;
          const isSelected = i === selectedIndex;
          const showFeedback = isSubmitted;

          const borderColor = showFeedback && isSelected
            ? (isCorrect ? '2px solid green' : '2px solid red')
            : isSelected
              ? '2px solid #2196f3'
              : '1px solid #ccc';

          const bgColor = showFeedback && isSelected
            ? (isCorrect ? '#d0f0c0' : '#f8d7da')
            : isSelected
              ? '#e3f2fd'
              : '#f4f4f4';

          return (
            <label
              key={i}
              htmlFor={`opt-${i}`}
              style={{
                display: 'block',
                padding: '12px',
                marginBottom: '10px',
                borderRadius: '8px',
                backgroundColor: bgColor,
                border: borderColor,
                cursor: isSubmitted ? 'default' : 'pointer'
              }}
            >
              <input
                type="radio"
                id={`opt-${i}`}
                name="quiz"
                value={i}
                checked={selectedIndex === i}
                onChange={() => setSelectedIndex(i)}
                disabled={isSubmitted}
                style={{ marginRight: '10px' }}
              />
              {String.fromCharCode(65 + i)}. {choice}
            </label>
          );
        })}
      </div>

      {!isSubmitted && (
        <div style={{ textAlign: 'right', marginTop: '20px' }}>
          <button
            onClick={() => setIsSubmitted(true)}
            disabled={selectedIndex === null}
            style={{
              padding: '10px 20px',
              backgroundColor: selectedIndex === null ? '#ccc' : '#4CAF50',
              color: 'white',
              border: 'none',
              borderRadius: '6px',
              cursor: selectedIndex === null ? 'not-allowed' : 'pointer',
              fontSize: '16px'
            }}
          >
            정답 확인
          </button>
        </div>
      )}

      {isSubmitted && (
        <div style={{ marginTop: '20px' }}>
          <p><strong>정답:</strong> {String.fromCharCode(65 + answerIndex)}. {quiz.choices[answerIndex]}</p>
          <p><strong>해설:</strong> {quiz.explanation}</p>
        </div>
      )}

      <div style={{ textAlign: 'right', marginTop: '20px' }}>
        <button
          onClick={() => navigate(from || '/')}
          style={{
            padding: '10px 20px',
            backgroundColor: '#2196f3',
            color: 'white',
            border: 'none',
            borderRadius: '6px',
            cursor: 'pointer'
          }}
        >
          돌아가기
        </button>
      </div>
    </div>
  );
};

export default QuizPage;
