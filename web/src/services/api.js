import axios from "axios";

// In development this falls back to localhost. On Render, set VITE_API_BASE_URL
// in the frontend service's environment variables to your deployed backend URL,
// e.g. https://your-backend.onrender.com/api
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api",
});

export default api;
