import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/NewsList';

const JapanPage = () => {
  const { handleSolve } = useQuizHandler('navigate', 'jp');

  return (
    <div style={{ padding: '20px' }}>
      <h2>일본 기사</h2>
      <NewsList country="jp" onSolve={handleSolve} />
    </div>
  );
};

export default JapanPage;
