$(document).ready(function () {
    loadUserData();
    loadOrderHistory();

    // Function to load user data
    function loadUserData() {
        const token = localStorage.getItem("jwtToken");
        $.ajax({
            url: "/api/v1/user/details",
            type: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                $('#profileName').val(response.name);
                $('#profileEmail').val(response.email);
                $('#profileUsername').val(response.username);
            },
            error: function (error) {
                console.error("Ошибка при загрузке данных профиля: " + error.responseJSON.message);
            }
        });
    }

    // Function to update user profile
    window.updateProfile = function () {
        const name = $('#profileName').val();
        const email = $('#profileEmail').val();
        const username = $('#profileUsername').val();
        const token = localStorage.getItem("jwtToken");

        $.ajax({
            url: "/api/v1/user/update",
            type: "PUT",
            headers: {
                "Authorization": "Bearer " + token
            },
            contentType: 'application/json',
            data: JSON.stringify({ name, email, username }),
            success: function (newToken) {  // принимаем новый токен от сервера
                alert("Профиль обновлен успешно!");
                // Сохраняем новый токен в localStorage
                localStorage.setItem("jwtToken", newToken);

                // Повторно загружаем данные пользователя после обновления
                loadUserData();
            },
            error: function (error) {
                alert("Ошибка при обновлении профиля: " + error.responseJSON.message);
            }
        });
    }

    // Function to load order history
    function loadOrderHistory() {
        const token = localStorage.getItem("jwtToken");
        $.ajax({
            url: "/api/v1/orders/history",
            type: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (response) {
                const ordersTable = $('#ordersTable');
                ordersTable.empty();  // Очистка таблицы перед добавлением новых данных
                response.orders.forEach(order => {
                    ordersTable.append(`<tr>
                <td>${order.orderNumber}</td>
                <td>${order.date}</td>
                <td>${order.totalAmount} ₽</td>
                <td>${order.status}</td>
              </tr>`);
                });
            },
            error: function (error) {
                console.error("Ошибка при загрузке истории заказов: " + error.responseJSON.message);
            }
        });
    }

    // Function to change user password
    window.changePassword = function () {
        const currentPassword = $('#currentPassword').val();
        const newPassword = $('#newPassword').val();
        const confirmNewPassword = $('#confirmNewPassword').val();
        const token = localStorage.getItem("jwtToken");

        if (newPassword !== confirmNewPassword) {
            alert("Новые пароли не совпадают!");
            return;
        }

        $.ajax({
            url: "/api/v1/user/change-password",
            type: "POST",
            headers: {
                "Authorization": "Bearer " + token
            },
            contentType: 'application/json',
            data: JSON.stringify({ currentPassword, newPassword }),
            success: function (response) {
                alert("Пароль успешно изменен!");
                $('#changePasswordForm')[0].reset();

                // Повторно загружаем данные пользователя после изменения пароля
                loadUserData();
            },
            error: function (error) {
                alert("Ошибка при изменении пароля: " + error.responseJSON.message);
            }
        });
    }
});

// Function to logout the user
window.logout = function () {
    localStorage.removeItem("jwtToken");
    window.location.href = "/api/v1/main";
}