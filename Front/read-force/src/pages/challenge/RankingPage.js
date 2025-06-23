import React, { useState, useEffect } from 'react';
import api from '../../api/axiosInstance'; // Assuming you have an axios instance configured
import './RankingPage.css'; // For styling the ranking page

const categories = [
  { label: '소설', classification: 'LITERATURE', type: 'NOVEL', language: null, scoreKey: 'novel' },
  { label: '동화', classification: 'LITERATURE', type: 'FAIRYTALE', language: null, scoreKey: 'fairytale' },
  { label: '뉴스(영어)', classification: 'NEWS', type: null, language: 'ENGLISH', scoreKey: 'english_news' },
  { label: '뉴스(일본어)', classification: 'NEWS', type: null, language: 'JAPANESE', scoreKey: 'japanese_news' },
  { label: '뉴스(한국어)', classification: 'NEWS', type: null, language: 'KOREAN', scoreKey: 'korean_news' },
];

const RankingPage = () => {
  // State to hold the currently selected category for ranking
  const [selectedCategory, setSelectedCategory] = useState(categories[0]);
  // State to store the ranking data fetched from the backend
  const [rankingData, setRankingData] = useState([]);
  // State to manage loading status for better UX
  const [isLoading, setIsLoading] = useState(true);
  // State to handle any errors during API calls
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRanking = async () => {
      setIsLoading(true); // Set loading to true when fetching starts
      setError(null); // Clear previous errors
      const { classification, type, language } = selectedCategory;

      const params = new URLSearchParams();
      params.append('classification', classification);

      // Append 'type' only if it's relevant for LITERATURE classification
      if (classification === 'LITERATURE' && type) {
        params.append('type', type);
      } else {
        // For NEWS classification, or if type is not applicable, send an empty string
        // Your backend currently expects a 'type' parameter even if empty for NEWS
        params.append('type', null);
      }

      // Append 'language' only if it's relevant for NEWS classification
      if (classification === 'NEWS' && language) {
        params.append('language', language);
      } else {
        // For LITERATURE classification, or if language is not applicable, send an empty string
        // Your backend currently expects a 'language' parameter even if empty for LITERATURE
        params.append('language', null);
      }

 console.log(params.toString());

      try {
        const res = await api.get(`/ranking/get-ranking-by-classification-and-type-or-language?${params.toString()}`);
        setRankingData(res.data);
      } catch (err) {
        console.error('❌ 랭킹 API 에러:', err);
        setError('랭킹 정보를 불러오지 못했습니다. 다시 시도해주세요.');
        setRankingData([]); // Clear ranking data on error
      } finally {
        setIsLoading(false); // Set loading to false once fetching is complete
      }
    };

    fetchRanking();
  }, [selectedCategory]); // Re-fetch ranking data whenever selectedCategory changes

  // Helper function to render the correct score based on the selected category's scoreKey
  const renderScore = (user) => {
    // Use optional chaining (?.) and nullish coalescing (??) for safer access and default to 0
    return user[selectedCategory.scoreKey] ?? 0;
  };

  return (
    <div className="ranking-page">
      <h2 className="ranking-title">🏆 문해력 랭킹</h2>

      <div className="category-buttons">
        {categories.map((cat) => (
          <button
            key={cat.label}
            onClick={() => setSelectedCategory(cat)}
            // Add 'active' class to the currently selected button for styling
            className={selectedCategory.label === cat.label ? 'active' : ''}
          >
            {cat.label}
          </button>
        ))}
      </div>

      {isLoading ? (
        <p className="loading-message">랭킹 정보를 불러오는 중입니다...</p>
      ) : error ? (
        <p className="error-message">{error}</p>
      ) : rankingData.length === 0 ? (
        <p className="no-data-message">해당 카테고리의 랭킹 정보가 없습니다.</p>
      ) : (
        <div className="ranking-list">
          {rankingData.map((user, idx) => (
            <div key={idx} className="ranking-item">
              <span className="rank">{idx + 1}위</span>
              <span className="nickname">{user.nickname}</span>
              <span className="score">{renderScore(user)}점</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default RankingPage;