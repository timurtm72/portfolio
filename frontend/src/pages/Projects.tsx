import React, { useState, useEffect } from 'react';
import ProjectModal from '../components/ProjectModal.tsx';
import WebPImage from '../components/WebPImage.tsx';
import { API_URL } from '../config.ts';

interface Project {
  id: number;
  title: string;
  description: string;
  detailedDescription?: string;
  imageUrl?: string;
  shortDescription?: string;
  category: string;
  categoryDisplayName?: string;
  status: string;
  statusDisplayName?: string;
  featured: boolean;
  technologies: string[];
  githubUrl?: string;
  liveUrl?: string;
  videoUrl?: string;
}

interface ProjectsResponse {
  content: Project[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

const ProjectsPage: React.FC = () => {
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedProject, setSelectedProject] = useState<Project | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const categories = [
    { value: 'all', label: 'Все проекты' },
    { value: 'STM32', label: 'STM32' },
    { value: 'ESP32', label: 'ESP32' },
    { value: 'ARDUINO', label: 'Arduino' },
    { value: 'PLC', label: 'ПЛК' },
    { value: 'IOT', label: 'IoT' },
    { value: 'AUTOMATION', label: 'Автоматизация' },
    { value: 'EMBEDDED', label: 'Встраиваемые системы' },
    { value: 'ROBOTICS', label: 'Робототехника' }
  ];

  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_URL}/projects?size=50`);

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data: ProjectsResponse = await response.json();
      setProjects(data.content || []);
      setError(null);
    } catch (err) {
      console.error('Ошибка загрузки проектов:', err);
      setError('Не удалось загрузить проекты. Проверьте подключение к серверу.');
    } finally {
      setLoading(false);
    }
  };

  const handleProjectClick = (project: Project) => {
    setSelectedProject(project);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedProject(null);
  };

  const filteredProjects = projects.filter(project => {
    const matchesCategory = selectedCategory === 'all' || project.category === selectedCategory;
    const matchesSearch = project.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      project.description.toLowerCase().includes(searchTerm.toLowerCase());
    return matchesCategory && matchesSearch;
  });

  const getCategoryColor = (category: string): string => {
    const colors: { [key: string]: string } = {
      STM32: 'bg-blue-100 text-blue-800',
      ESP32: 'bg-green-100 text-green-800',
      ARDUINO: 'bg-orange-100 text-orange-800',
      PLC: 'bg-purple-100 text-purple-800',
      IOT: 'bg-indigo-100 text-indigo-800',
      AUTOMATION: 'bg-yellow-100 text-yellow-800',
      EMBEDDED: 'bg-teal-100 text-teal-800',
      ROBOTICS: 'bg-red-100 text-red-800'
    };
    return colors[category] || 'bg-gray-100 text-gray-800';
  };

  const getStatusColor = (status: string): string => {
    const colors: { [key: string]: string } = {
      COMPLETED: 'bg-green-100 text-green-800',
      IN_PROGRESS: 'bg-blue-100 text-blue-800',
      PLANNING: 'bg-yellow-100 text-yellow-800',
      ON_HOLD: 'bg-gray-200 text-gray-800'
    };
    return colors[status] || 'bg-gray-100 text-gray-800';
  };

  const getStatusLabel = (status: string): string => {
    const labels: { [key: string]: string } = {
      COMPLETED: 'Завершен',
      IN_PROGRESS: 'В разработке',
      PLANNING: 'Планирование',
      ON_HOLD: 'Приостановлен'
    };
    return labels[status] || status;
  };

  if (loading) {
    return (
      <div className="container mx-auto px-6 py-12">
        <div className="flex justify-center items-center min-h-screen">
          <div className="loading-spinner"></div>
          <span className="ml-3 text-lg">Загружаю проекты...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container mx-auto px-6 py-12">
        <div className="bg-red-50 border border-red-200 rounded-lg p-6 text-center">
          <h2 className="text-xl font-semibold text-red-800 mb-2">Ошибка загрузки</h2>
          <p className="text-red-600 mb-4">{error}</p>
          <button
            onClick={fetchProjects}
            className="btn btn-primary"
          >
            Попробовать снова
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-6 py-12">
      <div className="text-center mb-12">
        <h1 className="text-4xl font-bold mb-4 gradient-text">Мои проекты</h1>
        <p className="text-xl text-gray-600 max-w-3xl mx-auto">
          Более {projects.length} реализованных проектов в области микроконтроллеров,
          автоматизации и IoT. От простых устройств до сложных промышленных систем.
        </p>
      </div>

      {/* Фильтры */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-8 border border-gray-100">
        <div className="grid md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Категория
            </label>
            <select
              value={selectedCategory}
              onChange={(e) => setSelectedCategory(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white text-gray-900"
            >
              {categories.map(category => (
                <option key={category.value} value={category.value}>
                  {category.label}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Поиск
            </label>
            <input
              type="text"
              placeholder="Поиск по названию или описанию..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 bg-white text-gray-900"
            />
          </div>
        </div>
      </div>

      {/* Результаты */}
      <div className="mb-6">
        <p className="text-gray-600">
          Найдено проектов: {filteredProjects.length} из {projects.length}
        </p>
      </div>

      {/* Сетка проектов */}
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredProjects.map((project) => (
          <div
            key={project.id}
            className="card card-hover bg-white border border-gray-200 shadow hover:shadow-lg cursor-pointer transition-transform hover:scale-105"
            onClick={() => handleProjectClick(project)}
          >
            <div className="p-6">
              {/* Заголовок и badges */}
              <div className="flex justify-between items-start mb-4">
                <div className="flex-1">
                  {project.featured && (
                    <span className="inline-block bg-yellow-100 text-yellow-800 text-xs px-2 py-1 rounded-full mb-2">
                      ⭐ Рекомендуемый
                    </span>
                  )}
                  <h3 className="text-lg font-semibold text-gray-900 mb-2 line-clamp-2">
                    {project.title}
                  </h3>
                </div>
              </div>

              {/* Категория и статус */}
              <div className="flex gap-2 mb-3">
                <span className={`inline-block px-2 py-1 rounded-full text-xs font-medium ${getCategoryColor(project.category)}`}>
                  {project.categoryDisplayName || project.category}
                </span>
                <span className={`inline-block px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(project.status)}`}>
                  {project.statusDisplayName || getStatusLabel(project.status)}
                </span>
              </div>

              {/* Описание */}
              <p className="text-gray-700 text-sm mb-4 line-clamp-3">
                {project.shortDescription || project.description}
              </p>

              {/* Технологии */}
              {project.technologies && project.technologies.length > 0 && (
                <div className="mb-4">
                  <div className="flex flex-wrap gap-1">
                    {project.technologies.slice(0, 3).map((tech, index) => (
                      <span
                        key={index}
                        className="inline-block bg-gray-100 text-gray-800 text-xs px-2 py-1 rounded"
                      >
                        {tech}
                      </span>
                    ))}
                    {project.technologies.length > 3 && (
                      <span className="text-xs text-gray-500 px-2 py-1">
                        +{project.technologies.length - 3}
                      </span>
                    )}
                  </div>
                </div>
              )}

              {/* Кнопка для просмотра деталей */}
              <div className="flex justify-between items-center">
                <div className="flex gap-2">
                  {project.githubUrl && (
                    <a
                      href={project.githubUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-blue-600 hover:text-blue-800 text-sm font-medium"
                      onClick={(e) => e.stopPropagation()}
                    >
                      GitHub
                    </a>
                  )}
                  {project.liveUrl && (
                    <a
                      href={project.liveUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-green-600 hover:text-green-800 text-sm font-medium"
                      onClick={(e) => e.stopPropagation()}
                    >
                      Демо
                    </a>
                  )}
                  {project.videoUrl && (
                    <a
                      href={project.videoUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-red-600 hover:text-red-800 text-sm font-medium"
                      onClick={(e) => e.stopPropagation()}
                    >
                      Видео
                    </a>
                  )}
                </div>
                <span className="text-sm text-gray-500">
                  Нажмите для деталей →
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>

      {filteredProjects.length === 0 && (
        <div className="text-center py-12">
          <p className="text-gray-500 text-lg">
            Проекты не найдены. Попробуйте изменить фильтры поиска.
          </p>
        </div>
      )}

      {/* Модальное окно */}
      <ProjectModal
        project={selectedProject}
        isOpen={isModalOpen}
        onClose={handleCloseModal}
      />
    </div>
  );
};

export default ProjectsPage; 