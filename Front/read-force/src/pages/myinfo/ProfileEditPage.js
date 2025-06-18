import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axiosInstance';
import defaultProfileImage from '../../assets/image/default-profile.png';
import './EditProfilePage.css';

const ProfileEditPage = () => {
  const [nickname, setNickname] = useState('');
  const [nicknameMessage, setNicknameMessage] = useState('');
  const [isNicknameValid, setIsNicknameValid] = useState(null);
  const [birthday, setBirthday] = useState('');
  const [birthdayMessage, setBirthdayMessage] = useState('');
  const [isBirthdayValid, setIsBirthdayValid] = useState(null);

  const [preview, setPreview] = useState(null);
  const [previewUrl, setPreviewUrl] = useState(null);
  const [serverProfileImageUrl, setServerProfileImageUrl] = useState(defaultProfileImage);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    const fetchProfileImage = async () => {
      try {
        const res = await axiosInstance.get('/member/get-profile-image', {
          responseType: 'blob',
        });
        const isImage = res.data.type.startsWith('image/');
        if (isImage) {
          const imageUrl = URL.createObjectURL(res.data);
          setServerProfileImageUrl(imageUrl);
        } else {
          console.warn('서버 응답이 이미지가 아님');
          setServerProfileImageUrl(defaultProfileImage);
        }
      } catch (error) {
        console.log('프로필 이미지 없음 또는 오류 → 기본 이미지 사용');
        setServerProfileImageUrl(defaultProfileImage);
      }
    };
    fetchProfileImage();
  }, []);

  const checkNicknameDuplicate = async (nickname) => {
    if (!nickname || nickname.length < 2) return false;
    try {
      const res = await axiosInstance.get(`/member/nickname-check?nickname=${nickname}`);
      return res.status === 200;
    } catch {
      return false;
    }
  };

  const validateNickname = async (value) => {
    const onlyKorean = /^[\uAC00-\uD7A3]+$/.test(value);
    const onlyEnglish = /^[a-zA-Z]+$/.test(value);
    if ((onlyKorean && value.length <= 8) || (onlyEnglish && value.length <= 20)) {
      const isAvailable = await checkNicknameDuplicate(value);
      setNicknameMessage(isAvailable ? '사용 가능한 닉네임입니다.' : '이미 존재하는 닉네임입니다.');
      setIsNicknameValid(isAvailable);
    } else {
      setNicknameMessage('한글 8자, 영문 20자 이하로 입력해주세요');
      setIsNicknameValid(false);
    }
  };

  const validateBirthday = (value) => {
    const regex = /^\d{4}-\d{2}-\d{2}$/;
    setBirthdayMessage(regex.test(value) ? '' : 'YYYY-MM-DD 형식으로 입력해주세요');
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
      try {
        await axiosInstance.delete('/member/delete-profile-image');
      } catch (err) {
        console.warn('서버 이미지 삭제 실패', err);
      }
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

  const removeImage = async () => {
    setPreview(null);
    setPreviewUrl(null);
    setServerProfileImageUrl(defaultProfileImage);
    try {
      await axiosInstance.delete('/member/delete-profile-image');
    } catch (err) {
      console.warn('서버 이미지 삭제 실패', err);
    }
  };

  return (
    <div className="profile-edit-page">
      <h2>회원정보 수정</h2>
      <form className="edit-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>회원 이미지</label>
          <div className="profile-image-box">
            <img
              src={previewUrl || serverProfileImageUrl || defaultProfileImage}
              alt="프로필 이미지"
              className="profile-image"
              onError={(e) => (e.target.src = defaultProfileImage)}
            />

            <div className="image-control-row">
              <input
                type="file"
                accept="image/*"
                onChange={(e) => {
                  const file = e.target.files[0];
                  if (file) {
                    setPreview(file);
                    setPreviewUrl(URL.createObjectURL(file));
                  }
                }}
              />
              <button type="button" className="remove-image-button" onClick={removeImage}>
                이미지 제거
              </button>
            </div>
          </div>
        </div>

        <div className="form-group">
          <label>닉네임</label>
          <div className="input-with-message">
            <input
              type="text"
              value={nickname}
              onChange={async (e) => {
                const value = e.target.value;
                setNickname(value);
                await validateNickname(value);
              }}
            />
            <div className="message-row">
              <span className="inline-hint">한글 8자, 영문 20자 이하로 입력해주세요</span>
              <span className="validation-message" style={{ color: isNicknameValid ? 'green' : 'red' }}>
                {nicknameMessage}
              </span>
            </div>
          </div>
        </div>

        <div className="form-group">
          <label>생년월일</label>
          <div className="input-with-message">
            <input
              type="text"
              placeholder="예: 1997-11-04"
              value={birthday}
              onChange={(e) => handleBirthdayChange(e.target.value)}
            />
            <div className="message-row">
              <span className="inline-hint">YYYY-MM-DD 형식으로 입력해주세요</span>
              <span className="validation-message" style={{ color: isBirthdayValid ? 'green' : 'red' }}>
                {birthdayMessage}
              </span>
            </div>
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
