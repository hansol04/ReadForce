import React, { useEffect, useState } from 'react';
import UniversalFilterBar from '../universal/UniversalFilterBar';
import LiteratureCard from './LiteratureCard'; 

const ClassicList = ({ onSolve }) => {
  const [literatureList, setLiteratureList] = useState([]);
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

  useEffect(() => {
    const fetchLiterature = async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/literature?page=${page}`);
        const data = await res.json();
        setTotalResults(data.totalResults || 0);

        const enriched = (data.items || []).map((item, i) => ({
          ...item,
          difficulty: ['초급', '중급', '고급'][i % 3], // 임의 분류 (원래는 백엔드에서 내려줘야 함)
        }));

        setLiteratureList(enriched);
      } catch (err) {
        console.error('문학 작품 가져오기 실패:', err);
      }
    };
    fetchLiterature();
  }, [page]);

  const filtered = literatureList.filter(
    (item) => level === '전체' || item.difficulty === level
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
      <UniversalFilterBar level={level} setLevel={setLevel} sort={sort} setSort={setSort} />

      {sorted.map((item, i) => (
        <LiteratureCard key={i} data={item} onSolve={onSolve} />
      ))}

      <div style={{ marginTop: '20px', textAlign: 'center' }}>
        <button onClick={() => setPage(1)} disabled={page === 1}>≪</button>
        <button onClick={() => setPage(Math.max(1, startPage - pagesPerBlock))} disabled={startPage === 1}>〈</button>

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

        <button onClick={() => setPage(Math.min(totalPages, startPage + pagesPerBlock))} disabled={endPage === totalPages}>〉</button>
        <button onClick={goToLastBlock} disabled={page === totalPages}>≫</button>
      </div>
    </div>
  );
};

export default ClassicList;
