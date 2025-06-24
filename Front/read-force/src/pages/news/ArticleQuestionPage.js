import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router-dom';
import api from '../../api/axiosInstance';
import './css/ArticleQuestionPage.css';

const ArticleQuestionPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [quiz, setQuiz] = useState(null);
  const [article, setArticle] = useState(null);
  const [selected, setSelected] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadedArticle = location.state?.article || {
      news_no: Number(id),
      title: '',
      summary: '',
      content: '',
      language: '한국어',
    };

    if (!loadedArticle.news_no) {
      setError("뉴스 또는 퀴즈 정보를 불러오지 못했습니다.");
      return;
    }

    setArticle(loadedArticle);

    console.log("🔍 요청할 news_no:", loadedArticle.news_no);  // 디버그용 로그

    api.get('/news/get-news-quiz-object', {
      params: { news_no: loadedArticle.news_no }
    })
    .then(res => {
      console.log("✅ 퀴즈 로딩 성공:", res.data);
      setQuiz(res.data);
    })
    .catch(err => {
      console.error("❌ 퀴즈 로딩 실패:", err);
      setError("퀴즈 로딩 중 오류 발생");
    });
  }, [id, location.state]);

  const handleSubmit = () => {
    if (selected === null) return;
    navigate('/question-result', {
      state: {
        isCorrect: selected === quiz.correct_answer_index,
        explanation: quiz.explanation,
        language: article.language || '한국어',
      },
    });
  };

  if (error) return <div className="ArticleQuestion-container">{error}</div>;
  if (!article || !quiz) return <div className="ArticleQuestion-container">로딩 중...</div>;

  return (
    <div className="ArticleQuestion-container">
      <div className="ArticleQuestion-article">
        <h3 className="ArticleQuestion-title">{article.title}</h3>
        <p className="ArticleQuestion-summary">{article.summary}</p>
        <p className="ArticleQuestion-content">{article.content}</p>
      </div>
      <div className="ArticleQuestion-quiz">
        <h4>💡 문제</h4>
        <p>{quiz.question_text}</p>
        <div className="ArticleQuestion-options">
          {[quiz.choice1, quiz.choice2, quiz.choice3, quiz.choice4].map((opt, idx) => (
            <button
              key={idx}
              className={selected === idx ? 'selected' : ''}
              onClick={() => setSelected(idx)}
            >
              {String.fromCharCode(65 + idx)}. {opt}
            </button>
          ))}
        </div>
        <button disabled={selected === null} onClick={handleSubmit}>정답 제출</button>
      </div>
    </div>
  );
};

export default ArticleQuestionPage;
