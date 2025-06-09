import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Main from "./pages/main";
import LayOut from "./components/layout";
import SignUp from "./pages/signup";

function App() {
  return (
    <Router>
      <Routes>
        <Route element={<LayOut />}>
          <Route path="/" element={<Main />} />
          <Route path="/signup" element={<SignUp />} />
          {/* 다른 페이지들도 여기 추가 */}
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
