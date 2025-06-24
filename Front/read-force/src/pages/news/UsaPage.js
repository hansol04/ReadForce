import React, { useEffect, useState } from 'react';
import UniversalList from '../../components/universal/UniversalList';
import {
  fetchNewsListByLanguage,
  fetchNewsListByLanguageAndLevel,
  fetchNewsListByLanguageAndLevelAndCategory
} from '../../api/newsApi';
import NewsCategory from '../../components/NewsCategory';

const UsaPage = () => {
  const [newsItems, setNewsItems] = useState([]);

  const [language] = useState('ENGLISH'); 
  const [level, setLevel] = useState('');
  const [category, setCategory] = useState('');
  const [order_by, setOrderBy] = useState('DESC');

  useEffect(() => {
    const fetchData = async () => {
      try {
        let data = [];

        if (!level && !category) {
          data = await fetchNewsListByLanguage({ language, order_by });
        } else if (level && !category) {
          data = await fetchNewsListByLanguageAndLevel({ language, level, order_by });
        } else if (level && category) {
          data = await fetchNewsListByLanguageAndLevelAndCategory({ language, level, category, order_by });
        }

        setNewsItems(data);
      } catch (err) {
        console.error('뉴스 목록 불러오기 실패:', err);
      }
    };

    fetchData();
  }, [language, level, category, order_by]);

  return (
    <div className="page-container">
   <UniversalList
    items={newsItems}
    language={language}
    level={level}
    setLevel={setLevel}
    category={category}
    setCategory={setCategory}
    order_by={order_by}
    setOrderBy={setOrderBy}
    categoryOptions={NewsCategory}
  />
    </div>
  );
};

export default UsaPage;
