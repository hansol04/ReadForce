// ✅ 공통 레이아웃 .page-container 반영됨
import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/news/NewsList';

const UsaPage = () => {
  const { handleSolve } = useQuizHandler('navigate', '영어');

  return (
    <div className="page-container">
      <h2>영어페이지</h2>
      <NewsList language="영어" onSolve={handleSolve} />
    </div>
  );
};

export default UsaPage;
