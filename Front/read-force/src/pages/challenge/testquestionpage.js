import React, { useEffect, useState } from 'react';
import './testquestionpage.css';
import { useLocation, useNavigate } from 'react-router-dom';
import api from '../../api/axiosInstance';

const TestQuestionPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const language = location.state?.language || 'KOREAN';

  const [questions, setQuestions] = useState([]);
  const [currentIdx, setCurrentIdx] = useState(0);
  const [selected, setSelected] = useState(null);
  const [score, setScore] = useState(0);
  const [showNext, setShowNext] = useState(false);

  useEffect(() => {
    api.get(`/proficiency_test/get-proficiency-test-quiz-map?language=${language}`)
      .then(res => {
        const parsed = Object.entries(res.data).map(([articleStr, quiz]) => {
          try {
            return { article: JSON.parse(articleStr), quiz };
          } catch {
            return null;
          }
        }).filter(Boolean);
        setQuestions(parsed);
      })
      .catch(() => alert("문제를 불러오지 못했습니다."));
  }, [language]);

  if (questions.length === 0) return <div>문제를 불러오는 중...</div>;

  const { article, quiz } = questions[currentIdx];

  const handleSubmit = () => {
    if (selected === quiz.answer) setScore(prev => prev + 1);
    setShowNext(true);
  };

  const handleNext = () => {
    if (currentIdx + 1 < questions.length) {
      setCurrentIdx(prev => prev + 1);
      setSelected(null);
      setShowNext(false);
    } else {
      navigate('/test-result', { state: { score, total: questions.length } });
    }
  };

  return (
    <div className="test-question-wrapper">
      <div className="article-section">
        <h3>{article.title}</h3>
        <p>{article.summary}</p>
        <p>{article.content}</p>
      </div>
      <div className="quiz-section">
        <h4>문제 {currentIdx + 1}</h4>
        <p>{quiz.question}</p>
        {quiz.options.map((opt, idx) => (
          <button
            key={idx}
            className={`quiz-option ${selected === opt ? 'selected' : ''}`}
            onClick={() => setSelected(opt)}
            disabled={showNext}
          >
            {String.fromCharCode(65 + idx)}. {opt}
          </button>
        ))}
        <div className="quiz-actions">
          {!showNext ? (
            <button disabled={!selected} onClick={handleSubmit}>정답 제출</button>
          ) : (
            <button onClick={handleNext}>
              {currentIdx + 1 === questions.length ? '결과 보기' : '다음 문제'}
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default TestQuestionPage;
