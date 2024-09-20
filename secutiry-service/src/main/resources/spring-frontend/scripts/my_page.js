$(document).ready(function () {
    const token = localStorage.getItem("jwtToken");

    // Функция для загрузки продуктов пользователя
    function loadUserProducts() {
        $.ajax({
            url: "/api/v1/user/my/products",
            type: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function (products) {
                renderProducts(products);
            },
            error: function (error) {
                console.error("Ошибка при загрузке продуктов: " + error.responseJSON.message);
            }
        });
    }

    // Функция для отображения продуктов на странице
    function renderProducts(products) {
        const productContainer = $('.row');
        productContainer.empty(); // Очищаем старые продукты

        products.forEach(product => {
            const productCard = `
                <div class="col-md-4 col-sm-6">
                    <div class="product-card" >
                        <a href="/api/v1/product/${product.id}" class="product-card-link">
                        <img src="${product.imageUrl || 'https://via.placeholder.com/150'}" alt="${product.name}">
                        <h5 class="product-name">${product.name}</h5>
                        <p class="product-price">${product.price} ₽ ${product.oldPrice ? `<span class="product-old-price">${product.oldPrice} ₽</span>` : ''}</p>
                        <p class="product-quantity">Количество: ${product.count}</p>
                        <div class="action-buttons">
                            <button class="edit-btn" data-id="${product.id}"><i class="fas fa-edit"></i></button>
                            <button class="delete-btn" data-id="${product.id}"><i class="fas fa-trash"></i></button>
                        </div>
                    </div>
                </div>`;
            productContainer.append(productCard);
        });

        // Привязываем события для кнопок после рендеринга
        attachActionButtons();
    }
    // Привязка событий для кнопок редактирования и удаления
        function attachActionButtons() {
            $(".edit-btn").click(function () {
                const productId = $(this).data("id");
                window.location.href = `/api/v1/product/edit/${productId}`; // Перенаправляем на страницу редактирования
            });

            $(".delete-btn").click(function () {
                const productId = $(this).data("id");
                if (confirm("Вы уверены, что хотите удалить этот товар?")) {
                    deleteProduct(productId);
                }
            });
        }


    // Функция для удаления товара
    function deleteProduct(productId) {
        $.ajax({
            url: `/api/v1/product/${productId}`,
            type: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            },
            success: function () {
                $(`button[data-id=${productId}]`).closest('.product-card').remove();
                alert("Товар успешно удален.");
            },
            error: function (error) {
                console.error("Ошибка при удалении товара: " + error.responseJSON.message);
            }
        });
    }

    // Загружаем продукты при загрузке страницы
    loadUserProducts();
});
