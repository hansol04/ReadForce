import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from 'react-router-dom';
import fetchWithAuth from "../../utils/fetchWithAuth";

const AdminUserInfo = () => {
    const navigate = useNavigate();
    const { email } = useParams();
    const [user, setUser] = useState(null);
    const [attendanceList, setAttendanceList] = useState([]);
    const [pointInfo, setPointInfo] = useState({});
    const [newsQuizAttempts, setNewsQuizAttempts] = useState([]);
    const [literatureQuizAttempts, setLiteratureQuizAttempts] = useState([]);

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const res = await fetchWithAuth(`/admin/get-member-info-object?email=${email}`);
                if (!res.ok) throw new Error("회원 정보 조회 실패");

                const data = await res.json();
                setUser(data);
            } catch (err) {
                console.error(err);
                alert("회원 정보를 불러오는 데 실패했습니다.");
            }
        };

        const fetchAttendanceList = async () => {
            try {
                const res = await fetchWithAuth(`/admin/get-member-attendance-list?email=${email}`);
                if (!res.ok) throw new Error("출석 정보 조회 실패");
                const data = await res.json();
                setAttendanceList(data);
            } catch (err) {
                console.error(err);
                alert("출석 정보를 불러오는 데 실패했습니다.");
            }
        };

        const fetchPointInfo = async () => {
            try {
                const res = await fetchWithAuth(`/admin/get-member-point-object?email=${email}`);
                if (!res.ok) throw new Error("포인트 정보 조회 실패");
                const data = await res.json();
                setPointInfo(data);
            } catch (err) {
                console.error(err);
                alert("포인트 정보를 불러오는 데 실패했습니다.");
            }
        };

        const fetchNewsQuizAttempts = async () => {
            try {
                const res = await fetchWithAuth(`/admin/get-member-news-quiz-attempt-list?email=${email}`);
                const data = await res.json();
                setNewsQuizAttempts(data);
            } catch (err) {
                console.error("뉴스 퀴즈 응시 기록 오류:", err);
            }
        };

        const fetchLiteratureQuizAttempts = async () => {
            try {
                const res = await fetchWithAuth(`/admin/get-member-literature-quiz-attempt-list?email=${email}`);
                const data = await res.json();
                setLiteratureQuizAttempts(data);
            } catch (err) {
                console.error("문학 퀴즈 응시 기록 오류:", err);
            }
        };

        fetchUserInfo();
        fetchAttendanceList();
        fetchPointInfo();
        fetchNewsQuizAttempts();
        fetchLiteratureQuizAttempts();
    }, [email]);

    if (!user) return <div>불러오는 중...</div>;

    return (
        <div style={{ padding: "24px" }}>
            <button onClick={() => navigate("/adminpage")} style={backbtn}>뒤로가기</button>
            <h2>회원 상세 정보</h2>
            <p><strong>이메일:</strong> {user.email}</p>
            <p><strong>닉네임:</strong> {user.nickname}</p>
            <p><strong>생일:</strong> {user.birthday}</p>
            <p><strong>상태:</strong> {user.status}</p>
            <p><strong>가입일:</strong> {new Date(user.create_date).toLocaleDateString()}</p>

            <hr style={{ margin: "24px 0" }} />

            <h3>출석 내역</h3>
            {attendanceList.length === 0 ? (
                <p>출석 기록이 없습니다.</p>
            ) : (
                <ul style={{ listStyle: "none", paddingLeft: 0 }}>
                    {attendanceList.map((a) => (
                        <li key={a.attendance_no} style={{ marginBottom: "8px" }}>
                            출석 날짜: {new Date(a.created_date).toLocaleDateString()}
                        </li>
                    ))}
                </ul>
            )}

            <hr style={{ margin: "24px 0" }} />

            <h3>포인트 정보</h3>
            <p><strong>총 포인트:</strong> {pointInfo.total}</p>
            <p><strong>영어 뉴스:</strong> {pointInfo.english_news}</p>
            <p><strong>일본어 뉴스:</strong> {pointInfo.japanese_news}</p>
            <p><strong>국어 뉴스:</strong> {pointInfo.korean_news}</p>
            <p><strong>동화:</strong> {pointInfo.fairytale}</p>
            <p><strong>소설:</strong> {pointInfo.novel}</p>
            <p><strong>최근 수정일:</strong> {new Date(pointInfo.last_modified_date).toLocaleDateString()}</p>

            <hr style={{ margin: "24px 0" }} />

            <h3>뉴스 퀴즈 응시 기록</h3>
            {newsQuizAttempts.length === 0 ? (
                <p>기록이 없습니다.</p>
            ) : (
                <table style={{ width: "100%", borderCollapse: "collapse" }}>
                    <thead>
                        <tr>
                            <th style={thStyle}>퀴즈 번호</th>
                            <th style={thStyle}>선택지</th>
                            <th style={thStyle}>정답 여부</th>
                            <th style={thStyle}>응시일</th>
                        </tr>
                    </thead>
                    <tbody>
                        {newsQuizAttempts.map((attempt, idx) => (
                            <tr key={idx}>
                                <td style={tdStyle}>{attempt.news_quiz_no}</td>
                                <td style={tdStyle}>{(attempt.selected_option_index ?? 0) + 1}번</td>
                                <td style={tdStyle}>{attempt.is_correct ? "정답" : "오답"}</td>
                                <td style={tdStyle}>{new Date(attempt.created_date).toLocaleString()}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}

            <hr style={{ margin: "24px 0" }} />

            <h3>문학 퀴즈 응시 기록</h3>
            {literatureQuizAttempts.length === 0 ? (
                <p>기록이 없습니다.</p>
            ) : (
                <table style={{ width: "100%", borderCollapse: "collapse" }}>
                    <thead>
                        <tr>
                            <th style={thStyle}>퀴즈 번호</th>
                            <th style={thStyle}>선택지</th>
                            <th style={thStyle}>정답 여부</th>
                            <th style={thStyle}>응시일</th>
                        </tr>
                    </thead>
                    <tbody>
                        {literatureQuizAttempts.map((attempt, idx) => (
                            <tr key={idx}>
                                <td style={tdStyle}>{attempt.literature_quiz_no}</td>
                                <td style={tdStyle}>{(attempt.selected_option_index ?? 0) + 1}번</td>
                                <td style={tdStyle}>{attempt.is_correct ? "정답" : "오답"}</td>
                                <td style={tdStyle}>{new Date(attempt.created_date).toLocaleString()}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
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

const thStyle = {
    border: "1px solid #ccc",
    padding: "8px",
    backgroundColor: "#f2f2f2",
    textAlign: "left",
};

const tdStyle = {
    border: "1px solid #ddd",
    padding: "8px",
};

export default AdminUserInfo;