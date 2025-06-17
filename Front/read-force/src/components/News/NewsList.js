import React, { useState } from 'react';
import './css/NewsFilterBar.css';
import './css/NewsCard.css';
import NewsCard from './NewsCard';
import NewsFilterBar from './NewsFilterBar';

const dummyArticles = [
  {
    id: 1,
    title: "대통령이 새로운 정책을 발표했습니다",
    summary: "식품의약품안전처는 ‘이마트24’가 판매한 식용얼음 ‘이프레소 얼음컵’(epresso ice cup)이 ‘세균수 기준규격 부적합’으로 확인됐다고 16일 밝혔다. 충남 아산시 식품 제조 가공업소 ‘블루파인’이 제조한 이프레소 얼음컵은 충남 아산시청에서 판매 중단 및 회수 조치 중이다. 회수 대상 제품의 제조 일자는 ‘2025.05.28’인 제품이다. 포장 단위는 180g이다. 해당 회수 식품 등을 보관하고 있는 판매자는 판매를 중지하고 회수 영업자에게 반품하고, 해당 제품을 구입한 소비자는 회수 대상 업소로 반납해 위해 식품 등 회수에 적극 협조해 달라고 식약처는 당부했다. 시중에 판매 중인 베트남산 ‘냉동 아욱’에서는 잔류농약(뷰프로페진)이 기준치(0.01㎎/㎏ 이하)보다 초과 검출돼 식약처가 해당 제품을 판매 중단하고 회수 조치하고 있다.",
    difficulty: "초급",
    publishedAt: "2025-06-16",
  },
  {
    id: 2,
    title: "AI 기술의 발전과 로봇의 미래",
    summary: "과학과 기술의 최전선에서 일어나는 변화들.",
    difficulty: "중급",
    publishedAt: "2025-06-15",
  },
  {
    id: 3,
    title: "축구 국가대표팀, 월드컵 본선 진출",
    summary: "국민들의 관심을 모은 경기에서 대승 거둬",
    difficulty: "고급",
    publishedAt: "2025-06-14",
  }
];

const categorizeArticle = (text) => {
  const content = text.toLowerCase();
  if (/정치|정부|대통령|의회|선거|국회/.test(content)) return '정치';
  if (/경제|금리|무역|환율|증시|소비자|투자|체결/.test(content)) return '경제';
  if (/사회|범죄|교육|복지|노동|인권|잠수함/.test(content)) return '사회';
  if (/생활|문화|연예|음식|관광|건강|패션|축구|뮤지컬/.test(content)) return '생활/문화';
  if (/it|과학|기술|ai|로봇|인터넷|우주/.test(content)) return 'IT/과학';
  return '기타';
};

const initialArticles = dummyArticles.map(article => ({
  ...article,
  category: categorizeArticle(article.title + ' ' + article.summary),
}));

const NewsList = ({ country = 'kr', onSolve = () => {} }) => {
  const [articles, setArticles] = useState(initialArticles);
  const [level, setLevel] = useState('');
  const [sort, setSort] = useState('latest');
  const [category, setCategory] = useState('');

  const filtered = articles.filter((a) => {
    const levelMatch = level ? a.difficulty === level : true;
    const categoryMatch = category ? a.category === category : true;
    return levelMatch && categoryMatch;
  });

  const sorted = [...filtered].sort((a, b) =>
    sort === 'latest'
      ? new Date(b.publishedAt) - new Date(a.publishedAt)
      : new Date(a.publishedAt) - new Date(b.publishedAt)
  );

  return (
    <div className="news-quiz-container">
      <NewsFilterBar
        level={level}
        setLevel={setLevel}
        sort={sort}
        setSort={setSort}
        category={category}
        setCategory={setCategory}
      />
      <div className="news-list">
        {sorted.map((item) => (
          <NewsCard key={item.id} article={item} />
        ))}
      </div>
    </div>
  );
};

export default NewsList;
