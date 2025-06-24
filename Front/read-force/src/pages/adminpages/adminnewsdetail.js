import { useLocation, useNavigate } from 'react-router-dom';
import { useEffect, useState } from "react";
import fetchWithAuth from "../../utils/fetchWithAuth";

const AdminNewsDetail = () => {
    const { state } = useLocation();
    const navigate = useNavigate();
    const news = state?.news;

    const [quizList, setQuizList] = useState([]);
    useEffect(() => {
        const fetchQuiz = async () => {
            try {
                const res = await fetchWithAuth("/admin/get-all-news-quiz-list");
                const data = await res.json();

                // 현재 뉴스 번호와 연관된 퀴즈만 필터링
                const relatedQuiz = data.filter(q => q.news_no === news.news_no);
                setQuizList(relatedQuiz);
            } catch (err) {
                console.error("퀴즈 불러오기 실패", err);
            }
        };

        if (news?.news_no) {
            fetchQuiz();
        }
    }, [news]);

    if (!news) return <div>잘못된 접근입니다.</div>;

    return (
        <div style={{ padding: "24px" }}>
            <button onClick={() => navigate("/adminpage/adminnews")}style={backbtn}>뒤로가기</button>
            <h2>{news.title}</h2>
            <p><strong>작성일:</strong> {new Date(news.created_date).toLocaleDateString()}</p>
            <p><strong>카테고리:</strong> {news.category}</p>
            <p><strong>언어:</strong> {news.language}</p>
            <p><strong>난이도:</strong> {news.level}</p>
            <hr />
            <p>{news.content}</p>

            <hr />
            <h3>퀴즈</h3>
            {quizList.length > 0 ? (
                <ul>
                    {quizList.map(q => (
                        <li key={q.news_quiz_no} style={{ marginBottom: "12px" }}>
                            <div><strong>Q.</strong> {q.question_text}</div>
                            <ol>
                                <li>{q.choice1}</li>
                                <li>{q.choice2}</li>
                                <li>{q.choice3}</li>
                                <li>{q.choice4}</li>
                            </ol>
                            <div><strong>정답:</strong> {q.correct_answer_index + 1}번</div>
                            <div><strong>해설:</strong> {q.explanation}</div>
                            <div><strong>배점:</strong> {q.score}</div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>관련된 퀴즈가 없습니다.</p>
            )}
        </div>
    );
};

const backbtn = {
    marginBottom: "16px",
    padding: "8px 16px",
    backgroundColor: "#6c757d",
    color: "white",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer"
}

export default AdminNewsDetail;