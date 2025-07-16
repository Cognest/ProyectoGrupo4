window.addEventListener('load', () => {
    // Solo imágenes de la galería que tengan la clase gallery-image
    const imagenes = document.querySelectorAll('.masonry-item img.gallery-image');

    imagenes.forEach(img => {
        const parent = img.parentNode;
        parent.style.position = 'relative';

        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');

        img.onload = () => {
            const width = img.naturalWidth;
            const height = img.naturalHeight;

            canvas.width = width;
            canvas.height = height;

            ctx.drawImage(img, 0, 0);

            ctx.font = '40px Arial';
            ctx.fillStyle = 'rgba(255,255,255,0.4)';
            ctx.textAlign = 'center';
            ctx.textBaseline = 'middle';
            ctx.fillText('MENTOR', width / 2, height / 2);

            canvas.className = img.className;

            img.replaceWith(canvas);
        };

        if (img.complete) {
            img.onload();
        }
    });
});
