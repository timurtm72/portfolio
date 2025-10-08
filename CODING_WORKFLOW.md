# Гайд по разработке, работе с Git/GitHub и деплою на сервер

> Обновлено: 04.07.2025

## 1. Локальная разработка

### 1.1. Требования к окружению

| Инструмент          | Версия | Установка                                   |
|---------------------|--------|---------------------------------------------|
| Git                 | ≥ 2.30 | `sudo apt install git`                      |
| Node.js + npm       | LTS    | `nvm install --lts`                         |
| Java JDK            | 17     | `sudo apt install openjdk-17-jdk`           |
| Maven (Wrapper)     | 3.9    | уже в репозитории (`./mvnw`)                |
| Docker (опц.)       | 24.x   | `sudo apt install docker.io`                |
| IDE/Editor          | VS Code| Расширения: _Java Extension Pack_, _ESLint_ |

### 1.2. Клонирование проекта

```bash
# однократно
git clone git@github.com:timurtm72/portfolio.git
cd portfolio
# подключить upstream, если есть форк
# git remote add upstream git@github.com:timurtm72/portfolio.git
```

### 1.3. Работа в ветке

```bash
# создать новую ветку от main
git switch -c feature/<имя-задачи>

# Писать код → npm start / mvn spring-boot:run

# проверить линтер, тесты
npm run lint
./mvnw test
```

### 1.4. Коммиты

```bash
git add <файлы>
git commit -m "feat(<scope>): краткое описание изменения"
```

*Совет:* придерживайтесь [Conventional Commits](https://www.conventionalcommits.org/).

## 2. Отправка изменений в GitHub

### 2.1. Push и Pull Request

```bash
# актуализировать main перед пушем
git fetch origin
git rebase origin/main

# отправить ветку
git push -u origin feature/<имя-задачи>
```

1. Открыть PR в GitHub → выбрать reviewers.
2. После аппрува _Squash & Merge_ → ветка удаляется.

### 2.2. Синхронизация локального main

```bash
git switch main
git pull --ff-only
```

## 3. Деплой обновлений на сервер

> Сервер: Ubuntu 24.04, IP `103.106.3.98`, проект лежит в `/opt/portfolio`.

### 3.1. Подготовка

SSH-доступ по ключу:
```bash
ssh-copy-id root@103.106.3.98
```
На сервере настроены:
* systemd-юнит `portfolio-backend.service`
* Nginx с root `/var/www/portfolio` и прокси к `127.0.0.1:8080`

### 3.2. Скрипт деплоя (run-deploy.sh)

```bash
#!/usr/bin/env bash
set -euo pipefail
cd /opt/portfolio

echo "👉 1. Обновляем код"
git fetch --all
# без локальных правок
git reset --hard origin/main

echo "👉 2. Сборка backend"
cd backend
./mvnw clean package -DskipTests
cp target/backend-*.jar ../portfolio-backend.jar

echo "👉 3. Сборка frontend"
cd ../frontend
npm ci --omit=dev
npm run build
rsync -a --delete build/ /var/www/portfolio/

echo "👉 4. Перезапуск сервисов"
systemctl restart portfolio-backend
nginx -s reload

echo "✅ Деплой завершён"
```

Запуск:
```bash
ssh root@103.106.3.98 'bash /opt/portfolio/run-deploy.sh'
```

### 3.3. Проверка

```bash
# API
curl -I http://103.106.3.98/api/health

# Frontend
curl -IL http://technocom.site123.me | head -n 5
```

### 3.4. Возможные проблемы

| Симптом                               | Диагностика                             | Решение                                   |
|---------------------------------------|-----------------------------------------|-------------------------------------------|
| `HTTP 301` на example.com             | `nginx -T | grep server_name`           | Удалить лишний vhost или добавить `default_server` |
| Старая статика после деплоя           | Service Worker или кэш браузера         | DevTools → Application → Unregister SW    |
| Backend не обновился                  | `journalctl -u portfolio-backend -n 50` | `systemctl restart portfolio-backend`     |

## 4. Автоматизация через GitHub Actions (опц.)

```yaml
name: Deploy
on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Sync to server
        uses: appleboy/ssh-action@v1.0.0
        with:
          host: ${{ secrets.SSH_HOST }}
          username: root
          key: ${{ secrets.SSH_KEY }}
          script: |
            bash /opt/portfolio/run-deploy.sh
```

## 5. Полезные команды Git

| Задача                          | Команда                                    |
|---------------------------------|--------------------------------------------|
| Список локальных веток          | `git branch`                               |
| Удалить локальную ветку         | `git branch -d feature/foo`                |
| Исправить последний коммит      | `git commit --amend --no-edit`             |
| Отменить локальные изменения    | `git restore <файл>`                       |
| Сжать историю (interactive rebase)| `git rebase -i HEAD~5`                    |

---

**Совет:** держите README актуальным и автоматизируйте всё, что повторяется! 