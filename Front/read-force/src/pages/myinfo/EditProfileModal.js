import React, { useState, useRef, useEffect } from 'react';
import './EditProfileModal.css';

const EditProfileModal = ({ onClose }) => {
    const [nickname, setNickname] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const modalRef = useRef(null);

    useEffect(() => {
        // 컴포넌트 마운트 시 애니메이션 트리거
        setTimeout(() => {
            modalRef.current?.classList.add('show');
        }, 10); // 약간의 delay로 트리거
    }, []);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        alert(`닉네임: ${nickname}, 비밀번호: ${password}`);
        onClose(); // 닫기
    };

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div
                className="modal-content"
                ref={modalRef}
                onClick={(e) => e.stopPropagation()}
            >                
            <h2>회원정보 수정</h2>
                <form onSubmit={handleSubmit} className="edit-form">
                    <label>닉네임</label>
                    <input type="text" value={nickname} onChange={(e) => setNickname(e.target.value)} />

                    <label>비밀번호</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />

                    <label>비밀번호 확인</label>
                    <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />

                    <div className="button-group">
                        <button type="submit">수정</button>
                        <button type="button" onClick={onClose}>취소</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default EditProfileModal;