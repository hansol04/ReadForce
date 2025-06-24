import React from 'react';
import { useNavigate } from 'react-router-dom';
import UniversalFilterBar from './UniversalFilterBar';
import UniversalCard from './UniversalCard';
import './css/UniversalList.css';

// 📌 함수 선언 먼저
const categorizeArticle = (text) => {
  if (!text) return 'ETC';
  if (text.includes('정치')) return 'POLITICS';
  if (text.includes('경제')) return 'ECONOMY';
  if (text.includes('사회')) return 'SOCIETY';
  if (text.includes('문화')) return 'CULTURE';
  if (text.includes('과학') || text.includes('IT')) return 'SCIENCE';
  return 'ETC';
};

const UniversalList = ({
  items = [],
  level, setLevel,
  category, setCategory,
  order_by, setOrderBy
}) => {
  const navigate = useNavigate();

  const handleSolve = (article) => {
    navigate(`/question/${article.news_no}`, {
      state: { article },
    });
  };

  // ✅ 함수 아래에서 사용
  const enriched = items.map(article => ({
    ...article,
    category: categorizeArticle((article.title || '') + ' ' + (article.content || '')),
  }));

  const filtered = enriched.filter(a => {
    const levelMatch = level === '' || a.level === level;
    const categoryMatch = category === '' || a.category === category;
    return levelMatch && categoryMatch;
  });

  const sorted = [...filtered].sort((a, b) =>
    order_by === 'latest'
      ? new Date(b.publishedAt) - new Date(a.publishedAt)
      : new Date(a.publishedAt) - new Date(b.publishedAt)
  );

  const itemsPerPage = 5;
  const [currentPage, setCurrentPage] = React.useState(1);
  const totalPages = Math.ceil(sorted.length / itemsPerPage);
  const pageGroupSize = 5;
  const currentGroup = Math.floor((currentPage - 1) / pageGroupSize);
  const startPage = currentGroup * pageGroupSize + 1;
  const endPage = Math.min(startPage + pageGroupSize - 1, totalPages);
  const visiblePages = Array.from(
    { length: endPage - startPage + 1 },
    (_, i) => startPage + i
  );

  const paginated = sorted.slice(
    (currentPage - 1) * itemsPerPage,
    currentPage * itemsPerPage
  );

  return (
    <div className="news-quiz-container">
      <UniversalFilterBar
        level={level}
        setLevel={setLevel}
        category={category}
        setCategory={setCategory}
        order_by={order_by}
        setOrderBy={setOrderBy}
      />

      <div className="news-list">
        {paginated.length === 0 ? (
          <p className="no-articles">조건에 맞는 기사가 없습니다.</p>
        ) : (
          paginated.map((item, index) => (
            <UniversalCard
              key={item.news_no ?? `unique-${index}`}
              data={item}
              onSolve={handleSolve}
            />
          ))
        )}
      </div>

      <div className="pagination">
        <button onClick={() => setCurrentPage(startPage - 1)} disabled={startPage === 1}>«</button>
        {visiblePages.map((pageNum) => (
          <button
            key={pageNum}
            onClick={() => setCurrentPage(pageNum)}
            className={currentPage === pageNum ? "active" : ""}
          >
            {pageNum}
          </button>
        ))}
        <button onClick={() => setCurrentPage(endPage + 1)} disabled={endPage === totalPages}>»</button>
      </div>
    </div>
  );
};

export default UniversalList;
