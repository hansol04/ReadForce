// import React from 'react';
// import './MyPage.css';
// import Calendar from 'react-calendar';
// import { useEffect, useState } from 'react';
// import 'react-calendar/dist/Calendar.css';
// import { Bar, Line } from 'react-chartjs-2';
// import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, LineElement, PointElement, Title, Tooltip, Legend } from 'chart.js';
// import EditProfileModal from './EditProfileModal';
// import { fetchWithAuth } from '../../utils/fetchWithAuth';

// ChartJS.register(CategoryScale, LinearScale, BarElement, LineElement, PointElement, Title, Tooltip, Legend);

// const MyPage = () => {
//   const [nickname, setNickname] = useState('');
//   const isLoggedIn = !!localStorage.getItem("token");
//   const [showModal, setShowModal] = useState(false);
//   const [attendanceDates, setAttendanceDates] = useState([]);
//   const [summary, setSummary] = useState({
//     total: 0,
//     monthlyRate: 0,
//     streak: 0,
//   });

//   // 출석 통계
//   const today = new Date();
//   const currentYear = today.getFullYear();
//   const currentMonth = today.getMonth(); // 0-based

//   const thisMonthDates = attendanceDates.filter(date =>
//     date.getFullYear() === currentYear && date.getMonth() === currentMonth
//   );

//   const currentDayOfMonth = today.getDate();
//   const monthlyRate = Math.round((thisMonthDates.length / currentDayOfMonth) * 100);

//   const getStreak = (dates) => {
//     const sorted = [...dates]
//       .map(d => new Date(d.getFullYear(), d.getMonth(), d.getDate()))
//       .sort((a, b) => b - a); // 최신순 정렬

//     let streak = 0;
//     let current = new Date(); // 오늘

//     for (const date of sorted) {
//       if (
//         date.toDateString() === current.toDateString()
//       ) {
//         streak++;
//         current.setDate(current.getDate() - 1); // 하루 전으로 이동
//       } else if (
//         date.toDateString() === new Date(current.getFullYear(), current.getMonth(), current.getDate() - 1).toDateString()
//       ) {
//         streak++;
//         current.setDate(current.getDate() - 1);
//       } else {
//         break;
//       }
//     }
//     return streak;
//   };

//   const streak = getStreak(attendanceDates);

//   useEffect(() => {
//     fetch('/member/attendance-dates')
//       .then(res => res.json())
//       .then(data => {
//         let dates = [];
//         if (Array.isArray(data)) {
//           dates = data.map(dateStr => new Date(dateStr));
//         } else if (Array.isArray(data.dates)) {
//           dates = data.dates.map(dateStr => new Date(dateStr));
//         }

//         setAttendanceDates(dates);

//         // 계산
//         const today = new Date();
//         const currentYear = today.getFullYear();
//         const currentMonth = today.getMonth();
//         const thisMonthDates = dates.filter(
//           d => d.getFullYear() === currentYear && d.getMonth() === currentMonth
//         );
//         const monthlyRate = Math.round((thisMonthDates.length / today.getDate()) * 100);

//         const streak = getStreak(dates);

//         setSummary({
//           total: dates.length,
//           monthlyRate,
//           streak,
//         });
//       });
//   }, []);
//   // 

//   useEffect(() => {
//     if (isLoggedIn) {
//       const storedNickname = localStorage.getItem("nickname");
//       setNickname(storedNickname || "사용자");
//     }
//   }, [isLoggedIn]);

//   useEffect(() => {
//     const token = localStorage.getItem('token');
//     fetch('/member/attendance-dates')
//       .then(res => res.json())
//       .then(data => {
//         console.log('✅ API 응답 확인:', data);
//         const parsed = data.map(dateStr => new Date(dateStr));
//         setAttendanceDates(parsed);
//       })
//       .catch(err => console.error("❌ 출석 데이터 불러오기 실패:", err));
//   }, []);

//   const user = {
//     name: '쌀튀밥감정',
//     level: '중급',
//     totalSolved: 12,
//     totalCorrect: 860,
//     avgAccuracy: 78,
//   };


//   const barData = {
//     labels: ['초급', '중급', '고급'],
//     datasets: [{
//       label: '정답률 (%)',
//       data: [75, 82, 65],
//       backgroundColor: '#4ABDAC',
//     }],
//   };

//   const barOptions = {
//     responsive: true,
//     plugins: {
//       legend: { position: 'top' },
//       title: { display: true, text: '문해력 정확도' }
//     },
//     scales: {
//       y: { beginAtZero: true, max: 100 }
//     }
//   };


//   const levelMap = { '초급': 0, '중급': 1, '고급': 2 };
//   const reverseLevelMap = ['고급', '중급', '초급'];

