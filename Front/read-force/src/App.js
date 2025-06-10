import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Main from "./pages/main";
import LayOut from "./components/layout";

import MyPage from './pages/myinfo/MyPage';
import KoreaPage from './pages/articles/KoreaPage';
import JapanPage from './pages/articles/JapanPage';
import UsaPage from './pages/articles/UsaPage';


import SignupWithEmail from "./pages/signup/signupwithemail";
import SignupChoice from "./pages/signup/signupchoice";
import EmailVerifyPage from "./pages/signup/emailverifypage";
import SignupCompletePage from "./pages/signup/signupcompletepage";
import Login from "./pages/login/login";
import FindPassword from "./pages/login/findpassword";
import ResetPassword from "./pages/login/resetpassword";


function App() {
  return (
    <Router>
      <Routes>
        <Route element={<LayOut />}>
          <Route path="/" element={<Main />} />
          <Route path="/login" element={<Login />} />
          <Route path="/findpassword" element={<FindPassword />} /> 
          <Route path="/resetpassword" element={<ResetPassword />} /> 
          <Route path="/signup" element={<SignupWithEmail />} />

       
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/korea" element={<KoreaPage />} />
          <Route path="/japan" element={<JapanPage />} />
          <Route path="/usa" element={<UsaPage />} />

          <Route path="/signup/signupchoice" element={<SignupChoice />} />
          <Route path="/signup/emailverifypage" element={<EmailVerifyPage />} />
          <Route path="/signup/signupcompletepage" element={<SignupCompletePage />} />

        </Route>
      </Routes>
    </Router>
  );
}

export default App;
