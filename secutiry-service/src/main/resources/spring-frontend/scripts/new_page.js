$(document).ready(function() {
    // Предварительный просмотр выбранного изображения
    $('#productImage').on('change', function(event) {
        let input = event.target;
        let reader = new FileReader();

        reader.onload = function(e) {
            $('#imagePreview').attr('src', e.target.result);
            $('#imagePreview').show();
        }

        reader.readAsDataURL(input.files[0]);
    });

    // Загрузка категорий через AJAX
    $.ajax({
        url: '/api/v1/categories',
        method: 'GET',
        success: function(categories) {
            let categorySelect = $('#productCategory');
            categorySelect.empty();
            categories.forEach(function(category) {
                categorySelect.append(new Option(category.name, category.id));
            });
        },
        error: function() {
            console.error('Ошибка при загрузке категорий');
        }
    });

    // Обработка отправки формы
    $('#productForm').on('submit', function(event) {
        event.preventDefault(); // Останавливаем стандартное поведение формы
        const token = localStorage.getItem("jwtToken");

        let formData = new FormData(this); // Создаем объект FormData

        $.ajax({
            url: '/api/v1/product/add',
            method: 'POST',
            data: formData,
            headers: {
                "Authorization": "Bearer " + token
            },
            processData: false, // Не обрабатывать данные
            contentType: false, // Не устанавливать заголовок Content-Type
            success: function(response) {
                alert('Товар успешно добавлен');
                window.location.href = '/api/v1/user/my';
            },
            error: function(xhr, status, error) {
                console.error('Ошибка при добавлении товара');
                alert('Ошибка при добавлении товара');
            }
        });
    });

});