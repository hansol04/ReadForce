import './css/NewsCard.css';
import { useNavigate } from 'react-router-dom';

const NewsCard = ({ article }) => {
  const navigate = useNavigate();

  return (
    <div className="news-card">
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <h3 className="news-title">{article.title}</h3>
        <span className={`news-badge ${article.difficulty}`}>{article.difficulty}</span>
      </div>
      
      <p className="news-summary">{article.summary}</p>

      <div className="news-footer">
        <div>
          <p className="news-date">등록일: {article.publishedAt}</p>
          <p className="news-category"># {article.category}</p>
        </div>
        <button className="news-button" onClick={() => navigate(`/question/${article.id}`)}>
          문제 풀기
        </button>
      </div>
    </div>
  );
};

export default NewsCard;
