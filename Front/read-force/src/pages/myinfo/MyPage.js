import React, { useEffect, useState } from 'react';
import './MyPage.css';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import EditProfileModal from './EditProfileModal';
import fetchWithAuth from '../../utils/fetchWithAuth';
import { useNavigate } from 'react-router-dom';

const MyPage = () => {
  const [nickname, setNickname] = useState('');
  const isLoggedIn = !!localStorage.getItem("token");
  const [showModal, setShowModal] = useState(false);
  const [attendanceDates, setAttendanceDates] = useState([]);
  const [profileImageUrl, setProfileImageUrl] = useState(null);
  const [wrongQuestions, setWrongQuestions] = useState([]);
  const [summary, setSummary] = useState({ total: 0, monthlyRate: 0, streak: 0 });

  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfileImage = async () => {
      try {
        const response = await fetch('/member/get-profile-image', {
          headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
        });
        if (!response.ok) throw new Error('이미지 로딩 실패');
        const blob = await response.blob();
        const url = URL.createObjectURL(blob);
        setProfileImageUrl(url);
      } catch (error) {
        console.error('프로필 이미지 불러오기 실패:', error);
      }
    };
    if (isLoggedIn) fetchProfileImage();
  }, [isLoggedIn]);

  useEffect(() => {
    fetchWithAuth('/member/get-attendance-date-list')
      .then(res => res.json())
      .then(data => {
        const dates = Array.isArray(data) ? data.map(d => new Date(d)) : [];
        setAttendanceDates(dates);

        const today = new Date();
        const currentYear = today.getFullYear();
        const currentMonth = today.getMonth();
        const thisMonthDates = dates.filter(d => d.getFullYear() === currentYear && d.getMonth() === currentMonth);
        const monthlyRate = Math.round((thisMonthDates.length / today.getDate()) * 100);

        const getStreak = (dates) => {
          const sorted = [...dates].map(d => new Date(d.getFullYear(), d.getMonth(), d.getDate())).sort((a, b) => b - a);
          let streak = 0;
          let current = new Date();
          for (const date of sorted) {
            if (date.toDateString() === current.toDateString()) {
              streak++;
              current.setDate(current.getDate() - 1);
            } else if (date.toDateString() === new Date(current.getFullYear(), current.getMonth(), current.getDate() - 1).toDateString()) {
              streak++;
              current.setDate(current.getDate() - 1);
            } else {
              break;
            }
          }
          return streak;
        };

        setSummary({
          total: dates.length,
          monthlyRate,
          streak: getStreak(dates),
        });
      });
  }, []);

  useEffect(() => {
    if (isLoggedIn) {
      const storedNickname = localStorage.getItem("nickname");
      setNickname(storedNickname || "사용자");
    }
  }, [isLoggedIn]);

  useEffect(() => {
    fetchWithAuth('/member/get-member-incorrect-quiz-list')
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data)) {
          setWrongQuestions(data); // 전체 객체 저장
        }
      });
  }, []);

  const handleRetry = (quiz) => {
    navigate(`/question/${quiz.quiz_no}`, { state: { article: { news_no: quiz.quiz_no } } });
  };

  const recentHistory = [
    { date: '2025.05.15', level: '중급', result: '2 / 3' },
    { date: '2025.05.15', level: '고급', result: '1 / 3' },
    { date: '2025.05.14', level: '중급', result: '2 / 3' },
  ];

  return (
    <div className="mypage-container">
      <div className="top-section">
        <div className="left-top">
          <img src={profileImageUrl} alt="프로필" className="profile-img" />
          <div>
            <h3>{nickname} 님</h3>
            <span className="badge">중급</span>
          </div>
          <button className='settings-button' onClick={() => setShowModal(true)}>⚙️</button>
          {showModal && <EditProfileModal onClose={() => setShowModal(false)} />}
        </div>
      </div>

      <div className="mypage-main">
        <div className="calendar-section">
          <h4>출석 현황</h4>
          <div className="calendar-summary">
            <div className="summary-row">
              <div className="summary-title">총 출석일</div>
              <div className="summary-title">이번 달 출석률</div>
              <div className="summary-title">연속 출석</div>
            </div>
            <div className="summary-row">
              <div className="summary-value">{summary.total}일</div>
              <div className="summary-value">{summary.monthlyRate}%</div>
              <div className="summary-value">{summary.streak}일</div>
            </div>
          </div>
          <Calendar
            calendarType="gregory"
            next2Label={null}
            prev2Label={null}
            minDetail="month"
            maxDetail="month"
            tileClassName={({ date, view }) => {
              if (view === 'month') {
                const isAttendance = attendanceDates.some(att => att.toDateString() === date.toDateString());
                const day = date.getDay();
                if (isAttendance) return 'attended-day';
                if (day === 0) return 'sunday';
                if (day === 6) return 'saturday';
              }
              return null;
            }}
          />
        </div>
      </div>

      <div className="history-section">
        <h4>최근 문제 풀이 기록</h4>
        <ul>
          {recentHistory.map((item, i) => (
            <li key={i}>{item.date} / {item.level} / {item.result}</li>
          ))}
        </ul>
      </div>

      <div className="wrong-section">
        <h4>틀린 문제 다시 풀기</h4>
        <ul>
          {wrongQuestions.length === 0 ? (
            <li>틀린 문제가 없습니다.</li>
          ) : (
            wrongQuestions.map((quiz, i) => (
              <li key={i}>
                {quiz.question_text} <button onClick={() => handleRetry(quiz)}>다시풀기</button>
              </li>
            ))
          )}
        </ul>
      </div>
    </div>
  );
};

export default MyPage;
