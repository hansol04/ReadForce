import React, { useEffect, useState, useCallback, useMemo } from 'react';
import UniversalList from '../../components/universal/UniversalList';
import {
  fetchNewsListByLanguage,
  fetchNewsListByLanguageAndLevel,
  fetchNewsListByLanguageAndLevelAndCategory
} from '../../api/newsApi';
import debounce from 'lodash/debounce';

const reverseLevelMap = {
  '초급': 'BEGINNER',
  '중급': 'INTERMEDIATE',
  '고급': 'ADVANCED',
};

const reverseCategoryMap = {
  '정치': 'POLITICS',
  '경제': 'ECONOMY',
  '사회': 'SOCIETY',
  '생활/문화': 'CULTURE',
  'IT/과학': 'SCIENCE',
  '기타': 'ETC',
};

const KoreaPage = () => {
  const [newsItems, setNewsItems] = useState([]);
  const [language] = useState('KOREAN');
  const [level, setLevel] = useState('');
  const [category, setCategory] = useState('');
  const [order_by, setOrderBy] = useState('latest');

  const fetchNews = useCallback(async (params) => {
    try {
      if (!params.level && !params.category) {
        return await fetchNewsListByLanguage({
          language: params.language,
          order_by: params.order_by
        });
      } else if (params.level && !params.category) {
        return await fetchNewsListByLanguageAndLevel({
          language: params.language,
          level: params.level,
          order_by: params.order_by
        });
      } else if (params.level && params.category) {
        return await fetchNewsListByLanguageAndLevelAndCategory({
          language: params.language,
          level: params.level,
          category: params.category,
          order_by: params.order_by
        });
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
    const apiLevel = reverseLevelMap[level] || '';
    const apiCategory = reverseCategoryMap[category] || '';
    debouncedFetch({
      language,
      level: apiLevel,
      category: apiCategory,
      order_by
    });
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
