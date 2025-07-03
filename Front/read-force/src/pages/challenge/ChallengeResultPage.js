import React, { useEffect, useRef } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './ChallengeResultPage.css';

const ChallengeResultPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { finalScore, maxScore } = location.state || {};

  const email = localStorage.getItem('challenge_email');
  const classification = localStorage.getItem('challenge_classification'); 
  const language = localStorage.getItem('challenge_language'); 
  const type = localStorage.getItem('challenge_type'); 

  const hasSavedRef = useRef(false);

  useEffect(() => {
    if (hasSavedRef.current) return;

    if (!classification || finalScore === undefined) {
      console.warn("점수 저장을 위한 정보가 부족합니다.");
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      console.warn("인증 토큰이 없습니다.");
      return;
    }

    const dto = { classification, point: finalScore };

    if (classification === 'NEWS') {
      if (!language) {
        console.error("뉴스 도전에서 언어 정보가 필요합니다.");
        return;
      }
      dto.language = language;
    } else if (classification === 'LITERATURE') {
      if (!type) {
        console.error("문학 도전에서 유형 정보가 필요합니다.");
        return;
      }
      dto.type = type;
    }

    fetch('/challenge/update-challenge-point', {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(dto),
    })
      .then(res => {
        if (!res.ok) {
          console.error("점수 저장 실패");
        } else {
          console.log("점수 저장 성공");
          hasSavedRef.current = true;
        }
      })
      .catch(err => {
        console.error("점수 저장 중 오류 발생:", err);
      });
  }, [classification, language, type, finalScore]);

  return (
    <div className="ChallengeResultPage-container">
      <h2 className="ChallengeResultPage-title">도전 완료!</h2>
      <p className="ChallengeResultPage-score">총 점수: {finalScore} / {maxScore}</p>
      <button className="ChallengeResultPage-button" onClick={() => navigate('/challenge')}>
        문해력 도전으로 돌아가기
      </button>
    </div>
  );
};

export default ChallengeResultPage;
