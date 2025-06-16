import React, { useState } from 'react';
import './css/NewsFilterBar.css';    
import './css/NewsCard.css';    
import NewsCard from './NewsCard';
import NewsFilterBar from './NewsFilterBar';

const dummyArticles = [
  {
    id: 1,
    title: "한국 첫 뉴스 제목",
    summary: "요약 내용입니다.",
    difficulty: "초급",
    publishedAt: "2025-06-15",
  },
  {
    id: 2,
    title: "또 다른 기사",
    summary: "이건 중급 기사입니다.",
    difficulty: "중급",
    publishedAt: "2025-06-14",
  },
  {
    id: 3,
    title: "세 번째 뉴스",
    summary: "이건 고급 기사입니다.",
    difficulty: "고급",
    publishedAt: "2025-06-13",
  },
  {
    id: 4,
    title: "네 번째 뉴스",
    summary: "이건 초급 기사입니다.",
    difficulty: "초급",
    publishedAt: "2025-06-13",
  },
  {
    id: 5,
    title: "다섯 번째 뉴스",
    summary: "이건 중급 기사입니다.",
    difficulty: "중급",
    publishedAt: "2025-06-13",
  },
  {
    id: 6,
    title: "여섯 번째 뉴스",
    summary: "이건 고급 기사입니다.",
    difficulty: "고급",
    publishedAt: "2025-06-13",
  },
];

const NewsList = ({ country = 'kr', onSolve = () => {} }) => {
  const [level, setLevel] = useState('');
  const [sort, setSort] = useState('latest');
  const [category, setCategory] = useState('모두');

  const filtered = level ? dummyArticles.filter(a => a.difficulty === level) : dummyArticles;

  const sorted = [...filtered].sort((a, b) => {
    return sort === 'latest'
      ? new Date(b.publishedAt) - new Date(a.publishedAt)
      : new Date(a.publishedAt) - new Date(b.publishedAt);
  });

  return (
    <div>
      <NewsFilterBar level={level} setLevel={setLevel} sort={sort} setSort={setSort} category={category} setCategory={setCategory} />

      {sorted.map((item) => (
        <NewsCard key={item.id} article={item} onSolve={onSolve} />
      ))}
    </div>
  );
};

export default NewsList;
