import React, { useState, useEffect } from 'react';
import api from '../../api/axiosInstance';
import './RankingPage.css';

const categories = [
  { label: '소설', classification: 'LITERATURE', type: 'NOVEL', language: 'NONE', scoreKey: 'novel' },
  { label: '동화', classification: 'LITERATURE', type: 'FAIRYTALE', language: 'NONE', scoreKey: 'fairytale' },
  { label: '뉴스(영어)', classification: 'NEWS', type: 'NONE', language: 'ENGLISH', scoreKey: 'english_news' },
  { label: '뉴스(일본어)', classification: 'NEWS', type: 'NONE', language: 'JAPANESE', scoreKey: 'japanese_news' },
  { label: '뉴스(한국어)', classification: 'NEWS', type: 'NONE', language: 'KOREAN', scoreKey: 'korean_news' },
];

const RankingPage = () => {
  const [selectedCategory, setSelectedCategory] = useState(categories[0]);
  const [rankingData, setRankingData] = useState([]);

  useEffect(() => {
    const fetchRanking = async () => {
      const { classification, type, language } = selectedCategory;

      const params = new URLSearchParams();
      params.append('classification', classification);
      params.append('type', type);
      params.append('language', language);

      try {
        const res = await api.get(`/ranking/get-ranking-by-classification-and-type-or-language?${params.toString()}`);
        setRankingData(res.data);
      } catch (err) {
        console.error('❌ 랭킹 API 에러:', err);
        alert('랭킹 정보를 불러오지 못했습니다.');
      }
    };

    fetchRanking();
  }, [selectedCategory]);

  const renderScore = (user) => {
    const scoreKey = selectedCategory.scoreKey;
    return user[scoreKey] ?? 0;
  };

  return (
    <div className="ranking-page">
      <h2>🏆 문해력 랭킹</h2>
      <div className="category-buttons">
        {categories.map((cat) => (
          <button
            key={cat.label}
            onClick={() => setSelectedCategory(cat)}
            className={selectedCategory.label === cat.label ? 'active' : ''}
          >
            {cat.label}
          </button>
        ))}
      </div>
      <div className="ranking-list">
        {rankingData.map((user, idx) => (
          <div key={idx} className="ranking-item">
            <span className="rank">{idx + 1}위</span>
            <span className="nickname">{user.nickname}</span>
            <span className="score">{renderScore(user)}점</span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default RankingPage;