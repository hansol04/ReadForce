import React, { useEffect, useState } from 'react';
import FilterBar from './FilterBar';
import NewsCard from './NewsCard';

const NewsList = ({ country, onSolve }) => {
  const [articles, setArticles] = useState([]);
  const [category, setCategory] = useState('all');
  const [level, setLevel] = useState('전체');
  const [sort, setSort] = useState('latest');
  const [page, setPage] = useState(1);
  const [totalResults, setTotalResults] = useState(0);

  const pageSize = 5;
  const pagesPerBlock = 10;
  const totalPages = Math.ceil(totalResults / pageSize);
  const currentBlock = Math.floor((page - 1) / pagesPerBlock);
  const startPage = currentBlock * pagesPerBlock + 1;
  const endPage = Math.min(startPage + pagesPerBlock - 1, totalPages);

  const categorizeArticle = (text) => {
    const content = text.toLowerCase();
    if (/정치|정부|대통령|의회|선거|국회/.test(content)) return '정치';
    if (/경제|금리|무역|환율|증시|소비자|투자|체결/.test(content)) return '경제';
    if (/사회|범죄|교육|복지|노동|인권|잠수함/.test(content)) return '사회';
    if (/생활|문화|연예|음식|관광|건강|패션|축구|뮤지컬/.test(content)) return '생활/문화';
    if (/it|과학|기술|ai|로봇|인터넷|우주/.test(content)) return 'IT/과학';
    return '기타';
  };

  useEffect(() => {
    const fetchNews = async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/news?country=${country}&page=${page}`);
        const data = await res.json();
        setTotalResults(data.totalResults || 0);
        const enriched = (data.articles || []).map((a, i) => {
          const text = `${a.title || ''} ${a.description || ''}`;
          return {
            ...a,
            difficulty: ['초급', '중급', '고급'][i % 3],
            category: categorizeArticle(text),
          };
        });
        setArticles(enriched);
      } catch (err) {
        console.error('뉴스 가져오기 실패:', err);
      }
    };
    fetchNews();
  }, [country, page]);

  const filtered = articles.filter(
    (a) =>
      (level === '전체' || a.difficulty === level) &&
      (category === 'all' || a.category === category)
  );

  const sorted = filtered.sort((a, b) =>
    sort === 'latest'
      ? new Date(b.publishedAt) - new Date(a.publishedAt)
      : new Date(a.publishedAt) - new Date(b.publishedAt)
  );

  const goToLastBlock = () => {
    const lastBlock = Math.floor((totalPages - 1) / pagesPerBlock);
    setPage(lastBlock * pagesPerBlock + 1);
  };

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

      <div style={{ marginTop: '20px', textAlign: 'center' }}>
        <button
          onClick={() => setPage(1)}
          disabled={page === 1}
          style={{ marginRight: 8 }}
        >
          ≪
        </button>

        <button
          onClick={() => setPage(Math.max(1, startPage - pagesPerBlock))}
          disabled={startPage === 1}
        >
          〈
        </button>

        {Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i).map((pageNumber) => (
          <button
            key={pageNumber}
            onClick={() => setPage(pageNumber)}
            style={{
              margin: '0 4px',
              padding: '6px 12px',
              fontWeight: page === pageNumber ? 'bold' : 'normal',
              backgroundColor: page === pageNumber ? '#eee' : 'transparent',
              border: '1px solid #ccc',
              borderRadius: '4px',
              cursor: 'pointer',
            }}
          >
            {pageNumber}
          </button>
        ))}

        <button
          onClick={() => setPage(Math.min(totalPages, startPage + pagesPerBlock))}
          disabled={endPage === totalPages}
        >
          〉
        </button>

        <button
          onClick={goToLastBlock}
          disabled={page === totalPages}
          style={{ marginLeft: 8 }}
        >
          ≫
        </button>
      </div>
    </div>
  );
};

export default NewsList;
