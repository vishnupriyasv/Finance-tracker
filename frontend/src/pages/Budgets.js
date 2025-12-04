import React, { useState, useEffect } from 'react';
import { budgetAPI, categoryAPI } from '../services/api';
import { format } from 'date-fns';

const Budgets = () => {
  const [budgets, setBudgets] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    categoryId: '',
    budgetAmount: '',
    month: format(new Date(), 'yyyy-MM'),
  });

  useEffect(() => {
    fetchBudgets();
    fetchCategories();
  }, []);

  const fetchBudgets = async () => {
    try {
      const response = await budgetAPI.getAll();
      setBudgets(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching budgets:', error);
      setLoading(false);
    }
  };

  const fetchCategories = async () => {
    try {
      const response = await categoryAPI.getAll();
      setCategories(response.data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await budgetAPI.create(formData);
      setFormData({
        categoryId: '',
        budgetAmount: '',
        month: format(new Date(), 'yyyy-MM'),
      });
      setShowForm(false);
      fetchBudgets();
    } catch (error) {
      console.error('Error creating budget:', error);
    }
  };

  const handleDelete = async (id) => {
    try {
      await budgetAPI.delete(id);
      fetchBudgets();
    } catch (error) {
      console.error('Error deleting budget:', error);
    }
  };

  if (loading) return <div className="text-center py-8">Loading...</div>;

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Budgets</h1>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded"
        >
          {showForm ? 'Cancel' : 'Set Budget'}
        </button>
      </div>

      {showForm && (
        <div className="bg-white rounded-lg shadow-lg p-6 mb-6">
          <form onSubmit={handleSubmit}>
            <div className="grid grid-cols-2 gap-4">
              <select
                name="categoryId"
                value={formData.categoryId}
                onChange={handleChange}
                className="px-4 py-2 border rounded-lg focus:outline-none focus:border-blue-500"
                required
              >
                <option value="">Select Category</option>
                {categories.map((cat) => (
                  <option key={cat.id} value={cat.id}>
                    {cat.name}
                  </option>
                ))}
              </select>
              <input
                type="number"
                name="budgetAmount"
                placeholder="Budget Amount"
                value={formData.budgetAmount}
                onChange={handleChange}
                className="px-4 py-2 border rounded-lg focus:outline-none focus:border-blue-500"
                step="0.01"
                required
              />
              <input
                type="month"
                name="month"
                value={formData.month}
                onChange={handleChange}
                className="col-span-2 px-4 py-2 border rounded-lg focus:outline-none focus:border-blue-500"
                required
              />
            </div>
            <button
              type="submit"
              className="mt-4 w-full bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 rounded"
            >
              Set Budget
            </button>
          </form>
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {budgets.map((budget) => {
          const percentage = Math.min((budget.spentAmount / budget.budgetAmount) * 100, 100);
          return (
            <div key={budget.id} className="bg-white rounded-lg shadow-lg p-6">
              <h3 className="text-xl font-bold mb-2">{budget.categoryName}</h3>
              <p className="text-sm text-gray-600 mb-4">{budget.month}</p>
              
              <div className="mb-4">
                <div className="flex justify-between text-sm mb-2">
                  <span>Spent: ${budget.spentAmount.toFixed(2)}</span>
                  <span>Budget: ${budget.budgetAmount.toFixed(2)}</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div
                    className={`h-2 rounded-full transition-all ${
                      percentage > 100 ? 'bg-red-500' : percentage > 80 ? 'bg-yellow-500' : 'bg-green-500'
                    }`}
                    style={{ width: `${Math.min(percentage, 100)}%` }}
                  />
                </div>
              </div>

              <p className={`text-lg font-bold mb-4 ${budget.remainingAmount >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                Remaining: ${budget.remainingAmount.toFixed(2)}
              </p>

              <button
                onClick={() => handleDelete(budget.id)}
                className="w-full bg-red-500 hover:bg-red-600 text-white px-3 py-2 rounded"
              >
                Delete
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default Budgets;
