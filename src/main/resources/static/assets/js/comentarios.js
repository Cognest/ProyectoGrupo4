const textarea = document.getElementById("comentario");
const boton = document.getElementById("btn-publicar");
const form = document.getElementById("comentario-form");

// Habilitar/deshabilitar botón si textarea tiene texto
textarea.addEventListener("input", () => {
    boton.disabled = textarea.value.trim().length === 0;
});

// Manejar envío del formulario con AJAX
form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const mensaje = textarea.value.trim();
    const contenidoId = document.getElementById("contenidoId").value;

    if (mensaje.length === 0) return;

    const formData = new URLSearchParams();
    formData.append("contenidoId", contenidoId);
    formData.append("mensaje", mensaje);

    const response = await fetch("/comentarios", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: formData
    });

    if (response.ok) {
        const data = await response.json();

        // Eliminar mensaje de "no hay comentarios"
        const emptyMsg = document.getElementById("no-comments-msg");
        if (emptyMsg) {
            emptyMsg.remove();
        }

        // Añadir comentario
        const commentContainer = document.createElement("div");
        commentContainer.className = "comment-card p-3 mb-3 d-flex";

        commentContainer.innerHTML = `
    <img src="${data.avatar}" class="avatar me-3" alt="Avatar">
    <div>
      <div>
        <span class="fw-bold">${data.nickname}</span>
        <small class="text-muted">${new Date(data.fecha).toLocaleString()}</small>
      </div>
      <p class="mb-0">${data.mensaje}</p>
    </div>
  `;
        document.querySelector(".listaComentarios").appendChild(commentContainer);

        textarea.value = "";
        boton.disabled = true;

    } else if (response.status === 401 || response.redirected) {
        // No autenticado, redirigir a login
        window.location.href = "/login";
    } else {
        alert("Error al publicar comentario.");
    }

});