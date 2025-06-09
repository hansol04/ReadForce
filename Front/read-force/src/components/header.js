import './header.css';
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const Header = () => {
    const [showLangMenu, setShowLangMenu] = useState(false);
    const [selectedLang, setSelectedLang] = useState('한국어');

    const handleLangSelect = (lang) => {
        setSelectedLang(lang);
        setShowLangMenu(false);
    };

      const navigate = useNavigate();

    return (
        <div>
            <header className="header">
                <h1 className="title">
                    <a href="/" style={{ textDecoration: 'none', color: 'inherit' }}>
                        오늘의 <span style={{ color: "#14b8a6" }}>문해력</span>
                    </a>
                </h1>
                <nav className="nav">
                    <a href="#">한국기사</a>
                    <a href="#">일본기사</a>
                    <a href="#">미국기사</a>
                    <a href="#">문해력 도전</a>
                </nav>
                <div className="auth-buttons">
                    <div className="lang-selector">
                        <button
                            className="lang-button"
                            onClick={() => setShowLangMenu(!showLangMenu)}
                        >
                            🌐 {selectedLang} ▼
                        </button>
                        {showLangMenu && (
                            <div className="lang-menu">
                                <div onClick={() => handleLangSelect('한국어')}>🇰🇷 한국어</div>
                                <div onClick={() => handleLangSelect('日本語')}>🇯🇵 日本語</div>
                                <div onClick={() => handleLangSelect('English')}>🇺🇸 English</div>
                            </div>
                        )}
                    </div>
                    <button>로그인</button>
                    <button onClick={() => navigate("/signupchoice")}>회원가입</button>
                </div>
            </header>
        </div>
    );
}

export default Header;