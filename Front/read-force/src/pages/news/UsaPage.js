// ✅ 공통 레이아웃 .page-container 반영됨
import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/news/NewsList';

const UsaPage = () => {
  const { handleSolve } = useQuizHandler('navigate', 'us');

  return (
    <div className="page-container">
      <NewsList country="us" onSolve={handleSolve} />
    </div>
  );
};

export default UsaPage;
