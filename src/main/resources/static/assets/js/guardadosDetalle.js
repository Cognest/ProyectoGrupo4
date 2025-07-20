document.addEventListener("DOMContentLoaded", () => {
    const saveButton = document.getElementById("save-button");
    const saveIcon = document.getElementById("save-icon");

    saveButton.addEventListener("click", async () => {
        const contenidoId = saveButton.getAttribute("data-id");

        try {
            const response = await fetch("/guardar", {
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

            if (data.guardado) {
                saveIcon.classList.remove("bi-bookmark");
                saveIcon.classList.add("bi-bookmark-fill");
            } else {
                saveIcon.classList.remove("bi-bookmark-fill");
                saveIcon.classList.add("bi-bookmark");
            }

        } catch (error) {
            console.error("Error al guardar:", error);
        }
    });
});