import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './EditProfilePage.css';

const ProfileEditPage = () => {
  const [nickname, setNickname] = useState('');
  const [nicknameMessage, setNicknameMessage] = useState('');
  const [isNicknameValid, setIsNicknameValid] = useState(null);
  const [birthday, setBirthday] = useState('');
  const [birthdayMessage, setBirthdayMessage] = useState('');
  const [isBirthdayValid, setIsBirthdayValid] = useState(null);
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [profileImage, setProfileImage] = useState('https://via.placeholder.com/100');
  const [preview, setPreview] = useState(null);
  const [isSocialUser, setIsSocialUser] = useState(false);

  const token = localStorage.getItem('token');

  useEffect(() => {
    const fetchMember = async () => {
      try {
        const res = await axios.get('/get-member-object', {
          headers: { Authorization: `Bearer ${token}` }
        });

        setNickname(res.data.nickname || '');
        setBirthday(res.data.birthday || '');
        setProfileImage(`/get-profile-image?${Date.now()}`);

        // 소셜회원 여부 판단
        setIsSocialUser(res.data.social === true || res.data.socialType);
      } catch (err) {
        alert('로그인이 필요합니다.');
      }
    };
    fetchMember();
  }, [token]);

  const checkNicknameDuplicate = async (nickname) => {
    try {
      const res = await fetch(`/member/nickname-check?nickname=${nickname}`);
      const data = await res.json();
      return res.ok;
    } catch {
      return false;
    }
  };

  const validateNickname = async (value) => {
    const onlyKorean = /^[가-힣]+$/.test(value);
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
    setBirthdayMessage(regex.test(value) ? '생일 입력 완료' : 'YYYY-MM-DD 형식으로 입력해주세요');
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

  const validatePassword = (value) => {
    const valid =
      value.length >= 8 &&
      value.length <= 16 &&
      /[a-zA-Z]/.test(value) &&
      /[0-9]/.test(value) &&
      /[!@#$%^&*]/.test(value);
    return valid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const updates = [];

    if (nickname && isNicknameValid) {
      updates.push(axios.patch('/modify-info', { nickname }, { headers: { Authorization: `Bearer ${token}` } }));
    }

    if (birthday && isBirthdayValid) {
      updates.push(axios.patch('/modify-info', { birthday }, { headers: { Authorization: `Bearer ${token}` } }));
    }

    if (!isSocialUser && password) {
      if (!validatePassword(password)) {
        alert('비밀번호는 8~16자, 영문/숫자/특수문자 포함이어야 합니다.');
        return;
      }
      if (password !== confirmPassword) {
        alert('비밀번호가 일치하지 않습니다.');
        return;
      }
      updates.push(axios.patch('/modify-info', { password }, { headers: { Authorization: `Bearer ${token}` } }));
    }

    if (preview) {
      const formData = new FormData();
      formData.append('profile_image_file', preview);
      updates.push(
        axios.post('/upload-profile-image', formData, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'multipart/form-data',
          }
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
    const ok = window.confirm('정말로 탈퇴하시겠습니까?');
    if (!ok) return;

    try {
      await axios.delete('/withdraw-member', {
        headers: { Authorization: `Bearer ${token}` }
      });
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
            <img src={profileImage} alt="프로필" className="profile-image" />
            <input type="file" accept="image/*" onChange={(e) => {
              const file = e.target.files[0];
              if (file) {
                setPreview(file);
                setProfileImage(URL.createObjectURL(file));
              }
            }} />
            <button type="button" onClick={() => {
              axios.delete('/delete-profile-image', {
                headers: { Authorization: `Bearer ${token}` }
              }).then(() => {
                setProfileImage('https://via.placeholder.com/100');
                setPreview(null);
              }).catch(() => alert('삭제 실패'));
            }}>삭제</button>
          </div>
        </div>

        <label>닉네임</label>
        <input
          type="text"
          value={nickname}
          onChange={async (e) => {
            const value = e.target.value;
            setNickname(value);
            await validateNickname(value);
          }}
        />
        <span className="validation-message" style={{
          color: isNicknameValid == null ? 'inherit' : isNicknameValid ? 'green' : 'red'
        }}>{nicknameMessage}</span>

        <label>생년월일</label>
        <input
          type="text"
          placeholder="예: 1997-11-04"
          value={birthday}
          onChange={(e) => handleBirthdayChange(e.target.value)}
        />
        <span className="validation-message" style={{
          color: isBirthdayValid == null ? 'inherit' : isBirthdayValid ? 'green' : 'red'
        }}>{birthdayMessage}</span>

        {!isSocialUser && (
          <>
            <label>새 비밀번호</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />

            <label>비밀번호 확인</label>
            <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
          </>
        )}

        <div className="button-group">
          <button type="submit">정보 수정</button>
          <button type="button" className="withdraw-button" onClick={handleWithdraw}>회원 탈퇴</button>
        </div>
      </form>
    </div>
  );
};

export default ProfileEditPage;
