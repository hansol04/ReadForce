import "./main.css";
import React, { useState, useEffect } from "react";
import mainImage from "../assets/image/mainimage.png";
import { useNavigate } from "react-router-dom";

const Main = () => {
  const [slideIndex, setSlideIndex] = useState(0);
  const navigate = useNavigate();

  const slides = [
    {
      title: (
        <>
          문해<span style={{ color: "#439395" }}>력</span>,<br />
          세상을 읽는 힘입니다
        </>
      ),
      description: "한국·일본·미국 뉴스로 나의 문해력을 테스트 해보세요!",
      buttonText: "문해력 테스트 시작하기",
    },
    {
      title: (
        <>
          AI 추천 콘텐츠와 함께<br />
          문해력을 성장시키세요
        </>
      ),
      description: "당신을 위한 맞춤 뉴스와 문제를 제공합니다.",
      buttonText: "AI 추천 콘텐츠 보기",
    },
  ];

  useEffect(() => {
    const interval = setInterval(() => {
      setSlideIndex((prevIndex) => (prevIndex + 1) % slides.length);
    }, 5000);
    return () => clearInterval(interval);
  }, [slides.length]);

  return (
    <div>
      <section className="hero">
        <div className="page-container">
          <div className="hero-content">
            <div className="hero-text">
              <h2>{slides[slideIndex].title}</h2>
              <p>{slides[slideIndex].description}</p>
              <button onClick={() => navigate("/test-start")}>
                {slides[slideIndex].buttonText}
              </button>
            </div>
            <div className="main-hero-image">
              <img src={mainImage} alt="문해력 일러스트" />
            </div>
          </div>
        </div>
      </section>

      {/* 기존 통계, 기사 영역 유지 */}
      <section className="stats-section">
        <div className="page-container stat-container">
          <div className="stat-box top5">
            <h3>🏆 <span className="bold">주간 Top 5</span></h3>
            <div className="tabs">
              <button className="active">한국</button>
              <button>일본</button>
              <button>미국</button>
            </div>
            <table className="top5-table">
              <tbody>
                <tr><td>1</td><td>김기찬</td><td>86,500</td></tr>
                <tr><td>2</td><td>김제현</td><td>85,300</td></tr>
                <tr><td>3</td><td>이하늘</td><td>83,800</td></tr>
                <tr><td>4</td><td>정용태</td><td>81,200</td></tr>
                <tr><td>5</td><td>최한솔</td><td>80,900</td></tr>
              </tbody>
            </table>
          </div>

          <div className="stat-box today-stats">
            <h3>오늘의 통계</h3>
            <div className="grid-2x2">
              <div><div className="number">3,288 명</div><div className="label">오늘의 응시자 수</div></div>
              <div><div className="number">72 %</div><div className="label">응답 정답률</div></div>
              <div><div className="number">15 %</div><div className="label">제출한 학습률</div></div>
              <div><div className="number">68</div><div className="label">틀린 문항 수</div></div>
            </div>
          </div>

          <div className="stat-box wrong-articles">
            <h3>가장 많이 틀린 기사</h3>
            <div className="article">
              <div className="flag">🇯🇵</div>
              <div>
                <div className="title">福島：花の癒し力</div>
                <div className="author">Ueno Yamamoto<br /><span className="sub">NHK World</span></div>
              </div>
            </div>
            <div className="article">
              <div className="flag">🇺🇸</div>
              <div>
                <div className="title">How 'big, beautiful' bill led to big ugly breakup for Trump and Musk</div>
                <div className="author">Anthony Zurcher<br /><span className="sub">North America Correspondent</span></div>
              </div>
            </div>
            <div className="article">
              <div className="flag">🇰🇷</div>
              <div>
                <div className="title">성남·경기도 라인 ‘7인회’ 대통령실 속속 합류</div>
                <div className="author">송경모 기자<br /><span className="sub">국민일보</span></div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Main;
