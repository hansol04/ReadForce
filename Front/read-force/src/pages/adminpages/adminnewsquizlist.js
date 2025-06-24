// import React, { useEffect, useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import fetchWithAuth from '../../utils/fetchWithAuth';

// const AdminNewsQuizList = () => {
//     const navigate = useNavigate();
//     const [quizList, setQuizList] = useState([]);

//     // 퀴즈 불러오기
//     useEffect(() => {
//         const fetchQuizList = async () => {
//             try {
//                 const res = await fetchWithAuth('/admin/get-all-news-quiz-list');
//                 if (!res.ok) throw new Error("뉴스 퀴즈 불러오기 실패");
//                 const data = await res.json();
//                 setQuizList(data);
//             } catch (err) {
//                 console.error(err);
//                 alert("뉴스 퀴즈 목록을 불러오지 못했습니다.");
//             }
//         };

//         fetchQuizList();
//     }, []);

//     // 문제 생성
//     const handleGenerateNewsQuiz = async () => {
//         if (!window.confirm("뉴스 퀴즈를 생성하시겠습니까?\n(뉴스에 해당 문제 없는 경우만 생성됩니다)")) return;

//         try {
//             const res = await fetchWithAuth("/admin/generate-creative-news-quiz", {
//                 method: "POST"
//             });

//             if (!res.ok) throw new Error("뉴스 퀴즈 생성 실패");

//             const data = await res.json();
//             alert("뉴스 퀴즈 생성 완료: " + data.messageCode);
//         } catch (err) {
//             console.error(err);
//             alert("뉴스 퀴즈 생성에 실패했습니다.");
//         }
//     };

//     // 문제 삭제
//     const handleDeleteQuiz = async (quizNo) => {
//         if (!window.confirm("정말 삭제하시겠습니까?")) return;

//         try {
//             const res = await fetchWithAuth(`/admin/delete-news-quiz?news_quiz_no=${quizNo}`, {
//                 method: 'DELETE'
//             });

//             if (!res.ok) throw new Error("삭제 실패");
//             setQuizList(prev => prev.filter(q => q.news_quiz_no !== quizNo));
//         } catch (err) {
//             console.error(err);
//             alert("뉴스 퀴즈 삭제에 실패했습니다.");
//         }
//     };

//     return (
//         <div style={{ padding: "24px" }}>
//             <button onClick={() => navigate("/adminpage/adminnews")} style={backbtn}>뒤로가기</button>
//             <div style={ADMIN_NEWS_TITLE}>
//                 <h2>전체 뉴스 퀴즈 목록</h2>
//                 <button style={ADMIN_BUTTONS} onClick={handleGenerateNewsQuiz}>뉴스 퀴즈 생성</button>
//             </div>
//             <table style={{ width: "100%", borderCollapse: "collapse" }}>
//                 <thead>
//                     <tr>
//                         <th style={thStyle}>번호</th>
//                         <th style={thStyle}>질문</th>
//                         <th style={thStyle}>보기1</th>
//                         <th style={thStyle}>보기2</th>
//                         <th style={thStyle}>보기3</th>
//                         <th style={thStyle}>보기4</th>
//                         <th style={thStyle}>정답</th>
//                         <th style={thStyle}>해설</th>
//                         <th style={thStyle}>배점</th>
//                         <th style={thStyle}>뉴스 번호</th>
//                         <th style={thStyle}>생성일</th>
//                         <th style={thStyle}>관리</th>
//                     </tr>
//                 </thead>
//                 <tbody>
//                     {quizList.map((quiz) => (
//                         <tr key={quiz.news_quiz_no}>
//                             <td style={tdStyle}>{quiz.news_quiz_no}</td>
//                             <td style={tdStyle}>{quiz.question_text}</td>
//                             <td style={tdStyle}>{quiz.choice1}</td>
//                             <td style={tdStyle}>{quiz.choice2}</td>
//                             <td style={tdStyle}>{quiz.choice3}</td>
//                             <td style={tdStyle}>{quiz.choice4}</td>
//                             <td style={tdStyle}>{quiz.correct_answer_index}</td>
//                             <td style={tdStyle}>{quiz.explanation}</td>
//                             <td style={tdStyle}>{quiz.score}</td>
//                             <td style={tdStyle}>{quiz.news_no}</td>
//                             <td style={tdStyle}>{new Date(quiz.created_date).toLocaleDateString()}</td>
//                             <td style={tdStyle}>
//                                 <button
//                                     onClick={() => handleDeleteQuiz(quiz.news_quiz_no)}
//                                     style={{ color: "red", border: "none", background: "none", cursor: "pointer" }}
//                                 >삭제
//                                 </button>
//                             </td>
//                         </tr>
//                     ))}
//                 </tbody>
//             </table>
//         </div>
//     );
// };

// const backbtn = {
//     marginBottom: "16px",
//     padding: "8px 16px",
//     backgroundColor: "#6c757d",
//     color: "white",
//     border: "none",
//     borderRadius: "4px",
//     cursor: "pointer"
// };

// const ADMIN_NEWS_TITLE = {
//     display: "flex",
//     justifyContent: "space-between",
//     alignItems: "center",
//     marginBottom: "16px"
// };

// const ADMIN_BUTTONS = {
//     marginBottom: "16px",
//     padding: "8px 16px",
//     backgroundColor: "#007BFF",
//     color: "white",
//     border: "none",
//     borderRadius: "4px",
//     cursor: "pointer"
// };

// const thStyle = {
//     border: "1px solid #ccc",
//     padding: "8px",
//     backgroundColor: "#f8f8f8"
// };

// const tdStyle = {
//     border: "1px solid #ddd",
//     padding: "8px"
// };

// export default AdminNewsQuizList;