import React from 'react';
import './css/UniversalFilterBar.css';
import Select from 'react-select';

const UniversalFilterBar = ({ level, setLevel, category, setCategory, order_by, setOrderBy }) => {
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
    { value: '', label: '모두' },
    { value: 'POLITICS', label: '정치' },
    { value: 'ECONOMY', label: '경제' },
    { value: 'SOCIETY', label: '사회' },
    { value: 'CULTURE', label: '생활/문화' },
    { value: 'SCIENCE', label: 'IT/과학' },
    { value: 'ETC', label: '기타' },
  ];

  const selectedCategory = categoryOptions.find((o) => o.value === category) || categoryOptions[0];

  const handleLevelClick = (lvl) => {
    if (level === lvl) {
      setLevel(''); // 이미 선택된 거 다시 누르면 전체 보기
    } else {
      setLevel(lvl);
    }
  };

  return (
    <div className="filter-bar-layout">
      <div className="filter-left">
        <button
          className={`sort-button ${order_by === 'latest' ? 'active' : ''}`}
          onClick={() => setOrderBy('latest')}
        >
          최신순
        </button>
        <button
          className={`sort-button ${order_by === 'oldest' ? 'active' : ''}`}
          onClick={() => setOrderBy('oldest')}
        >
          오래된순
        </button>
      </div>

      <div className="filter-center">
        {['초급', '중급', '고급'].map((lvl) => (
          <button key={lvl} onClick={() => handleLevelClick(lvl)} className={levelClass(lvl)}>
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

export default UniversalFilterBar;
