
    $(document).ready(function () {
    // Функция проверки аутентификации пользователя
    function checkAuthState() {
        const authButtonsContainer = document.getElementById("auth-buttons");
        authButtonsContainer.innerHTML = "";  // Очистка контейнера кнопок

        const token = localStorage.getItem("jwtToken");  // Получение токена из локального хранилища
        if (token) {  // Если токен есть, показать кнопки для профиля и выхода
            authButtonsContainer.innerHTML = `
          <button type="button" class="btn btn-outline-primary ml-2" onclick="redirectToProfile()">Profile</button>
          <button type="button" class="btn btn-danger ml-2" onclick="logout()">Logout</button>
        `;

            // Запрос информации о пользователе
            $.ajax({
                url: "/api/v1/user/me",
                type: "GET",
                headers: {
                    "Authorization": "Bearer " + token  // Добавление токена в заголовок запроса
                },
                success: function (response) {
                    $('#greeting-text').html(getGreeting() + ', ' + response.username);  // Приветствие с именем пользователя
                },
                error: function (error) {
                    console.error("Ошибка при получении данных пользователя: " + error.responseJSON.message);
                }
            });
        } else {  // Если токена нет, показать кнопку входа
            authButtonsContainer.innerHTML = `
          <button type="button" class="btn btn-outline-primary ml-2" data-toggle="modal" data-target="#loginModal">Login</button>
        `;
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
    localStorage.setItem('jwtToken', response.token);  // Сохранение токена в локальном хранилище
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
    window.location.href = "/api/v1/profile";  // Перенаправление на профиль
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
});

    // Функция для получения приветствия в зависимости от времени суток
    function getGreeting() {
    const currentHour = new Date().getHours();
    if (currentHour >= 5 && currentHour < 12) return "Доброе утро";
    if (currentHour >= 12 && currentHour < 18) return "Добрый день";
    if (currentHour >= 18 && currentHour < 22) return "Добрый вечер";
    return "Доброй ночи";
}

