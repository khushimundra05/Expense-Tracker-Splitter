import { useState } from 'react';
import { api } from '../api/client';

export default function AddExpenseForm({ groupId, members, onSuccess }) {
  const [description, setDescription] = useState('');
  const [amount, setAmount] = useState('');
  const [paidByUserId, setPaidByUserId] = useState(members[0]?.id ?? '');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = (e) => {
    e.preventDefault();
    const amt = parseFloat(amount);
    if (!description.trim() || isNaN(amt) || amt <= 0) {
      setError('Please enter a description and a valid positive amount.');
      return;
    }
    if (!paidByUserId) {
      setError('Please select who paid.');
      return;
    }
    setSubmitting(true);
    setError(null);
    api
      .addExpense({
        groupId,
        paidByUserId: Number(paidByUserId),
        description: description.trim(),
        amount: amt,
      })
      .then(() => {
        setDescription('');
        setAmount('');
        setPaidByUserId(members[0]?.id ?? '');
        onSuccess?.();
      })
      .catch((err) => setError(err.body?.message || err.message || 'Failed to add expense'))
      .finally(() => setSubmitting(false));
  };

  if (members.length === 0) {
    return (
      <p className="text-slate-400 text-sm">Add at least one member to this group before adding expenses.</p>
    );
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4 max-w-md">
      <div>
        <label className="label">Description</label>
        <input
          type="text"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="e.g. Dinner"
          className="input-field"
        />
      </div>
      <div>
        <label className="label">Amount (₹)</label>
        <input
          type="number"
          step="0.01"
          min="0"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          placeholder="0.00"
          className="input-field"
        />
      </div>
      <div>
        <label className="label">Paid by</label>
        <select
          value={paidByUserId}
          onChange={(e) => setPaidByUserId(e.target.value)}
          className="input-field"
        >
          {members.map((m) => (
            <option key={m.id} value={m.id}>{m.name}</option>
          ))}
        </select>
      </div>
      {error && <p className="text-red-400 text-sm">{error}</p>}
      <button type="submit" disabled={submitting} className="btn-primary">
        {submitting ? 'Adding…' : 'Add expense'}
      </button>
    </form>
  );
}
