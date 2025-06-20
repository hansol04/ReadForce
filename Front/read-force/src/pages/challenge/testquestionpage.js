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
        const parsed = Object.entries(res.data).map(([key, quiz]) => {
          console.log(quiz.news);
          return {
            article: quiz.news ?? {}, // ✅ 지문을 직접 가져옴
            quiz
          };
        });
        setQuestions(parsed);
      })
      .catch(() => alert("문제를 불러오지 못했습니다."));
  }, [language]);

  if (questions.length === 0) return <div>문제를 불러오는 중...</div>;

  const { article, quiz } = questions[currentIdx];

  const handleSubmit = () => {
    if (selected === quiz.correct_answer_index) {
      setScore(prev => prev + 1);
    }
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
    <div className="article-question-layout">
      <div className="article-box">
        <h3 className="article-title">{article.title || '제목 없음'}</h3>
        <p className="article-content">{article.content || '내용 없음'}</p>
      </div>

      <div className="quiz-box">
        <h4 className="quiz-title">문제 {currentIdx + 1}</h4>
        <p className="quiz-question">{quiz.question_text}</p>
        {[quiz.choice1, quiz.choice2, quiz.choice3, quiz.choice4].map((opt, idx) => (
          <button
            key={idx}
            className={`quiz-option ${selected === idx ? 'selected' : ''}`}
            onClick={() => setSelected(idx)}
            disabled={showNext}
          >
            {String.fromCharCode(65 + idx)}. {opt}
          </button>
        ))}
        <div className="quiz-button-container">
          {!showNext ? (
            <button className="submit-button" disabled={selected === null} onClick={handleSubmit}>
              정답 제출
            </button>
          ) : (
            <button className="submit-button" onClick={handleNext}>
              {currentIdx + 1 === questions.length ? '결과 보기' : '다음 문제'}
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default TestQuestionPage;
