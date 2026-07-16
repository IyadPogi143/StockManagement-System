import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../services/api";
import "./Auth.css";

function Login() {
  const [form, setForm] = useState({ username: "", password: "" });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const res = await api.post("/auth/login", form);
      sessionStorage.setItem("user", JSON.stringify(res.data));
      navigate("/dashboard");
    } catch (err) {
      setError(err.response?.data || "Login failed");
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-brand-panel">
        <div className="brand-logo">
          <div className="brand-logo-icon">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L3 7v10l9 5 9-5V7l-9-5z" stroke="#1b1f27" strokeWidth="1.8" strokeLinejoin="round"/>
              <path d="M3 7l9 5 9-5M12 12v10" stroke="#1b1f27" strokeWidth="1.8" strokeLinejoin="round"/>
            </svg>
          </div>
          <span className="brand-logo-text">MACOPIA</span>
        </div>

        <div className="brand-eyebrow">Inventory Control Platform</div>
        <h1 className="brand-heading">Welcome to Stock Management System</h1>
        <p className="brand-subtext">
          A centralized platform for real-time inventory tracking across your web dashboard and floor operations.
        </p>

        <div className="brand-stats">
          <div>
            <div className="brand-stat-value">2</div>
            <div className="brand-stat-label">User Roles</div>
          </div>
          <div>
            <div className="brand-stat-value">24/7</div>
            <div className="brand-stat-label">Live Tracking</div>
          </div>
          <div>
            <div className="brand-stat-value">100%</div>
            <div className="brand-stat-label">Centralized</div>
          </div>
        </div>
      </div>

      <div className="auth-form-panel">
        <div className="auth-form-card">
          <h2 className="auth-form-title">Sign in to your account</h2>
          <p className="auth-form-subtitle">Enter your credentials to continue</p>

          {error && <div className="auth-error">{error}</div>}

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Username</label>
              <input name="username" value={form.username} onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label>Password</label>
              <input type="password" name="password" value={form.password} onChange={handleChange} required />
            </div>

            <button type="submit" className="auth-submit-btn">Sign In</button>
          </form>

          <p className="auth-switch-text">
            Don't have an account? <Link to="/register">Create one</Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Login;