import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/News/NewsList';

const JapanPage = () => {
  const { handleSolve } = useQuizHandler('navigate', 'jp');

  return (
    <div style={{ padding: '20px' }}>
      <NewsList country="jp" onSolve={handleSolve} />
    </div>
  );
};

export default JapanPage;
