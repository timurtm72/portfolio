import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Bars3Icon, XMarkIcon } from '@heroicons/react/24/outline';

const Header: React.FC = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const location = useLocation();

  const navigation = [
    { name: 'Главная', href: '/' },
    { name: 'Проекты', href: '/projects' },
    { name: 'Опыт работы', href: '/experience' },
    { name: 'Сертификаты', href: '/certificates' },
    { name: 'О себе', href: '/about' },
    { name: 'Контакты', href: '/contact' },
  ];

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const isActive = (path: string) => {
    return location.pathname === path;
  };

  return (
    <header className="bg-white/95 backdrop-blur-sm border-b border-secondary-200 sticky top-0 z-50">
      <div className="container mx-auto px-6">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link to="/" className="flex items-center space-x-3">
            <div className="flex items-center">
              <div className="w-10 h-10 bg-gradient-to-r from-blue-400 to-emerald-400 rounded-lg flex items-center justify-center mr-3">
                <span className="text-white font-bold text-xl">Т</span>
              </div>
              <div className="text-white">
                <h1 className="font-bold text-lg">Тимур Султанов</h1>
                <p className="text-sm text-gray-600">Инженер-программист</p>
              </div>
            </div>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex space-x-8">
            {navigation.map((item) => (
              <Link
                key={item.name}
                to={item.href}
                className={`relative px-3 py-2 text-sm font-medium transition-colors duration-200 ${isActive(item.href)
                  ? 'text-accent-600'
                  : 'text-secondary-700 hover:text-accent-600'
                  }`}
              >
                {item.name}
                {isActive(item.href) && (
                  <div className="absolute bottom-0 left-0 right-0 h-0.5 bg-accent-600" />
                )}
              </Link>
            ))}
          </nav>

          {/* Contact Button */}
          <div className="hidden md:block">
            <a
              href="mailto:timursultanw@yandex.ru"
              className="bg-accent-500 hover:bg-accent-600 text-white px-6 py-2 rounded-lg text-sm font-medium transition-colors duration-200"
            >
              Связаться
            </a>
          </div>

          {/* Mobile menu button */}
          <button
            onClick={toggleMenu}
            className="md:hidden p-2 rounded-lg text-secondary-600 hover:text-secondary-900 hover:bg-secondary-100 transition-colors duration-200"
          >
            {isMenuOpen ? (
              <XMarkIcon className="w-6 h-6" />
            ) : (
              <Bars3Icon className="w-6 h-6" />
            )}
          </button>
        </div>

        {/* Mobile Navigation */}
        {isMenuOpen && (
          <div className="md:hidden">
            <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3 bg-secondary-600/90 backdrop-blur-sm rounded-lg mt-2">
              {navigation.map((item) => (
                <Link
                  key={item.name}
                  to={item.href}
                  onClick={() => setIsMenuOpen(false)}
                  className={`px-3 py-2 text-sm font-medium transition-colors duration-200 rounded-lg ${isActive(item.href)
                    ? 'text-accent-500 bg-secondary-500/60'
                    : 'text-secondary-50 hover:text-accent-400 hover:bg-secondary-600/60'
                    }`}
                >
                  {item.name}
                </Link>
              ))}
              <div className="pt-4 border-t border-secondary-200">
                <a
                  href="mailto:timursultanw@yandex.ru"
                  className="block w-full text-center bg-accent-500 hover:bg-accent-600 text-white px-6 py-2 rounded-lg text-sm font-medium transition-colors duration-200"
                >
                  Связаться
                </a>
              </div>
            </div>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header; 