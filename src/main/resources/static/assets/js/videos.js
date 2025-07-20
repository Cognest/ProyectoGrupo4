document.addEventListener("DOMContentLoaded", function () {
    const items = document.querySelectorAll(".preview-item[data-type='video']");

    items.forEach(item => {
        const src = item.dataset.src;
        const previewImg = item.querySelector(".preview-img");
        const video = item.querySelector("video");

        // --- Poster base64 desde primer frame del video
        const videoTemp = document.createElement("video");
        videoTemp.src = src;
        videoTemp.crossOrigin = "anonymous";
        videoTemp.preload = "metadata";
        videoTemp.muted = true;
        videoTemp.playsInline = true;

        videoTemp.addEventListener("loadeddata", () => {
            videoTemp.currentTime = 0.1;
        });

        videoTemp.addEventListener("seeked", () => {
            const canvas = document.createElement("canvas");
            canvas.width = videoTemp.videoWidth;
            canvas.height = videoTemp.videoHeight;
            const ctx = canvas.getContext("2d");
            ctx.drawImage(videoTemp, 0, 0, canvas.width, canvas.height);
            const dataURL = canvas.toDataURL("image/png");
            previewImg.src = dataURL;
        });

        // --- Reproducir video en hover
        item.addEventListener("mouseenter", () => {
            video.currentTime = 0;
            video.play();
        });

        item.addEventListener("mouseleave", () => {
            video.pause();
            video.currentTime = 0;
        });

        // --- Evitar que el botón bloquee el enlace
        const playBtn = item.querySelector(".play-button");
        if (playBtn) {
            playBtn.addEventListener("click", e => {
                e.stopPropagation(); // evita que bloquee el enlace
                // Opcional: podrías activar un modal o algo si quieres
            });
        }
    });
});
