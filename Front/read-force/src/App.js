// src/App.js
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Main from "./pages/main";
import LayOut from "./components/layout";
import SignupWithEmail from "./pages/signupwithemail";
import SignupChoice from "./pages/signupchoice";
import MyPage from './pages/myinfo/MyPage';
import KoreaPage from './pages/articles/KoreaPage';
import JapanPage from './pages/articles/JapanPage';
import UsaPage from './pages/articles/UsaPage';


function App() {
  return (
    <Router>
      <Routes>
        <Route element={<LayOut />}>
          <Route path="/" element={<Main />} />
          <Route path="/signup" element={<SignupWithEmail />} />
          <Route path="/signupchoice" element={<SignupChoice />} />
          <Route path="/mypage" element={<MyPage />} />

          {/* ✅ 추가된 뉴스 페이지 라우트 */}
          <Route path="/korea" element={<KoreaPage />} />
          <Route path="/japan" element={<JapanPage />} />
          <Route path="/usa" element={<UsaPage />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
