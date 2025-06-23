import React, { useEffect, useState, useCallback, useMemo } from 'react';
import UniversalList from '../../components/universal/UniversalList';
import { fetchNewsListByLanguage, fetchNewsListByLanguageAndLevel, fetchNewsListByLanguageAndLevelAndCategory } from '../../api/newsApi';
import debounce from 'lodash/debounce';

  const levelMap = {
    BEGINNER: '초급',
    INTERMEDIATE: '중급',
    ADVANCED: '고급',
  };

  const categoryMap = {
    POLITICS:'정치',
    ECONOMY:'경제',
    SOCIETY:'사회',
    CULTURE:'생활/문화',
    SCIENCE:'IT/과학',
    ETC:'기타',
  };
      
  const KoreaPage = () => {
    const [newsItems, setNewsItems] = useState([]);
    const [language] = useState('KOREAN');
    const [level, setLevel] = useState('');
    const [category, setCategory] = useState('');
    const [order_by, setOrderBy] = useState('DESC');

    const fetchNews = useCallback(async (params) => {
    const apiLevel = levelMap[params.level] || '';
    const apiCategory = categoryMap[params.category] || '';
    try {
      if (!params.level && !params.category) {
        return await fetchNewsListByLanguage({ language: params.language, order_by: params.order_by });
      } else if (params.level && !params.category) {
        return await fetchNewsListByLanguageAndLevel({ language: params.language, level: apiLevel, order_by: params.order_by });
      } else if (params.level && params.category) {
        return await fetchNewsListByLanguageAndLevelAndCategory({ language: params.language, level: apiLevel, category: apiCategory, order_by: params.order_by });
      }
    } catch (err) {
      console.error('뉴스 목록 불러오기 실패:', err);
      return [];
    }
  }, []);

  const debouncedFetch = useMemo(() => debounce(async (params) => {
    const data = await fetchNews(params);
    setNewsItems(data);
  }, 300), [fetchNews]);

    const fetchData = useCallback(() => {
      debouncedFetch({ language, level, category, order_by });
    }, [debouncedFetch, language, level, category, order_by]);

    useEffect(() => {
      fetchData();
      return () => {
        debouncedFetch.cancel();
      };
    }, [fetchData, debouncedFetch]);

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
      />
    </div>
  );
};

export default KoreaPage;
