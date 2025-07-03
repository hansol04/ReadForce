import React, { useEffect, useRef, useState } from 'react';
import './testquestionpage.css';
import { useLocation, useNavigate } from 'react-router-dom';
import api from '../../api/axiosInstance';

const TestQuestionPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const language = location.state?.language || 'KOREAN';

  const [questions, setQuestions] = useState([]);
  const [currentIdx, setCurrentIdx] = useState(0);
  const [answers, setAnswers] = useState([]);
  const [submitted, setSubmitted] = useState(false);

  const quizBoxRef = useRef(null);
  const omrGridRef = useRef(null);
  const articleBoxRef = useRef(null);

  useEffect(() => {
    api
      .get(`/proficiency_test/get-proficiency-test-quiz-list?language=${language}`)
      .then((res) => {
        const parsed = res.data.map((item) => ({
          article: item.get_news,
          quiz: item.get_news_quiz,
        }));
        setQuestions(parsed);
        setAnswers(Array(parsed.length).fill(null));
      })
      .catch(() => alert('문제를 불러오지 못했습니다.'));
  }, [language]);

  useEffect(() => {
    if (quizBoxRef.current) quizBoxRef.current.scrollTop = 0;
    if (omrGridRef.current) omrGridRef.current.scrollTop = 0;
    if (articleBoxRef.current) articleBoxRef.current.scrollTop = 0;
  }, [currentIdx]);

  if (questions.length === 0) return <div>문제를 불러오는 중...</div>;

  const current = questions[currentIdx];

  const handleSelect = (idx) => {
    if (submitted) return;
    const updated = [...answers];
    updated[currentIdx] = idx;
    setAnswers(updated);
  };

  const handleSubmitAll = () => {
    let score = 0;
    questions.forEach((q, i) => {
      if (answers[i] === q.quiz.correct_answer_index) score++;
    });
    setSubmitted(true);
    navigate('/test-result', {
      state: { score, total: questions.length, answers, questions },
    });
  };

  const goPrev = () => {
    if (currentIdx > 0) setCurrentIdx(currentIdx - 1);
  };

  const goNext = () => {
    if (currentIdx < questions.length - 1) setCurrentIdx(currentIdx + 1);
  };

  return (
    <div className="TestQuestion-layout">
      <div className="TestQuestion-article-box" ref={articleBoxRef}>
        <h3 className="TestQuestion-article-title">{current.article.title || '제목 없음'}</h3>
        <p className="TestQuestion-article-content">{current.article.content || '내용 없음'}</p>
      </div>

      <div className="TestQuestion-right-container">
        <div className="TestQuestion-quiz-box" ref={quizBoxRef}>
          <h4 className="TestQuestion-quiz-title">문제 {currentIdx + 1}</h4>
          <p className="TestQuestion-quiz-question">{current.quiz.question_text}</p>

          {[current.quiz.choice1, current.quiz.choice2, current.quiz.choice3, current.quiz.choice4].map((opt, idx) => (
            <button
              key={idx}
              className={`TestQuestion-quiz-option ${answers[currentIdx] === idx ? 'selected' : ''}`}
              onClick={() => handleSelect(idx)}
              disabled={submitted}
            >
              {String.fromCharCode(65 + idx)}. {opt}
            </button>
          ))}
        </div>

        <div className="TestQuestion-controls">
          <button onClick={goPrev} disabled={currentIdx === 0}>이전</button>
          <button onClick={goNext} disabled={currentIdx === questions.length - 1}>다음</button>
          {currentIdx === questions.length - 1 && !submitted && (
            <button
              className="TestQuestion-submit"
              onClick={handleSubmitAll}
              disabled={answers.includes(null)}
            >
              정답 제출
            </button>
          )}
        </div>

        <div className="TestQuestion-omr" ref={omrGridRef}>
          {questions.map((_, qIdx) => (
            <div key={qIdx} className="TestQuestion-omr-row">
              <span className="TestQuestion-omr-number" onClick={() => setCurrentIdx(qIdx)}>
                {qIdx + 1}
              </span>
              {['A', 'B', 'C', 'D'].map((label, cIdx) => (
                <span
                  key={cIdx}
                  className={`TestQuestion-omr-choice ${answers[qIdx] === cIdx ? 'selected' : ''}`}
                  onClick={() => {
                    if (!submitted) {
                      const updated = [...answers];
                      updated[qIdx] = cIdx;
                      setAnswers(updated);
                    }
                    setCurrentIdx(qIdx);
                  }}
                >
                  {label}
                </span>
              ))}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default TestQuestionPage;
