// Almacena todos los reproductores
const players = document.querySelectorAll(".music-player");

// Función para formatear tiempo mm:ss
function formatTime(seconds) {
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return mins + ":" + (secs < 10 ? "0" : "") + secs;
}

players.forEach(player => {

    const audio = player.querySelector("audio");
    let previousVolume = audio.volume;
    const playPauseBtn = player.querySelector(".play-pause");
    const progressContainer = player.querySelector(".progress-container");
    const progress = player.querySelector(".progress");
    const currentTimeEl = player.querySelector(".current-time");
    const durationEl = player.querySelector(".duration");
    const volumeSlider = player.querySelector(".volume-slider");

    const muteBtn = player.querySelector(".volume-toggle");

    // Guardamos el volumen anterior al mutear
    // Guardamos el volumen anterior al mutear

    muteBtn.addEventListener("click", () => {
        if (audio.muted) {
            // Desmutear y aplicar el volumen guardado
            audio.muted = false;
            audio.volume = previousVolume;
            muteBtn.innerHTML = '<i class="fa fa-volume-up"></i>';
            volumeSlider.value = previousVolume;
            volumeSlider.style.setProperty('--vol', previousVolume * 100 + '%');
        } else {
            // Mutear sin cambiar el volumen
            previousVolume = audio.volume;
            audio.muted = true;
            muteBtn.innerHTML = '<i class="fa fa-volume-off"></i>';
        }
    });






    // Inicializar duración cuando metadata cargue
    audio.addEventListener("loadedmetadata", () => {
        durationEl.textContent = formatTime(audio.duration);
    });

    // Play/Pause
    playPauseBtn.addEventListener("click", () => {
        if(audio.paused) {
            audio.play();
            playPauseBtn.classList.remove("play");
            playPauseBtn.classList.add("pause");
        } else {
            audio.pause();
            playPauseBtn.classList.remove("pause");
            playPauseBtn.classList.add("play");
        }
    });

    // Actualizar barra y tiempos
    audio.addEventListener("timeupdate", () => {
        if(audio.duration) {
            const progressPercent = (audio.currentTime / audio.duration) * 100;
            progress.style.width = progressPercent + "%";
            currentTimeEl.textContent = formatTime(audio.currentTime);
        }
    });

    // Click en barra para cambiar tiempo
    progressContainer.addEventListener("click", (e) => {
        const width = progressContainer.clientWidth;
        const clickX = e.offsetX;
        const duration = audio.duration;
        audio.currentTime = (clickX / width) * duration;
    });

    // Volumen
    volumeSlider.addEventListener("input", (e) => {
        const newVolume = parseFloat(e.target.value);

        if (audio.muted) {
            // Guardamos el nuevo volumen pero no lo aplicamos aún
            previousVolume = newVolume;
        } else {
            // Aplicamos el volumen directamente
            audio.volume = newVolume;
            previousVolume = newVolume;
        }

        // Actualizamos visualmente la barra de volumen
        volumeSlider.style.setProperty('--vol', newVolume * 100 + '%');
    });



    // Pausar otros audios cuando uno empiece a reproducirse
    audio.addEventListener("play", () => {
        players.forEach(p => {
            if(p !== player) {
                const a = p.querySelector("audio");
                a.pause();
                const btn = p.querySelector(".play-pause");
                btn.classList.remove("pause");
                btn.classList.add("play");
            }
        });
    });
    audio.addEventListener("ended", () => {
        // Reinicia la barra de progreso
        progress.style.width = "0%";
        currentTimeEl.textContent = "0:00";

        // Vuelve a poner el botón en modo 'play'
        playPauseBtn.classList.remove("pause");
        playPauseBtn.classList.add("play");

        // Opcional: rebobina el audio
        audio.currentTime = 0;
    });

});