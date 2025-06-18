// ✅ 공통 레이아웃 .page-container 반영됨
import React from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import ClassicList from '../../components/literature/ClassicList';

const ClassicPage = () => {
  const { handleSolve } = useQuizHandler('navigate', 'classic');

  return (
    <div className="page-container">
      <h2>고전 문학</h2>
      <ClassicList category="classic" onSolve={handleSolve} />
    </div>
  );
};

export default ClassicPage;

