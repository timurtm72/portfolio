package com.example.backend.controller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    // Путь к статическим изображениям
    private static final String IMAGES_BASE_PATH = "/images/projects/";
    private static final String WEBP_IMAGES_PATH = "/images/projects/webp/";

    @GetMapping("/projects/**")
    public ResponseEntity<byte[]> getProjectImage(HttpServletRequest request) {
        // Извлекаем filename из пути запроса
        String requestPath = request.getRequestURI();
        // Учитываем server.servlet.context-path (например, "/api")
        String prefix = request.getContextPath() + "/images/projects/"; // будет "/api/images/projects/"
        if (!requestPath.startsWith(prefix)) {
            logger.warn("Некорректный путь запроса к изображению: {}", requestPath);
            return ResponseEntity.badRequest().build();
        }
        String filename = requestPath.substring(prefix.length());

        logger.debug("Запрошен файл: {}", filename);

        try {

            // Определяем MediaType по расширению файла
            MediaType mediaType = getMediaTypeForFilename(filename);

            byte[] imageBytes = null;

            // Проверяем, содержит ли filename подпапки (например, webp/janome.webp)
            if (filename.contains("/")) {
                // Для файлов с подпапками пытаемся найти в classpath
                imageBytes = loadImageFromClasspath(IMAGES_BASE_PATH + filename);

                if (imageBytes == null) {
                    // Пытаемся найти в public папке с подпапкой
                    imageBytes = loadImageWithSubfolder(filename);
                }
            } else {
                // Сначала пытаемся найти изображение в classpath (resources)
                imageBytes = loadImageFromClasspath(IMAGES_BASE_PATH + filename);

                if (imageBytes == null) {
                    // Если не найдено в classpath, пытаемся найти в public папке frontend
                    imageBytes = loadImageFromPublicFolder(filename);
                }
            }

            if (imageBytes == null) {
                logger.warn("Изображение не найдено: {}", filename);
                return ResponseEntity.notFound().build();
            }

            logger.debug("Успешно загружено изображение: {} ({} байт)", filename, imageBytes.length);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                    .body(imageBytes);

        } catch (Exception e) {
            logger.error("Ошибка при загрузке изображения {}: {}", filename, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/projects/webp/{filename}")
    public ResponseEntity<byte[]> getWebPImage(@PathVariable String filename) {
        try {
            // Для WebP файлов
            byte[] imageBytes = loadImageFromClasspath(WEBP_IMAGES_PATH + filename);

            if (imageBytes == null) {
                imageBytes = loadWebPFromPublicFolder(filename);
            }

            if (imageBytes == null) {
                logger.warn("WebP изображение не найдено: {}", filename);
                return ResponseEntity.notFound().build();
            }

            logger.debug("Успешно загружено WebP изображение: {} ({} байт)", filename, imageBytes.length);

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("image/webp"))
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                    .body(imageBytes);

        } catch (Exception e) {
            logger.error("Ошибка при загрузке WebP изображения {}: {}", filename, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private byte[] loadImageFromClasspath(String path) {
        try {
            Resource resource = new ClassPathResource("static" + path);
            if (resource.exists()) {
                return FileUtils.readFileToByteArray(resource.getFile());
            }
        } catch (IOException e) {
            logger.debug("Не удалось загрузить из classpath: {}", path);
        }
        return null;
    }

    private byte[] loadImageFromPublicFolder(String filename) {
        try {
            // Возможные пути к изображениям в зависимости от рабочей директории
            Path[] candidatePaths = {
                    // 1. При запуске из корня проекта
                    Paths.get("frontend/public/images/projects/" + filename),
                    // 2. При запуске из каталога backend
                    Paths.get("../frontend/public/images/projects/" + filename),
                    // 3. Каталог public в корне проекта
                    Paths.get("public/images/projects/" + filename)
            };

            for (Path path : candidatePaths) {
                if (Files.exists(path)) {
                    logger.debug("Найден файл: {}", path.toAbsolutePath());
                    return Files.readAllBytes(path);
                }
            }
        } catch (IOException e) {
            logger.debug("Не удалось загрузить из public папки: {}", filename);
        }
        return null;
    }

    private byte[] loadWebPFromPublicFolder(String filename) {
        try {
            Path[] candidatePaths = {
                    Paths.get("frontend/public/images/projects/webp/" + filename),
                    Paths.get("../frontend/public/images/projects/webp/" + filename),
                    Paths.get("public/images/projects/webp/" + filename)
            };

            for (Path path : candidatePaths) {
                if (Files.exists(path)) {
                    logger.debug("Найден WebP файл: {}", path.toAbsolutePath());
                    return Files.readAllBytes(path);
                }
            }
        } catch (IOException e) {
            logger.debug("Не удалось загрузить WebP из public папки: {}", filename);
        }
        return null;
    }

    private byte[] loadImageWithSubfolder(String filenameWithPath) {
        try {
            Path[] candidatePaths = {
                    Paths.get("frontend/public/images/projects/" + filenameWithPath),
                    Paths.get("../frontend/public/images/projects/" + filenameWithPath),
                    Paths.get("public/images/projects/" + filenameWithPath)
            };

            for (Path path : candidatePaths) {
                if (Files.exists(path)) {
                    logger.debug("Найден файл с подпапкой: {}", path.toAbsolutePath());
                    return Files.readAllBytes(path);
                }
            }
        } catch (IOException e) {
            logger.debug("Не удалось загрузить файл с подпапкой: {}", filenameWithPath);
        }
        return null;
    }

    private MediaType getMediaTypeForFilename(String filename) {
        String extension = getFileExtension(filename).toLowerCase();

        switch (extension) {
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "webp":
                return MediaType.valueOf("image/webp");
            case "svg":
                return MediaType.valueOf("image/svg+xml");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }

    // OPTIONS endpoint для CORS preflight запросов
    @RequestMapping(method = RequestMethod.OPTIONS, value = "/**")
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }
}