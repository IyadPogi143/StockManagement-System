import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./Dashboard.css";
import "./Products.css";

function Products() {
  const navigate = useNavigate();
  const user = JSON.parse(sessionStorage.getItem("user") || "null");

  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!user || user.role !== "ADMINISTRATOR") {
      navigate("/dashboard", { replace: true });
      return;
    }
    loadProducts();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadProducts = () => {
    setLoading(true);
    api
      .get("/products")
      .then((res) => setProducts(res.data))
      .catch(() => setError("Failed to load products."))
      .finally(() => setLoading(false));
  };

  if (!user || user.role !== "ADMINISTRATOR") {
    return null;
  }

  const handleLogout = () => {
    sessionStorage.removeItem("user");
    navigate("/login");
  };

  const handleAdjust = async (sku, changeAmount) => {
    try {
      await api.patch(`/products/${sku}/quantity`, {
        changeAmount,
        userId: user.userId,
      });
      loadProducts();
    } catch (err) {
      alert(err.response?.data?.message || "Failed to adjust quantity.");
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
          <button className="prod-nav-btn" onClick={() => navigate("/product-requests/review")}>Review Requests</button>
          <button className="prod-nav-btn" onClick={() => navigate("/dashboard")}>Dashboard</button>
          <span className="dash-role-badge">ADMINISTRATOR</span>
          <button className="dash-logout-btn" onClick={handleLogout}>Logout</button>
        </div>
      </div>

      <div className="dash-content">
        <div className="prod-header-row">
          <div>
            <div className="dash-welcome-eyebrow">Inventory</div>
            <h1 className="dash-welcome-title">Product Catalog</h1>
            <p className="dash-welcome-subtitle">
              View-only. Adding, editing, or deleting products requires a request submitted by a clerk
              and approved here through Review Requests. Quantity can still be adjusted directly below.
            </p>
          </div>
        </div>

        <div className="dash-card admin-table-card">
          {loading && <p className="admin-status-text">Loading products…</p>}
          {error && <p className="admin-status-text admin-error-text">{error}</p>}

          {!loading && !error && (
            <table className="admin-users-table">
              <thead>
                <tr>
                  <th>SKU</th>
                  <th>Name</th>
                  <th>Category</th>
                  <th>Quantity</th>
                  <th>Min Threshold</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {products.length === 0 && (
                  <tr>
                    <td colSpan={6} className="admin-status-text">No products yet.</td>
                  </tr>
                )}
                {products.map((p) => (
                  <tr key={p.sku} className={p.lowStock ? "prod-row-low" : ""}>
                    <td>{p.sku}</td>
                    <td>{p.productName}</td>
                    <td>{p.category}</td>
                    <td>
                      <div className="prod-qty-controls">
                        <button onClick={() => handleAdjust(p.sku, -1)} disabled={p.quantity <= 0}>−</button>
                        <span>{p.quantity}</span>
                        <button onClick={() => handleAdjust(p.sku, 1)}>+</button>
                      </div>
                    </td>
                    <td>{p.minThreshold}</td>
                    <td>
                      {p.lowStock ? (
                        <span className="stamp stamp-rejected">Low stock</span>
                      ) : (
                        <span className="stamp stamp-approved">OK</span>
                      )}
                    </td>
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

export default Products;
