import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import fetchWithAuth from "../../utils/fetchWithAuth";

const AdminUserInfo = () => {
    const { email } = useParams();
    const [user, setUser] = useState(null);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const res = await fetchWithAuth(`/admin/get-user-info?email=${email}`);
                if (!res.ok) throw new Error("유저 정보 조회 실패");
                const data = await res.json();
                setUser(data);
            } catch (err) {
                console.error(err);
                alert("유저 정보를 불러오는 데 실패했습니다.");
            }
        };

        fetchUser();
    }, [email]);

    if (!user) return <div>불러오는 중...</div>;

    return (
        <div style={{ padding: "24px" }}>
            <h2>{user.nickname} 님의 정보</h2>
            <p><strong>이메일:</strong> {user.email}</p>
            <p><strong>가입일:</strong> {new Date(user.created_date).toLocaleDateString()}</p>
            <p><strong>활성 여부:</strong> {user.active ? "활성화됨" : "비활성화됨"}</p>
            {/* 필요시 더 추가 */}
        </div>
    );
};

export default AdminUserInfo;