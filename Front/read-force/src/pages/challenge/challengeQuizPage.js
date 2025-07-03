import React, { useEffect, useState, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './ChallengeQuizPage.css';

const ChallengeQuizPage = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const params = new URLSearchParams(location.search);
  const classification = params.get('classification');
  const language = params.get('language');
  const type = params.get('type');

  const [quizzes, setQuizzes] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedAnswer, setSelectedAnswer] = useState(null);
  const [submittedAnswers, setSubmittedAnswers] = useState({});
  const [score, setScore] = useState(0);
  const [timeLeft, setTimeLeft] = useState(30 * 60);
  const [loading, setLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const timerRef = useRef(null);

  const getKoreanLevel = (level) => {
    switch (level) {
      case 'BEGINNER': return '초급';
      case 'INTERMEDIATE': return '중급';
      case 'ADVANCED': return '고급';
      default: return level;
    }
  };

  useEffect(() => {
    if (!classification) {
      alert('classification 파라미터가 필요합니다.');
      navigate('/');
      return;
    }

    localStorage.setItem('challenge_classification', classification);
    if (classification === 'NEWS' && language) {
      localStorage.setItem('challenge_language', language);
    }
    if (classification === 'LITERATURE' && type) {
      localStorage.setItem('challenge_type', type);
    }

    const email = localStorage.getItem('email');
    if (email) localStorage.setItem('challenge_email', email);

    let url = '';
    if (classification === 'NEWS' && language) {
      url = `/challenge/get-news-challenge-quiz?language=${language}`;
    } else if (classification === 'LITERATURE' && type) {
      url = `/challenge/get-literature-challenge-quiz?type=${type}`;
    } else {
      alert('올바른 파라미터가 필요합니다.');
      setLoading(false);
      return;
    }

    const token = localStorage.getItem('token');

    fetch(url, { headers: { Authorization: `Bearer ${token}` } })
      .then(res => res.ok ? res.json() : Promise.reject('문제 불러오기 실패'))
      .then(data => {
        const formatted = data.map(q => ({
          ...q,
          choices: [q.choice1, q.choice2, q.choice3, q.choice4],
          questionText: q.question_text,
          quizNo: q.news_quiz_no || q.literature_quiz_no,
          correctAnswerIndex: q.correct_answer_index,
          score: q.score || 1,
          content: q.content || '',
          title: q.title || '',
          level: q.level || '',
        }));
        setQuizzes(formatted);
        setLoading(false);

        timerRef.current = setInterval(() => {
          setTimeLeft(prev => {
            if (prev <= 1) {
              clearInterval(timerRef.current);
              alert('시간이 종료되었습니다!');
              return 0;
            }
            return prev - 1;
          });
        }, 1000);
      })
      .catch(() => {
        alert('문제 불러오기 실패');
        setLoading(false);
      });

    return () => clearInterval(timerRef.current);
  }, [classification, language, type, navigate]);

  const currentQuiz = quizzes[currentIndex];
  const isLastQuestion = currentIndex === quizzes.length - 1;

  const handleSelect = (idx) => setSelectedAnswer(idx);

  const handleNext = () => {
    if (selectedAnswer === null || isSubmitting) {
      alert('답변을 선택해주세요.');
      return;
    }

    setIsSubmitting(true);
    const isCorrect = selectedAnswer === currentQuiz.correctAnswerIndex;
    const newScore = isCorrect ? score + currentQuiz.score : score;

    setScore(newScore);
    setSubmittedAnswers(prev => ({ ...prev, [currentQuiz.quizNo]: selectedAnswer }));
    setSelectedAnswer(null);

    setTimeout(() => {
      setCurrentIndex(currentIndex + 1);
      setIsSubmitting(false);
    }, 500);
  };

  const handleSubmit = () => {
    if (selectedAnswer === null) {
      alert('답변을 선택해주세요.');
      return;
    }

    const isCorrect = selectedAnswer === currentQuiz.correctAnswerIndex;
    const final = isCorrect ? score + currentQuiz.score : score;

    setSubmittedAnswers(prev => ({ ...prev, [currentQuiz.quizNo]: selectedAnswer }));

    navigate('/challenge/result', {
      state: {
        finalScore: final,
        maxScore: quizzes.reduce((sum, q) => sum + q.score, 0),
      },
    });
  };

  const formatTime = (sec) => {
    const m = String(Math.floor(sec / 60)).padStart(2, '0');
    const s = String(sec % 60).padStart(2, '0');
    return `${m}:${s}`;
  };

  if (loading) return <p>문제 로딩중...</p>;
  if (quizzes.length === 0) return <p>문제가 없습니다.</p>;

  return (
    <div className="ChallengeQuizPage-layout">
      <div className="ChallengeQuizPage-article">
        <h3 className="ChallengeQuizPage-title">{currentQuiz.title}</h3>
        {currentQuiz.level && (
          <div className="ChallengeQuizPage-level">난이도: {getKoreanLevel(currentQuiz.level)}</div>
        )}
        <div>{currentQuiz.content}</div>
      </div>

      <div className="ChallengeQuizPage-quiz-box">
        <div className="ChallengeQuizPage-header">
          <strong>문제 {currentIndex + 1}</strong>
          <span className="ChallengeQuizPage-timer">남은 시간: {formatTime(timeLeft)}</span>
        </div>

        <div className="ChallengeQuizPage-question">{currentQuiz.questionText}</div>
        {currentQuiz.choices.map((choice, i) => (
          choice && (
            <button
              key={i}
              className={`ChallengeQuizPage-option ${selectedAnswer === i ? 'selected' : ''}`}
              onClick={() => handleSelect(i)}
              disabled={isSubmitting}
            >
              {choice}
            </button>
          )
        ))}

        <button
          className="ChallengeQuizPage-submit"
          onClick={isLastQuestion ? handleSubmit : handleNext}
          disabled={isSubmitting}
        >
          {isLastQuestion ? '제출하기' : '다음 문제'}
        </button>
      </div>
    </div>
  );
};

export default ChallengeQuizPage;
