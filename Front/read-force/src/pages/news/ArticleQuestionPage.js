import React, { useState } from "react";
import "../challenge/readtest.css";
import { useNavigate } from "react-router-dom";

const ReadTest = () => {
  const navigate = useNavigate();

  // ✅ 백엔드 enum과 정확히 일치하는 값으로 설정
  const [language, setLanguage] = useState("KOREAN");

  const handleStart = () => {
    navigate("/test/1", { state: { language } }); // ✅ 전달값도 그대로
  };

  return (
    <div className="readtest-wrapper">
      <h2 className="readtest-title">당신의 문해력은 어느 정도일까요?</h2>

      <div className="readtest-card">
        <h3>
          <strong>
            <a style={{ textDecoration: 'none', color: 'inherit' }}>
              리드 <span style={{ color: "#439395" }}>포스</span>
            </a>
          </strong>는 뉴스 기반 문해력 테스트 플랫폼입니다.
        </h3>
        <p><strong><i>AI</i></strong>가 뉴스를 요약하고, 우리는 문제를 풀며 <strong>문해력</strong>을 기릅니다.</p>
        <p>세상을 <strong>읽는 힘</strong>, 지금부터 시작하세요.</p>
      </div>

      {/* 언어 선택 버튼 */}
      <div className="language-buttons">
        <button
          className={language === "KOREAN" ? "lang-btn active" : "lang-btn"}
          onClick={() => setLanguage("KOREAN")}
        >
          한국어
        </button>
        <button
          className={language === "ENGLISH" ? "lang-btn active" : "lang-btn"}
          onClick={() => setLanguage("ENGLISH")}
        >
          English
        </button>
        <button
          className={language === "JAPANESE" ? "lang-btn active" : "lang-btn"}
          onClick={() => setLanguage("JAPANESE")}
        >
          日本語
        </button>
      </div>

      <button className="readtest-btn" onClick={handleStart}>
        문해력 테스트 시작하기
      </button>
    </div>
  );
};

export default ReadTest;
