import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { api } from '../api/client';
import AddExpenseForm from '../components/AddExpenseForm';
import AddMemberForm from '../components/AddMemberForm';

export default function GroupDetail() {
  const { groupId } = useParams();
  const [summary, setSummary] = useState(null);
  const [expenses, setExpenses] = useState([]);
  const [members, setMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const load = () => {
    if (!groupId) return;
    setLoading(true);
    setError(null);
    Promise.all([
      api.getGroupSummary(groupId),
      api.getGroupExpenses(groupId),
      api.getGroupMembers(groupId),
    ])
      .then(([s, e, m]) => {
        setSummary(s);
        setExpenses(e || []);
        setMembers(m || []);
      })
      .catch((err) => setError(err.body?.message || err.message || 'Failed to load group'))
      .finally(() => setLoading(false));
  };

  useEffect(load, [groupId]);

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
        <Link to="/" className="text-primary-400 hover:underline">← Back to Dashboard</Link>
        <div className="card bg-red-900/20 border-red-500/50 text-red-200">
          {error || 'Group not found'}
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-8">
      <div className="flex flex-wrap items-center gap-4">
        <Link to="/" className="text-primary-400 hover:underline">← Dashboard</Link>
        <h1 className="text-2xl font-bold text-slate-100">{summary.groupName}</h1>
      </div>

      {/* Total & summary card */}
      <div className="card">
        <h2 className="text-lg font-semibold text-slate-200 mb-2">Summary</h2>
        <p className="text-2xl font-bold text-primary-400">
          Total spent: ₹{Number(summary.totalExpense).toFixed(2)}
        </p>
      </div>

      {/* Balances */}
      <div className="card">
        <h2 className="text-lg font-semibold text-slate-200 mb-4">Balances</h2>
        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-slate-600">
                <th className="pb-2 text-slate-400 font-medium">Member</th>
                <th className="pb-2 text-slate-400 font-medium">Balance</th>
              </tr>
            </thead>
            <tbody>
              {(summary.balances || []).map((b) => (
                <tr key={b.userId} className="border-b border-slate-700/50">
                  <td className="py-2 text-slate-200">{b.userName}</td>
                  <td className={`py-2 font-medium ${b.balance < 0 ? 'text-red-400' : 'text-green-400'}`}>
                    ₹{Number(b.balance).toFixed(2)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Who owes whom (pairwise): B owes A, C owes A, C owes B, etc. */}
      {(summary.whoOwesWhom && summary.whoOwesWhom.length > 0) && (
        <div className="card">
          <h2 className="text-lg font-semibold text-slate-200 mb-2">Who owes whom</h2>
          <p className="text-slate-400 text-sm mb-4">Net debt between each pair (e.g. B owes A because A paid more)</p>
          <ul className="space-y-2">
            {summary.whoOwesWhom.map((s, i) => (
              <li key={i} className="flex items-center gap-2 text-slate-300">
                <span className="font-medium text-slate-200">{s.fromUserName}</span>
                <span>owes</span>
                <span className="font-medium text-slate-200">{s.toUserName}</span>
                <span className="text-primary-400 font-semibold">₹{Number(s.amount).toFixed(2)}</span>
              </li>
            ))}
          </ul>
        </div>
      )}

      {/* Minimal settlements: fewest payments to clear all debts */}
      {(summary.settlements && summary.settlements.length > 0) && (
        <div className="card">
          <h2 className="text-lg font-semibold text-slate-200 mb-2">Settle in fewest steps</h2>
          <p className="text-slate-400 text-sm mb-4">Minimum number of payments to clear all debts</p>
          <ul className="space-y-2">
            {summary.settlements.map((s, i) => (
              <li key={i} className="flex items-center gap-2 text-slate-300">
                <span className="text-red-400 font-medium">{s.fromUserName}</span>
                <span>pays</span>
                <span className="text-green-400 font-medium">{s.toUserName}</span>
                <span className="text-primary-400 font-semibold">₹{Number(s.amount).toFixed(2)}</span>
              </li>
            ))}
          </ul>
        </div>
      )}

      {/* Add expense */}
      <div className="card">
        <h2 className="text-lg font-semibold text-slate-200 mb-4">Add expense</h2>
        <AddExpenseForm
          groupId={Number(groupId)}
          members={members}
          onSuccess={load}
        />
      </div>

      {/* Add member */}
      <div className="card">
        <h2 className="text-lg font-semibold text-slate-200 mb-4">Add member to group</h2>
        <AddMemberForm groupId={Number(groupId)} onSuccess={load} />
      </div>

      {/* Expenses list */}
      <div className="card">
        <h2 className="text-lg font-semibold text-slate-200 mb-4">Expenses</h2>
        {expenses.length === 0 ? (
          <p className="text-slate-400">No expenses yet. Add one above.</p>
        ) : (
          <ul className="space-y-2">
            {expenses.map((ex) => (
              <li key={ex.id} className="flex justify-between items-center py-2 border-b border-slate-700/50 last:border-0">
                <span className="text-slate-200">{ex.description}</span>
                <span className="text-primary-400 font-medium">₹{Number(ex.amount).toFixed(2)}</span>
                <span className="text-slate-400 text-sm">by {ex.paidByUserName}</span>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
