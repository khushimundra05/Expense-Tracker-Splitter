import { useEffect, useState } from 'react';
import { api } from '../api/client';

export default function AddMemberForm({ groupId, onSuccess }) {
  const [users, setUsers] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    api.getUsers().then(setUsers).catch(() => setUsers([]));
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!selectedUserId) return;
    setSubmitting(true);
    setError(null);
    api
      .addUserToGroup(groupId, Number(selectedUserId))
      .then(() => {
        setSelectedUserId('');
        onSuccess?.();
      })
      .catch((err) => setError(err.body?.message || err.message || 'Failed to add member'))
      .finally(() => setSubmitting(false));
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-wrap gap-4 items-end max-w-md">
      <div className="flex-1 min-w-[200px]">
        <label className="label">User to add</label>
        <select
          value={selectedUserId}
          onChange={(e) => setSelectedUserId(e.target.value)}
          className="input-field"
        >
          <option value="">Select user</option>
          {users.map((u) => (
            <option key={u.id} value={u.id}>{u.name} (ID: {u.id})</option>
          ))}
        </select>
      </div>
      <button type="submit" disabled={submitting || !selectedUserId} className="btn-primary">
        {submitting ? 'Adding…' : 'Add to group'}
      </button>
      {error && <p className="text-red-400 text-sm w-full">{error}</p>}
    </form>
  );
}
