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
    return <p>기사를 찾을 수 없습니다.</p>;
  }

  return <ArticleQuestion article={article} />;
};

export default ArticleQuestionPage;
