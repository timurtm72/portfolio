import React from 'react';

const Contact: React.FC = () => {
    return (
        <div className="container mx-auto px-6 py-12">
            <div className="max-w-4xl mx-auto">
                {/* Заголовок */}
                <div className="text-center mb-12">
                    <h1 className="text-4xl font-bold mb-4 gradient-text">Контакты</h1>
                    <div className="w-24 h-1 bg-gradient-to-r from-blue-600 to-purple-600 mx-auto rounded-full"></div>
                    <p className="text-xl text-gray-600 mt-6 max-w-2xl mx-auto">
                        Свяжитесь со мной для обсуждения проектов, сотрудничества или консультаций
                    </p>
                </div>

                {/* Основной блок контактов */}
                <div className="grid md:grid-cols-2 gap-8 mb-12">
                    {/* Левая колонка - Контактная информация */}
                    <div className="bg-white rounded-xl shadow-lg p-8">
                        <h2 className="text-2xl font-semibold mb-6 text-gray-900">Контактная информация</h2>

                        <div className="space-y-6">
                            {/* Телефон */}
                            <div className="flex items-start space-x-4">
                                <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center flex-shrink-0">
                                    <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                                    </svg>
                                </div>
                                <div>
                                    <h3 className="text-lg font-medium text-gray-900">Телефон</h3>
                                    <p className="text-gray-600">Предпочитаемый способ связи</p>
                                    <a
                                        href="tel:+79272444051"
                                        className="text-blue-600 hover:text-blue-800 font-medium text-lg"
                                    >
                                        +7 927 244-40-51
                                    </a>
                                </div>
                            </div>

                            {/* Email */}
                            <div className="flex items-start space-x-4">
                                <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center flex-shrink-0">
                                    <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                                    </svg>
                                </div>
                                <div>
                                    <h3 className="text-lg font-medium text-gray-900">Email</h3>
                                    <p className="text-gray-600">Для деловой переписки</p>
                                    <div className="space-y-1">
                                        <a
                                            href="mailto:timursultanw@yandex.ru"
                                            className="block text-blue-600 hover:text-blue-800 font-medium"
                                        >
                                            timursultanw@yandex.ru
                                        </a>
                                        <a
                                            href="mailto:timur.sultanov1972@gmail.com"
                                            className="block text-blue-600 hover:text-blue-800 font-medium"
                                        >
                                            timur.sultanov1972@gmail.com
                                        </a>
                                    </div>
                                </div>
                            </div>

                            {/* Адрес */}
                            <div className="flex items-start space-x-4">
                                <div className="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center flex-shrink-0">
                                    <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                                    </svg>
                                </div>
                                <div>
                                    <h3 className="text-lg font-medium text-gray-900">Местоположение</h3>
                                    <p className="text-gray-600">Готов к удаленной работе и командировкам</p>
                                    <p className="text-gray-800 font-medium">
                                        Иннополис, Татарстан, Россия
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Правая колонка - Дополнительная информация */}
                    <div className="bg-white rounded-xl shadow-lg p-8">
                        <h2 className="text-2xl font-semibold mb-6 text-gray-900">О сотрудничестве</h2>

                        <div className="space-y-6">
                            <div>
                                <h3 className="text-lg font-medium text-gray-900 mb-3">Специализация</h3>
                                <ul className="space-y-2 text-gray-600">
                                    <li className="flex items-center">
                                        <span className="w-2 h-2 bg-blue-500 rounded-full mr-3"></span>
                                        Программирование микроконтроллеров (STM32, ESP32)
                                    </li>
                                    <li className="flex items-center">
                                        <span className="w-2 h-2 bg-green-500 rounded-full mr-3"></span>
                                        ПЛК и SCADA системы (Omron, JUMO)
                                    </li>
                                    <li className="flex items-center">
                                        <span className="w-2 h-2 bg-purple-500 rounded-full mr-3"></span>
                                        IoT и промышленная автоматизация
                                    </li>
                                    <li className="flex items-center">
                                        <span className="w-2 h-2 bg-orange-500 rounded-full mr-3"></span>
                                        Java Spring Boot разработка
                                    </li>
                                </ul>
                            </div>

                            <div>
                                <h3 className="text-lg font-medium text-gray-900 mb-3">Форматы сотрудничества</h3>
                                <ul className="space-y-2 text-gray-600">
                                    <li className="flex items-center">
                                        <span className="w-2 h-2 bg-blue-500 rounded-full mr-3"></span>
                                        Полная занятость
                                    </li>
                                    <li className="flex items-center">
                                        <span className="w-2 h-2 bg-green-500 rounded-full mr-3"></span>
                                        Проектная работа
                                    </li>
                                    <li className="flex items-center">
                                        <span className="w-2 h-2 bg-purple-500 rounded-full mr-3"></span>
                                        Консультации и экспертиза
                                    </li>
                                    <li className="flex items-center">
                                        <span className="w-2 h-2 bg-orange-500 rounded-full mr-3"></span>
                                        Удаленная работа
                                    </li>
                                </ul>
                            </div>

                            <div className="bg-blue-50 rounded-lg p-4">
                                <p className="text-blue-800 text-sm">
                                    <strong>Опыт работы:</strong> 25+ лет в области программирования и автоматизации
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Карточки быстрого контакта */}
                <div className="grid md:grid-cols-3 gap-6">
                    <a
                        href="tel:+79272444051"
                        className="block bg-blue-50 hover:bg-blue-100 rounded-lg p-6 text-center transition-colors group"
                    >
                        <div className="w-16 h-16 bg-blue-500 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:bg-blue-600 transition-colors">
                            <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                            </svg>
                        </div>
                        <h3 className="text-lg font-semibold text-blue-900 mb-2">Позвонить</h3>
                        <p className="text-blue-700">+7 927 244-40-51</p>
                    </a>

                    <a
                        href="mailto:timursultanw@yandex.ru"
                        className="block bg-green-50 hover:bg-green-100 rounded-lg p-6 text-center transition-colors group"
                    >
                        <div className="w-16 h-16 bg-green-500 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:bg-green-600 transition-colors">
                            <svg className="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                            </svg>
                        </div>
                        <h3 className="text-lg font-semibold text-green-900 mb-2">Написать</h3>
                        <p className="text-green-700">timursultanw@yandex.ru</p>
                    </a>

                    <a
                        href="https://github.com/timurtm72"
                        target="_blank"
                        rel="noopener noreferrer"
                        className="block bg-gray-50 hover:bg-gray-100 rounded-lg p-6 text-center transition-colors group"
                    >
                        <div className="w-16 h-16 bg-gray-500 rounded-full flex items-center justify-center mx-auto mb-4 group-hover:bg-gray-600 transition-colors">
                            <svg className="w-8 h-8 text-white" fill="currentColor" viewBox="0 0 24 24">
                                <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z" />
                            </svg>
                        </div>
                        <h3 className="text-lg font-semibold text-gray-900 mb-2">GitHub</h3>
                        <p className="text-gray-700">Посмотреть проекты</p>
                    </a>
                </div>
            </div>
        </div>
    );
};

export default Contact; 