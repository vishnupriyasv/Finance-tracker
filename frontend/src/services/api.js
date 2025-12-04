import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
});

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authAPI = {
  signup: (data) => api.post('/auth/signup', data),
  login: (data) => api.post('/auth/login', data),
  getCurrentUser: (username) => api.get(`/auth/me?username=${username}`),
};

export const transactionAPI = {
  getAll: () => api.get('/transactions'),
  create: (data) => api.post('/transactions', data),
  update: (id, data) => api.put(`/transactions/${id}`, data),
  delete: (id) => api.delete(`/transactions/${id}`),
  getByType: (type) => api.get(`/transactions/type/${type}`),
  getByDateRange: (start, end) => api.get(`/transactions/date-range?start=${start}&end=${end}`),
  getTotalByType: (type) => api.get(`/transactions/total/${type}`),
};

export const categoryAPI = {
  getAll: () => api.get('/categories'),
  getById: (id) => api.get(`/categories/${id}`),
  create: (data) => api.post('/categories', data),
  update: (id, data) => api.put(`/categories/${id}`, data),
  delete: (id) => api.delete(`/categories/${id}`),
  getByType: (type) => api.get(`/categories/type/${type}`),
};

export const budgetAPI = {
  getAll: () => api.get('/budgets'),
  getByMonth: (month) => api.get(`/budgets/month/${month}`),
  create: (data) => api.post('/budgets', data),
  update: (id, data) => api.put(`/budgets/${id}`, data),
  delete: (id) => api.delete(`/budgets/${id}`),
};

export const dashboardAPI = {
  getDashboard: () => api.get('/dashboard'),
};

export default api;
