import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./Dashboard.css";
import "./Products.css";
import "./ProductRequests.css";

const emptyForm = {
  requestType: "CREATE",
  sku: "",
  productName: "",
  category: "",
  description: "",
  quantity: 0,
  minThreshold: 0,
};

function SubmitProductRequest() {
  const navigate = useNavigate();
  const user = JSON.parse(sessionStorage.getItem("user") || "null");

  const [form, setForm] = useState(emptyForm);
  const [errors, setErrors] = useState({});
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState(false);

  if (!user) {
    navigate("/login", { replace: true });
    return null;
  }

  const handleChange = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const handleLogout = () => {
    sessionStorage.removeItem("user");
    navigate("/login");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setErrors({});
    setSuccess(false);

    const payload = {
      requestType: form.requestType,
      sku: form.sku,
      productName: form.requestType !== "DELETE" ? form.productName : null,
      category: form.requestType !== "DELETE" ? form.category : null,
      description: form.description || null,
      quantity: form.requestType !== "DELETE" ? Number(form.quantity) : null,
      minThreshold: form.requestType !== "DELETE" ? Number(form.minThreshold) : null,
      userId: user.userId,
    };

    try {
      await api.post("/product-requests", payload);
      setSuccess(true);
      setForm(emptyForm);
    } catch (err) {
      const data = err.response?.data;
      if (data?.fieldErrors) {
        setErrors(data.fieldErrors);
      } else {
        setErrors({ _general: data?.message || data || "Something went wrong." });
      }
    } finally {
      setSaving(false);
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
          <button className="prod-nav-btn" onClick={() => navigate("/product-requests/mine")}>My Requests</button>
          <button className="prod-nav-btn" onClick={() => navigate("/dashboard")}>Dashboard</button>
          <span className="dash-role-badge">{user.role.replace("_", " ")}</span>
          <button className="dash-logout-btn" onClick={handleLogout}>Logout</button>
        </div>
      </div>

      <div className="dash-content">
        <div className="dash-welcome-eyebrow">Product Requests</div>
        <h1 className="dash-welcome-title">Submit a Product Request</h1>
        <p className="dash-welcome-subtitle">
          Adding, editing, or deleting a product requires Administrator approval. Fill out the form below,
          your request will show as Pending until it's reviewed.
        </p>

        <div className="dash-card" style={{ maxWidth: "560px" }}>
          {success && (
            <p className="pr-success-banner">
              Request submitted. Check "My Requests" to see when it's reviewed.
            </p>
          )}
          {errors._general && <p className="prod-form-error">{errors._general}</p>}

          <form onSubmit={handleSubmit} className="prod-form">
            <label>
              Request Type
              <select
                value={form.requestType}
                onChange={(e) => handleChange("requestType", e.target.value)}
              >
                <option value="CREATE">Add new product</option>
                <option value="UPDATE">Edit existing product</option>
                <option value="DELETE">Delete existing product</option>
              </select>
            </label>

            <label>
              SKU {form.requestType !== "CREATE" && "(of the existing product)"}
              <input
                type="text"
                value={form.sku}
                onChange={(e) => handleChange("sku", e.target.value)}
                required
              />
              {errors.sku && <span className="prod-field-error">{errors.sku}</span>}
            </label>

            {form.requestType !== "DELETE" && (
              <>
                <label>
                  Product Name
                  <input
                    type="text"
                    value={form.productName}
                    onChange={(e) => handleChange("productName", e.target.value)}
                    required
                  />
                </label>

                <label>
                  Category
                  <input
                    type="text"
                    value={form.category}
                    onChange={(e) => handleChange("category", e.target.value)}
                    required
                  />
                </label>

                <div className="prod-form-row">
                  <label>
                    Quantity
                    <input
                      type="number"
                      min="0"
                      value={form.quantity}
                      onChange={(e) => handleChange("quantity", e.target.value)}
                      required
                    />
                  </label>
                  <label>
                    Min Threshold
                    <input
                      type="number"
                      min="0"
                      value={form.minThreshold}
                      onChange={(e) => handleChange("minThreshold", e.target.value)}
                      required
                    />
                  </label>
                </div>
              </>
            )}

            <label>
              {form.requestType === "DELETE" ? "Reason for deletion" : "Description"}
              <textarea
                value={form.description}
                onChange={(e) => handleChange("description", e.target.value)}
                rows={2}
                required={form.requestType === "DELETE"}
              />
            </label>

            <div className="prod-modal-actions">
              <button type="submit" className="prod-primary-btn" disabled={saving}>
                {saving ? "Submitting…" : "Submit Request"}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default SubmitProductRequest;
