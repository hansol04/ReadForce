import React, { useState} from 'react';
import axiosInstance from '../../api/axiosInstance';
import kakaoIcon from '../../assets/image/kakao.png';
import naverIcon from '../../assets/image/naver.png';
import googleIcon from '../../assets/image/google.png';
import './EditProfilePage.css';

const ProfileEditPage = () => {
  const [nickname, setNickname] = useState('');
  const [isNicknameValid, setIsNicknameValid] = useState(null);
  const [birthday, setBirthday] = useState('');
  const [isBirthdayValid, setIsBirthdayValid] = useState(null);
  const [preview, setPreview] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const checkNicknameDuplicate = async (nickname) => {
    if (!nickname || nickname.length < 2) return false;
    try {
      const res = await axiosInstance.get(`/member/nickname-check?nickname=${nickname}`);
      return res.status === 200;
    } catch {
      return false;
    }
  };

  const validateBirthday = (value) => {
    const regex = /^\d{4}-\d{2}-\d{2}$/;
    setIsBirthdayValid(regex.test(value));
  };

  const handleBirthdayChange = (value) => {
    const numeric = value.replace(/\D/g, '').slice(0, 8);
    let formatted = numeric;
    if (numeric.length >= 5) {
      formatted = `${numeric.slice(0, 4)}-${numeric.slice(4, 6)}`;
      if (numeric.length >= 7) {
        formatted += `-${numeric.slice(6, 8)}`;
      }
    }
    setBirthday(formatted);
    validateBirthday(formatted);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const updates = [];

    const infoPayload = {};
    if (nickname && isNicknameValid) infoPayload.nickname = nickname;
    if (birthday && isBirthdayValid) infoPayload.birthday = birthday;
    if (Object.keys(infoPayload).length > 0) {
      updates.push(axiosInstance.patch('/member/modify-info', infoPayload));
    }

    if (preview) {
      const formData = new FormData();
      formData.append('profile_image_file', preview);
      updates.push(
        axiosInstance.post('/member/upload-profile-image', formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        })
      );
    }

    try {
      await Promise.all(updates);
      alert('회원정보가 수정되었습니다.');
      window.location.reload();
    } catch {
      alert('회원정보 수정 실패');
    }
  };

  const handleWithdraw = async () => {
    try {
      await axiosInstance.delete('/member/withdraw-member');
      localStorage.clear();
      alert('탈퇴 완료되었습니다.');
      window.location.href = '/';
    } catch {
      alert('탈퇴 실패');
    }
  };

  return (
    <div className="profile-edit-page">
      <h2>회원정보 수정</h2>
      <form className="edit-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>회원 이미지</label>
          <div className="profile-image-box">
            <input
              type="file"
              accept="image/*"
              onChange={(e) => {
                const file = e.target.files[0];
                if (file) {
                  setPreview(file);
                }
              }}
            />
          </div>
        </div>

        <div className="form-group">
          <label>닉네임</label>
          <input
            type="text"
            value={nickname}
            onChange={async (e) => {
              const value = e.target.value;
              setNickname(value);
              const isValid = await checkNicknameDuplicate(value);
              setIsNicknameValid(isValid);
            }}
          />
        </div>

        <div className="form-group">
          <label>생년월일</label>
          <input
            type="text"
            placeholder="예: 1997-11-04"
            value={birthday}
            onChange={(e) => handleBirthdayChange(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label>SNS 계정 연동</label>
          <div className="social-login">
            <button
              type="button"
              className="social-btn"
              onClick={() => window.location.href = 'http://localhost:8080/oauth2/authorization/kakao'}
            >
              <img src={kakaoIcon} alt="카카오" />
            </button>
            <button
              type="button"
              className="social-btn"
              onClick={() => window.location.href = 'http://localhost:8080/oauth2/authorization/naver'}
            >
              <img src={naverIcon} alt="네이버" />
            </button>
            <button
              type="button"
              className="social-btn"
              onClick={() => window.location.href = 'http://localhost:8080/oauth2/authorization/google'}
            >
              <img src={googleIcon} alt="구글" />
            </button>
          </div>
        </div>

        <div className="button-group">
          <button type="submit">정보 수정</button>
        </div>

        <div className="withdraw-area">
          <button type="button" className="withdraw-button" onClick={() => setShowModal(true)}>
            회원 탈퇴
          </button>
        </div>
      </form>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-box">
            <p>정말로 탈퇴하시겠습니까?</p>
            <div className="modal-buttons">
              <button className="confirm" onClick={handleWithdraw}>예</button>
              <button className="cancel" onClick={() => setShowModal(false)}>아니요</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProfileEditPage;
