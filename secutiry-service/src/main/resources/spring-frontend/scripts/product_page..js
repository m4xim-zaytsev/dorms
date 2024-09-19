$(document).ready(function() {
    // Функция для проверки состояния аутентификации
    function checkAuthState() {
        const authButtonsContainer = document.getElementById("auth-buttons");
        authButtonsContainer.innerHTML = "";  // Очистка контейнера кнопок
        console.log("Проверка аутентификации");
        const token = localStorage.getItem("jwtToken");  // Получение токена из локального хранилища
        const navMenu = document.querySelector('.nav');
        document.cookie = 'jwtToken=' + localStorage['jwtToken'];

        if (token) {
            authButtonsContainer.innerHTML = `
                <button type="button" class="btn btn-outline-primary ml-2" onclick="redirectToProfile()">Profile</button>
                <button type="button" class="btn btn-danger ml-2" onclick="logout()">Logout</button>
            `;

            // Добавляем пункт "Моё" в меню, если пользователь аутентифицирован
            const myMenuItem = document.createElement('li');
            myMenuItem.innerHTML = `<a href="/api/v1/user/my" class="nav-link px-2">Моё</a>`;
            navMenu.appendChild(myMenuItem);  // Добавляем элемент в конец списка меню

            // Пример запроса для проверки аутентификации
            $.ajax({
                url: "/api/v1/user/hello",
                type: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                },
                success: function (response) {
                    $('#greeting-text').html(getGreeting() + response.username);
                },
                error: function (error) {
                    console.error("Ошибка при получении данных пользователя: " + error.responseJSON.message);
                }
            });
        } else {
            authButtonsContainer.innerHTML = `
                <button type="button" class="btn btn-outline-primary ml-2" data-toggle="modal" data-target="#loginModal">Login</button>
                <button type="button" class="btn btn-primary ml-2" data-toggle="modal" data-target="#registerModal">Sign Up</button>
            `;
            $('#greeting-text').html(getGreeting());
        }
    }

    // Функция для входа пользователя
    window.handleLogin = function () {
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        // AJAX-запрос для аутентификации пользователя
        $.ajax({
            url: '/api/v1/auth/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ username, password }),  // Передача данных в формате JSON
            success: function(response) {
                localStorage.setItem('jwtToken', response.token);
                window.location.href = '/api/v1/main';  // Перенаправление на главную страницу
            },
            error: function(error) {
                alert('Ошибка входа: ' + error.responseJSON.message);  // Сообщение об ошибке
            }
        });
    };

    // Функция выхода пользователя
    window.logout = function () {
        const token = localStorage.getItem("jwtToken");
        $.ajax({
            url: "/api/v1/auth/logout",
            type: "POST",
            headers: {
                "Authorization": "Bearer " + token  // Добавление токена в заголовок запроса
            },
            success: function (response) {
                localStorage.removeItem("jwtToken");  // Удаление токена из локального хранилища
                window.location.href = "/api/v1/main";  // Перенаправление на главную страницу
            },
            error: function (error) {
                alert("Ошибка при выходе: " + error.responseJSON.message);  // Сообщение об ошибке
            }
        });
    };

    // Функция для перенаправления на страницу профиля
    window.redirectToProfile = function () {
        const token = localStorage.getItem("jwtToken");
        if (token) {
            $.ajax({
                url: "/api/v1/user",
                type: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                },
                success: function(response) {
                    window.location.href = "/api/v1/user";  // Перенаправление на страницу профиля
                },
                error: function(error) {
                    alert("Ошибка аутентификации. Пожалуйста, войдите заново.");
                    window.location.href = "/api/v1/main";  // Перенаправление на страницу входа при ошибке
                }
            });
        } else {
            alert("Пожалуйста, войдите в систему.");
            window.location.href = "/api/v1/main";  // Перенаправление на страницу входа, если токена нет
        }
    };
    let productId = null;
    $.ajax({
        url: `/api/v1/product/${productId}/reviews`,
        method: 'GET',
        success: function(reviews) {
            // Отображаем отзывы
        },
        error: function(error) {
            console.error("Error fetching reviews:", error);
        }
    });

    checkAuthState();

    // Функция регистрации пользователя
    window.handleRegister = function () {
        const username = document.getElementById('regUsername').value;
        const email = document.getElementById('regEmail').value;
        const password = document.getElementById('regPassword').value;
        const name = document.getElementById('regName').value;

        $.ajax({
            url: '/api/v1/auth/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ username, email, password, name }),  // Отправка данных в формате JSON
            success: function(response) {
                alert('Регистрация успешна!');
                $('#registerModal').modal('hide');  // Закрытие модального окна
            },
            error: function(error) {
                alert('Ошибка регистрации: ' + error.responseJSON.message);
            }
        });
    };
});
