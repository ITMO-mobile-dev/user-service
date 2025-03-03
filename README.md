# user-service

## Настройки проекта

### Общие настройки
  - Версия JVM: `17`
  - Проект запускается по адресу http://127.0.0.1:8081 (или http://localhost:8081)

### Рабочие URL

  - GET: `http://localhost:8081/micro1/hello` - выводится приветствие на экран

  - GET: `http://localhost:8081/micro1/postgres` - вывод всех записей из таблицы `People` из PostgreSQL (таблица создаётся автоматически при запуске проекта)

  - POST: `http://localhost:8081/micro1/postgres` - создание новой записи в таблице Postgres (при отправке запроса необходимо передать параметры `name` и `age`, советую использовать Postman)

  - GET: `http://localhost:8081//micro1/redis/set/{key}/{value}` - создание пары `ключ-значение` в БД Redis. Прямо в ссылке передаётся ключ `key` (например `name`) и значение `value` (например `Максим`)

  - GET: `http://localhost:8081//micro1/redis/get/{key}` - получение значения по ключу из БД Redis

  - GET: `http://localhost:8081//micro1/kafka` - создаёт `Producer` и `Consumer` Кафки. `Producer` отправляет сообщение, `Consumer` получает его и выводит переданное сообщение по этой же ссылке


### 1. PostgreSQL
  - Ссылка для подключения к Postgres:
  `jdbc:postgresql://localhost:5432/ITMO-mobile`
  - Название БД: `ITMO-mobile`
  - Порт: `5432`
  - Имя пользователя: `postgres`
  - Пароль: `123`

### 2. Redis
  - ссылка для подключения к Redis: `redis://localhost:6379` (используется внутри программы)

### 3. Kafka
  - порт Zookeeper\`a: `2181`
  - порт консьюмера и продюсера: `9092`






