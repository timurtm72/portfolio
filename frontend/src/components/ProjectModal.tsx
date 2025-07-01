import React, { useState, useEffect } from 'react';
import WebPImage from './WebPImage.tsx';

interface Project {
    id: number;
    title: string;
    description: string;
    detailedDescription?: string;
    imageUrl?: string;
    images?: string[];
    category: string;
    categoryDisplayName?: string;
    status: string;
    statusDisplayName?: string;
    technologies: string[];
    githubUrl?: string;
    liveUrl?: string;
    videoUrl?: string;
    featured: boolean;
}

interface ProjectModalProps {
    project: Project | null;
    isOpen: boolean;
    onClose: () => void;
}

const ProjectModal: React.FC<ProjectModalProps> = ({ project, isOpen, onClose }) => {
    const [isZoomed, setIsZoomed] = useState(false);
    const [currentIndex, setCurrentIndex] = useState(0);

    // Сброс индекса изображения при открытии модального окна или смене проекта
    useEffect(() => {
        if (isOpen) {
            setCurrentIndex(0);
            setIsZoomed(false);
        }
    }, [isOpen, project]);

    // Убираем возможные дубликаты путей и кешируем список (всегда вызывается, даже если project=null)
    const images = React.useMemo(() => {
        if (!project) return [];
        const raw: string[] = project.images && project.images.length > 0 ? project.images : (() => {
            if (project.imageUrl) {
                const dotIndex = project.imageUrl.lastIndexOf('.');
                if (dotIndex !== -1) {
                    const base = project.imageUrl.substring(0, dotIndex);
                    const ext = project.imageUrl.substring(dotIndex);
                    return [project.imageUrl, `${base}1${ext}`, `${base}2${ext}`, `${base}3${ext}`];
                }
            }
            return ['/images/projects/webp/janome.webp'];
        })();
        return Array.from(new Set(raw));
    }, [project]);

    if (!isOpen || !project) return null;

    const handleModalClick = (e: React.MouseEvent) => {
        e.stopPropagation();
    };

    const getCategoryColor = (category: string) => {
        const colors = {
            'STM32': 'bg-blue-100 text-blue-800',
            'ESP32': 'bg-green-100 text-green-800',
            'ARDUINO': 'bg-orange-100 text-orange-800',
            'PLC': 'bg-purple-100 text-purple-800',
            'IOT': 'bg-indigo-100 text-indigo-800',
            'AUTOMATION': 'bg-yellow-100 text-yellow-800',
            'EMBEDDED': 'bg-teal-100 text-teal-800',
            'ROBOTICS': 'bg-red-100 text-red-800'
        };
        return colors[category as keyof typeof colors] || 'bg-gray-100 text-gray-800';
    };

    const getStatusColor = (status: string) => {
        const colors = {
            'COMPLETED': 'bg-green-100 text-green-800',
            'IN_PROGRESS': 'bg-yellow-100 text-yellow-800',
            'PLANNING': 'bg-blue-100 text-blue-800',
            'ON_HOLD': 'bg-red-100 text-red-800'
        };
        return colors[status as keyof typeof colors] || 'bg-gray-100 text-gray-800';
    };

    const getStatusText = (status: string) => {
        const statusTexts = {
            'COMPLETED': 'Завершен',
            'IN_PROGRESS': 'В разработке',
            'PLANNING': 'Планирование',
            'ON_HOLD': 'Приостановлен'
        };
        return statusTexts[status as keyof typeof statusTexts] || status;
    };

    return (
        <div
            className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
            onClick={onClose}
        >
            <div
                className="bg-white rounded-lg max-w-4xl max-h-[90vh] w-full overflow-y-auto"
                onClick={handleModalClick}
            >
                {/* Заголовок модального окна */}
                <div className="sticky top-0 bg-white border-b px-6 py-4 flex justify-between items-center">
                    <h2 className="text-2xl font-bold text-gray-900">{project.title}</h2>
                    <button
                        onClick={onClose}
                        className="text-gray-400 hover:text-gray-600 text-2xl"
                    >
                        ×
                    </button>
                </div>

                {/* Содержимое модального окна */}
                <div className="p-6">
                    {/* Галерея изображений (скрывается при зуме, чтобы не было дублирования) */}
                    {!isZoomed && (
                        <div className="mb-6">
                            {/* Основное изображение */}
                            <div
                                className="bg-gray-100 rounded-lg flex items-center justify-center p-4 cursor-zoom-in border border-gray-200 shadow"
                                onClick={() => setIsZoomed(true)}
                            >
                                <WebPImage
                                    src={images[currentIndex]}
                                    alt={`${project.title} ${currentIndex + 1}`}
                                    className="w-full h-auto max-h-[260px] object-contain"
                                    onError={(e) => {
                                        (e.target as HTMLImageElement).style.display = 'none';
                                    }}
                                />
                            </div>

                            {/* Thumbnails */}
                            {images.length > 1 && (
                                <div className="flex mt-4 gap-2 overflow-x-auto">
                                    {images.map((img, idx) => (
                                        <div
                                            key={idx}
                                            className={`h-20 w-20 rounded-lg overflow-hidden cursor-pointer border-2 ${currentIndex === idx ? 'border-blue-400' : 'border-gray-300'}`}
                                            onClick={() => setCurrentIndex(idx)}
                                        >
                                            <WebPImage
                                                src={img}
                                                alt={`thumb-${idx}`}
                                                style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                                            />
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
                    )}

                    {/* Overlay for zoomed image с навигацией */}
                    {isZoomed && (
                        <div
                            className="fixed inset-0 bg-black bg-opacity-90 flex items-center justify-center z-50"
                            onClick={() => setIsZoomed(false)}
                        >
                            {/* Кнопка закрытия */}
                            <button
                                className="absolute top-4 right-4 text-white text-3xl font-bold hover:text-gray-300"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    setIsZoomed(false);
                                }}
                                aria-label="Закрыть изображение"
                            >
                                ×
                            </button>

                            {/* Стрелка влево */}
                            {images.length > 1 && (
                                <button
                                    className="absolute left-4 text-white text-4xl select-none"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        setCurrentIndex((currentIndex - 1 + images.length) % images.length);
                                    }}
                                >
                                    &lsaquo;
                                </button>
                            )}

                            <div className="flex items-center justify-center w-full h-full overflow-auto">
                                <img
                                    src={images[currentIndex]}
                                    alt={`${project.title} zoom ${currentIndex + 1}`}
                                    className="max-w-[95vw] max-h-[95vh] w-auto h-auto object-contain cursor-zoom-out"
                                    onClick={(e) => e.stopPropagation()}
                                />
                            </div>

                            {/* Стрелка вправо */}
                            {images.length > 1 && (
                                <button
                                    className="absolute right-4 text-white text-4xl select-none"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        setCurrentIndex((currentIndex + 1) % images.length);
                                    }}
                                >
                                    &rsaquo;
                                </button>
                            )}
                        </div>
                    )}

                    {/* Категория и статус */}
                    <div className="flex flex-wrap gap-3 mb-4">
                        <span className={`px-3 py-1 text-sm font-medium rounded-full ${getCategoryColor(project.category)}`}>
                            {project.categoryDisplayName || project.category}
                        </span>
                        <span className={`px-3 py-1 text-sm font-medium rounded-full ${getStatusColor(project.status)}`}>
                            {project.statusDisplayName || getStatusText(project.status)}
                        </span>
                        {project.featured && (
                            <span className="px-3 py-1 text-sm font-medium rounded-full bg-yellow-100 text-yellow-800 flex items-center">
                                ⭐ Рекомендуемый
                            </span>
                        )}
                    </div>

                    {/* Технологии */}
                    {project.technologies && project.technologies.length > 0 && (
                        <div className="mb-6">
                            <h3 className="text-lg font-semibold mb-3">Используемые технологии</h3>
                            <div className="flex flex-wrap gap-2">
                                {project.technologies.map((tech, index) => (
                                    <span
                                        key={index}
                                        className="px-3 py-1 bg-gray-100 text-gray-700 rounded-full text-sm"
                                    >
                                        {tech}
                                    </span>
                                ))}
                            </div>
                        </div>
                    )}

                    {/* Описание */}
                    <div className="mb-6">
                        <h3 className="text-lg font-semibold mb-3">Описание проекта</h3>
                        <p className="text-gray-700 leading-relaxed mb-4">
                            {project.description}
                        </p>

                        {/* Детальное описание */}
                        {project.detailedDescription && (
                            <div>
                                <h4 className="text-md font-semibold mb-2">Подробности</h4>
                                <div className="text-gray-700 leading-relaxed whitespace-pre-line">
                                    {project.detailedDescription}
                                </div>
                            </div>
                        )}
                    </div>

                    {/* Ссылки */}
                    <div className="flex flex-wrap gap-4">
                        {project.githubUrl && (
                            <a
                                href={project.githubUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="inline-flex items-center px-4 py-2 bg-gray-900 text-white rounded-lg hover:bg-gray-800 transition-colors"
                            >
                                <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                                    <path fillRule="evenodd" d="M10 0C4.477 0 0 4.484 0 10.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0110 4.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.203 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.942.359.31.678.921.678 1.856 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0020 10.017C20 4.484 15.522 0 10 0z" clipRule="evenodd" />
                                </svg>
                                GitHub
                            </a>
                        )}

                        {project.liveUrl && (
                            <a
                                href={project.liveUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                            >
                                <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
                                </svg>
                                Демо
                            </a>
                        )}

                        {project.videoUrl && (
                            <a
                                href={project.videoUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="inline-flex items-center px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
                            >
                                <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14.828 14.828a4 4 0 01-5.656 0M9 10h1m4 0h1m-6 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                                Видео
                            </a>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProjectModal; 