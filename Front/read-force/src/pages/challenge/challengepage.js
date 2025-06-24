import React, { useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import './challengepage.css';
import api from '../../api/axiosInstance';

const ChallengePage = () => {
  const navigate = useNavigate();
  const [wrongQuestions, setWrongQuestions] = useState([]);
  const [top5Data, setTop5Data] = useState([]);
  const debounceRef = useRef(null);

  const [selectedCategory, setSelectedCategory] = useState({
    label: '뉴스(한국어)',
    type: 'NEWS',
    language: 'KOREAN',
    contentKey: 'korean_news',
  });

  const categories = [
    { label: '뉴스(한국어)', type: 'NEWS', language: 'KOREAN', contentKey: 'korean_news' },
    { label: '뉴스(일본어)', type: 'NEWS', language: 'JAPANESE', contentKey: 'japanese_news' },
    { label: '뉴스(영어)', type: 'NEWS', language: 'ENGLISH', contentKey: 'english_news' },
    { label: '소설', type: 'LITERATURE', subType: 'NOVEL', contentKey: 'novel' },
    { label: '동화', type: 'LITERATURE', subType: 'FAIRYTALE', contentKey: 'fairytale' },
  ];

  const handleRankingClick = () => navigate('/ranking');
  const handleSolveClick = (quizNo) => navigate(`/question/${quizNo}`);
  const handleChallengeClick = () => navigate('/challenge-start');

  useEffect(() => {
    const fetchWrongQuestions = async () => {
      try {
        const res = await api.get('/quiz/get-most-incorrected-quiz');
        setWrongQuestions(res.data);
      } catch (error) {
        console.error('가장 많이 틀린 문제 불러오기 실패:', error);
      }
    };
    fetchWrongQuestions();
  }, []);

  useEffect(() => {
    if (debounceRef.current) clearTimeout(debounceRef.current);
    debounceRef.current = setTimeout(async () => {
      try {
        let res;
        if (selectedCategory.type === 'NEWS') {
          res = await api.get(`/ranking/get-news-ranking?language=${selectedCategory.language}`);
        } else {
          res = await api.get(`/ranking/get-literature-ranking?type=${selectedCategory.subType}`);
        }
        setTop5Data(res.data.slice(0, 5));
      } catch (err) {
        console.error('TOP 5 랭킹 불러오기 실패:', err);
        setTop5Data([]);
      }
    }, 500);

    return () => clearTimeout(debounceRef.current);
  }, [selectedCategory]);

  return (
    <div className="page-container">
      <h2 className="title">“ 오늘 <span className="highlight">812명</span>이 도전했어요! ”</h2>

      <div className="top-container">
        <div className="stats-box">
          <h3>실시간 통계</h3>
          <div className="grid-4x2">
            <div><div className="label">응시자 수</div><div className="number">20,180</div></div>
            <div><div className="label">총 문제</div><div className="number">100</div></div>
            <div><div className="label">푼 문제</div><div className="number">23</div></div>
            <div><div className="label">정답률</div><div className="number">74%</div></div>
          </div>
          <button className="challenge-btn" onClick={handleChallengeClick}>문제 도전하기</button>
        </div>

        <div className="top5-box">
          <div className="top5-header">
            <h3>주간 TOP 5</h3>
            <button className="ranking-more-btn" onClick={handleRankingClick}>＋</button>
          </div>

          <div className="top5-tabs">
            {categories.map((cat) => (
              <button
                key={cat.label}
                className={selectedCategory.label === cat.label ? 'active' : ''}
                onClick={() => setSelectedCategory(cat)}
              >
                {cat.label}
              </button>
            ))}
          </div>

          <ol>
            {top5Data.map((user, index) => (
              <li key={user.nickname}>
                <span className={`rank ${index === 0 ? 'gold' : index === 1 ? 'silver' : index === 2 ? 'bronze' : ''}`}>
                  {index + 1}
                </span>
                {user.nickname}
                <span className="point">{user[selectedCategory.contentKey] ?? 0}p</span>
              </li>
            ))}
          </ol>
        </div>
      </div>

      {/* 가장 많이 틀린 문제 */}
      <div className="wrong-section">
        <div className="wrong-header-row">
          <h3>가장 많이 틀린 문제</h3>
          <div className="right-label">정답률</div>
          <div></div>
        </div>

        {wrongQuestions.map((item, index) => {
          const correctRate = 100 - item.percentage;
          return (
            <div className="wrong-item" key={item.news_quiz_no ?? item.literature_quiz_no ?? `wrong-${index}`}>
              <p>{item.question_text}</p>
              <span>{correctRate}%</span>
              <button onClick={() => handleSolveClick(item.news_quiz_no ?? item.literature_quiz_no)}>문제풀기</button>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ChallengePage;
