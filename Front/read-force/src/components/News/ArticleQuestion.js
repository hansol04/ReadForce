import ArticleQuestion from './ArticleQuestion';

const ArticleQuestionPage = () => {
  const article = {
    id: 1,
    title: "임시 뉴스 제목",
    summary: "이건 임시 뉴스입니다.",
    difficulty: "초급",
    publishedAt: "2025-06-16",
  };

  return <ArticleQuestion article={article} />;
};

export default ArticleQuestionPage;