import React from 'react';

const AdminDashboard: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-50">
      <div className="container mx-auto px-6 py-8">
        <h1 className="text-3xl font-bold mb-8">Панель администратора</h1>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
          <div className="bg-white p-6 rounded-lg shadow">
            <h3 className="text-lg font-semibold mb-2">Проекты</h3>
            <p className="text-3xl font-bold text-blue-600">12</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow">
            <h3 className="text-lg font-semibold mb-2">Навыки</h3>
            <p className="text-3xl font-bold text-green-600">8</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow">
            <h3 className="text-lg font-semibold mb-2">Просмотры</h3>
            <p className="text-3xl font-bold text-purple-600">1,234</p>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold">Управление проектами</h2>
          </div>
          <div className="p-6">
            <button className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
              Добавить проект
            </button>
            <div className="mt-6">
              <p>Здесь будет список проектов для редактирования</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard; 