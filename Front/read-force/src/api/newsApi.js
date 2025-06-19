import api from './axiosInstance';

export const fetchNewsList = async ({ language = '한국어', level = 'all' }) => {
  try {
    const res = await api.get('/news/get-news-passage-list', {
      params: { language, level },
    });
    return res.data;
  } catch (err) {
    console.error('뉴스 로딩 실패:', err);
    return [];
  }
};