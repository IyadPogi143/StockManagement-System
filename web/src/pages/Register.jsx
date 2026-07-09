import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../services/api";
import "./Auth.css";

function Register() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    middleName: "",
    username: "",
    email: "",
    password: "",
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      await api.post("/auth/register", form);
      navigate("/login");
    } catch (err) {
      setError(err.response?.data || "Registration failed");
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-brand-panel">
        <div className="brand-logo">
          <div className="brand-logo-icon">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L3 7v10l9 5 9-5V7l-9-5z" stroke="#0f1729" strokeWidth="1.8" strokeLinejoin="round"/>
              <path d="M3 7l9 5 9-5M12 12v10" stroke="#0f1729" strokeWidth="1.8" strokeLinejoin="round"/>
            </svg>
          </div>
          <span className="brand-logo-text">MACOPIA</span>
        </div>

        <div className="brand-eyebrow">Inventory Control Platform</div>
        <h1 className="brand-heading">Welcome to Stock Management System</h1>
        <p className="brand-subtext">
          Create an account to get real-time visibility into every product, quantity, and stock movement.
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
          <h2 className="auth-form-title">Create an account</h2>
          <p className="auth-form-subtitle">Fill in your details to get started</p>

          {error && <div className="auth-error">{error}</div>}

          <form onSubmit={handleSubmit}>
            <div className="form-row">
              <div className="form-group">
                <label>First Name</label>
                <input name="firstName" value={form.firstName} onChange={handleChange} required />
              </div>
              <div className="form-group">
                <label>Last Name</label>
                <input name="lastName" value={form.lastName} onChange={handleChange} required />
              </div>
            </div>

            <div className="form-group">
              <label>Middle Name (optional)</label>
              <input name="middleName" value={form.middleName} onChange={handleChange} />
            </div>

            <div className="form-group">
              <label>Username</label>
              <input name="username" value={form.username} onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label>Email</label>
              <input type="email" name="email" value={form.email} onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label>Password</label>
              <input type="password" name="password" value={form.password} onChange={handleChange} required />
            </div>

            <button type="submit" className="auth-submit-btn">Create Account</button>
          </form>

          <p className="auth-switch-text">
            Already have an account? <Link to="/login">Sign in</Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Register;