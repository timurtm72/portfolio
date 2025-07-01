import React from 'react';
import {
  EnvelopeIcon,
  PhoneIcon,
  MapPinIcon,
  LinkIcon
} from '@heroicons/react/24/outline';

const Footer: React.FC = () => {
  const currentYear = new Date().getFullYear();

  const socialLinks = [
    {
      name: 'GitHub',
      url: 'https://github.com/timurtm72',
      icon: (
        <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
          <path fillRule="evenodd" d="M10 0C4.477 0 0 4.484 0 10.017c0 4.425 2.865 8.18 6.839 9.504.5.092.682-.217.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.369-1.343-3.369-1.343-.454-1.158-1.11-1.466-1.11-1.466-.908-.62.069-.608.069-.608 1.003.07 1.531 1.032 1.531 1.032.892 1.53 2.341 1.088 2.91.832.092-.647.35-1.088.636-1.338-2.22-.253-4.555-1.113-4.555-4.951 0-1.093.39-1.988 1.029-2.688-.103-.253-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.026A9.564 9.564 0 0110 4.844c.85.004 1.705.115 2.504.337 1.909-1.296 2.747-1.027 2.747-1.027.546 1.379.203 2.398.1 2.651.64.7 1.028 1.595 1.028 2.688 0 3.848-2.339 4.695-4.566 4.942.359.31.678.921.678 1.856 0 1.338-.012 2.419-.012 2.747 0 .268.18.58.688.482A10.019 10.019 0 0020 10.017C20 4.484 15.522 0 10 0z" clipRule="evenodd" />
        </svg>
      )
    },
    {
      name: 'YouTube',
      url: 'https://youtube.com/channel/UCGnkZF95JhfO7tGaB-oHC6A',
      icon: (
        <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
          <path d="M10 0C4.477 0 0 4.477 0 10s4.477 10 10 10 10-4.477 10-10S15.523 0 10 0zm5.622 8.622c-.074 1.948-.74 3.184-1.947 3.622C12.622 12.622 10 12.622 10 12.622s-2.622 0-3.675-.378c-1.207-.438-1.873-1.674-1.947-3.622C4.378 7.378 4.378 5 4.378 5s0-2.378 0-3.622c.074-1.948.74-3.184 1.947-3.622C7.378 7.378 10 7.378 10 7.378s2.622 0 3.675.378c1.207.438 1.873 1.674 1.947 3.622 0 1.244 0 3.622 0 3.622z" />
          <path d="M8.5 6.5v7l6-3.5-6-3.5z" fill="white" />
        </svg>
      )
    }
  ];

  const portfolioGithubRepos = [
    {
      name: 'STM32 Android Projects',
      url: 'https://github.com/timurtm72/STM32_ANDROID_PROJECTS'
    },
    {
      name: 'IoT Backend',
      url: 'https://github.com/timurtm72/iot_backend'
    },
    {
      name: 'Smart Home Django',
      url: 'https://github.com/timurtm72/SMART-HOME-PYTHON-DJANGO'
    },
    {
      name: 'Android Firebase ESP32',
      url: 'https://github.com/timurtm72/ANDROID-FIREBASE-ESP32-RTOS-IOT'
    }
  ];

  return (
    <footer className="bg-primary-900 text-white">
      <div className="container mx-auto px-6 py-12">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">

          {/* Основная информация */}
          <div className="lg:col-span-1">
            <div className="flex items-center mb-4">
              <div className="w-8 h-8 bg-gradient-to-br from-accent-500 to-accent-600 rounded-lg flex items-center justify-center mr-3">
                <span className="text-white font-bold text-sm">Т</span>
              </div>
              <div>
                <h3 className="text-lg font-bold">Тимур Султанов</h3>
                <p className="text-secondary-300 text-sm">Инженер-программист</p>
              </div>
            </div>
            <p className="text-secondary-300 mb-6">
              Специализируюсь на программировании микроконтроллеров, ПЛК,
              разработке IoT решений и промышленной автоматизации.
            </p>

            {/* Социальные сети */}
            <div className="flex space-x-4">
              {socialLinks.map((link) => (
                <a
                  key={link.name}
                  href={link.url}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-secondary-300 hover:text-accent-400 transition-colors duration-200"
                  title={link.name}
                >
                  {link.icon}
                </a>
              ))}
            </div>
          </div>

          {/* Контактная информация */}
          <div>
            <h4 className="text-lg font-semibold mb-4">Контакты</h4>
            <div className="space-y-3">
              <div className="flex items-center">
                <PhoneIcon className="w-5 h-5 text-accent-400 mr-3 flex-shrink-0" />
                <a
                  href="tel:+79272444051"
                  className="text-secondary-300 hover:text-white transition-colors"
                >
                  +7 927 244-40-51
                </a>
              </div>
              <div className="flex items-center">
                <EnvelopeIcon className="w-5 h-5 text-accent-400 mr-3 flex-shrink-0" />
                <a
                  href="mailto:timursultanw@yandex.ru"
                  className="text-secondary-300 hover:text-white transition-colors"
                >
                  timursultanw@yandex.ru
                </a>
              </div>
              <div className="flex items-center">
                <EnvelopeIcon className="w-5 h-5 text-accent-400 mr-3 flex-shrink-0" />
                <a
                  href="mailto:timur.sultanov1972@gmail.com"
                  className="text-secondary-300 hover:text-white transition-colors"
                >
                  timur.sultanov1972@gmail.com
                </a>
              </div>
              <div className="flex items-start">
                <MapPinIcon className="w-5 h-5 text-accent-400 mr-3 flex-shrink-0 mt-0.5" />
                <span className="text-secondary-300">
                  Иннополис, Татарстан, Россия
                </span>
              </div>
            </div>
          </div>

          {/* Специализация */}
          <div>
            <h4 className="text-lg font-semibold mb-4">Специализация</h4>
            <ul className="space-y-2">
              <li className="text-secondary-300 hover:text-white transition-colors cursor-default">
                Микроконтроллеры STM32, ESP32
              </li>
              <li className="text-secondary-300 hover:text-white transition-colors cursor-default">
                ПЛК Omron, ОВЕН, СПК107
              </li>
              <li className="text-secondary-300 hover:text-white transition-colors cursor-default">
                IoT и промышленная автоматизация
              </li>
              <li className="text-secondary-300 hover:text-white transition-colors cursor-default">
                Проектирование плат Altium
              </li>
              <li className="text-secondary-300 hover:text-white transition-colors cursor-default">
                Java Spring Boot, Android
              </li>
              <li className="text-secondary-300 hover:text-white transition-colors cursor-default">
                SCADA системы
              </li>
            </ul>
          </div>

          {/* GitHub репозитории */}
          <div>
            <h4 className="text-lg font-semibold text-gray-200 mb-4">GitHub репозитории</h4>
            <div className="space-y-2">
              <a
                href="https://github.com/timurtm72"
                target="_blank"
                rel="noopener noreferrer"
                className="text-blue-400 hover:text-blue-300 transition-colors block"
              >
                timurtm72 - Основной профиль
              </a>
              <a
                href="https://github.com/timurtm72?tab=repositories"
                target="_blank"
                rel="noopener noreferrer"
                className="text-blue-400 hover:text-blue-300 transition-colors block"
              >
                Все репозитории
              </a>
              <a
                href="https://youtube.com/channel/UCGnkZF95JhfO7tGaB-oHC6A"
                target="_blank"
                rel="noopener noreferrer"
                className="text-red-500 hover:text-red-400 transition-colors block"
              >
                YouTube канал
              </a>
            </div>
          </div>
        </div>

        {/* Разделитель */}
        <div className="border-t border-secondary-700 mt-8 pt-8">
          <div className="flex flex-col md:flex-row justify-between items-center">
            <div className="text-secondary-300 text-sm mb-4 md:mb-0">
              © {currentYear} Тимур Султанов. Все права защищены.
            </div>
            <div className="flex items-center text-sm space-x-4">
              <span className="text-secondary-300 whitespace-nowrap">Инженер-программист • Микроконтроллеры • ПЛК • IoT</span>
            </div>
          </div>

          {/* Информация о технологиях */}
          <div className="text-center mt-4 pt-4 border-t border-secondary-800">
            <div className="text-secondary-400 text-sm">
              Сайт разработан на базе React и Java Spring Boot в 2025 году
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer; 