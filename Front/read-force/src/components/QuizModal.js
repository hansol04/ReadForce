import React, { useState } from 'react';

const QuizModal = ({ quiz, onClose }) => {
  const [selectedIndex, setSelectedIndex] = useState(null);
  const [isSubmitted, setIsSubmitted] = useState(false);

  // 예외 처리: 데이터 없을 경우
  if (!quiz || !quiz.choices || !Array.isArray(quiz.choices)) {
    return (
      <div style={{ padding: '20px', backgroundColor: '#fff' }}>
        <h3>퀴즈 정보를 불러올 수 없습니다.</h3>
        <button onClick={onClose}>닫기</button>
      </div>
    );
  }

  const answerIndex = quiz.choices.findIndex(opt => opt === quiz.answer);

  return (
    <div style={{
      maxWidth: '600px',
      margin: '20px auto',
      padding: '24px',
      border: '2px solid #ccc',
      borderRadius: '12px',
      backgroundColor: '#ffffff',
      boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
    }}>
      <h3 style={{ marginBottom: '12px' }}>📘 퀴즈 문제</h3>
      <p style={{ fontSize: '18px', fontWeight: 'bold', marginBottom: '20px' }}>
        {quiz.question}
      </p>

      {quiz.choices.map((choice, i) => {
        const isCorrect = i === answerIndex;
        const isSelected = i === selectedIndex;
        const showFeedback = isSubmitted;

        const borderColor = showFeedback
          ? isSelected
            ? isCorrect ? '#4CAF50' : '#f44336'
            : '1px solid #ddd'
          : isSelected
            ? '#2196f3'
            : '#ddd';

        const backgroundColor = showFeedback
          ? isSelected
            ? isCorrect ? '#e8f5e9' : '#ffebee'
            : '#f9f9f9'
          : isSelected
            ? '#e3f2fd'
            : '#f9f9f9';

        return (
          <label
            key={i}
            htmlFor={`option-${i}`}
            style={{
              display: 'block',
              padding: '12px',
              marginBottom: '8px',
              borderRadius: '8px',
              border: `2px solid ${borderColor}`,
              backgroundColor,
              cursor: isSubmitted ? 'default' : 'pointer'
            }}
          >
            <input
              type="radio"
              id={`option-${i}`}
              name="quiz"
              value={i}
              checked={selectedIndex === i}
              onChange={() => setSelectedIndex(i)}
              disabled={isSubmitted}
              style={{ marginRight: '10px' }}
            />
            <strong>{String.fromCharCode(65 + i)}.</strong> {choice}
          </label>
        );
      })}

      {/* 정답 확인 버튼 */}
      {!isSubmitted && (
        <div style={{ textAlign: 'right', marginTop: '20px' }}>
          <button
            onClick={() => setIsSubmitted(true)}
            disabled={selectedIndex === null}
            style={{
              padding: '10px 20px',
              backgroundColor: selectedIndex === null ? '#ccc' : '#4CAF50',
              color: '#fff',
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

      {/* 정답 해설 */}
      {isSubmitted && (
        <>
          <div style={{ marginTop: '20px' }}>
            <p><strong>정답:</strong> {String.fromCharCode(65 + answerIndex)}. {quiz.choices[answerIndex]}</p>
            <p><strong>해설:</strong> {quiz.explanation || '해설 정보 없음'}</p>
          </div>

          <div style={{ textAlign: 'right', marginTop: '20px' }}>
            <button
              onClick={onClose}
              style={{
                padding: '10px 20px',
                backgroundColor: '#2196f3',
                color: '#fff',
                border: 'none',
                borderRadius: '6px',
                cursor: 'pointer',
                fontSize: '16px'
              }}
            >
              돌아가기
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default QuizModal;
