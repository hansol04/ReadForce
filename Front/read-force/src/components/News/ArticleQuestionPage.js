// ✅ 공통 레이아웃 .page-container 반영됨
import { useParams } from 'react-router-dom';
import ArticleQuestion from './ArticleQuestion';

const dummyArticles = [
  {
    id: 1,
    title: "임시 뉴스 제목",
    summary: "이건 임시 뉴스입니다.",
    difficulty: "초급",
    publishedAt: "2025-06-16",
  }
];

const ArticleQuestionPage = () => {
  const { id } = useParams();
  const article = dummyArticles.find(a => a.id === Number(id));

  if (!article) {
    return <div className="page-container"><p>기사를 찾을 수 없습니다.</p></div>;
  }

  return (
    <div className="page-container">
      <ArticleQuestion article={article} />
    </div>
  );
};

export default ArticleQuestionPage;
