import { useNavigate } from "react-router-dom";
import "./Dashboard.css";

function Dashboard() {
  const user = JSON.parse(localStorage.getItem("user") || "null");
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("user");
    navigate("/login");
  };

  if (!user) {
    return (
      <div className="dash-wrapper">
        <div className="dash-content">
          <p>You're not logged in.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="dash-wrapper">
      <div className="dash-topbar">
        <div className="dash-logo">
          <div className="dash-logo-icon">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L3 7v10l9 5 9-5V7l-9-5z" stroke="#0f1729" strokeWidth="1.8" strokeLinejoin="round"/>
              <path d="M3 7l9 5 9-5M12 12v10" stroke="#0f1729" strokeWidth="1.8" strokeLinejoin="round"/>
            </svg>
          </div>
          <span className="dash-logo-text">MACOPIA</span>
        </div>
        <div className="dash-user-info">
          <span className="dash-role-badge">{user.role.replace("_", " ")}</span>
          <button className="dash-logout-btn" onClick={handleLogout}>Logout</button>
        </div>
      </div>

      <div className="dash-content">
        <div className="dash-welcome-eyebrow">Dashboard</div>
        <h1 className="dash-welcome-title">Welcome, {user.firstName} {user.lastName}</h1>
        <p className="dash-welcome-subtitle">Here's your current access overview.</p>

        <div className="dash-info-grid">
          <div className="dash-info-box">
            <div className="dash-info-label">Username</div>
            <div className="dash-info-value">{user.username}</div>
          </div>
          <div className="dash-info-box">
            <div className="dash-info-label">Email</div>
            <div className="dash-info-value">{user.email}</div>
          </div>
          <div className="dash-info-box">
            <div className="dash-info-label">Role</div>
            <div className="dash-info-value">{user.role.replace("_", " ")}</div>
          </div>
        </div>

        {user.role === "ADMINISTRATOR" && (
          <div className="dash-card">
            <div className="dash-card-title">Administrator Access</div>
            <div className="dash-card-text">
              You can manage the full product catalog, add or remove items, and monitor low-stock alerts across the system.
            </div>
          </div>
        )}

        {user.role === "INVENTORY_CLERK" && (
          <div className="dash-card">
            <div className="dash-card-title">Inventory Clerk Access</div>
            <div className="dash-card-text">
              You can view the live product list and adjust stock quantities as items move in and out.
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Dashboard;