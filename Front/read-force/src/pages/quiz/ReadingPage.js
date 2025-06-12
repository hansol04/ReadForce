import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const cleanText = (text) => {
  if (!text) return '내용이 없습니다.';
  return text
    .replace(/\s{2,}/g, ' ')
    .replace(/\.(?=\S)/g, '. ')
    .replace(/(\.|\!|\?)\s*(?=[가-힣])/g, '$1\n')
    .trim();
};

const ReadingPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { article, quiz, from } = location.state || {};
  const [countdown, setCountdown] = useState(30);

  useEffect(() => {
    if (!article || !quiz) {
      alert('잘못된 접근입니다.');
      navigate('/');
      return;
    }

    const timer = setInterval(() => {
      setCountdown((prev) => {
        if (prev <= 1) {
          clearInterval(timer);
          navigate('/quiz', {
            state: { article, quiz, from } // ✅ from 같이 넘기기
          });
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [navigate, article, quiz, from]);

  return (
    <div style={{ padding: '30px', maxWidth: '800px', margin: '0 auto' }}>
      <h2>📰 기사 읽기</h2>
      <p style={{
        whiteSpace: 'pre-wrap',
        lineHeight: 1.6,
        marginTop: '20px',
        fontSize: '16px',
      }}>
        {cleanText(article?.content)}
      </p>
      <div style={{ marginTop: '40px', textAlign: 'center', fontStyle: 'italic' }}>
        ⏳ 퀴즈로 자동 이동 중… ({countdown}초)
      </div>
    </div>
  );
};

export default ReadingPage;
