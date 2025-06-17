import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Main from "./pages/main";
import MyPage from './pages/myinfo/MyPage';
import LayOut from "./components/layout";

import KoreaPage from './pages/articles/KoreaPage';
import JapanPage from './pages/articles/JapanPage';
import UsaPage from './pages/articles/UsaPage';

import SignupWithEmail from "./pages/signup/signupwithemail";
import SignupChoice from "./pages/signup/signupchoice";
import EmailVerifyPage from "./pages/signup/emailverifypage";
import SignupCompletePage from "./pages/signup/signupcompletepage";
import Socialsignup from './pages/signup/socialsignup';

import Login from "./pages/login/login";
import FindPassword from "./pages/login/findpassword";
import ResetPassword from "./pages/login/resetpassword";
import Oauth2redirect from './pages/login/oauth2redirect';
import Authcallback from './pages/login/authcallback';

import QuizPage from './pages/quiz/QuizPage';
import ReadingPage from './pages/quiz/ReadingPage';

import ChallengePage from "./pages/challenge/challengepage";

import ClassicPage from './pages/literature/ClassicPage';

import NewsList from './components/News/NewsList';
import ArticleQuestion from './components/News/ArticleQuestion';



function App() {
  return (
    <Router>
      <Routes>
        <Route element={<LayOut />}>
          <Route path="/" element={<Main />} />
          <Route path="/login" element={<Login />} />
          <Route path="/findpassword" element={<FindPassword />} /> 
          <Route path="/resetpassword" element={<ResetPassword />} /> 
          <Route path="/quiz" element={<QuizPage />} />
          <Route path="/reading" element={<ReadingPage />} />
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/korea" element={<KoreaPage />} />
          <Route path="/japan" element={<JapanPage />} />
          <Route path="/usa" element={<UsaPage />} />
          <Route path="/signup/signupchoice" element={<SignupChoice />} />
          <Route path="/signup" element={<SignupWithEmail />} />  
          <Route path="/signup/emailverifypage" element={<EmailVerifyPage />} />
          <Route path="/signup/signupcompletepage" element={<SignupCompletePage />} />
          <Route path="/challenge" element={<ChallengePage />} />
          <Route path="/oauth2/redirect" element={<Oauth2redirect />} />
          <Route path="/authcallback" element={<Authcallback />} />
          <Route path="/social-sign-up" element={<Socialsignup />} />
          <Route path="/literature/classic" element={<ClassicPage />} />
          <Route path="/question/:id" element={<ArticleQuestion />} />
       </Route>
      </Routes>
    </Router>
  );
}

export default App;
