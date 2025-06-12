import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import NewsList from '../../components/NewsList';

const KoreaPage = () => {
 const { handleSolve } = useQuizHandler('navigate', 'kr');


  return (
    <div style={{ padding: '20px' }}>
      <h2>한국 기사</h2>
      <NewsList country="kr" onSolve={handleSolve} />
    </div>
  );
};

export default KoreaPage;
