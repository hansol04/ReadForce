import React from 'react';

const FilterBar = ({ category, setCategory, level, setLevel, sort, setSort }) => {
  return (
    <div style={{ display: 'flex', gap: '10px', marginBottom: '20px' }}>
      <select value={category} onChange={(e) => setCategory(e.target.value)}>
        <option value="all">모두</option>
        <option value="정치">정치</option>
        <option value="경제">경제</option>
        <option value="사회">사회</option>
        <option value="생활/문화">생활/문화</option>
        <option value="IT/과학">IT/과학</option>
      </select>

      {['초급', '중급', '고급'].map((lvl) => (
        <button
          key={lvl}
          onClick={() => setLevel(lvl)}
          style={{
            backgroundColor: level === lvl ? '#4fc3f7' : '#e0e0e0',
            border: 'none',
            padding: '5px 10px',
            borderRadius: '4px',
          }}
        >
          {lvl}
        </button>
      ))}

      <select value={sort} onChange={(e) => setSort(e.target.value)}>
        <option value="latest">최신순</option>
        <option value="oldest">오래된순</option>
      </select>
    </div>
  );
};

export default FilterBar;
