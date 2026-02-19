import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { api } from '../api/client';

export default function UserDetail() {
  const { userId } = useParams();
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!userId) return;
    setLoading(true);
    setError(null);
    api
      .getUserSummary(userId)
      .then(setSummary)
      .catch((e) => setError(e.body?.message || e.message || 'Failed to load user'))
      .finally(() => setLoading(false));
  }, [userId]);

  if (loading) {
    return (
      <div className="flex items-center justify-center py-20">
        <div className="animate-spin rounded-full h-10 w-10 border-2 border-primary-500 border-t-transparent" />
      </div>
    );
  }

  if (error || !summary) {
    return (
      <div className="space-y-4">
        <Link to="/users" className="text-primary-400 hover:underline">← Back to Users</Link>
        <div className="card bg-red-900/20 border-red-500/50 text-red-200">
          {error || 'User not found'}
        </div>
      </div>
    );
  }

  const total = Number(summary.totalNetBalance);
  const owesOrOwed = total < 0 ? 'owes' : 'is owed';

  return (
    <div className="space-y-8">
      <div className="flex flex-wrap items-center gap-4">
        <Link to="/users" className="text-primary-400 hover:underline">← Users</Link>
        <h1 className="text-2xl font-bold text-slate-100">{summary.userName}</h1>
      </div>

      <div className="card">
        <h2 className="text-lg font-semibold text-slate-200 mb-2">Overall</h2>
        <p className="text-slate-300">
          Across all groups, {summary.userName} {owesOrOwed}{' '}
          <span className={total < 0 ? 'text-red-400 font-semibold' : 'text-green-400 font-semibold'}>
            ₹{Math.abs(total).toFixed(2)}
          </span>
        </p>
      </div>

      <div className="card">
        <h2 className="text-lg font-semibold text-slate-200 mb-4">Balance by group</h2>
        {!summary.groupBalances || summary.groupBalances.length === 0 ? (
          <p className="text-slate-400">Not in any group yet.</p>
        ) : (
          <ul className="space-y-3">
            {summary.groupBalances.map((g) => {
              const bal = Number(g.balance);
              return (
                <li
                  key={g.groupId}
                  className="flex justify-between items-center py-2 border-b border-slate-700/50 last:border-0"
                >
                  <Link
                    to={`/groups/${g.groupId}`}
                    className="text-primary-400 hover:underline font-medium"
                  >
                    {g.groupName}
                  </Link>
                  <span className={bal < 0 ? 'text-red-400' : 'text-green-400'}>
                    {bal < 0 ? 'owes ₹' : 'is owed ₹'}
                    {Math.abs(bal).toFixed(2)}
                  </span>
                </li>
              );
            })}
          </ul>
        )}
      </div>
    </div>
  );
}
