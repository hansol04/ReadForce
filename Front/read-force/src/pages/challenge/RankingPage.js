import React, { useState, useEffect } from 'react';
import api from '../../api/axiosInstance';
import './RankingPage.css';

const categories = [
  { label: '소설', value: 'NOVEL' },
  { label: '동화', value: 'FAIRYTALE' },
  { label: '뉴스(영어)', value: 'NEWS_ENGLISH' },
  { label: '뉴스(일본어)', value: 'NEWS_JAPANESE' },
  { label: '뉴스(한국어)', value: 'NEWS_KOREAN' },
];

const RankingPage = () => {
  const [selectedCategory, setSelectedCategory] = useState('NOVEL');
  const [rankingData, setRankingData] = useState([]);

  useEffect(() => {
    api.get(`/ranking/top50?category=${selectedCategory}`)
      .then(res => setRankingData(res.data))
      .catch(() => alert('랭킹 정보를 불러오지 못했습니다.'));
  }, [selectedCategory]);

  return (
    <div className="ranking-page">
      <h2>🏆 문해력 랭킹</h2>
      <div className="category-buttons">
        {categories.map(cat => (
          <button
            key={cat.value}
            onClick={() => setSelectedCategory(cat.value)}
            className={selectedCategory === cat.value ? 'active' : ''}
          >
            {cat.label}
          </button>
        ))}
      </div>
      <div className="ranking-list">
        {rankingData.map((user, idx) => (
          <div key={user.member_id} className="ranking-item">
            <span className="rank">{idx + 1}위</span>
            <span className="nickname">{user.nickname}</span>
            <span className="score">{user.score}점</span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default RankingPage;
