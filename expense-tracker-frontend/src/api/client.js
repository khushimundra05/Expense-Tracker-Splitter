const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080';

async function request(path, options = {}) {
  const url = path.startsWith('http') ? path : `${BASE}${path}`;
  const res = await fetch(url, {
    ...options,
    headers: { 'Content-Type': 'application/json', ...options.headers },
  });
  if (!res.ok) {
    const err = new Error(res.statusText || 'Request failed');
    err.status = res.status;
    try {
      err.body = await res.json();
    } catch {
      err.body = { message: await res.text() };
    }
    throw err;
  }
  const text = await res.text();
  if (!text) return null;
  try {
    return JSON.parse(text);
  } catch {
    return null;
  }
}

export const api = {
  // Groups
  getGroups: () => request('/groups'),
  getGroupSummary: (groupId) => request(`/groups/${groupId}/summary`),
  getGroupMembers: (groupId) => request(`/groups/${groupId}/members`),
  getGroupExpenses: (groupId) => request(`/groups/${groupId}/expenses`),
  createGroup: (name) => request(`/groups?name=${encodeURIComponent(name)}`, { method: 'POST' }),
  addUserToGroup: (groupId, userId) =>
    request(`/groups/${groupId}/users/${userId}`, { method: 'POST' }),

  // Expenses
  addExpense: (body) => request('/expenses', { method: 'POST', body: JSON.stringify(body) }),

  // Users
  getUsers: () => request('/users'),
  getUser: (id) => request(`/user/${id}`),
  getUserSummary: (id) => request(`/users/${id}/summary`),
  createUser: (body) => request('/user', { method: 'POST', body: JSON.stringify(body) }),
  updateUser: (id, body) => request(`/user/${id}`, { method: 'PUT', body: JSON.stringify(body) }),
  deleteUser: (id) => request(`/user/${id}`, { method: 'DELETE' }),
  searchUsers: (name) => request(`/users/search?name=${encodeURIComponent(name)}`),
};
