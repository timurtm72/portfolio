# 🖼️ WebP изображения в React портфолио

## ✅ Что уже настроено

В вашем проекте уже готова полная поддержка WebP изображений:

1. ✅ **Компонент `WebPImage`** - автоматическое переключение между WebP и fallback
2. ✅ **Обновленные страницы** - Projects.tsx и ProjectModal.tsx используют WebPImage
3. ✅ **Структура папок** - готовая организация файлов
4. ✅ **Скрипт конвертации** - автоматическая конвертация в WebP

## 🚀 Как использовать прямо сейчас

### 1. Добавьте изображения проектов

Поместите ваши изображения в формате JPG/PNG в папку:
```
frontend/public/images/projects/
```

### 2. Конвертируйте в WebP (опционально)

**Автоматически (рекомендуется):**
```bash
# Установите библиотеку для конвертации
npm install sharp

# Запустите скрипт конвертации
node scripts/convert-to-webp.js
```

**Вручную:**
- Используйте [Squoosh.app](https://squoosh.app/) от Google
- Сохраните WebP файлы в папку `frontend/public/images/projects/webp/`

### 3. Используйте в коде

Компонент `WebPImage` уже подключен в страницы Projects и ProjectModal:

```tsx
import WebPImage from '../components/WebPImage.tsx';

<WebPImage
  src="/images/projects/my-project.jpg"
  alt="Описание проекта"
/>
```

## 📁 Структура файлов

```
frontend/public/images/projects/
├── webp/                          # WebP версии (автогенерация)
│   ├── stm32-project.webp
│   ├── esp32-iot.webp
│   └── automation-system.webp
├── stm32-project.jpg              # Исходные изображения
├── esp32-iot.png
├── automation-system.jpg
└── placeholder.svg                # Заглушка
```

## 🎯 Примеры использования

### В данных проектов (backend)
```java
// В вашем проекте уже настроено получение imageUrl из базы данных
// Просто убедитесь, что поле imageUrl содержит правильный путь
```

### На фронтенде
```tsx
// Автоматическое определение WebP
<WebPImage
  src="/images/projects/my-project.jpg"
  alt="STM32 проект"
/>

// Ручное указание WebP пути
<WebPImage
  src="/images/projects/my-project.jpg"
  webpSrc="/images/projects/webp/my-project.webp"
  alt="STM32 проект"
/>
```

## 📊 Результат

После внедрения WebP изображений:
- **30-50% меньше трафика** 🚀
- **Быстрее загрузка страниц** ⚡
- **Лучше SEO** 📈
- **Автоматический fallback** для старых браузеров 🔄

## 💡 Рекомендации

1. **Называйте файлы понятно:**
   - `stm32-gas-detector.jpg` → `stm32-gas-detector.webp`
   - `esp32-smart-home.png` → `esp32-smart-home.webp`

2. **Оптимальные настройки WebP:**
   - Качество: 75-85%
   - Для технических схем: выше качество (85-90%)
   - Для фото устройств: можно ниже (70-80%)

3. **В базе данных указывайте путь к исходному файлу:**
   ```sql
   UPDATE projects SET image_url = '/images/projects/stm32-project.jpg' WHERE id = 1;
   ```
   Компонент сам найдет WebP версию!

## 🔧 Техническая информация

### Как работает WebPImage компонент:
1. Браузер видит `<picture>` элемент
2. Первым пытается загрузить WebP версию
3. Если WebP не поддерживается → загружает fallback (JPG/PNG)
4. Все прозрачно для пользователя!

### Поддержка браузерами:
- ✅ Chrome, Firefox, Safari 14+, Edge (95%+ пользователей)
- ⚠️ Старые браузеры автоматически получают JPG/PNG

---

## 🎉 Готово к использованию!

Ваш проект уже настроен для WebP. Просто:
1. Добавьте изображения в `frontend/public/images/projects/`
2. Запустите `node scripts/convert-to-webp.js` (опционально)
3. Наслаждайтесь быстрой загрузкой! 🚀

**Вопросы?** Проверьте `frontend/public/images/projects/README-WebP.md` для подробностей. 