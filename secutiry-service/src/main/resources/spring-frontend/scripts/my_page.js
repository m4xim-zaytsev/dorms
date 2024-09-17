$(document).ready(function () {

    // Функция редактирования товара
    $(".edit-btn").click(function () {
        alert("Редактирование товара.");
        // Реализация редактирования товара
    });

    // Функция удаления товара
    $(".delete-btn").click(function () {
        if (confirm("Вы уверены, что хотите удалить этот товар?")) {
            $(this).closest('.product-card').remove();
            // Реализация удаления товара
        }
    });
});