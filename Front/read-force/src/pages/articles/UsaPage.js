import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/News/NewsList';

const UsaPage = () => {
  const { handleSolve } = useQuizHandler('navigate', 'us');

  return (
    <div style={{ padding: '20px' }}>
      <h2>미국 기사</h2>
      <NewsList country="us" onSolve={handleSolve} />
    </div>
  );
};

export default UsaPage;
