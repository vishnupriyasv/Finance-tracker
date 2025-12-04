import React from 'react';
import { Link } from 'react-router-dom';

const Sidebar = ({ isOpen }) => {
  return (
    <aside
      className={`${
        isOpen ? 'block' : 'hidden'
      } md:block bg-gray-800 text-white w-64 min-h-screen p-4 fixed md:relative z-40`}
    >
      <nav className="space-y-2">
        <Link
          to="/dashboard"
          className="block px-4 py-3 rounded-lg hover:bg-gray-700 transition"
        >
          📊 Dashboard
        </Link>
        <Link
          to="/transactions"
          className="block px-4 py-3 rounded-lg hover:bg-gray-700 transition"
        >
          💳 Transactions
        </Link>
        <Link
          to="/categories"
          className="block px-4 py-3 rounded-lg hover:bg-gray-700 transition"
        >
          📂 Categories
        </Link>
        <Link
          to="/budgets"
          className="block px-4 py-3 rounded-lg hover:bg-gray-700 transition"
        >
          💡 Budgets
        </Link>
      </nav>
    </aside>
  );
};

export default Sidebar;
