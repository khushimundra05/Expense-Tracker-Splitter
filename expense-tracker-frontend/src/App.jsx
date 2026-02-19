<<<<<<< HEAD
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import GroupDetail from './pages/GroupDetail';
import Users from './pages/Users';
import UserDetail from './pages/UserDetail';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Dashboard />} />
          <Route path="groups/:groupId" element={<GroupDetail />} />
          <Route path="users" element={<Users />} />
          <Route path="users/:userId" element={<UserDetail />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
=======
import { useEffect, useState } from "react";

function App() {
  const [groups, setGroups] = useState([]);
  const [selectedGroupId, setSelectedGroupId] = useState(null);
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);

  // Fetch all groups on load
  useEffect(() => {
    fetch("http://localhost:8080/groups")
      .then((res) => res.json())
      .then((data) => {
        setGroups(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setLoading(false);
      });
  }, []);

  // Fetch summary when a group is selected
  useEffect(() => {
    if (!selectedGroupId) return;

    fetch(`http://localhost:8080/groups/${selectedGroupId}/summary`)
      .then((res) => res.json())
      .then((data) => setSummary(data))
      .catch((err) => console.error(err));
  }, [selectedGroupId]);

  if (loading) return <h2>Loading groups...</h2>;

  return (
    <div style={{ padding: "20px" }}>
      <h1>Expense Groups</h1>

      <ul>
        {groups.map((group) => (
          <li
            key={group.id}
            style={{ cursor: "pointer", marginBottom: "5px" }}
            onClick={() => setSelectedGroupId(group.id)}
          >
            {group.name}
          </li>
        ))}
      </ul>

      {summary && (
        <div style={{ marginTop: "30px" }}>
          <h2>{summary.groupName}</h2>
          <h3>Total Expense: ₹{summary.totalExpense}</h3>

          <h3>Balances</h3>
          <table border="1" cellPadding="8">
            <thead>
              <tr>
                <th>User</th>
                <th>Balance</th>
              </tr>
            </thead>
            <tbody>
              {summary.balances.map((b) => (
                <tr key={b.userId}>
                  <td>{b.userName}</td>
                  <td
                    style={{
                      color: b.balance < 0 ? "red" : "green",
                    }}
                  >
                    ₹{b.balance}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default App;
>>>>>>> b10887ff55024b65c8153fdd4b3715f2755bddc2
