import React from 'react';
import './MyPage.css';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import { Bar, Line } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Title,
  Tooltip,
  Legend
);

const MyPage = () => {
  const user = {
    name: '쌀튀밥감정',
    level: '중급',
    totalSolved: 12,
    totalCorrect: 860,
    avgAccuracy: 78,
  };

  // 📊 Bar Chart (정답률 요약)
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

  // 📈 Line Chart (레벨 변화 – ⬆️ Y축 위로 초급 → 고급)
  const levelMap = { '초급': 0, '중급': 1, '고급': 2 };
  const reverseLevelMap = ['고급', '중급', '초급']; // ⬅️ 역순으로 바꿔야 차트 방향이 맞음

  const levelData = {
    labels: ['1월', '2월', '3월'],
    datasets: [
      {
        label: '문해력 레벨 변화',
        data: [0, 1, 2], // 초급 → 중급 → 고급
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
        labels: reverseLevelMap, // ⬅️ 고급이 위로 가도록 정렬
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
      <header className="mypage-header">
        <h2>오늘의 문해력</h2>
      </header>

      <div className="top-section">
        <div className="left-top">
          <img src="https://via.placeholder.com/80" alt="프로필" className="profile-img" />
          <div>
            <h3>{user.name}</h3>
            <span className="badge">{user.level}</span>
          </div>
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
          <Calendar />
          <div className="calendar-summary">
            <p>총 출석일: 18일</p>
            <p>이번 달 출석률: 62%</p>
            <p>연속 출석: 5일</p>
          </div>
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
