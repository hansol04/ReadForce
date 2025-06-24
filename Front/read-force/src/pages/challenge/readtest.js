import React, { useState } from "react";
import "./readtest.css";
import { useNavigate } from "react-router-dom";

const ReadTest = () => {
  const navigate = useNavigate();
  const [language, setLanguage] = useState("KOREAN");

  const handleStart = () => {
    navigate("/test-question", { state: { language } });
  };

  return (
    <div className="ReadTest-wrapper">
      <h2 className="ReadTest-title">당신의 문해력은 어느 정도일까요?</h2>

      <div className="ReadTest-card">
        <h3>
          <strong>
            <span style={{ textDecoration: 'none', color: 'inherit' }}>
              리드 <span style={{ color: "#439395" }}>포스</span>
            </span>
          </strong>
          는 뉴스 기반 문해력 테스트 플랫폼입니다.
        </h3>
        <p>
          <strong><i>AI</i></strong>가 뉴스를 요약하고, 우리는 문제를 풀며 <strong>문해력</strong>을 기릅니다.
        </p>
        <p>세상을 <strong>읽는 힘</strong>, 지금부터 시작하세요.</p>
      </div>

      <div className="ReadTest-language-buttons">
        <button
          className={language === "KOREAN" ? "ReadTest-lang-btn active" : "ReadTest-lang-btn"}
          onClick={() => setLanguage("KOREAN")}
        >
          한국어
        </button>
        <button
          className={language === "ENGLISH" ? "ReadTest-lang-btn active" : "ReadTest-lang-btn"}
          onClick={() => setLanguage("ENGLISH")}
        >
          English
        </button>
        <button
          className={language === "JAPANESE" ? "ReadTest-lang-btn active" : "ReadTest-lang-btn"}
          onClick={() => setLanguage("JAPANESE")}
        >
          日本語
        </button>
      </div>

      <button className="ReadTest-btn" onClick={handleStart}>
        문해력 테스트 시작하기
      </button>
    </div>
  );
};

export default ReadTest;
