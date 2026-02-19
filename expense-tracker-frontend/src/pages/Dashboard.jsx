import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../api/client';

export default function Dashboard() {
  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [createName, setCreateName] = useState('');
  const [creating, setCreating] = useState(false);

  const loadGroups = () => {
    setLoading(true);
    setError(null);
    api
      .getGroups()
      .then(setGroups)
      .catch((e) => setError(e.body?.message || e.message || 'Failed to load groups'))
      .finally(() => setLoading(false));
  };

  useEffect(loadGroups, []);

  const handleCreate = (e) => {
    e.preventDefault();
    if (!createName.trim()) return;
    setCreating(true);
    setError(null);
    api
      .createGroup(createName.trim())
      .then(() => {
        setCreateName('');
        loadGroups();
      })
      .catch((e) => setError(e.body?.message || e.message || 'Failed to create group'))
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
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <h1 className="text-2xl font-bold text-slate-100">Groups</h1>
        <form onSubmit={handleCreate} className="flex gap-2 flex-wrap">
          <input
            type="text"
            value={createName}
            onChange={(e) => setCreateName(e.target.value)}
            placeholder="New group name"
            className="input-field flex-1 min-w-[180px]"
          />
          <button type="submit" disabled={creating} className="btn-primary whitespace-nowrap">
            {creating ? 'Creating…' : 'Create group'}
          </button>
        </form>
      </div>

      {error && (
        <div className="card bg-red-900/20 border-red-500/50 text-red-200">{error}</div>
      )}

      {groups.length === 0 ? (
        <div className="card text-center py-12 text-slate-400">
          <p className="mb-4">No groups yet. Create one to start tracking expenses.</p>
          <p className="text-sm">Use the form above or add users first from the Users page.</p>
        </div>
      ) : (
        <ul className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
          {groups.map((g) => (
            <li key={g.id}>
              <Link
                to={`/groups/${g.id}`}
                className="card block hover:border-primary-500/50 hover:bg-slate-800 transition-colors"
              >
                <span className="font-semibold text-slate-100">{g.name}</span>
                <span className="text-slate-400 text-sm mt-1 block">View details →</span>
              </Link>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
