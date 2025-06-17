import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/News/NewsList';

const UsaPage = () => {
  const { handleSolve } = useQuizHandler('navigate', 'us');

  return (
    <div style={{ padding: '20px' }}>
      <NewsList country="us" onSolve={handleSolve} />
    </div>
  );
};

export default UsaPage;
