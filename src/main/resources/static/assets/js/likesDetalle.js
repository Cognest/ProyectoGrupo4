document.addEventListener("DOMContentLoaded", () => {
    const likeButton = document.getElementById("like-button");
    const likeIcon = document.getElementById("like-icon");
    const likeCount = document.getElementById("like-count");

    likeButton.addEventListener("click", async () => {
        const contenidoId = likeButton.getAttribute("data-id");

        try {
            const response = await fetch("/like", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: `contenidoId=${contenidoId}`
            });

            if (response.status === 401 || response.redirected) {
                window.location.href = "/login";
                return;
            }

            const data = await response.json();

            // Actualizar contador
            likeCount.textContent = data.totalLikes;

            // Cambiar icono si se ha dado like o quitado
            if (data.liked) {
                likeIcon.classList.remove("bi-heart");
                likeIcon.classList.add("bi-heart-fill");
            } else {
                likeIcon.classList.remove("bi-heart-fill");
                likeIcon.classList.add("bi-heart");
            }
        } catch (error) {
            console.error("Error al dar like:", error);
        }
    });
});