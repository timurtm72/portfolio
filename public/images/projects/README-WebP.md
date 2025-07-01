# 📸 Инструкция по работе с WebP изображениями

## 🎯 Структура папок

```
public/images/projects/
├── webp/                    # WebP версии изображений
│   ├── project1.webp
│   ├── project2.webp
│   └── ...
├── project1.jpg            # Fallback изображения
├── project2.png            # Fallback изображения
└── placeholder.svg          # Заглушка
```

## 🔧 Как использовать WebP в коде

### Вариант 1: Автоматическое определение WebP
```tsx
import WebPImage from '../components/WebPImage.tsx';

// Компонент автоматически попытается загрузить project1.webp
<WebPImage
  src="/images/projects/project1.jpg"
  alt="Название проекта"
/>
```

### Вариант 2: Явное указание WebP
```tsx
<WebPImage
  src="/images/projects/project1.jpg"
  webpSrc="/images/projects/webp/project1.webp"
  alt="Название проекта"
/>
```

### Вариант 3: Нативный HTML Picture
```tsx
<picture>
  <source srcSet="/images/projects/webp/project1.webp" type="image/webp" />
  <img src="/images/projects/project1.jpg" alt="Название проекта" />
</picture>
```

## 🛠️ Конвертация изображений в WebP

### Онлайн инструменты:
- [Squoosh.app](https://squoosh.app/) - от Google
- [CloudConvert](https://cloudconvert.com/webp-converter)
- [TinyPNG](https://tinypng.com/) - поддерживает WebP

### Командная строка (если установлен cwebp):
```bash
cwebp input.jpg -o output.webp -q 80
```

### Photoshop/GIMP:
- Экспорт -> WebP формат
- Качество: 75-85% для лучшего баланса размер/качество

## 📊 Преимущества WebP

- **30-50% меньше размер** файла по сравнению с JPEG/PNG
- **Поддержка прозрачности** (как PNG)
- **Лучшее сжатие** без потери качества
- **Поддержка анимации** (как GIF)

## 🌍 Поддержка браузерами

✅ **Поддерживают WebP (95%+ пользователей):**
- Chrome/Chromium (все версии)
- Firefox 65+
- Safari 14+
- Edge 18+
- Opera 19+

❌ **Не поддерживают:**
- Internet Explorer
- Старые версии Safari (<14)

**Решение:** Наш компонент `WebPImage` автоматически показывает fallback изображение для несовместимых браузеров.

## 📝 Рекомендации

1. **Всегда предоставляйте fallback** изображение в формате JPG/PNG
2. **Используйте качество 75-85%** для WebP изображений
3. **Называйте файлы одинаково**: `project1.jpg` → `project1.webp`
4. **Тестируйте в разных браузерах** для проверки fallback
5. **Сжимайте исходные изображения** перед конвертацией в WebP

## 🚀 Как добавить новое изображение

1. Поместите исходное изображение в `/public/images/projects/`
2. Конвертируйте его в WebP и поместите в `/public/images/projects/webp/`
3. Используйте компонент `WebPImage` в коде
4. Браузер автоматически выберет лучший формат!

---

💡 **Tip:** WebP значительно ускоряет загрузку сайта и улучшает SEO! 