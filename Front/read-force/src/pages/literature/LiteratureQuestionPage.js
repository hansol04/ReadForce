import React, { useEffect, useState } from 'react';
import api from '../../api/axiosInstance';
import { useParams } from 'react-router-dom';
import './css/LiteratureQuestionPage.css'; // 필요 시 문학.css로 교체

const LiteratureQuizPage = () => {
  const { quizId } = useParams();
  const [quiz, setQuiz] = useState(null);
  const [selected, setSelected] = useState(null);
  const [isSubmitted, setIsSubmitted] = useState(false);

  useEffect(() => {
    api.get('/literature/get-literature-quiz-object', {
      params: {
        literature_paragraph_no: quizId,
        literature_no: 3,
      },
    })
      .then((res) => {
        console.log('퀴즈 데이터 응답:', res.data);
        setQuiz(res.data);
      })
      .catch((err) => console.error("퀴즈 데이터 불러오기 실패", err));
  }, [quizId]);

  if (!quiz) return <div>로딩 중...</div>;

  const options = [
    { text: quiz.choice1 },
    { text: quiz.choice2 },
    { text: quiz.choice3 },
    { text: quiz.choice4 },
  ];

  const handleSubmit = () => {
    if (selected !== null) {
      setIsSubmitted(true);
    }
  };

  const correctAnswerText = options[quiz.correct_answer_index - 1]?.text;
  const isCorrect = selected === correctAnswerText;

  return (
    <div className="page-container article-question-layout">
      <div className="article-box">
        <h3 className="article-title">📖 문학 발췌문</h3>
        <p className="article-content">{quiz.content || '※ 발췌문 내용은 별도 처리 필요'}</p>
      </div>

      <div className="quiz-box">
        <h4 className="quiz-title">💡 문제</h4>
        <p className="quiz-question">{quiz.question_text}</p>
        <div className="quiz-options">
          {options.map((opt, idx) => (
            <button
              key={idx}
              className={`quiz-option ${selected === opt.text ? 'selected' : ''}`}
              onClick={() => setSelected(opt.text)}
              disabled={isSubmitted}
            >
              {String.fromCharCode(65 + idx)}. {opt.text}
            </button>
          ))}
        </div>

        <div className="quiz-button-container">
          {!isSubmitted ? (
            <button className="submit-button" disabled={!selected} onClick={handleSubmit}>
              정답 제출
            </button>
          ) : (
            <div className="quiz-result">
              <p>{isCorrect ? '✅ 정답입니다!' : '❌ 오답입니다.'}</p>
              <p className="explanation">해설: {quiz.explanation}</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default LiteratureQuizPage;
