import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import ClassicList from '../../components/literature/ClassicList';

const ClassicPage = () => {
  const { handleSolve } = useQuizHandler('navigate', 'classic');

  return (
    <div style={{ padding: '20px' }}>
      <h2>고전 문학</h2>
      <ClassicList category="classic" onSolve={handleSolve} />
    </div>
  );
};

export default ClassicPage;
