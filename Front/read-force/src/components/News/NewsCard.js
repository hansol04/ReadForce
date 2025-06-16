import './css/NewsCard.css';

const NewsCard = ({ article, setSelectedArticle }) => {
  return (
    <div className="news-card">
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <h3 className="news-title">{article.title}</h3>
        <span className={`news-badge ${article.difficulty}`}>{article.difficulty}</span>
      </div>
      <p className="news-summary">{article.summary}</p>
      <p className="news-meta">등록일: {article.publishedAt}</p>
      <button className="news-button" onClick={() => setSelectedArticle(article)}>문제 풀기</button>
    </div>
  );
};

export default NewsCard;