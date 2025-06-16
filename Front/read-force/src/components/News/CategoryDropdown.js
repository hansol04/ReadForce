import React, { useState } from 'react';

const CategoryDropdown = ({ selected, onChange }) => {
  const [open, setOpen] = useState(false);
  const categories = ['모두', '정치', '경제', '사회', '생활/문화', 'IT/과학'];

  return (
    <div style={{ position: 'relative', display: 'inline-block' }}>
      <button
        onClick={() => setOpen(!open)}
        className="dropdown-button"
        >
        {selected} ⌄
      </button>
        {open && (
        <ul className="dropdown-menu">
            {categories.map((cat) => (
            <li
                key={cat}
                onClick={() => {
                onChange(cat);
                setOpen(false);
                }}
                className="dropdown-item"
                style={{
                backgroundColor: selected === cat ? '#eee' : '#fff'
                }}
            >
                {cat}
            </li>
            ))}
        </ul>
        )}
    </div>
  );
};

export default CategoryDropdown;
