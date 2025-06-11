import React, { useState } from 'react';
import NewsList from '../../components/NewsList';
import QuizModal from '../../components/QuizModal';

const KoreaPage = () => {
  const [quizData, setQuizData] = useState(null);

  const handleSolve = async (article) => {
    const token = localStorage.getItem('token'); // ✅ JWT 토큰 가져오기

    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    const content = article.content || article.description;
    if (!content) {
      alert("기사 내용이 없습니다. 다른 기사를 선택해주세요.");
      return;
    }

    try {
      const res = await fetch('http://localhost:8080/api/quiz/generate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ article: content }),
      });

      if (res.ok) {
        const quiz = await res.json();
        setQuizData(quiz);
      } else if (res.status === 401) {
        alert('인증되지 않은 사용자입니다. 다시 로그인해주세요.');
      } else {
        const errMsg = await res.text();
        alert('⚠️ 퀴즈 생성 중 오류가 발생했습니다:\n' + errMsg);
        console.error('Server error:', res.status);
      }
    } catch (err) {
      console.error('요청 실패:', err);
      alert('네트워크 오류로 퀴즈 생성에 실패했습니다.');
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>한국 기사</h2>
      <NewsList country="kr" onSolve={handleSolve} />
      <QuizModal quiz={quizData} onClose={() => setQuizData(null)} />
    </div>
  );
};

export default KoreaPage;
