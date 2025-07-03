import React from 'react';
import { useNavigate } from 'react-router-dom';
import UniversalFilterBar from './UniversalFilterBar';
import UniversalCard from './UniversalCard';
import './css/UniversalList.css';

const UniversalList = ({
  items = [],
  level, setLevel,
  category, setCategory,
  order_by, setOrderBy,
  categoryOptions = [],
  onSolve
}) => {
  const filteredItems = items.filter((item) => {
    const matchLevel = level ? item.level === level : true;
    const matchCategory = category ? item.category === category : true;
    return matchLevel && matchCategory;
  });

  const sorted = [...filteredItems].sort((a, b) =>
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
    <div className="UniversalList-container">
      <UniversalFilterBar 
        level={level}
        setLevel={setLevel}
        order_by={order_by}
        setOrderBy={setOrderBy}
        category={category}
        setCategory={setCategory}
        categoryOptions={categoryOptions}
      />

      <div className="UniversalList-list">
        {paginated.length > 0 ? paginated.map((item, index) => (
          <UniversalCard
            key={item.id ?? item.new_passage_no ?? item.news_no ?? `unique-${index}`}
            data={item}
            onSolve={onSolve}
          />
        )) : (
          <div className="UniversalList-no-articles">게시물이 없습니다.</div>
        )}
      </div>

      <div className="UniversalList-pagination">
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
