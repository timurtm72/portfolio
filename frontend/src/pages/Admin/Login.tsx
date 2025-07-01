import React from 'react';

const AdminLogin: React.FC = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="max-w-md w-full space-y-8">
        <h2 className="text-3xl font-bold text-center">Вход в админ панель</h2>
        <form className="mt-8 space-y-6">
          <div>
            <label htmlFor="username" className="sr-only">Логин</label>
            <input
              type="text"
              id="username"
              name="username"
              required
              className="relative block w-full px-3 py-2 border border-gray-300 rounded-md"
              placeholder="Логин"
            />
          </div>
          <div>
            <label htmlFor="password" className="sr-only">Пароль</label>
            <input
              type="password"
              id="password"
              name="password"
              required
              className="relative block w-full px-3 py-2 border border-gray-300 rounded-md"
              placeholder="Пароль"
            />
          </div>
          <button
            type="submit"
            className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
          >
            Войти
          </button>
        </form>
      </div>
    </div>
  );
};

export default AdminLogin; 