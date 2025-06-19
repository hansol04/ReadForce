import React, { useEffect, useState } from 'react';
import { useQuizHandler } from '../../hooks/useQuizHandler';
import UniversalList from '../../components/universal/UniversalList';
import { fetchNewsList } from '../../api/newsApi';

const KoreaPage = () => {
  const { handleSolve } = useQuizHandler('navigate', '한국어');
  const [newsItems, setNewsItems] = useState([]);

  useEffect(() => {
    fetchNewsList({ language: '한국어', level: 'all' })
      .then(data => setNewsItems(data))
      .catch(err => console.error('뉴스 데이터 실패', err));
  }, []);

  return (
    <div className="page-container">
      <UniversalList items={newsItems} onSolve={handleSolve} />
    </div>
  );
};

export default KoreaPage;
