import React, { useState } from 'react';
import NewsList from '../../components/NewsList';
import QuizModal from '../../components/QuizModal';

const JapanPage = () => {
  const [quizData, setQuizData] = useState(null);

  const handleSolve = async (article) => {
   const res = await fetch('http://localhost:8080/api/quiz/generate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ article: article.content || article.description }),
            });

    const quiz = await res.json();
    setQuizData(quiz);
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>일본 기사</h2>
      <NewsList country="jp" onSolve={handleSolve} />
      <QuizModal quiz={quizData} onClose={() => setQuizData(null)} />
    </div>
  );
};

export default JapanPage;
