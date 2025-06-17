import { useNavigate } from 'react-router-dom';
import { useState } from 'react';

export const useQuizHandler = (mode = 'navigate', country = 'kr') => {
  const navigate = useNavigate();
  const [quizData, setQuizData] = useState(null);

  const getLanguage = (code) => {
    switch (code) {
      case 'us': return 'English';
      case 'jp': return 'Japanese';
      default: return '한국어';
    }
  };

  const getFromPath = (code) => {
    switch (code) {
      case 'us': return '/usa';
      case 'jp': return '/japan';
      default: return '/korea';
    }
  };

  const handleSolve = async (article) => {
    const token = localStorage.getItem('token');
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    const language = getLanguage(country);
    const from = getFromPath(country); // 시작 경로 계산

    try {
      const transformRes = await fetch('http://localhost:8080/api/news/transform', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ title: article.title, description: article.description, content: article.content, language }),
      });

      const { rewritten } = await transformRes.json();

      const res = await fetch('http://localhost:8080/api/quiz/generate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ article: rewritten, language }),
      });

      const quiz = await res.json();

      if (mode === 'navigate') {
        navigate('/reading', {
          state: {
            article: { content: rewritten },
            quiz,
            from // 포함
          }
        });
      } else {
        setQuizData(quiz);
      }
    } catch (err) {
      alert('⚠️ 오류 발생:\n' + err.message);
    }
  };

  return { handleSolve, quizData, setQuizData };
};
