document.addEventListener("DOMContentLoaded", function () {
    loadRestaurants();
});

function loadRestaurants() {
    fetch("/api/restaurants")
        .then(response => response.json())
        .then(data => {
            const list = document.getElementById("restaurant-list");
            list.innerHTML = "";
            data.forEach((restaurant, index) => {
                const listItem = document.createElement("li");
                listItem.textContent = restaurant;

                const deleteButton = document.createElement("button");
                deleteButton.textContent = "Eliminar";
                deleteButton.onclick = () => deleteRestaurant(index);

                listItem.appendChild(deleteButton);
                list.appendChild(listItem);
            });
        })
        .catch(error => console.error("Error cargando restaurantes:", error));
}

function addRestaurant() {
    const restaurantName = document.getElementById("restaurant-name").value;
    if (!restaurantName) return;

    fetch("/api/restaurants", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(restaurantName)
    })
    .then(() => {
        document.getElementById("restaurant-name").value = "";
        loadRestaurants();
    })
    .catch(error => console.error("Error agregando restaurante:", error));
}

function deleteRestaurant(index) {
    fetch(`/api/restaurants/${index}`, { method: "DELETE" })
        .then(() => loadRestaurants())
        .catch(error => console.error("Error eliminando restaurante:", error));
}
