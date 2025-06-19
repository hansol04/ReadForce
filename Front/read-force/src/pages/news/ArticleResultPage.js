import React from 'react';
import './css/ArticleResultPage.css';
import { useNavigate } from 'react-router-dom';

const ArticleResultPage = () => {
  const navigate = useNavigate();

  return (
    <div className="result-wrapper">
      <div className="result-card">
        <h2>🎉 당신의 문해력은 <span className="highlight">중급</span>입니다!</h2>
        <p>9문제 중 6문제를 맞았어요</p>
        <p>전체 응시자 중<br />상위 34%에 해당해요!</p>
        <div className="button-group">
          <button onClick={() => navigate('/challenge')}>해설보기</button>
          <button onClick={() => navigate(-1)}>다시 도전하기</button>
        </div>
      </div>
    </div>
  );
};

export default ArticleResultPage;
