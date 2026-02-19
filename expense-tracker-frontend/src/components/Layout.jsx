import { Link, Outlet, useLocation } from 'react-router-dom';

export default function Layout() {
  const location = useLocation();
  const isActive = (path) => location.pathname === path || location.pathname.startsWith(path + '/');

  return (
    <div className="min-h-screen flex flex-col">
      <header className="border-b border-slate-700 bg-slate-800/50 sticky top-0 z-10 backdrop-blur">
        <div className="max-w-5xl mx-auto px-4 py-3 flex items-center justify-between">
          <Link to="/" className="text-xl font-bold text-primary-400 hover:text-primary-300 transition-colors">
            Expense Tracker
          </Link>
          <nav className="flex gap-4">
            <Link
              to="/"
              className={`px-3 py-2 rounded-lg font-medium transition-colors ${
                isActive('/') && !location.pathname.startsWith('/groups/')
                  ? 'bg-primary-600 text-white'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-slate-700'
              }`}
            >
              Dashboard
            </Link>
            <Link
              to="/users"
              className={`px-3 py-2 rounded-lg font-medium transition-colors ${
                isActive('/users')
                  ? 'bg-primary-600 text-white'
                  : 'text-slate-400 hover:text-slate-200 hover:bg-slate-700'
              }`}
            >
              Users
            </Link>
          </nav>
        </div>
      </header>
      <main className="flex-1 max-w-5xl w-full mx-auto px-4 py-6">
        <Outlet />
      </main>
    </div>
  );
}