//   const levelData = {
//     labels: ['1월', '2월', '3월'],
//     datasets: [
//       {
//         label: '문해력 레벨 변화',
//         data: [0, 1, 2],
//         borderColor: '#4ABDAC',
//         backgroundColor: '#4ABDAC',
//         tension: 0.3,
//         pointRadius: 5,
//       },
//     ],
//   };

//   const levelOptions = {
//     responsive: true,
//     plugins: {
//       legend: { display: false },
//     },
//     scales: {
//       y: {
//         type: 'category',
//         labels: reverseLevelMap,
//         ticks: { stepSize: 1 },
//         title: { display: false },
//       },
//     },
//   };

//   const recentHistory = [
//     { date: '2025.05.15', level: '중급', result: '2 / 3' },
//     { date: '2025.05.15', level: '고급', result: '1 / 3' },
//     { date: '2025.05.14', level: '중급', result: '2 / 3' },
//   ];

//   const wrongQuestions = [
//     "삼성전자 원자재 이슈 '수급 무효증 가능'; EU 인증 획득",
//     '대선 하루 앞두고 3시간 마비 예보…서울중, 정부도 초긴장',
//     "'불법소굴 근절'…전기통신사업법 시행령 입법 예고",
//   ];

//   return (
//     <div className="mypage-container">
//       {/* <header className="mypage-header">
//         <h2>오늘의 문해력</h2>
//       </header> */}

//       <div className="top-section">
//         <div className="left-top">
//           <img src="https://via.placeholder.com/80" alt="프로필" className="profile-img" />
//           <div>
//             <h3>{nickname} 님</h3>
//             <span className="badge">{user.level}</span>
//           </div>
//           <button className='settings-button' onClick={() => setShowModal(true)}>⚙️</button>
//           {showModal && <EditProfileModal onClose={() => setShowModal(false)} />}
//         </div>

//         <div className="right-top">
//           <div className="lang-select">
//             <button>한글</button>
//             <button>일어</button>
//             <button>영어</button>
//           </div>
//           <div className="line-chart-container">
//             <Line data={levelData} options={levelOptions} />
//           </div>
//         </div>
//       </div>

//       <div className="mypage-main">
//         <div className="stats-section">
//           <h4>문해력 요약</h4>
//           <p>총 도전 횟수: {user.totalSolved}</p>
//           <p>누적 정답 수: {user.totalCorrect}</p>
//           <p>평균 정확도: {user.avgAccuracy}%</p>
//           <Bar data={barData} options={barOptions} />
//         </div>

//         <div className="calendar-section">
//           <h4>출석 현황</h4>
//           <div className="calendar-summary">
//             <div className="summary-row">
//               <div className="summary-title">총 출석일</div>
//               <div className="summary-title">이번 달 출석률</div>
//               <div className="summary-title">연속 출석</div>
//             </div>
//             <div className="summary-row">
//               <div className="summary-value">{summary.total}일</div>
//               <div className="summary-value">{summary.monthlyRate}%</div>
//               <div className="summary-value">{summary.streak}일</div>
//             </div>
//           </div>
//           <Calendar
//             calendarType="gregory"
//             next2Label={null}
//             prev2Label={null}
//             minDetail="month"
//             maxDetail="month"
//             tileClassName={({ date, view }) => {
//               if (view === 'month') {
//                 const isAttendance = attendanceDates.some(att => att.toDateString() === date.toDateString());
//                 const day = date.getDay(); // 0 = 일요일, 6 = 토요일

//                 if (isAttendance) return 'attended-day';
//                 if (day === 0) return 'sunday'; // ✅ 일요일 날짜
//                 if (day === 6) return 'saturday'; // ✅ 토요일 날짜
//               }
//               return null;
//             }}
//           />
//         </div>
//       </div>

//       <div className="history-section">
//         <h4>최근 문제 풀이 기록</h4>
//         <ul>
//           {recentHistory.map((item, i) => (
//             <li key={i}>
//               {item.date} / {item.level} / {item.result}
//             </li>
//           ))}
//         </ul>
//       </div>

//       <div className="wrong-section">
//         <h4>틀린 문제 다시 풀기</h4>
//         <ul>
//           {wrongQuestions.map((q, i) => (
//             <li key={i}>
//               {q} <button>다시풀기</button>
//             </li>
//           ))}
//         </ul>
//       </div>
//     </div>
//   );
// };

// export default MyPage;
import React from 'react';
import './MyPage.css';
import Calendar from 'react-calendar';
import { useEffect, useState } from 'react';
import 'react-calendar/dist/Calendar.css';
import { Bar, Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, LineElement, PointElement, Title, Tooltip, Legend } from 'chart.js';
import EditProfileModal from './EditProfileModal';
import fetchWithAuth from '../../utils/fetchWithAuth';

ChartJS.register(CategoryScale, LinearScale, BarElement, LineElement, PointElement, Title, Tooltip, Legend);

