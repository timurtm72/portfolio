import React from 'react';
import { API_URL } from '../config.ts';

interface WebPImageProps {
    src: string;          // Путь к изображению (например: "/images/projects/example.jpg")
    alt: string;
    className?: string;
    style?: React.CSSProperties;
    onClick?: () => void;
    onError?: (e: React.SyntheticEvent<HTMLImageElement, Event>) => void;
}

const WebPImage: React.FC<WebPImageProps> = ({
    src,
    alt,
    className,
    style,
    onClick,
    onError
}) => {
    const PLACEHOLDER = '/images/projects/placeholder.svg';

    const getApiUrl = (imagePath: string | undefined, isWebP = false) => {
        if (!imagePath) {
            // если путь отсутствует – возвращаем плейсхолдер без преобразований
            return PLACEHOLDER;
        }

        // Убираем "/images/projects/" из начала пути
        const filename = imagePath.replace(/^\/images\/projects\//, '');

        // Если путь уже содержит webp папку - используем как есть
        if (filename.startsWith('webp/')) {
            if (isWebP) {
                return `${API_URL}/images/projects/${filename}`;
            } else {
                // Для fallback убираем webp/ и меняем расширение
                const fallbackFilename = filename.replace(/^webp\//, '').replace(/\.webp$/i, '.jpg');
                return `${API_URL}/images/projects/${fallbackFilename}`;
            }
        }

        if (isWebP) {
            // Для WebP изображений используем специальный endpoint
            const webpFilename = filename.replace(/\.(jpg|jpeg|png)$/i, '.webp');
            return `${API_URL}/images/projects/webp/${webpFilename}`;
        } else {
            // Для обычных изображений
            return `${API_URL}/images/projects/${filename}`;
        }
    };

    // Генерируем WebP и fallback URLs
    const webpUrl = getApiUrl(src, true);
    const fallbackUrl = getApiUrl(src, false);

    // Если путь не начинается с "/images/projects/", используем исходный путь (или плейсхолдер)
    const isProjectImage = typeof src === 'string' && src.startsWith('/images/projects/');

    if (!isProjectImage) {
        // Для остальных изображений используем прямой путь
        return (
            <img
                src={src || PLACEHOLDER}
                alt={alt}
                className={className || "w-full h-full object-contain"}
                style={style}
                onClick={onClick}
                onError={onError}
            />
        );
    }

    return (
        <picture onClick={onClick}>
            {/* Браузер попытается загрузить WebP через API сначала */}
            <source srcSet={webpUrl} type="image/webp" />

            {/* Fallback для браузеров без поддержки WebP или если WebP не найден */}
            <img
                src={fallbackUrl}
                alt={alt}
                className={className ?? "w-full h-full object-contain"}
                style={style}
                onError={onError}
            />
        </picture>
    );
};

export default WebPImage; 