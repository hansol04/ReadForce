import React from 'react';
import './css/UniversalFilterBar.css';
import Select from 'react-select';


const UniversalFilterBar = ({ level, setLevel, category, setCategory, order_by, setOrderBy, categoryOptions }) => {

  const levelClass = (lvl) => {
    const base =
      lvl === '초급'
        ? 'level-button beginner'
        : lvl === '중급'
        ? 'level-button intermediate'
        : 'level-button advanced';
    return `${base} ${level === lvl ? 'selected' : ''}`;
  };

  const levelOptions = [
    { value: '', label: '전체' },  
    { value: 'BEGINNER', label: '초급' },
    { value: 'INTERMEDIATE', label: '중급' },
    { value: 'ADVANCED', label: '고급' },
  ];

  const selectedCategory = categoryOptions.find((o) => o.value === category) || categoryOptions[0];


  const handleLevelClick = (value) => {
    if (level === value) {
      setLevel(''); 

    } else {
      setLevel(value);
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
        {levelOptions.slice(1).map((opt) => (
          <button
            key={opt.value}
            onClick={() => handleLevelClick(opt.value)}
            className={`level-button ${
              opt.value === 'BEGINNER' ? 'beginner' :
              opt.value === 'INTERMEDIATE' ? 'intermediate' : 'advanced'
            } ${level === opt.value ? 'selected' : ''}`}
          >
            {opt.label}
          </button>
        ))}
      </div>

      <div className="filter-right">
        <Select
          className="custom-select"
          classNamePrefix="react-select"
          options={categoryOptions}

          value={categoryOptions.find((o) => o.value === category) || categoryOptions[0]}

          onChange={(selected) => setCategory(selected.value)}
          isSearchable={false}
        />
      </div>
    </div>
  );
};

export default UniversalFilterBar;