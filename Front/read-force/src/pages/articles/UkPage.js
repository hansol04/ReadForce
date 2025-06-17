import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/News/NewsList';

const UsaPage = () => {
  const { handleSolve } = useQuizHandler('navigate', 'uk');

  return (
    <div style={{ padding: '20px' }}>
      <NewsList country="uk" onSolve={handleSolve} />
    </div>
  );
};

export default UsaPage;
