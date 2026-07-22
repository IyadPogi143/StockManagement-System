import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./Dashboard.css";
import "./Products.css";

function fullName(u) {
  return [u.firstName, u.middleName, u.lastName].filter(Boolean).join(" ");
}

function formatDate(d) {
  return d
    ? new Date(d).toLocaleDateString(undefined, { year: "numeric", month: "short", day: "numeric" })
    : "-";
}

function Dashboard() {
  const user = JSON.parse(sessionStorage.getItem("user") || "null");
  const navigate = useNavigate();

  const [users, setUsers] = useState([]);
  const [loadingUsers, setLoadingUsers] = useState(true);
  const [usersError, setUsersError] = useState("");
  const [selectedUser, setSelectedUser] = useState(null);

  useEffect(() => {
    if (!user || user.role !== "ADMINISTRATOR") return;
    api
      .get("/users")
      .then((res) => setUsers(res.data))
      .catch(() => setUsersError("Failed to load users."))
      .finally(() => setLoadingUsers(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleLogout = () => {
    sessionStorage.clear();
    navigate("/login");
  };

  if (!user) {
    return (
      <div className="dash-wrapper">
        <div className="dash-content">
          <p>You are not logged in.</p>
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
              <path d="M12 2L3 7v10l9 5 9-5V7l-9-5z" stroke="#1b1f27" strokeWidth="1.8" strokeLinejoin="round"/>
              <path d="M3 7l9 5 9-5M12 12v10" stroke="#1b1f27" strokeWidth="1.8" strokeLinejoin="round"/>
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
        <p className="dash-welcome-subtitle">Here is your current access overview.</p>

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
          <>
            <div className="dash-card">
              <div className="dash-card-title">Administrator Access</div>
              <div className="dash-card-text">
                View the full product catalog, adjust stock quantities, and review add/edit/delete
                requests submitted by inventory clerks.
              </div>
              <div style={{ display: "flex", gap: "12px", marginTop: "16px", flexWrap: "wrap" }}>
                <button
                  className="dash-logout-btn"
                  style={{ borderColor: "#a9691f", color: "#1b1f27", background: "#a9691f" }}
                  onClick={() => navigate("/admin/products")}
                >
                  Product Catalog
                </button>
                <button
                  className="dash-logout-btn dash-btn-outline"
                  onClick={() => navigate("/product-requests/review")}
                >
                  Review Requests
                </button>
              </div>
            </div>

            <div className="dash-card admin-table-card" style={{ padding: 0 }}>
              <div style={{ padding: "28px 28px 0" }}>
                <div className="dash-card-title">Registered Users</div>
                <p className="dash-card-text" style={{ marginBottom: "20px" }}>
                  All accounts currently registered in the system. Select View to see full profile details.
                </p>
              </div>

              {loadingUsers && <p className="admin-status-text">Loading users...</p>}
              {usersError && <p className="admin-status-text admin-error-text">{usersError}</p>}

              {!loadingUsers && !usersError && (
                <table className="admin-users-table">
                  <thead>
                    <tr>
                      <th>Name</th>
                      <th>Username</th>
                      <th>Role</th>
                      <th>Date Registered</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {users.map((u) => (
                      <tr key={u.userId}>
                        <td>{fullName(u)}</td>
                        <td>{u.username}</td>
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
                        <td>
                          <button className="prod-link-btn" onClick={() => setSelectedUser(u)}>
                            View Profile
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
          </>
        )}

        {user.role === "INVENTORY_CLERK" && (
          <div className="dash-card">
            <div className="dash-card-title">Inventory Clerk Access</div>
            <div className="dash-card-text">
              You can submit requests to add, edit, or delete products. An Administrator will review each
              request and leave feedback before it takes effect.
            </div>
            <div style={{ display: "flex", gap: "12px", marginTop: "16px", flexWrap: "wrap" }}>
              <button
                className="dash-logout-btn"
                style={{ borderColor: "#a9691f", color: "#1b1f27", background: "#a9691f" }}
                onClick={() => navigate("/product-requests/new")}
              >
                Submit Product Request
              </button>
              <button
                className="dash-logout-btn dash-btn-outline"
                onClick={() => navigate("/product-requests/mine")}
              >
                My Requests
              </button>
            </div>
          </div>
        )}
      </div>

      {selectedUser && (
        <div className="prod-modal-overlay" onClick={() => setSelectedUser(null)}>
          <div className="prod-modal" onClick={(e) => e.stopPropagation()}>
            <h2 className="prod-modal-title">User Profile</h2>
            <div className="profile-grid">
              <div className="profile-field">
                <div className="dash-info-label">Full Name</div>
                <div className="dash-info-value">{fullName(selectedUser)}</div>
              </div>
              <div className="profile-field">
                <div className="dash-info-label">Username</div>
                <div className="dash-info-value">{selectedUser.username}</div>
              </div>
              <div className="profile-field">
                <div className="dash-info-label">Email</div>
                <div className="dash-info-value">{selectedUser.email}</div>
              </div>
              <div className="profile-field">
                <div className="dash-info-label">Role</div>
                <div className="dash-info-value">{selectedUser.role.replace("_", " ")}</div>
              </div>
              <div className="profile-field">
                <div className="dash-info-label">Date Registered</div>
                <div className="dash-info-value">{formatDate(selectedUser.dateCreated)}</div>
              </div>
            </div>
            <div className="prod-modal-actions">
              <button className="prod-secondary-btn" onClick={() => setSelectedUser(null)}>Close</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Dashboard;
