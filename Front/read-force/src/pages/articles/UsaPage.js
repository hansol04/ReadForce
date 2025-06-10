import React, { useState } from 'react';
import NewsList from '../../components/NewsList';
import QuizModal from '../../components/QuizModal';

const UsaPage = () => {
  const [quizData, setQuizData] = useState(null);

  const handleSolve = async (article) => {
    const res = await fetch('http://localhost:8080/api/generate-quiz', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ article: article.content || article.description }),
    });
    const quiz = await res.json();
    setQuizData(quiz);
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>미국 기사</h2>
      <NewsList country="us" onSolve={handleSolve} />
      <QuizModal quiz={quizData} onClose={() => setQuizData(null)} />
    </div>
  );
};

export default UsaPage;
