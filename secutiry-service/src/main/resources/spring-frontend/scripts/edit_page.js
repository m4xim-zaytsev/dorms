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

    // Если изображение уже загружено, то показываем его
    let existingImageUrl = $('#imagePreview').attr('src');
    if (existingImageUrl) {
        $('#imagePreview').show();
    }

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

            let selectedCategoryId = $('#productCategory').data('selectedCategory');
            if (selectedCategoryId) {
                categorySelect.val(selectedCategoryId);
            }
        },
        error: function() {
            console.error('Ошибка при загрузке категорий');
        }
    });
});
