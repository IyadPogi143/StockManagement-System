import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import Products from "./pages/Products";
import SubmitProductRequest from "./pages/SubmitProductRequest";
import MyProductRequests from "./pages/MyProductRequests";
import ReviewRequests from "./pages/ReviewRequests";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={<Dashboard />} />
        {/* Regular admin feature, linked from Dashboard.jsx for ADMINISTRATOR users. View-only catalog. */}
        <Route path="/admin/products" element={<Products />} />
        {/* Reachable by both roles: submit an add/edit/delete request. */}
        <Route path="/product-requests/new" element={<SubmitProductRequest />} />
        <Route path="/product-requests/mine" element={<MyProductRequests />} />
        {/* Administrator only: approve/reject pending requests. */}
        <Route path="/product-requests/review" element={<ReviewRequests />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;