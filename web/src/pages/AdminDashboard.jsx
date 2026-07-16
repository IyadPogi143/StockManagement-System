import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./Dashboard.css";
import "./AdminDashboard.css";

function AdminDashboard() {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Client-side gate only. This does NOT secure the data — it just stops
  // a regular clerk from seeing the page render in their own browser.
  // Anyone who knows the /api/users URL can still fetch it directly.
  useEffect(() => {
    const stored = JSON.parse(localStorage.getItem("user") || "null");
    if (!stored || stored.role !== "ADMINISTRATOR") {
      navigate("/dashboard", { replace: true });
      return;
    }

    api
      .get("/users")
      .then((res) => setUsers(res.data))
      .catch(() => setError("Failed to load users."))
      .finally(() => setLoading(false));
  }, [navigate]);

  const user = JSON.parse(localStorage.getItem("user") || "null");
  if (!user || user.role !== "ADMINISTRATOR") {
    return null;
  }

  const handleLogout = () => {
    localStorage.removeItem("user");
    navigate("/login");
  };

  const fullName = (u) =>
    [u.firstName, u.middleName, u.lastName].filter(Boolean).join(" ");

  const formatDate = (d) =>
    d
      ? new Date(d).toLocaleDateString(undefined, {
          year: "numeric",
          month: "short",
          day: "numeric",
        })
      : "—";

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
          <span className="dash-role-badge">ADMINISTRATOR</span>
          <button className="dash-logout-btn" onClick={handleLogout}>Logout</button>
        </div>
      </div>

      <div className="dash-content">
        <div className="dash-welcome-eyebrow">Admin</div>
        <h1 className="dash-welcome-title">Registered Users</h1>
        <p className="dash-welcome-subtitle">
          All accounts currently registered in the system.
        </p>

        <div className="dash-card admin-table-card">
          {loading && <p className="admin-status-text">Loading users…</p>}
          {error && <p className="admin-status-text admin-error-text">{error}</p>}

          {!loading && !error && (
            <table className="admin-users-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Username</th>
                  <th>Email</th>
                  <th>Role</th>
                  <th>Date Registered</th>
                </tr>
              </thead>
              <tbody>
                {users.map((u) => (
                  <tr key={u.userId}>
                    <td>{fullName(u)}</td>
                    <td>{u.username}</td>
                    <td>{u.email}</td>
                    <td>
                      <span
                        className={
                          u.role === "ADMINISTRATOR"
                            ? "admin-role-pill admin-role-pill-admin"
                            : "admin-role-pill"
                        }
                      >
                        {u.role.replace("_", " ")}
                      </span>
                    </td>
                    <td>{formatDate(u.dateCreated)}</td>
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

export default AdminDashboard;
