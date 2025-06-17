import React from 'react';
import './css/NewsFilterBar.css';
import Select from 'react-select';

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

  const categoryOptions = [
    { value: 'all', label: '모두' },
    { value: '정치', label: '정치' },
    { value: '경제', label: '경제' },
    { value: '사회', label: '사회' },
    { value: '생활/문화', label: '생활/문화' },
    { value: 'IT/과학', label: 'IT/과학' },
  ];

  const selectedCategory = categoryOptions.find((o) => o.value === category) || categoryOptions[0];

  return (
    <div className="filter-bar-layout">
      
      <div className="filter-left">
        <button
          className={`sort-button ${sort === 'latest' ? 'active' : ''}`}
          onClick={() => setSort('latest')}
        >
          최신순
        </button>
        <button
          className={`sort-button ${sort === 'oldest' ? 'active' : ''}`}
          onClick={() => setSort('oldest')}
        >
          오래된순
        </button>
      </div>

      <div className="filter-center">
        {['초급', '중급', '고급'].map((lvl) => (
          <button key={lvl} onClick={() => setLevel(lvl)} className={levelClass(lvl)}>
            {lvl}
          </button>
        ))}
      </div>

      <div className="filter-right">
        <Select
          className="custom-select"
          classNamePrefix="react-select"
          options={categoryOptions}
          value={selectedCategory}
          onChange={(selected) => setCategory(selected.value)}
          isSearchable={false}
        />
      </div>
    </div>
  );
};

export default NewsFilterBar;
