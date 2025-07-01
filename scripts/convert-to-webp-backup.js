#!/usr/bin/env node

/**
 * Скрипт для автоматической конвертации изображений в WebP формат
 * 
 * Использование:
 * npm install sharp  (для установки зависимости)
 * node scripts/convert-to-webp.js
 */

const fs = require('fs');
const path = require('path');

// Проверяем наличие библиотеки sharp
let sharp;
try {
    sharp = require('sharp');
} catch (error) {
    console.error('❌ Ошибка: Библиотека sharp не установлена.');
    console.log('📦 Установите её командой: npm install sharp');
    process.exit(1);
}

const INPUT_DIR = path.join(__dirname, '../public/images/projects');
const OUTPUT_DIR = path.join(__dirname, '../public/images/projects/webp');

// Поддерживаемые форматы
const SUPPORTED_FORMATS = ['.jpg', '.jpeg', '.png'];

/**
 * Конвертирует изображение в WebP формат
 */
async function convertToWebP(inputPath, outputPath) {
    try {
        await sharp(inputPath)
            .webp({
                quality: 80,           // Качество 80% - хороший баланс
                effort: 6              // Уровень сжатия (0-6, 6 = максимальное сжатие)
            })
            .toFile(outputPath);

        // Получаем размеры файлов для сравнения
        const originalSize = fs.statSync(inputPath).size;
        const webpSize = fs.statSync(outputPath).size;
        const savings = Math.round((1 - webpSize / originalSize) * 100);

        console.log(`✅ ${path.basename(inputPath)} → ${path.basename(outputPath)} (экономия: ${savings}%)`);

    } catch (error) {
        console.error(`❌ Ошибка при конвертации ${inputPath}:`, error.message);
    }
}

/**
 * Основная функция
 */
async function main() {
    console.log('🔄 Начинаю конвертацию изображений в WebP...\n');

    // Создаем папку для WebP если её нет
    if (!fs.existsSync(OUTPUT_DIR)) {
        fs.mkdirSync(OUTPUT_DIR, { recursive: true });
        console.log(`📁 Создана папка: ${OUTPUT_DIR}`);
    }

    // Читаем файлы из директории
    const files = fs.readdirSync(INPUT_DIR);
    const imageFiles = files.filter(file => {
        const ext = path.extname(file).toLowerCase();
        return SUPPORTED_FORMATS.includes(ext);
    });

    if (imageFiles.length === 0) {
        console.log('ℹ️  Изображения для конвертации не найдены.');
        console.log(`📂 Поместите JPG/PNG файлы в: ${INPUT_DIR}`);
        return;
    }

    console.log(`📸 Найдено изображений: ${imageFiles.length}\n`);

    // Конвертируем каждое изображение
    for (const file of imageFiles) {
        const inputPath = path.join(INPUT_DIR, file);
        const nameWithoutExt = path.parse(file).name;
        const outputPath = path.join(OUTPUT_DIR, `${nameWithoutExt}.webp`);

        // Проверяем, не существует ли уже WebP версия
        if (fs.existsSync(outputPath)) {
            console.log(`⏭️  Пропускаю ${file} (WebP уже существует)`);
            continue;
        }

        await convertToWebP(inputPath, outputPath);
    }

    console.log('\n🎉 Конвертация завершена!');
    console.log('\n💡 Как использовать:');
    console.log('   import WebPImage from "../components/WebPImage.tsx";');
    console.log('   <WebPImage src="/images/projects/photo.jpg" alt="Описание" />');
}

// Запускаем скрипт
main().catch(console.error); 