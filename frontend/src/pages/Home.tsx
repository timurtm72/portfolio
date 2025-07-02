import React from 'react';
import {
  BriefcaseIcon,
  AcademicCapIcon,
  MapPinIcon,
  PhoneIcon,
  EnvelopeIcon
} from '@heroicons/react/24/outline';
import { motion } from 'framer-motion';
import {
  ChevronDownIcon,
  CodeBracketIcon,
  CpuChipIcon,
  CommandLineIcon,
  CheckCircleIcon,
  ArrowRightIcon,
  LinkIcon,
} from '@heroicons/react/24/outline';
import { Link } from 'react-router-dom';

const Home: React.FC = () => {
  // Реальная информация о навыках
  const skills = [
    { name: 'STM32', level: 95, category: 'Микроконтроллеры' },
    { name: 'ESP32', level: 90, category: 'Микроконтроллеры' },
    { name: 'Omron PLC', level: 90, category: 'ПЛК' },
    { name: 'Altium Designer', level: 90, category: 'Проектирование' },
    { name: 'Java Spring Boot', level: 85, category: 'Программирование' },
    { name: 'Modbus', level: 90, category: 'Протоколы' },
  ];

  // Реальные проекты
  const featuredProjects = [
    {
      title: 'USB Bluetooth Mass Storage для швейных машин',
      description: 'Устройство для швейной машинки Janome с передачей файлов по Bluetooth',
      tech: ['STM32', 'Bluetooth', 'Android'],
    },
    {
      title: 'Сигнализатор утечки газа автомобильный',
      description: 'Контроль утечки газа по трем зонам с Android приложением',
      tech: ['STM32', 'Android', 'Bluetooth'],
    },
    {
      title: 'Система управления инкубатором',
      description: 'Промышленный инкубатор с контролем температуры и влажности',
      tech: ['Texas Instruments Tiva', 'DHT-22', 'TFT'],
    },
  ];

  // Реальные достижения
  const achievements = [
    { number: '25+', label: 'Проектов реализовано' },
    { number: '25+', label: 'Лет опыта' },
    { number: '15+', label: 'Технологий освоено' },
    { number: '78%', label: 'Удовлетворенность клиентов' },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-50 to-secondary-50">
      {/* Hero Section */}
      <section className="relative min-h-screen flex items-center">
        <div className="container mx-auto px-6 py-20">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <motion.div
              initial={{ opacity: 0, x: -50 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.8 }}
              className="space-y-8"
            >
              <div className="space-y-4">
                <motion.h1
                  className="text-5xl lg:text-7xl font-bold text-primary-900"
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: 0.2 }}
                >
                  Султанов
                  <span className="block text-transparent bg-clip-text bg-gradient-to-r from-accent-500 to-accent-600">
                    Тимур
                  </span>
                </motion.h1>

                <motion.p
                  className="text-2xl lg:text-3xl text-secondary-600 font-light"
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: 0.4 }}
                >
                  Инженер-программист
                </motion.p>

                <motion.p
                  className="text-lg text-secondary-500"
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: 0.6 }}
                >
                  Микроконтроллеры • ПЛК • IoT • Промышленная автоматизация
                </motion.p>
              </div>

              <motion.div
                className="flex flex-wrap gap-4"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.8 }}
              >
                <Link
                  to="/projects"
                  className="bg-accent-500 hover:bg-accent-600 text-white px-8 py-4 rounded-lg font-semibold transition-all duration-300 transform hover:scale-105 flex items-center gap-2"
                >
                  Мои проекты
                  <ArrowRightIcon className="w-5 h-5" />
                </Link>

                <a
                  href="mailto:timursultanw@yandex.ru"
                  className="border-2 border-primary-600 text-primary-600 hover:bg-primary-600 hover:text-white px-8 py-4 rounded-lg font-semibold transition-all duration-300 flex items-center gap-2"
                >
                  <EnvelopeIcon className="w-5 h-5" />
                  Связаться
                </a>
              </motion.div>

              {/* Контактная информация */}
              <motion.div
                className="space-y-2 pt-6 border-t border-secondary-200"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 1 }}
              >
                <p className="text-secondary-600">
                  <span className="font-semibold">Телефон:</span> +7 927 244-40-51
                </p>
                <p className="text-secondary-600">
                  <span className="font-semibold">Email:</span> timursultanw@yandex.ru
                </p>
                <p className="text-secondary-600">
                  <span className="font-semibold">Местоположение:</span> Иннополис, Татарстан, Россия
                </p>
              </motion.div>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, x: 50 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.8, delay: 0.2 }}
              className="relative"
            >
              <div className="bg-white rounded-3xl shadow-2xl p-8 transform rotate-3 hover:rotate-0 transition-transform duration-500">
                <div className="grid grid-cols-2 gap-6">
                  {achievements.map((achievement, index) => (
                    <motion.div
                      key={index}
                      className="text-center p-4 bg-gradient-to-br from-accent-50 to-accent-100 rounded-xl"
                      whileHover={{ scale: 1.05 }}
                      transition={{ type: 'spring', stiffness: 300 }}
                    >
                      <div className="text-3xl font-bold text-accent-600 mb-2">
                        {achievement.number}
                      </div>
                      <div className="text-secondary-600 text-sm font-medium">
                        {achievement.label}
                      </div>
                    </motion.div>
                  ))}
                </div>
              </div>
            </motion.div>
          </div>
        </div>

        {/* Scroll indicator */}
        <motion.div
          className="absolute bottom-8 left-1/2 transform -translate-x-1/2"
          animate={{ y: [0, 10, 0] }}
          transition={{ repeat: Infinity, duration: 2 }}
        >
          <ChevronDownIcon className="w-8 h-8 text-secondary-400" />
        </motion.div>
      </section>

      {/* Специализация */}
      <section className="py-20 bg-white">
        <div className="container mx-auto px-6">
          <motion.div
            initial={{ opacity: 0, y: 50 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            viewport={{ once: true }}
            className="text-center mb-16"
          >
            <h2 className="text-4xl lg:text-5xl font-bold text-primary-900 mb-6">
              Моя специализация
            </h2>
            <p className="text-xl text-secondary-600 max-w-3xl mx-auto">
              Команда разработчиков с многолетним опытом в области промышленной автоматизации,
              программирования микроконтроллеров и разработки IoT решений
            </p>
          </motion.div>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            {/* Микроконтроллеры */}
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
              viewport={{ once: true }}
              className="bg-gradient-to-br from-blue-50 to-blue-100 p-8 rounded-2xl hover:shadow-lg transition-shadow"
            >
              <CpuChipIcon className="w-12 h-12 text-blue-600 mb-4" />
              <h3 className="text-2xl font-bold text-primary-900 mb-4">
                Микроконтроллеры
              </h3>
              <p className="text-secondary-600 mb-4">
                Программирование STM32, ESP32, Texas Instruments Tiva, Atmel, Nuvoton
              </p>
              <ul className="space-y-2">
                <li className="flex items-center gap-2 text-secondary-600">
                  <CheckCircleIcon className="w-4 h-4 text-green-500" />
                  STM32 различных серий
                </li>
                <li className="flex items-center gap-2 text-secondary-600">
                  <CheckCircleIcon className="w-4 h-4 text-green-500" />
                  ESP32 IoT разработка
                </li>
                <li className="flex items-center gap-2 text-secondary-600">
                  <CheckCircleIcon className="w-4 h-4 text-green-500" />
                  Texas Instruments Tiva
                </li>
              </ul>
            </motion.div>

            {/* ПЛК */}
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.1 }}
              viewport={{ once: true }}
              className="bg-gradient-to-br from-green-50 to-green-100 p-8 rounded-2xl hover:shadow-lg transition-shadow"
            >
              <CommandLineIcon className="w-12 h-12 text-green-600 mb-4" />
              <h3 className="text-2xl font-bold text-primary-900 mb-4">
                ПЛК и автоматизация
              </h3>
              <p className="text-secondary-600 mb-4">
                Промышленные контроллеры и системы автоматизации
              </p>
              <ul className="space-y-2">
                <li className="flex items-center gap-2 text-secondary-600">
                  <CheckCircleIcon className="w-4 h-4 text-green-500" />
                  Omron, ОВЕН, СПК107
                </li>
                <li className="flex items-center gap-2 text-secondary-600">
                  <CheckCircleIcon className="w-4 h-4 text-green-500" />
                  SCADA системы
                </li>
                <li className="flex items-center gap-2 text-secondary-600">
                  <CheckCircleIcon className="w-4 h-4 text-green-500" />
                  CodeSys программирование
                </li>
              </ul>
            </motion.div>

            {/* Разработка */}
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.2 }}
              viewport={{ once: true }}
              className="bg-gradient-to-br from-purple-50 to-purple-100 p-8 rounded-2xl hover:shadow-lg transition-shadow"
            >
              <CodeBracketIcon className="w-12 h-12 text-purple-600 mb-4" />
              <h3 className="text-2xl font-bold text-primary-900 mb-4">
                Разработка ПО
              </h3>
              <p className="text-secondary-600 mb-4">
                Backend и мобильная разработка
              </p>
              <ul className="space-y-2">
                <li className="flex items-center gap-2 text-secondary-600">
                  <CheckCircleIcon className="w-4 h-4 text-green-500" />
                  Java Spring Boot
                </li>
                <li className="flex items-center gap-2 text-secondary-600">
                  <CheckCircleIcon className="w-4 h-4 text-green-500" />
                  Android приложения
                </li>
                <li className="flex items-center gap-2 text-secondary-600">
                  <CheckCircleIcon className="w-4 h-4 text-green-500" />
                  Python Django
                </li>
              </ul>
            </motion.div>
          </div>
        </div>
      </section>

      {/* Избранные проекты */}
      <section className="py-20 bg-gradient-to-br from-primary-50 to-secondary-50">
        <div className="container mx-auto px-6">
          <motion.div
            initial={{ opacity: 0, y: 50 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            viewport={{ once: true }}
            className="text-center mb-16"
          >
            <h2 className="text-4xl lg:text-5xl font-bold text-primary-900 mb-6">
              Избранные проекты
            </h2>
            <p className="text-xl text-secondary-600 max-w-3xl mx-auto">
              Примеры реализованных проектов в области промышленной автоматизации и IoT
            </p>
          </motion.div>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            {featuredProjects.map((project, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 30 }}
                whileInView={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: index * 0.1 }}
                viewport={{ once: true }}
                className="bg-white rounded-2xl p-8 shadow-lg hover:shadow-xl transition-shadow"
              >
                <h3 className="text-xl font-bold text-primary-900 mb-4">
                  {project.title}
                </h3>
                <p className="text-secondary-600 mb-6">
                  {project.description}
                </p>
                <div className="flex flex-wrap gap-2 mb-6">
                  {project.tech.map((tech, techIndex) => (
                    <span
                      key={techIndex}
                      className="bg-accent-100 text-accent-700 px-3 py-1 rounded-full text-sm font-medium"
                    >
                      {tech}
                    </span>
                  ))}
                </div>
                <Link
                  to="/projects"
                  className="text-accent-600 hover:text-accent-700 font-semibold flex items-center gap-1"
                >
                  Подробнее
                  <ArrowRightIcon className="w-4 h-4" />
                </Link>
              </motion.div>
            ))}
          </div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: 0.4 }}
            viewport={{ once: true }}
            className="text-center mt-12"
          >
            <Link
              to="/projects"
              className="bg-accent-500 hover:bg-accent-600 text-white px-8 py-4 rounded-lg font-semibold transition-all duration-300 transform hover:scale-105 inline-flex items-center gap-2"
            >
              Все проекты
              <ArrowRightIcon className="w-5 h-5" />
            </Link>
          </motion.div>
        </div>
      </section>

      {/* Процесс работы */}
      <section className="py-20 bg-white">
        <div className="container mx-auto px-6">
          <motion.div
            initial={{ opacity: 0, y: 50 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            viewport={{ once: true }}
            className="text-center mb-16"
          >
            <h2 className="text-4xl lg:text-5xl font-bold text-primary-900 mb-6">
              Как мы работаем
            </h2>
            <p className="text-xl text-secondary-600 max-w-3xl mx-auto">
              Пошаговый подход к реализации ваших проектов
            </p>
          </motion.div>

          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
            {[
              {
                step: '01',
                title: 'Анализ задачи',
                description: 'Вы описываете проблему с электроникой или ПО'
              },
              {
                step: '02',
                title: 'Изучение',
                description: 'Мы изучаем поставленную задачу'
              },
              {
                step: '03',
                title: 'Решение',
                description: 'Предлагаем вариант решения'
              },
              {
                step: '04',
                title: 'Реализация',
                description: 'Согласуем фронт работы и выполняем'
              }
            ].map((item, index) => (
              <motion.div
                key={index}
                initial={{ opacity: 0, y: 30 }}
                whileInView={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: index * 0.1 }}
                viewport={{ once: true }}
                className="text-center"
              >
                <div className="bg-gradient-to-br from-accent-500 to-accent-600 text-white w-16 h-16 rounded-full flex items-center justify-center text-2xl font-bold mx-auto mb-4">
                  {item.step}
                </div>
                <h3 className="text-xl font-bold text-primary-900 mb-3">
                  {item.title}
                </h3>
                <p className="text-secondary-600">
                  {item.description}
                </p>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-20 bg-gradient-to-r from-accent-500 to-accent-600">
        <div className="container mx-auto px-6 text-center">
          <motion.div
            initial={{ opacity: 0, y: 50 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
            viewport={{ once: true }}
            className="max-w-4xl mx-auto"
          >
            <h2 className="text-4xl lg:text-5xl font-bold text-white mb-6">
              Готовы начать проект?
            </h2>
            <p className="text-xl text-accent-100 mb-8">
              Свяжитесь с нами для обсуждения вашей задачи в области промышленной автоматизации
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <a
                href="mailto:timursultanw@yandex.ru"
                className="bg-white text-accent-600 hover:bg-accent-50 px-8 py-4 rounded-lg font-semibold transition-all duration-300 transform hover:scale-105 flex items-center justify-center gap-2"
              >
                <EnvelopeIcon className="w-5 h-5" />
                Написать на email
              </a>
              <a
                href="tel:+79272444051"
                className="border-2 border-white text-white hover:bg-white hover:text-accent-600 px-8 py-4 rounded-lg font-semibold transition-all duration-300 flex items-center justify-center gap-2"
              >
                Позвонить: +7 927 244-40-51
              </a>
            </div>

            <div className="mt-8 pt-8 border-t border-accent-400">
              <p className="text-accent-100 mb-4">Дополнительные ресурсы:</p>
              <div className="flex flex-wrap justify-center gap-4">
                <a
                  href="https://youtube.com/channel/UCGnkZF95JhfO7tGaB-oHC6A"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-accent-100 hover:text-white transition-colors flex items-center gap-2"
                >
                  <LinkIcon className="w-4 h-4" />
                  YouTube канал
                </a>
                <a
                  href="https://github.com/timurtm72"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-accent-100 hover:text-white transition-colors flex items-center gap-2"
                >
                  <LinkIcon className="w-4 h-4" />
                  GitHub
                </a>
              </div>
            </div>
          </motion.div>
        </div>
      </section>
    </div>
  );
};

export default Home; 