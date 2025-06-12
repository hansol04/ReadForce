// import './header.css';
// import React, { useState } from "react";
// import { useNavigate } from "react-router-dom";
// import { Link } from "react-router-dom";
// const Header = () => {
//     const [showLangMenu, setShowLangMenu] = useState(false);
//     const [selectedLang, setSelectedLang] = useState('한국어');
//     const navigate = useNavigate();

//     const handleLangSelect = (lang) => {
//         setSelectedLang(lang);
//         setShowLangMenu(false);
//     };

    
//     const isLoggedIn = !!localStorage.getItem("token");

//     const handleLogout = () => {
//         localStorage.removeItem("token");
//         navigate("/"); 
//     };

//     return (
//         <div>
//             <header className="header">
//                 <h1 className="title">
//                     <a href="/" style={{ textDecoration: 'none', color: 'inherit' }}>
//                         오늘의 <span style={{ color: "#14b8a6" }}>문해력</span>
//                     </a>
//                 </h1>

//                 <nav className="nav">

//                 <Link to="/korea">한국기사</Link>
//                 <Link to="/japan">일본기사</Link>
//                 <Link to="/usa">미국기사</Link>
//                 <Link to="/challenge">문해력 도전</Link>

//                 </nav>

//                 <div className="auth-buttons">
//                     <div className="lang-selector">
//                         <button
//                             className="lang-button"
//                             onClick={() => setShowLangMenu(!showLangMenu)}
//                         >
//                             🌐 {selectedLang} ▼
//                         </button>
//                         {showLangMenu && (
//                             <div className="lang-menu">
//                                 <div onClick={() => handleLangSelect('한국어')}>🇰🇷 한국어</div>
//                                 <div onClick={() => handleLangSelect('日本語')}>🇯🇵 日本語</div>
//                                 <div onClick={() => handleLangSelect('English')}>🇺🇸 English</div>
//                             </div>
//                         )}
//                     </div>


                   
//                     {isLoggedIn ? (
//                         <>
//                             <button onClick={handleLogout}>로그아웃</button>
//                             <button onClick={() => navigate("/mypage")}>마이페이지</button>
//                         </>
//                     ) : (
//                         <>
//                             <button onClick={() => navigate("/login")}>로그인</button>
//                             <button onClick={() => navigate("/signup/signupchoice")}>회원가입</button>
//                         </>
//                     )}

//                 </div>
//             </header>
//         </div>
//     );
// };

// export default Header;
import './header.css';
import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";

const Header = () => {
  const [showLangMenu, setShowLangMenu] = useState(false);
  const [selectedLang, setSelectedLang] = useState('한국어');
  const [showUserMenu, setShowUserMenu] = useState(false);
  const [nickname, setNickname] = useState('');
  const navigate = useNavigate();

  const isLoggedIn = !!localStorage.getItem("token");

  useEffect(() => {
    if (isLoggedIn) {
      const storedNickname = localStorage.getItem("nickname");
      setNickname(storedNickname || "사용자");
    }
  }, [isLoggedIn]);

  const handleLangSelect = (lang) => {
    setSelectedLang(lang);
    setShowLangMenu(false);
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("nickname");
    setShowUserMenu(false);
    navigate("/");
  };

  return (
    <header className="header">
      <h1 className="title">
        <a href="/" style={{ textDecoration: 'none', color: 'inherit' }}>
          오늘의 <span style={{ color: "#14b8a6" }}>문해력</span>
        </a>
      </h1>

      <nav className="nav">
        <Link to="/korea">한국기사</Link>
        <Link to="/japan">일본기사</Link>
        <Link to="/usa">미국기사</Link>
        <Link to="/challenge">문해력 도전</Link>
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

        {isLoggedIn ? (
          <div className="user-menu-wrapper">
            <button className="nickname-button" onClick={() => setShowUserMenu(!showUserMenu)}>
              {nickname}님 ▼
            </button>
            {showUserMenu && (
              <div className="user-dropdown">
                <div onClick={() => { setShowUserMenu(false); navigate("/mypage"); }}>마이페이지</div>
                <div onClick={handleLogout}>로그아웃</div>
              </div>
            )}
          </div>
        ) : (
          <>
            <button onClick={() => navigate("/login")}>로그인</button>
            <button onClick={() => navigate("/signup/signupchoice")}>회원가입</button>
          </>
        )}
      </div>
    </header>
  );
};

export default Header; 