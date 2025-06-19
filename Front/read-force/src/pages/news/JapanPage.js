// ✅ 공통 레이아웃 .page-container 반영됨
import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/news/NewsList';

const JapanPage = () => {
  const { handleSolve } = useQuizHandler('navigate', 'jp');

  return (
    <div className="page-container">
      <NewsList country="jp" onSolve={handleSolve} />
    </div>
  );
};

export default JapanPage;
