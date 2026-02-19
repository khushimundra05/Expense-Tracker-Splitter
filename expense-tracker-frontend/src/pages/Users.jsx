import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../api/client';

export default function Users() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [name, setName] = useState('');
  const [idInput, setIdInput] = useState('');
  const [creating, setCreating] = useState(false);

  const loadUsers = () => {
    setLoading(true);
    setError(null);
    api
      .getUsers()
      .then(setUsers)
      .catch((e) => setError(e.body?.message || e.message || 'Failed to load users'))
      .finally(() => setLoading(false));
  };

  useEffect(loadUsers, []);

  const handleCreate = (e) => {
    e.preventDefault();
    const numId = idInput.trim() ? Number(idInput.trim()) : null;
    const userName = name.trim();
    if (!userName) return;
    if (numId == null || isNaN(numId)) {
      setError('Please enter a numeric user ID.');
      return;
    }
    setCreating(true);
    setError(null);
    api
      .createUser({ id: numId, name: userName })
      .then(() => {
        setName('');
        setIdInput('');
        loadUsers();
      })
      .catch((e) => setError(e.body?.message || e.message || 'Failed to create user'))
      .finally(() => setCreating(false));
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-20">
        <div className="animate-spin rounded-full h-10 w-10 border-2 border-primary-500 border-t-transparent" />
      </div>
    );
  }

  return (
    <div className="space-y-8">
      <h1 className="text-2xl font-bold text-slate-100">Users</h1>
      <p className="text-slate-400">Create users here, then add them to groups from the group detail page.</p>

      <div className="card">
        <h2 className="text-lg font-semibold text-slate-200 mb-4">Create user</h2>
        <form onSubmit={handleCreate} className="flex flex-col sm:flex-row gap-4 max-w-md">
          <div className="flex-1">
            <label className="label">ID (number)</label>
            <input
              type="text"
              value={idInput}
              onChange={(e) => setIdInput(e.target.value)}
              placeholder="e.g. 1"
              className="input-field"
            />
          </div>
          <div className="flex-1">
            <label className="label">Name</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Display name"
              className="input-field"
            />
          </div>
          <div className="flex items-end">
            <button type="submit" disabled={creating} className="btn-primary">
              {creating ? 'Creating…' : 'Create'}
            </button>
          </div>
        </form>
      </div>

      {error && (
        <div className="card bg-red-900/20 border-red-500/50 text-red-200">{error}</div>
      )}

      {users.length === 0 ? (
        <div className="card text-slate-400">No users yet. Create one above to add to groups.</div>
      ) : (
        <div className="card">
          <h2 className="text-lg font-semibold text-slate-200 mb-4">All users</h2>
          <ul className="space-y-2">
            {users.map((u) => (
              <li key={u.id} className="flex justify-between items-center py-2 border-b border-slate-700/50 last:border-0">
                <Link to={`/users/${u.id}`} className="text-primary-400 hover:underline font-medium">
                  {u.name}
                </Link>
                <span className="text-slate-500 text-sm">ID: {u.id}</span>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
