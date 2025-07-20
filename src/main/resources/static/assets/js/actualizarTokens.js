const tokenCounter = document.getElementById("token-counter");

fetch("/api/tokens")
    .then(res => {
        if (!res.ok) throw new Error("No autenticado");
        return res.text(); // Si devuelves JSON, usa .json()
    })
    .then(tokens => {
        tokenCounter.textContent = " " + tokens + " Tk";
    })
    .catch(err => {
        console.error("Error actualizando tokens:", err);
    });