import React, { useState, useEffect } from 'react';
import {
  BriefcaseIcon,
  CalendarIcon,
  MapPinIcon,
  BuildingOfficeIcon
} from '@heroicons/react/24/outline';

interface WorkExperience {
  id: number;
  position: string;
  company: string;
  location: string;
  startDate: string;
  endDate: string | null;
  isCurrent: boolean;
  description: string;
  technologies: string;
  achievements: string;
}

const Experience: React.FC = () => {
  const [experiences, setExperiences] = useState<WorkExperience[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Статичные данные как fallback
  const fallbackExperiences: WorkExperience[] = [
    {
      id: 1,
      position: 'Инженер-программист',
      company: 'ООО «Проект 24»',
      location: 'Иннополис',
      startDate: '2022-02-01',
      endDate: null,
      isCurrent: true,
      description: 'Программирование промышленных контроллеров (SysMac Studio, CodeSys, SCADA, HMI). Разработка и отладка алгоритмов управления оборудованием. Написание ПО для встраиваемых систем на ESP32, STM32, RISC-V.',
      technologies: 'SysMac Studio, CodeSys, SCADA, HMI, ESP32, STM32, RISC-V, C/C++',
      achievements: 'Автоматизация промышленных процессов; Разработка алгоритмов управления; Создание ПО для встраиваемых систем'
    },
    {
      id: 2,
      position: 'Java-программист',
      company: 'Maxima OOO',
      location: 'Казань',
      startDate: '2022-11-01',
      endDate: '2023-06-30',
      isCurrent: false,
      description: 'Разработал проект REST API для документооборота: Spring Boot, PostgreSQL, JPA, Hibernate. Валидация данных, работа с JSON. Интеграция со сторонним API.',
      technologies: 'Java, Spring Boot, PostgreSQL, JPA, Hibernate, REST API, JSON, Spring Security',
      achievements: 'Разработан REST API для документооборота; Спроектирована база данных; GitHub: https://github.com/timurtm72'
    },
    {
      id: 3,
      position: 'Инженер-программист',
      company: 'ООО "Челны свет"',
      location: 'Набережные Челны',
      startDate: '2021-03-01',
      endDate: '2021-12-31',
      isCurrent: false,
      description: 'Разработка контроллеров управления освещением. Создание системы удалённого мониторинга освещения.',
      technologies: 'C/C++, микроконтроллеры, драйверы, системы освещения',
      achievements: 'Разработаны контроллеры управления освещением; Создана система удалённого мониторинга'
    },
    {
      id: 4,
      position: 'Инженер программист микроконтроллеров',
      company: 'ООО "Водий нефтегазовик энергия"',
      location: 'Ташкент',
      startDate: '2017-08-01',
      endDate: '2018-04-30',
      isCurrent: false,
      description: 'Проектировал плату контроля утечки газа (метан, пропан, угарный газ) для автомобилей и бытового применения.',
      technologies: 'STM32, C/C++, GSM SIM800, ESP32, RISC-V, Wi-Fi, газовые датчики',
      achievements: 'Разработана плата контроля утечки газа; Автомобильная и домашняя версии'
    },
    {
      id: 5,
      position: 'Инженер-программист АСУ ТП',
      company: 'ХО "Восполнение"',
      location: 'Туркменистан',
      startDate: '2015-04-01',
      endDate: '2017-07-31',
      isCurrent: false,
      description: 'Обслуживание и доработка систем автоматизации и диспетчеризации (BMS) управления зданиями.',
      technologies: 'BMS, ПЛК Honeywell, SCADA, STM32, Texas Instruments, Atmel, C/C++',
      achievements: 'Автоматизация и диспетчеризация зданий; Разработка плат управления'
    }
  ];

  useEffect(() => {
    const fetchExperiences = async () => {
      try {
        const response = await fetch('/api/work-experience');

        if (response.ok) {
          const data = await response.json();
          setExperiences(data);
          setError(null);
        } else {
          console.warn('API недоступен, используются статичные данные');
          setExperiences(fallbackExperiences);
          setError('Данные загружены из кэша (API недоступен)');
        }
      } catch (err) {
        console.error('Ошибка загрузки данных:', err);
        setExperiences(fallbackExperiences);
        setError('Данные загружены из кэша (ошибка соединения)');
      } finally {
        setLoading(false);
      }
    };

    fetchExperiences();
  }, []);

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU', { year: 'numeric', month: 'long' });
  };

  const calculateDuration = (startDate: string, endDate: string | null): string => {
    const start = new Date(startDate);
    const end = endDate ? new Date(endDate) : new Date();

    const months = (end.getFullYear() - start.getFullYear()) * 12 + (end.getMonth() - start.getMonth());
    const years = Math.floor(months / 12);
    const remainingMonths = months % 12;

    if (years === 0) {
      return `${remainingMonths} мес.`;
    } else if (remainingMonths === 0) {
      return `${years} ${years === 1 ? 'год' : years < 5 ? 'года' : 'лет'}`;
    } else {
      return `${years} ${years === 1 ? 'год' : years < 5 ? 'года' : 'лет'} ${remainingMonths} мес.`;
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50 py-20">
        <div className="max-w-4xl mx-auto px-6">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">Загрузка опыта работы...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-50 to-blue-50 py-20">
      <div className="max-w-4xl mx-auto px-6">
        <div className="text-center mb-16">
          <h1 className="text-4xl md:text-5xl font-bold text-gray-900 mb-6">
            Опыт работы
          </h1>
          <p className="text-xl text-gray-600 max-w-3xl mx-auto">
            25 лет в области автоматизации, микроконтроллеров и промышленного программирования
          </p>
          {error && (
            <div className="mt-4 p-3 bg-yellow-100 border border-yellow-400 text-yellow-700 rounded-lg">
              {error}
            </div>
          )}
        </div>

        <div className="relative">
          {/* Timeline line */}
          <div className="absolute left-8 top-0 bottom-0 w-0.5 bg-gradient-to-b from-blue-500 to-purple-600"></div>

          <div className="space-y-12">
            {experiences.map((experience, index) => (
              <div key={experience.id} className="relative flex items-start">
                {/* Timeline dot */}
                <div className="absolute left-6 w-4 h-4 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full border-4 border-white shadow-lg z-10"></div>

                {/* Content */}
                <div className="ml-16 bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 p-8 border border-gray-100">
                  <div className="flex flex-col md:flex-row md:items-center justify-between mb-4">
                    <div>
                      <h3 className="text-2xl font-bold text-gray-900 mb-1">
                        {experience.position}
                      </h3>
                      <p className="text-lg font-semibold text-blue-600 mb-1">
                        {experience.company}
                      </p>
                      <p className="text-gray-600">
                        {experience.location}
                      </p>
                    </div>
                    <div className="md:text-right mt-4 md:mt-0">
                      <div className="flex items-center text-sm text-gray-600 mb-1">
                        <span>
                          {formatDate(experience.startDate)} — {' '}
                          {experience.isCurrent ? 'настоящее время' : formatDate(experience.endDate!)}
                        </span>
                        {experience.isCurrent && (
                          <span className="ml-2 px-2 py-1 bg-green-100 text-green-800 text-xs rounded-full">
                            Текущая работа
                          </span>
                        )}
                      </div>
                      <p className="text-sm font-medium text-gray-500">
                        {calculateDuration(experience.startDate, experience.endDate)}
                      </p>
                    </div>
                  </div>

                  <div className="space-y-4">
                    <div>
                      <h4 className="font-semibold text-gray-900 mb-2">Описание:</h4>
                      <p className="text-gray-700 leading-relaxed">
                        {experience.description}
                      </p>
                    </div>

                    <div>
                      <h4 className="font-semibold text-gray-900 mb-2">Технологии:</h4>
                      <div className="flex flex-wrap gap-2">
                        {experience.technologies.split(', ').map((tech, techIndex) => (
                          <span
                            key={techIndex}
                            className="px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm font-medium"
                          >
                            {tech}
                          </span>
                        ))}
                      </div>
                    </div>

                    <div>
                      <h4 className="font-semibold text-gray-900 mb-2">Достижения:</h4>
                      <div className="space-y-1">
                        {experience.achievements.split('; ').map((achievement, achievementIndex) => (
                          <div key={achievementIndex} className="flex items-start">
                            <div className="w-2 h-2 bg-green-500 rounded-full mt-2 mr-3 flex-shrink-0"></div>
                            <span className="text-gray-700 text-sm">{achievement}</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Summary */}
        <div className="mt-16 bg-white rounded-xl shadow-lg p-8 border border-gray-100">
          <h2 className="text-2xl font-bold text-gray-900 mb-4">Итого</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="text-center p-4 bg-blue-50 rounded-lg">
              <div className="text-3xl font-bold text-blue-600 mb-2">25+</div>
              <div className="text-sm text-gray-600">лет опыта</div>
            </div>
            <div className="text-center p-4 bg-green-50 rounded-lg">
              <div className="text-3xl font-bold text-green-600 mb-2">{experiences.length}</div>
              <div className="text-sm text-gray-600">мест работы</div>
            </div>
            <div className="text-center p-4 bg-purple-50 rounded-lg">
              <div className="text-3xl font-bold text-purple-600 mb-2">50+</div>
              <div className="text-sm text-gray-600">проектов</div>
            </div>
          </div>
        </div>

        {/* Contact info */}
        <div className="mt-8 text-center">
          <p className="text-gray-600 mb-4">
            Готов к новым вызовам в области автоматизации и программирования микроконтроллеров
          </p>
          <div className="flex justify-center space-x-4">
            <a
              href="https://github.com/timurtm72"
              target="_blank"
              rel="noopener noreferrer"
              className="text-blue-600 hover:text-blue-800 font-medium"
            >
              GitHub
            </a>
            <span className="text-gray-600">•</span>
            <a
              href="https://youtube.com/channel/UCGnkZF95JhfO7tGaB-oHC6A"
              target="_blank"
              rel="noopener noreferrer"
              className="text-blue-600 hover:text-blue-800 font-medium"
            >
              YouTube
            </a>
            <span className="text-gray-600">•</span>
            <a
              href="mailto:timur.sultanov1972@gmail.com"
              className="text-blue-600 hover:text-blue-800 font-medium"
            >
              Email
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Experience; 