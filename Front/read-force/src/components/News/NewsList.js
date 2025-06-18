import React, { useEffect, useState } from 'react';
import './css/NewsFilterBar.css';
import './css/NewsCard.css';
import NewsCard from './NewsCard';
import NewsFilterBar from './NewsFilterBar';
import './css/NewsList.css';
import axios from 'axios';

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

const NewsList = ({ country = 'kr', onSolve = () => {} }) => {
  const [articles, setArticles] = useState([]);
  const [level, setLevel] = useState('');
  const [sort, setSort] = useState('latest');
  const [category, setCategory] = useState('');

  useEffect(() => {
    //if (!level) return; // level이 없으면 요청 안 보냄

  //   axios.get("/news/get-news-passage-list", {
  //     params: {
  //       country: "kr",
  //       level: level
  //     }
  //   })
  //   .then(res => {
  //     console.log("응답 데이터 확인 👉", res.data);
  //     const enriched = res.data.map(article => ({
  //       ...article,
  //       category: categorizeArticle(article.title + ' ' + article.summary),
  //     }));
  //     setArticles(enriched);
  //   })
  //   .catch(err => console.error('뉴스 로딩 실패', err));
  // }, [country, level]);

    const dummyArticles = [
    {
      id: 1,
      title: "AI 기술이 바꾸는 미래",
      summary: "AI 기술이 다양한 산업에 도입되며 삶의 질을 변화시키고 있다.",
      difficulty: "초급",
      publishedAt: "2025-06-16",
    },
    {
      id: 2,
      title: "기후 변화 대응 위한 국제 협약 체결",
      summary: "전 세계가 협력해 기후 위기에 대응하는 협약을 체결했다.",
      difficulty: "중급",
      publishedAt: "2025-06-15",
    }
  ];

  const enriched = dummyArticles.map(article => ({
    ...article,
    category: categorizeArticle(article.title + ' ' + article.summary),
  }));
  setArticles(enriched);
}, [country, level]);

  const filtered = articles.filter((a) => {
    const levelMatch = level ? a.difficulty === level : true;
    const categoryMatch = category ? a.category === category : true;
    return levelMatch && categoryMatch;
  });

  const sorted = [...filtered].sort((a, b) =>
    sort === 'latest'
      ? new Date(b.publishedAt) - new Date(a.publishedAt)
      : new Date(a.publishedAt) - new Date(b.publishedAt)
  );

  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;

  const paginated = sorted.slice(
  (currentPage - 1) * itemsPerPage,
  currentPage * itemsPerPage
);

  const totalPages = Math.ceil(sorted.length / itemsPerPage);

  const pageGroupSize = 5;
  const currentGroup = Math.floor((currentPage - 1) / pageGroupSize);
  const startPage = currentGroup * pageGroupSize + 1;
  const endPage = Math.min(startPage + pageGroupSize - 1, totalPages);

  const visiblePages = Array.from(
    { length: endPage - startPage + 1 },
    (_, i) => startPage + i
  );

  return (
    <div className="news-quiz-container">
      <NewsFilterBar
        level={level}
        setLevel={setLevel}
        sort={sort}
        setSort={setSort}
        category={category}
        setCategory={setCategory}
      />

      <div className="news-list">
        {paginated.length === 0 ? (
          <p className="no-articles">조건에 맞는 기사가 없습니다.</p>
        ) : (
          paginated.map((item) => <NewsCard key={item.id} article={item} />)
        )}
      </div>

      <div className="pagination">
        <button
          onClick={() => setCurrentPage(startPage - 1)}
          disabled={startPage === 1}
        >
          «
        </button>

        {visiblePages.map((pageNum) => (
          <button
            key={pageNum}
            onClick={() => setCurrentPage(pageNum)}
            className={currentPage === pageNum ? "active" : ""}
          >
            {pageNum}
          </button>
        ))}

        <button
          onClick={() => setCurrentPage(endPage + 1)}
          disabled={endPage === totalPages}
        >
          »
        </button>
      </div>
    </div>
  );
};

export default NewsList;
