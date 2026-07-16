import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./Dashboard.css";
import "./Products.css";
import "./ProductRequests.css";

function statusPillClass(status) {
  if (status === "APPROVED") return "stamp stamp-approved";
  if (status === "REJECTED") return "stamp stamp-rejected";
  return "stamp stamp-pending";
}

function MyProductRequests() {
  const navigate = useNavigate();
  const user = JSON.parse(sessionStorage.getItem("user") || "null");

  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!user) {
      navigate("/login", { replace: true });
      return;
    }
    api
      .get("/product-requests/mine", { params: { userId: user.userId } })
      .then((res) => setRequests(res.data))
      .catch(() => setError("Failed to load your requests."))
      .finally(() => setLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  if (!user) return null;

  const handleLogout = () => {
    sessionStorage.removeItem("user");
    navigate("/login");
  };

  return (
    <div className="dash-wrapper">
      <div className="dash-topbar">
        <div className="dash-logo">
          <div className="dash-logo-icon">
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M12 2L3 7v10l9 5 9-5V7l-9-5z" stroke="#1b1f27" strokeWidth="1.8" strokeLinejoin="round"/>
              <path d="M3 7l9 5 9-5M12 12v10" stroke="#1b1f27" strokeWidth="1.8" strokeLinejoin="round"/>
            </svg>
          </div>
          <span className="dash-logo-text">MACOPIA</span>
        </div>
        <div className="dash-user-info">
          <button className="prod-nav-btn" onClick={() => navigate("/product-requests/new")}>New Request</button>
          <button className="prod-nav-btn" onClick={() => navigate("/dashboard")}>Dashboard</button>
          <span className="dash-role-badge">{user.role.replace("_", " ")}</span>
          <button className="dash-logout-btn" onClick={handleLogout}>Logout</button>
        </div>
      </div>

      <div className="dash-content">
        <div className="dash-welcome-eyebrow">Product Requests</div>
        <h1 className="dash-welcome-title">My Requests</h1>
        <p className="dash-welcome-subtitle">Track the status and feedback on requests you've submitted.</p>

        <div className="dash-card admin-table-card">
          {loading && <p className="admin-status-text">Loading…</p>}
          {error && <p className="admin-status-text admin-error-text">{error}</p>}

          {!loading && !error && (
            <table className="admin-users-table">
              <thead>
                <tr>
                  <th>Type</th>
                  <th>SKU</th>
                  <th>Submitted</th>
                  <th>Status</th>
                  <th>Admin Feedback</th>
                </tr>
              </thead>
              <tbody>
                {requests.length === 0 && (
                  <tr>
                    <td colSpan={5} className="admin-status-text">You haven't submitted any requests yet.</td>
                  </tr>
                )}
                {requests.map((r) => (
                  <tr key={r.requestId}>
                    <td>{r.requestType}</td>
                    <td>{r.sku}</td>
                    <td>{new Date(r.createdAt).toLocaleString()}</td>
                    <td><span className={statusPillClass(r.status)}>{r.status}</span></td>
                    <td>{r.adminFeedback || <span className="pr-muted">Awaiting review</span>}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
}

export default MyProductRequests;
