$(document).ready(function () {
    // Функция проверки аутентификации пользователя
    function checkAuthState() {
        const authButtonsContainer = document.getElementById("auth-buttons");
        authButtonsContainer.innerHTML = "";  // Очистка контейнера кнопок
        console.log("hello")
        const token = localStorage.getItem("jwtToken");  // Получение токена из локального хранилища
        if (token) {
            authButtonsContainer.innerHTML = `
            <button type="button" class="btn btn-outline-primary ml-2" onclick="redirectToProfile()">Profile</button>
            <button type="button" class="btn btn-danger ml-2" onclick="logout()">Logout</button>
        `;

            // Проверка аутентификации
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
            $('#greeting-text').html(getGreeting());  // Установите приветствие для неавторизованных пользователей
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
                // Сохранение токена в локальном хранилище
                window.location.href = '/api/v1/main';  // Перенаправление на главную страницу
            },
            error: function(error) {
                alert('Ошибка входа: ' + error.responseJSON.message);  // Сообщение об ошибке
            }
        });
    }

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
    }

    // Функция для перенаправления на страницу профиля
    window.redirectToProfile = function () {
        const token = localStorage.getItem("jwtToken");
        console.log("JWT Token:", token);  // Проверка наличия токена
        if (token) {
            $.ajax({
                url: "/api/v1/profile",
                type: "GET",
                headers: {
                    "Authorization": "Bearer " + token
                },
                success: function(response) {
                    window.location.href = "/api/v1/profile";  // Перенаправление на страницу профиля
                },
                error: function(error) {
                    console.error("Ошибка при переходе на профиль:", error.responseJSON.message);
                    alert("Ошибка аутентификации. Пожалуйста, войдите заново.");
                    window.location.href = "/api/v1/auth/login";  // Перенаправление на страницу входа при ошибке
                }
            });
        } else {
            alert("Пожалуйста, войдите в систему.");
            window.location.href = "/api/v1/auth/login";  // Перенаправление на страницу входа, если токена нет
        }
    }



    // Вызов функции проверки состояния аутентификации при загрузке страницы
    checkAuthState();

    // Инициализация слайдера (пример)
    $('.product-slider').owlCarousel({
        loop: true,
        margin: 10,
        nav: true,
        responsive: {
            0: { items: 1 },
            600: { items: 3 },
            1000: { items: 5 }
        }
    });

    var owl = $(".owl-carousel").owlCarousel({
        loop: true,
        margin: 10,
        nav: false,
        dots: false,
        responsive: {
            0: {
                items: 1
            },
            600: {
                items: 2
            },
            1000: {
                items: 6 // 6 айтемов на экране при большом разрешении
            }
        }
    });

    // Привязываем внешние кнопки к слайдеру
    $(".slider-btn-prev").click(function () {
        owl.trigger('prev.owl.carousel');
    });
    $(".slider-btn-next").click(function () {
        owl.trigger('next.owl.carousel');
    });

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
                alert('Ошибка регистрации: ' + error.responseJSON.message);  // Сообщение об ошибке
            }
        });
    }

    $(window).on('scroll', function () {
        $('.section').each(function () {
            if ($(window).scrollTop() > $(this).offset().top - $(window).height() / 1.2) {
                $(this).addClass('section-visible');
            }
        });
    });


});

// Функция для получения приветствия в зависимости от времени суток
function getGreeting() {
    const currentHour = new Date().getHours();
    if (currentHour >= 5 && currentHour < 12) return "Доброе утро";
    if (currentHour >= 12 && currentHour < 18) return "Добрый день";
    if (currentHour >= 18 && currentHour < 22) return "Добрый вечер";
    return "Доброй ночи";
}
