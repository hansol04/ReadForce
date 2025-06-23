import React from 'react';
import './testresultpage.css';
import { useLocation, useNavigate } from 'react-router-dom';

const TestResultPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { score, total, questions, answers } = location.state || {};
  const percent = Math.round((score / total) * 100);

  const getLiteracyLevel = (questions, answers) => {
    if (!questions || !answers || questions.length !== answers.length) return '초급';

    let correct = { beginner: 0, intermediate: 0, advanced: 0 };

    questions.forEach((q, idx) => {
      const isCorrect = answers[idx] === q.quiz.correct_answer_index;
      const level = (q.article.level || '').toLowerCase(); 

      console.log(`문제 ${idx + 1} - Level: ${level}, 정답 여부: ${isCorrect}`);

      if (isCorrect) {
        if (level === 'beginner') correct.beginner++;
        else if (level === 'intermediate') correct.intermediate++;
        else if (level === 'advanced') correct.advanced++;
      }
    });

    console.log('정답 수 통계:', correct);

    const { beginner, intermediate, advanced } = correct;

    if (beginner === 0) return '초급';
    if (beginner === 1 && intermediate === 2 && advanced >= 1) return '고급';
    if (
      (beginner === 1 && intermediate === 2) ||
      (beginner === 1 && intermediate === 1 && advanced === 1) ||
      (beginner === 1 && advanced === 2)
    ) return '중급';

    return '초급';
  };

  const level = getLiteracyLevel(questions, answers);
  console.log('최종 문해력 등급:', level);

  const goToReview = () => {
    navigate('/test-review', { state: { questions, answers } });
  };

  return (
    <div className="test-result-wrapper">
      <div className="test-result-card">
        <h2>🎉 당신의 문해력은 <span className="highlight">{level}</span>입니다!</h2>
        <p>{total}문제 중 {score}문제를 맞았습니다.</p>
        <p>상위 {100 - percent}%에 해당합니다.</p>
        <div className="test-result-actions">
          <button onClick={goToReview}>해설 보기</button>
        </div>
      </div>
    </div>
  );
};

export default TestResultPage;
