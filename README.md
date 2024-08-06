## Онбординг Бот

Бот для онбординга новых сотрудников с динамической настройкой "Сценариев"

### Функционал

* Работа бота по сценариям
* Динамическая настройка сценариев
* Интерактивный редактор сценариев
* Возможность загрузки файлов
* Работа с локальными переменными
* Пересылка сообщений "HR-специалистами"
* Тестирование работы сценариев "HR-специалистами"

### Стек

* Backend: Java 21, Spring Boot 3, Jooq, Telegram Bots, MinIO, PostgreSQL
* Frontend: TypeScript, Svelte, SvelteKit

### Переменные окружения

| ENV_PROPERTY         | REQUIRED | DEFAULT_VALUE |
|----------------------|----------|---------------|
| TELEGRAM_BOT_ENABLED | -        | true          |
| TELEGRAM_BOT_NAME    | +        | -             |
| TELEGRAM_BOT_TOKEN   | +        | -             |
| DATABASE_URL         | +        | -             |
| DATABASE_USERNAME    | +        | -             |
| DATABASE_PASSWORD    | +        | -             |
| S3_URL               | +        | -             |
| S3_ACCESS_KEY        | +        | -             |
| S3_SECRET_KEY        | +        | -             |
| S3_CONNECT_TIMEOUT   | -        | PT30S         |
