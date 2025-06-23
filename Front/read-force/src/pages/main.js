import "./main.css";
import React, { useState, useEffect } from "react";
import mainImage from "../assets/image/mainimage.png";
import slide2Image from "../assets/image/slide2.png";
import { useNavigate } from "react-router-dom";

const Main = () => {
  const [slideIndex, setSlideIndex] = useState(0);
  const [isPaused, setIsPaused] = useState(false);
  const navigate = useNavigate();

  const slides = [
    {
      image: mainImage,
      title: (
        <>
          문해<span style={{ color: "#439395" }}>력</span>,<br />
          세상을 읽는 힘입니다
        </>
      ),
      description: "한국·일본·미국 뉴스로 나의 문해력을 테스트 해보세요!",
      buttonText: "문해력 테스트 시작하기",
      buttonLink: "/test-start",
    },
    {
      image: slide2Image,
      title: (
        <>
          AI 추천 콘텐츠와 함께<br />
          문해력을 성장시키세요
        </>
      ),
      description: "국내 베스트셀러 1위 //누적 30만부 돌파!!",
      buttonText: "책 구매하러가기",
      buttonLink: "https://www.kyobobook.co.kr/",
    },
  ];

  useEffect(() => {
    if (isPaused) return;
    const interval = setInterval(() => {
      setSlideIndex((prev) => (prev + 1) % slides.length);
    }, 5000);
    return () => clearInterval(interval);
  }, [slides.length, isPaused]);

  const currentSlide = slides[slideIndex];

  const handleButtonClick = () => {
    if (currentSlide.buttonLink) {
      if (currentSlide.buttonLink.startsWith("http")) {
        window.open(currentSlide.buttonLink, "_blank");
      } else {
        navigate(currentSlide.buttonLink);
      }
    }
  };

  const goToPrev = () => {
    setSlideIndex((prev) => (prev - 1 + slides.length) % slides.length);
  };

  const goToNext = () => {
    setSlideIndex((prev) => (prev + 1) % slides.length);
  };

  const togglePause = () => {
    setIsPaused((prev) => !prev);
  };

  return (
    <div>
      <section className="hero-fullwidth">
        <div className="hero-overlay">
          <div className="hero-inner">
            <div className="hero-text">
              <h2>{currentSlide.title}</h2>
              <p>{currentSlide.description}</p>
              {currentSlide.buttonText && (
                <button onClick={handleButtonClick}>
                  {currentSlide.buttonText}
                </button>
              )}
            </div>
            <div className="hero-image">
              <img src={currentSlide.image} alt="슬라이드 이미지" />
            </div>
          </div>

          <button className="slide-arrow left" onClick={goToPrev}>⮜</button>
          <button className="slide-arrow right" onClick={goToNext}>⮞</button>
          <div className="slide-ui">
            <button onClick={togglePause}>{isPaused ? "▶" : "⏸"}</button>
            <span>{String(slideIndex + 1).padStart(2, '0')} / {String(slides.length).padStart(2, '0')}</span>
          </div>
        </div>
      </section>

      {/* 통계 영역 */}
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
              <div>
                <div className="number">3,288 명</div>
                <div className="label">오늘의 응시자 수</div>
              </div>
              <div>
                <div className="number">72 %</div>
                <div className="label">응답 정답률</div>
              </div>
              <div>
                <div className="number">15 %</div>
                <div className="label">제출한 학습률</div>
              </div>
              <div>
                <div className="number">68</div>
                <div className="label">틀린 문항 수</div>
              </div>
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
