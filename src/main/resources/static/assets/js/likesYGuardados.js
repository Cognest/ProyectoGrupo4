document.addEventListener("DOMContentLoaded", () => {
    // LIKE
    document.querySelectorAll(".like-btn").forEach(button => {
        button.addEventListener("click", async (e) => {
            e.preventDefault();         // Evita que se siga el enlace
            e.stopPropagation();        // Evita que el click se propague al <a>

            const contenidoId = button.getAttribute("data-id");
            const icon = button.querySelector("i");

            try {
                const response = await fetch("/like", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: `contenidoId=${contenidoId}`
                });

                if (response.status === 401 || response.redirected) {
                    window.location.href = "/login";
                    return;
                }

                const data = await response.json();
                icon.className = data.liked ? "bi bi-heart-fill" : "bi bi-heart";
            } catch (err) {
                console.error("Error al dar like:", err);
            }
        });
    });

    // GUARDAR
    document.querySelectorAll(".guardar-btn").forEach(button => {
        button.addEventListener("click", async (e) => {
            e.preventDefault();
            e.stopPropagation();

            const contenidoId = button.getAttribute("data-id");
            const icon = button.querySelector("i");

            try {
                const response = await fetch("/guardar", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: `contenidoId=${contenidoId}`
                });

                if (response.status === 401 || response.redirected) {
                    window.location.href = "/login";
                    return;
                }

                const data = await response.json();
                icon.className = data.guardado ? "bi bi-bookmark-fill" : "bi bi-bookmark";
            } catch (err) {
                console.error("Error al guardar:", err);
            }
        });
    });
});