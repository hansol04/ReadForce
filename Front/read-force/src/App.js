import HomePage from "./pages/homepage";
import Header from "./components/header";
import Footer from "./components/footer";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LayOut from "./components/layout";

function App() {
  return (
    <Router>
      <Routes>
        <Route element={<LayOut />}>
          <Route path="/" element={<HomePage />} />
          {/* <Route path="/login" element={<LoginPage />} /> */}
          {/* 다른 페이지들도 여기 추가 */}
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
