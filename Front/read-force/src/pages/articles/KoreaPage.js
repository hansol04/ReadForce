import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/News/NewsList';

const KoreaPage = () => {
  const { handleSolve } = useQuizHandler('navigate', 'kr');

  return (
    <div className="container">
      <NewsList country="kr" onSolve={handleSolve} />
    </div>
  );
};

export default KoreaPage;