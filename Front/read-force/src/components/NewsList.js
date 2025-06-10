import React, { useEffect, useState } from 'react';
import FilterBar from './FilterBar';
import NewsCard from './NewsCard';

const NewsList = ({ country, onSolve }) => {
  const [articles, setArticles] = useState([]);
  const [category, setCategory] = useState('all');
  const [level, setLevel] = useState('전체');
  const [sort, setSort] = useState('latest');
  const [page, setPage] = useState(1);

  useEffect(() => {
    const fetchNews = async () => {
      const res = await fetch(`https://newsapi.org/v2/top-headlines?country=${country}&pageSize=5&page=${page}&apiKey=52047c836f2d4b659280136c95e8132c`);
      const data = await res.json();
      const enriched = (data.articles || []).map((a, i) => ({
        ...a,
        difficulty: ['초급', '중급', '고급'][i % 3],
        category: ['정치', '경제', '사회', '생활/문화', 'IT/과학'][i % 5],
      }));
      setArticles(enriched);
    };
    fetchNews();
  }, [country, page]);

  const filtered = articles.filter((a) =>
    (level === '전체' || a.difficulty === level) &&
    (category === 'all' || a.category === category)
  );

  const sorted = filtered.sort((a, b) => {
    return sort === 'latest'
      ? new Date(b.publishedAt) - new Date(a.publishedAt)
      : new Date(a.publishedAt) - new Date(b.publishedAt);
  });

  return (
    <div>
      <FilterBar
        category={category}
        setCategory={setCategory}
        level={level}
        setLevel={setLevel}
        sort={sort}
        setSort={setSort}
      />

      {sorted.map((article, i) => (
        <NewsCard key={i} article={article} onSolve={onSolve} />
      ))}

      <div style={{ marginTop: '20px' }}>
        <button disabled={page <= 1} onClick={() => setPage(page - 1)}>〈</button>
        <span> {page} </span>
        <button onClick={() => setPage(page + 1)}>〉</button>
      </div>
    </div>
  );
};

export default NewsList;
