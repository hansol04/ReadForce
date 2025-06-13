import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import fetchWithAuth from '../../utils/fetchWithAuth'; // 경로는 상황에 따라 조정

const AdminPage = () => {
    const navigate = useNavigate();
    const [users, setUsers] = useState([]);

    useEffect(() => {
        const nickname = localStorage.getItem("nickname");
        if (nickname !== "관리자") {
            alert("접근 권한이 없습니다.");
            navigate("/");
        }
    }, []);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const res = await fetchWithAuth("/member/get-all-members");
                if (!res.ok) {
                    throw new Error("권한 없음 또는 토큰 문제");
                }

                const data = await res.json();
                setUsers(data);
            } catch (error) {
                console.error("회원 목록 불러오기 실패", error);
            }
        };

        fetchUsers();
    }, []);


    return (
        <div style={{ padding: "24px" }}>
            <h2>회원 목록</h2>
            <table style={{ width: "100%", borderCollapse: "collapse", marginTop: "16px" }}>
                <thead>
                    <tr>
                        <th style={{ border: "1px solid #ccc", padding: "8px" }}>닉네임</th>
                        <th style={{ border: "1px solid #ccc", padding: "8px" }}>이메일</th>
                        <th style={{ border: "1px solid #ccc", padding: "8px" }}>가입일</th>
                        <th style={{ border: "1px solid #ccc", padding: "8px" }}>상태</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.email}>
                            <td style={{ border: "1px solid #ddd", padding: "8px" }}>{user.nickname}</td>
                            <td style={{ border: "1px solid #ddd", padding: "8px" }}>{user.email}</td>
                            <td style={{ border: "1px solid #ddd", padding: "8px" }}>
                                {new Date(user.createDate).toLocaleDateString()}
                            </td>
                            <td style={{ border: "1px solid #ddd", padding: "8px" }}>{user.status}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>);
};

export default AdminPage;