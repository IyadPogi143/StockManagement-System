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

function describeChange(r) {
  if (r.requestType === "CREATE") {
    return `Add "${r.proposedProductName}" (${r.proposedCategory}), qty ${r.proposedQuantity}, min ${r.proposedMinThreshold}`;
  }
  if (r.requestType === "UPDATE") {
    return `Update to "${r.proposedProductName}" (${r.proposedCategory}), qty ${r.proposedQuantity}, min ${r.proposedMinThreshold}`;
  }
  return `Delete this product${r.proposedDescription ? ` — reason: ${r.proposedDescription}` : ""}`;
}

function ReviewRequests() {
  const navigate = useNavigate();
  const user = JSON.parse(sessionStorage.getItem("user") || "null");

  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [filter, setFilter] = useState("PENDING");

  // Which request is currently showing its feedback input, and what's typed into it
  const [activeRequestId, setActiveRequestId] = useState(null);
  const [feedbackText, setFeedbackText] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [actionError, setActionError] = useState("");

  useEffect(() => {
    if (!user || user.role !== "ADMINISTRATOR") {
      navigate("/dashboard", { replace: true });
      return;
    }
    loadRequests(filter);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filter]);

  const loadRequests = (status) => {
    setLoading(true);
    api
      .get("/product-requests", { params: status ? { status } : {} })
      .then((res) => setRequests(res.data))
      .catch(() => setError("Failed to load requests."))
      .finally(() => setLoading(false));
  };

  if (!user || user.role !== "ADMINISTRATOR") return null;

  const handleLogout = () => {
    sessionStorage.clear();
    navigate("/login");
  };

  const openDecision = (requestId) => {
    setActiveRequestId(requestId);
    setFeedbackText("");
    setActionError("");
  };

  const closeDecision = () => {
    setActiveRequestId(null);
    setFeedbackText("");
    setActionError("");
  };

  const submitDecision = async (requestId, decision) => {
    if (!feedbackText.trim()) {
      setActionError("Feedback is required.");
      return;
    }
    setSubmitting(true);
    setActionError("");
    try {
      await api.patch(`/product-requests/${requestId}/review`, {
        decision,
        feedback: feedbackText,
        reviewedByUserId: user.userId,
      });
      closeDecision();
      loadRequests(filter);
    } catch (err) {
      setActionError(err.response?.data?.message || err.response?.data || "Failed to submit decision.");
    } finally {
      setSubmitting(false);
    }
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
          <button className="prod-nav-btn" onClick={() => navigate("/admin/products")}>Product Catalog</button>
          <button className="prod-nav-btn" onClick={() => navigate("/dashboard")}>Dashboard</button>
          <span className="dash-role-badge">ADMINISTRATOR</span>
          <button className="dash-logout-btn" onClick={handleLogout}>Logout</button>
        </div>
      </div>

      <div className="dash-content">
        <div className="prod-header-row">
          <div>
            <div className="dash-welcome-eyebrow">Product Requests</div>
            <h1 className="dash-welcome-title">Review Requests</h1>
            <p className="dash-welcome-subtitle">Approve or reject add/edit/delete requests. Feedback is required either way.</p>
          </div>
          <select value={filter} onChange={(e) => setFilter(e.target.value)} className="pr-filter-select">
            <option value="PENDING">Pending</option>
            <option value="APPROVED">Approved</option>
            <option value="REJECTED">Rejected</option>
            <option value="">All</option>
          </select>
        </div>

        <div className="dash-card admin-table-card">
          {loading && <p className="admin-status-text">Loading…</p>}
          {error && <p className="admin-status-text admin-error-text">{error}</p>}

          {!loading && !error && (
            <table className="admin-users-table">
              <thead>
                <tr>
                  <th>Type</th>
                  <th>SKU</th>
                  <th>Change</th>
                  <th>Requested By</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {requests.length === 0 && (
                  <tr>
                    <td colSpan={6} className="admin-status-text">No requests here.</td>
                  </tr>
                )}
                {requests.map((r) => (
                  <tr key={r.requestId}>
                    <td>{r.requestType}</td>
                    <td>{r.sku}</td>
                    <td className="pr-change-cell">{describeChange(r)}</td>
                    <td>{r.requestedByUsername}</td>
                    <td><span className={statusPillClass(r.status)}>{r.status}</span></td>
                    <td>
                      {r.status === "PENDING" ? (
                        <button className="prod-link-btn" onClick={() => openDecision(r.requestId)}>Review</button>
                      ) : (
                        <span className="pr-muted">{r.adminFeedback}</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      {activeRequestId !== null && (
        <div className="prod-modal-overlay" onClick={closeDecision}>
          <div className="prod-modal" onClick={(e) => e.stopPropagation()}>
            <h2 className="prod-modal-title">Review Request</h2>
            {actionError && <p className="prod-form-error">{actionError}</p>}
            <label style={{ display: "flex", flexDirection: "column", gap: "6px", fontSize: "13px", fontWeight: 600, color: "#374151" }}>
              Feedback (required)
              <textarea
                rows={3}
                value={feedbackText}
                onChange={(e) => setFeedbackText(e.target.value)}
                placeholder="Explain your decision to the requester…"
              />
            </label>
            <div className="prod-modal-actions" style={{ marginTop: "16px" }}>
              <button className="prod-secondary-btn" onClick={closeDecision}>Cancel</button>
              <button
                className="prod-secondary-btn"
                style={{ borderColor: "#dc2626", color: "#dc2626" }}
                disabled={submitting}
                onClick={() => submitDecision(activeRequestId, "REJECT")}
              >
                Reject
              </button>
              <button
                className="prod-primary-btn"
                disabled={submitting}
                onClick={() => submitDecision(activeRequestId, "APPROVE")}
              >
                Approve
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default ReviewRequests;
