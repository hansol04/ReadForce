import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './challengepage.css';
import api from '../../api/axiosInstance';

const ChallengePage = () => {
  const navigate = useNavigate();
  const [wrongQuestions, setWrongQuestions] = useState([]);

  const handleRankingClick = () => {
    navigate('/ranking');
  };

  const handleSolveClick = (quizNo) => {
    navigate(`/question/${quizNo}`);
  };

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
          <button className="challenge-btn">문제 도전하기</button>
        </div>

        <div className="top5-box">
          <div className="top5-header">
            <h3>주간 TOP 5</h3>
            <button className="ranking-more-btn" onClick={handleRankingClick}>＋</button>
          </div>
          <ol>
            <li><span className="rank gold">1</span> 김기찬 <span className="point">145p</span></li>
            <li><span className="rank silver">2</span> 김제헌 <span className="point">137p</span></li>
            <li><span className="rank bronze">3</span> 이하늘 <span className="point">129p</span></li>
            <li><span className="rank">4</span> 최한솔 <span className="point">112p</span></li>
            <li><span className="rank">5</span> 정웅태 <span className="point">98p</span></li>
          </ol>
        </div>
      </div>

      <div className="wrong-section">
        <div className="wrong-header-row">
          <h3>가장 많이 틀린 문제</h3>
          <div className="right-label">정답률</div>
          <div></div>
        </div>

        {wrongQuestions.map((item, index) => {
          const total = Number(item.total_count);
          const incorrect = Number(item.incorrect_count);
          const correctRate = total > 0 ? Math.round((1 - incorrect / total) * 100) : 0;

          return (
            <div className="wrong-item" key={item.quiz_no ?? `wrong-${index}`}>
              <p>{item.question_text}</p>
              <span>{correctRate}%</span>
              <button onClick={() => handleSolveClick(item.quiz_no)}>문제풀기</button>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ChallengePage;
