import Header from "./header";
import Footer from "./footer";
import { Outlet } from "react-router-dom";

const LayOut = () => {
  return (
    <div className="app-wrapper">
      <Header />
      <main style={{ minHeight: "80vh" }}>
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default LayOut;