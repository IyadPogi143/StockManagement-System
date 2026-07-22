import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api",
});

api.interceptors.request.use((config) => {
  const accessToken = sessionStorage.getItem("accessToken");
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }
  return config;
});

let isRefreshing = false;
let pendingRequests = [];

function resolvePending(newAccessToken) {
  pendingRequests.forEach((cb) => cb(newAccessToken));
  pendingRequests = [];
}

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status !== 401 || originalRequest._retry || originalRequest.url?.includes("/auth/")) {
      return Promise.reject(error);
    }

    const refreshToken = sessionStorage.getItem("refreshToken");
    if (!refreshToken) {
      sessionStorage.clear();
      window.location.href = "/login";
      return Promise.reject(error);
    }

    originalRequest._retry = true;

    if (isRefreshing) {
      return new Promise((resolve) => {
        pendingRequests.push((newAccessToken) => {
          originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
          resolve(api(originalRequest));
        });
      });
    }

    isRefreshing = true;
    try {
      const res = await axios.post(
        `${api.defaults.baseURL}/auth/refresh`,
        { refreshToken }
      );
      const { accessToken, refreshToken: newRefreshToken } = res.data;
      sessionStorage.setItem("accessToken", accessToken);
      sessionStorage.setItem("refreshToken", newRefreshToken);
      isRefreshing = false;
      resolvePending(accessToken);

      originalRequest.headers.Authorization = `Bearer ${accessToken}`;
      return api(originalRequest);
    } catch (refreshError) {
      isRefreshing = false;
      sessionStorage.clear();
      window.location.href = "/login";
      return Promise.reject(refreshError);
    }
  }
);

export default api;
