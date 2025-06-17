// ✅ 공통 레이아웃 .page-container 반영됨
import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/News/NewsList';

const UkPage = () => {
  const { handleSolve } = useQuizHandler('navigate', 'uk');

  return (
    <div className="page-container">
      <NewsList country="uk" onSolve={handleSolve} />
    </div>
  );
};

export default UkPage;
