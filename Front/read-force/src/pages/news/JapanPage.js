// ✅ 공통 레이아웃 .page-container 반영됨
import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/news/NewsList';

const JapanPage = () => {
  const { handleSolve } = useQuizHandler('navigate', '일어');

  return (
    <div className="page-container">
      <h2>일어페이지</h2>
      <NewsList language="일어" onSolve={handleSolve} />
    </div>
  );
};

export default JapanPage;
