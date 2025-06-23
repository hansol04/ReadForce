import React from 'react';
import UniversalFilterBar from './UniversalFilterBar';
import UniversalCard from './UniversalCard';
import './css/UniversalList.css';

const categorizeArticle = (text) => {
  const content = text.toLowerCase();
  const categories = {
    '정치': ['정치', '정부', '대통령', '의회', '선거', '국회'],
    '경제': ['경제', '금리', '무역', '환율', '증시', '소비자', '투자', '체결'],
    '사회': ['사회', '범죄', '교육', '복지', '노동', '인권', '잠수함'],
    '생활/문화': ['생활', '문화', '연예', '음식', '관광', '건강', '패션', '축구', '뮤지컬'],
    'IT/과학': ['it', '과학', '기술', 'ai', '로봇', '인터넷', '우주'],
  };
  const counts = {};
  for (const [category, keywords] of Object.entries(categories)) {
    counts[category] = keywords.reduce((acc, word) => acc + (content.includes(word) ? 1 : 0), 0);
  }
  const topCategory = Object.entries(counts).sort((a, b) => b[1] - a[1])[0];
  return topCategory[1] > 0 ? topCategory[0] : '기타';
};

const categoryEnumMap = {
  '정치': 'POLITICS',
  '경제': 'ECONOMY',
  '사회': 'SOCIETY',
  '생활/문화': 'CULTURE',
  'IT/과학': 'SCIENCE',
  '기타': 'ETC'
};

const UniversalList = ({
  items = [],
  level, setLevel,
  category, setCategory,
  order_by, setOrderBy
}) => {

  	console.log('===== 카테고리 디버깅 시작 =====');
      items.forEach((item, index) => {
        console.log(`[${index}] title:`, item.title);
        console.log(`[${index}] category:`, item.category);
      });
      console.log('현재 선택된 category 필터:', category);
        
  const enriched = items.map(article => ({
    ...article,
    autoCategory: categorizeArticle((article.title || '') + ' ' + (article.content || '')),
  }));

  const filteredItems = items.filter((item) => {
  const matchLevel = level ? item.level === level : true;
  const matchCategory = category ? item.category === category.toUpperCase() : true;
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
            <UniversalCard key={item.id ?? item.new_passage_no ?? `unique-${index}`} data={item} />
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