const MyPage = () => {
  const [nickname, setNickname] = useState('');
  const isLoggedIn = !!localStorage.getItem("token");
  const [showModal, setShowModal] = useState(false);
  const [attendanceDates, setAttendanceDates] = useState([]);
  const [summary, setSummary] = useState({
    total: 0,
    monthlyRate: 0,
    streak: 0,
  });

  const getStreak = (dates) => {
    const sorted = [...dates]
      .map(d => new Date(d.getFullYear(), d.getMonth(), d.getDate()))
      .sort((a, b) => b - a);

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

  useEffect(() => {
    fetchWithAuth('/member/attendance-dates')
      .then(res => res.json())
      .then(data => {
        let dates = [];
        if (Array.isArray(data)) {
          dates = data.map(dateStr => new Date(dateStr));
        } else if (Array.isArray(data.dates)) {
          dates = data.dates.map(dateStr => new Date(dateStr));
        }

        setAttendanceDates(dates);

        const today = new Date();
        const currentYear = today.getFullYear();
        const currentMonth = today.getMonth();
        const thisMonthDates = dates.filter(
          d => d.getFullYear() === currentYear && d.getMonth() === currentMonth
        );
        const monthlyRate = Math.round((thisMonthDates.length / today.getDate()) * 100);

        const streak = getStreak(dates);

        setSummary({
          total: dates.length,
          monthlyRate,
          streak,
        });
      });
  }, []);

  useEffect(() => {
    if (isLoggedIn) {
      const storedNickname = localStorage.getItem("nickname");
      setNickname(storedNickname || "사용자");
    }
  }, [isLoggedIn]);

  const user = {
    name: '쌀튀밥감정',
    level: '중급',
    totalSolved: 12,
    totalCorrect: 860,
    avgAccuracy: 78,
  };

  const barData = {
    labels: ['초급', '중급', '고급'],
    datasets: [{
      label: '정답률 (%)',
      data: [75, 82, 65],
      backgroundColor: '#4ABDAC',
    }],
  };

  const barOptions = {
    responsive: true,
    plugins: {
      legend: { position: 'top' },
      title: { display: true, text: '문해력 정확도' }
    },
    scales: {
      y: { beginAtZero: true, max: 100 }
    }
  };

  // const levelMap = { '초급': 0, '중급': 1, '고급': 2 };
  const reverseLevelMap = ['고급', '중급', '초급'];

  const levelData = {
    labels: ['1월', '2월', '3월'],
    datasets: [
      {
        label: '문해력 레벨 변화',
        data: [0, 1, 2],
        borderColor: '#4ABDAC',
        backgroundColor: '#4ABDAC',
        tension: 0.3,
        pointRadius: 5,
      },
    ],
  };

  const levelOptions = {
    responsive: true,
    plugins: {
      legend: { display: false },
    },
    scales: {
      y: {
        type: 'category',
        labels: reverseLevelMap,
        ticks: { stepSize: 1 },
        title: { display: false },
      },
    },
  };

  const recentHistory = [
    { date: '2025.05.15', level: '중급', result: '2 / 3' },
    { date: '2025.05.15', level: '고급', result: '1 / 3' },
    { date: '2025.05.14', level: '중급', result: '2 / 3' },
  ];

  const wrongQuestions = [
    "삼성전자 원자재 이슈 '수급 무효증 가능'; EU 인증 획득",
    '대선 하루 앞두고 3시간 마비 예보…서울중, 정부도 초긴장',
    "'불법소굴 근절'…전기통신사업법 시행령 입법 예고",
  ];

  return (
    <div className="mypage-container">
      <div className="top-section">
        <div className="left-top">
          <img src="https://via.placeholder.com/80" alt="프로필" className="profile-img" />
          <div>
            <h3>{nickname} 님</h3>
            <span className="badge">{user.level}</span>
          </div>
          <button className='settings-button' onClick={() => setShowModal(true)}>⚙️</button>
          {showModal && <EditProfileModal onClose={() => setShowModal(false)} />}
        </div>

        <div className="right-top">
          <div className="lang-select">
            <button>한글</button>
            <button>일어</button>
            <button>영어</button>
          </div>
          <div className="line-chart-container">
            <Line data={levelData} options={levelOptions} />
          </div>
        </div>
      </div>

      <div className="mypage-main">
        <div className="stats-section">
          <h4>문해력 요약</h4>
          <p>총 도전 횟수: {user.totalSolved}</p>
          <p>누적 정답 수: {user.totalCorrect}</p>
          <p>평균 정확도: {user.avgAccuracy}%</p>
          <Bar data={barData} options={barOptions} />
        </div>

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
            <li key={i}>
              {item.date} / {item.level} / {item.result}
            </li>
          ))}
        </ul>
      </div>

      <div className="wrong-section">
        <h4>틀린 문제 다시 풀기</h4>
        <ul>
          {wrongQuestions.map((q, i) => (
            <li key={i}>
              {q} <button>다시풀기</button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default MyPage;