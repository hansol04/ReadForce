import React, { useEffect, useState } from 'react';
// import api from '../../api/axiosInstance';
import fetchWithAuth from '../../utils/fetchWithAuth';
import { useParams, useNavigate } from 'react-router-dom';
import './css/LiteratureQuestionPage.css';

const LiteratureQuizPage = () => {
  const { quizId } = useParams();
  const navigate = useNavigate();

  const [quiz, setQuiz] = useState(null);
  const [selected, setSelected] = useState(null);
  const [notFound, setNotFound] = useState(false);

  // useEffect(() => {
  //   api.get('/literature/get-literature-quiz-object', {
  //     params: {
  //       literature_paragraph_no: quizId,
  //       literature_no: 3,
  //     },
  //   })
  //     .then((res) => {
  //       if (!res.data || !res.data.question_text) {
  //         setNotFound(true);
  //       } else {
  //         setQuiz(res.data);
  //       }
  //     })
  //     .catch((err) => {
  //       console.error("퀴즈 데이터 불러오기 실패", err);
  //       setNotFound(true);
  //     });
  // }, [quizId]);

  // refresh-token 적용
  useEffect(() => {
    const fetchQuiz = async () => {
      try {
        const response = await fetchWithAuth(
          `/literature/get-literature-quiz-object?literature_paragraph_no=${quizId}&literature_no=3`
        );

        const data = await response.json();

        if (!data || !data.question_text) {
          setNotFound(true);
        } else {
          setQuiz(data);
        }
      } catch (err) {
        console.error("퀴즈 데이터 불러오기 실패", err);
        setNotFound(true);
      }
    };

    fetchQuiz();
  }, [quizId]);

  if (notFound) {
    return (
      <div className="page-container">
        <div className="quiz-notfound-container">
          <div className="warning">❗ 제공된 문제가 없습니다.</div>
          <div className="description">다른 문제를 선택해 주세요.</div>
          <button className="go-back-button" onClick={() => navigate(-1)}>🔙 돌아가기</button>
        </div>
      </div>
    );
  }

  if (!quiz) return <div className="page-container">로딩 중...</div>;

  const options = [
    { text: quiz.choice1 },
    { text: quiz.choice2 },
    { text: quiz.choice3 },
    { text: quiz.choice4 },
  ];

  // const handleSubmit = () => {
  //   if (!selected) return;

  //   const correctAnswerText = options[quiz.correct_answer_index - 1]?.text;
  //   const isCorrect = selected === correctAnswerText;
  //   const explanation = quiz.explanation || '해설이 제공되지 않았습니다.';
  //   const language = '한국어';
  //   const category = quiz.category?.toUpperCase() || 'NOVEL';

  //   navigate('/literature-result', {
  //     state: {
  //       isCorrect,
  //       explanation,
  //       language,
  //       category,
  //     },
  //   });
  // };
  const handleSubmit = async () => {
    if (!selected) return;

    const correctAnswerText = options[quiz.correct_answer_index - 1]?.text;
    const isCorrect = selected === correctAnswerText;
    const explanation = quiz.explanation || '해설이 제공되지 않았습니다.';
    const language = '한국어';
    const category = quiz.category?.toUpperCase() || 'NOVEL';

    try {
      // 정답 인덱스 계산 (1~4 중 선택된 번호)
      const selectedIndex = options.findIndex(opt => opt.text === selected) + 1;

      await fetchWithAuth('/literature/save-member-solved-literature-quiz', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          literature_quiz_no: quiz.literature_quiz_no,
          selected_option_index: selectedIndex,
        }),
      });

      // 저장 성공 후 결과 페이지로 이동
      navigate('/literature-result', {
        state: {
          isCorrect,
          explanation,
          language,
          category,
        },
      });
    } catch (error) {
      console.error('문학 퀴즈 저장 실패:', error);
      alert('문제 저장에 실패했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className="page-container quiz-layout">
      <div className="quiz-passage">
        <h3 className="passage-title">📖 {quiz.title || '문학 발췌문'}</h3>
        <p className="passage-text">{quiz.content || '※ 발췌문 내용은 별도 처리 필요'}</p>
      </div>

      <div className="quiz-box">
        <h4 className="question-heading">💡 문제</h4>
        <p className="question-text">{quiz.question_text.replace(/[<>]/g, '')}</p>
        <div className="quiz-options">
          {options.map((opt, idx) => (
            <button
              key={idx}
              className={`quiz-option ${selected === opt.text ? 'selected' : ''}`}
              onClick={() => setSelected(opt.text)}
            >
              {String.fromCharCode(65 + idx)}. {opt.text}
            </button>
          ))}
        </div>

        <div className="quiz-button-container">
          <button
            className="submit-button"
            disabled={!selected}
            onClick={handleSubmit}
          >
            정답 제출
          </button>
        </div>
      </div>
    </div>
  );
};

export default LiteratureQuizPage;
