import React, { useState, useEffect } from 'react';
import { Chart as ChartJS, ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement, Title } from 'chart.js';
import { Pie, Bar } from 'react-chartjs-2';
import { dashboardAPI } from '../services/api';

ChartJS.register(ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement, Title);

const Dashboard = () => {
  const [dashboard, setDashboard] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboard();
  }, []);

  const fetchDashboard = async () => {
    try {
      const response = await dashboardAPI.getDashboard();
      setDashboard(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching dashboard:', error);
      setLoading(false);
    }
  };

  if (loading) return <div className="text-center py-8">Loading...</div>;
  if (!dashboard) return <div className="text-center py-8">No data available</div>;

  const pieData = {
    labels: Object.keys(dashboard.categoryExpenses),
    datasets: [
      {
        data: Object.values(dashboard.categoryExpenses),
        backgroundColor: [
          'rgba(255, 99, 132, 0.8)',
          'rgba(54, 162, 235, 0.8)',
          'rgba(255, 206, 86, 0.8)',
          'rgba(75, 192, 192, 0.8)',
          'rgba(153, 102, 255, 0.8)',
        ],
      },
    ],
  };

  const barData = {
    labels: Object.keys(dashboard.monthlyData),
    datasets: [
      {
        label: 'Monthly Income',
        data: Object.values(dashboard.monthlyData),
        backgroundColor: 'rgba(75, 192, 192, 0.8)',
      },
    ],
  };

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-8">Dashboard</h1>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-gradient-to-br from-green-400 to-green-600 rounded-lg shadow-lg p-6 text-white">
          <h3 className="text-lg font-semibold mb-2">Total Income</h3>
          <p className="text-4xl font-bold">${dashboard.totalIncome.toFixed(2)}</p>
        </div>
        <div className="bg-gradient-to-br from-red-400 to-red-600 rounded-lg shadow-lg p-6 text-white">
          <h3 className="text-lg font-semibold mb-2">Total Expense</h3>
          <p className="text-4xl font-bold">${dashboard.totalExpense.toFixed(2)}</p>
        </div>
        <div className={`bg-gradient-to-br ${dashboard.netBalance >= 0 ? 'from-blue-400 to-blue-600' : 'from-purple-400 to-purple-600'} rounded-lg shadow-lg p-6 text-white`}>
          <h3 className="text-lg font-semibold mb-2">Net Balance</h3>
          <p className="text-4xl font-bold">${dashboard.netBalance.toFixed(2)}</p>
        </div>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h3 className="text-xl font-bold mb-4">Expenses by Category</h3>
          <Pie data={pieData} />
        </div>
        <div className="bg-white rounded-lg shadow-lg p-6">
          <h3 className="text-xl font-bold mb-4">Monthly Income Trend</h3>
          <Bar data={barData} options={{ responsive: true, maintainAspectRatio: true }} />
        </div>
      </div>

      {/* Stats */}
      <div className="mt-8 bg-white rounded-lg shadow-lg p-6">
        <h3 className="text-xl font-bold mb-4">Quick Stats</h3>
        <p className="text-gray-600">Total Transactions: <span className="font-bold text-lg">{dashboard.transactionCount}</span></p>
      </div>
    </div>
  );
};

export default Dashboard;
