
Ветка inMemory 
Представен вид аутентификации inMemory - когда пользователи и их пароли хранятся в приложении.

Ветка JdbcUserDetails 
Данные пользователя получаем из БД, перед запуском нужно запустить скрипт src/main/resources/Script-150.sql БД postgres. Пароль захэширован aлгоритмом bcrypt.
У пользователя lucien@mail.ru пароль password, y helena@mail.ru 654321

Ветка AuthenticationProvider 
AuthenticationProvider обрабатывает запрос аутентификации и возвращает объект с учетными данными.
Перед запуском ветки запустить часть скрипта Script-150.sql для создания и наполнеиня таблицы authorities
Добавлены роли, пользователю lucien@mail.ru больше не удастся просматривать страницу http://localhost:8090/myInfo, 
страница http://localhost:8090/contact доступна обоим пользователям.

Ветка Filter 
Добавлен AuthoritiesLoggingAfterFilter, который выведет в лог данные авторизованного пользователя после того как отработает BasicAuthenticationFilter

Ветка JsonWebToken
Для проверки работы понадобится Postman. 
См. файл JWT.docx в resources