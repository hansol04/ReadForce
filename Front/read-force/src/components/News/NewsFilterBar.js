import React from 'react';
import './css/NewsFilterBar.css';
import SortDropdown from './SortDropdown';

const NewsFilterBar = ({ level, setLevel, sort, setSort, category, setCategory }) => {
  const levelClass = (lvl) => {
    const base =
      lvl === '초급'
        ? 'level-button beginner'
        : lvl === '중급'
        ? 'level-button intermediate'
        : 'level-button advanced';
    return `${base} ${level === lvl ? 'selected' : ''}`;
  };

  return (
    <div className="filter-bar-layout">
      <div className="filter-left">
        <select value={category} onChange={(e) => setCategory(e.target.value)} className="dropdown">
          <option value="all">모두</option>
          <option value="정치">정치</option>
          <option value="경제">경제</option>
          <option value="사회">사회</option>
          <option value="생활/문화">생활/문화</option>
          <option value="IT/과학">IT/과학</option>
        </select>
      </div>

      <div className="filter-center">
        {['초급', '중급', '고급'].map((lvl) => (
          <button key={lvl} onClick={() => setLevel(lvl)} className={levelClass(lvl)}>
            {lvl}
          </button>
        ))}
      </div>

      <div className="filter-right">
        <select
            className="select-box" value={sort} onChange={(e) => setSort(e.target.value)}>
            <option value="latest">최신순</option>
            <option value="oldest">오래된순</option>
        </select>
      </div>
    </div>
  );
};

export default NewsFilterBar;
