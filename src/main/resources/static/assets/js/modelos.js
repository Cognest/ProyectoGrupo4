
    import * as THREE from 'https://esm.sh/three@0.160.0';
    import { GLTFLoader } from 'https://esm.sh/three@0.160.0/examples/jsm/loaders/GLTFLoader.js';
    import { OrbitControls } from 'https://esm.sh/three@0.160.0/examples/jsm/controls/OrbitControls.js';

    document.querySelectorAll(".visor3d-container").forEach(container => {
    const img = container.querySelector(".preview-img");
    const visor = container.querySelector(".visor3d");
    const modeloRuta = img.dataset.model;


    // Escena y cámara para la preview
    const scene = new THREE.Scene();
    scene.background = null;

    const camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000);
    const angle = 150;
    const distance = 2;

    camera.position.x = Math.sin(angle) * 10;
    camera.position.z = Math.cos(angle) * distance;
    camera.position.y = 0.5;

    camera.lookAt(0, 0, 0);



    const renderer = new THREE.WebGLRenderer({ alpha: true, antialias: true, preserveDrawingBuffer: true });
    renderer.setSize(300, 300);

    const ambient = new THREE.AmbientLight(0xffffff, 0.8);
    scene.add(ambient);
    const directional = new THREE.DirectionalLight(0xffffff, 0.8);
    directional.position.set(3, 5, 2);
    scene.add(directional);

    const loader = new GLTFLoader();
    loader.load(
    modeloRuta,
    gltf => {
    const model = gltf.scene;
    scene.add(model);
    console.log("Model loaded:", model);



    // Asegurarse de que todos los nodos hijos están actualizados
    model.updateMatrixWorld(true);

    // Calcular caja de límites
    const box = new THREE.Box3().setFromObject(model);
    const center = box.getCenter(new THREE.Vector3());
    model.position.sub(center);

    const size = box.getSize(new THREE.Vector3()).length();
    const scaleFactor = (4 / size) * 1.5;
    model.scale.setScalar(scaleFactor);


    // Probar render sin gradiente
    let framesRendered = 0;

        function renderLoop() {
            const previewSize = 600; // Mayor resolución para más calidad

            // Ajustar renderer al nuevo tamaño
            renderer.setSize(previewSize, previewSize);

            // Renderizar la escena
            renderer.render(scene, camera);

            // Crear canvas 2D de alta resolución
            const gradientCanvas = document.createElement("canvas");
            gradientCanvas.width = previewSize;
            gradientCanvas.height = previewSize;
            const ctx = gradientCanvas.getContext("2d");

            // Dibuja gradiente de fondo en alta resolución
            const gradient = ctx.createLinearGradient(0, 0, 0, previewSize);
            gradient.addColorStop(0.5, "rgba(10,11,31,0.9)");
            gradient.addColorStop(0.7, "rgb(10,11,31)");
            ctx.fillStyle = gradient;
            ctx.fillRect(0, 0, previewSize, previewSize);

            // Dibujar el render del modelo encima
            ctx.drawImage(renderer.domElement, 0, 0, previewSize, previewSize);

            // Convertir a imagen base64 y asignar al <img>
            const dataURL = gradientCanvas.toDataURL("image/png");
            img.src = dataURL;
        }


        renderLoop();




},
    undefined,
    error => console.error("Error cargando modelo:", error)
    );


    img.addEventListener("click", () => {
    img.style.display = "none";
    visor.style.display = "block";
    iniciarVisor3D(visor, modeloRuta);
});
});

    function iniciarVisor3D(container, modeloRuta) {
    const scene = new THREE.Scene();

    const camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000);
    camera.position.set(0, 2, 10);
    camera.lookAt(0, 0, 0);

    const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setSize(container.offsetWidth, container.offsetHeight, false);
    renderer.domElement.style.width = "100%";
    renderer.domElement.style.height = "100%";
    container.appendChild(renderer.domElement);

    const controls = new OrbitControls(camera, renderer.domElement);
    controls.enableDamping = true;
    controls.dampingFactor = 0.05;
    controls.zoomSpeed = 0.5;
    controls.enabled = false;

    const gridHelper = new THREE.GridHelper(20, 20, 0xffffff, 0xffffff);
    gridHelper.material.opacity = 0.3;
    gridHelper.material.transparent = true;
    scene.add(gridHelper);

    const ambientLight = new THREE.AmbientLight(0xffffff, 0.8);
    scene.add(ambientLight);
    const directionalLight = new THREE.DirectionalLight(0xffffff, 1);
    directionalLight.position.set(10, 20, 10);
    scene.add(directionalLight);

    const clock = new THREE.Clock();
    let animationProgress = 0;
    const animationDuration = 2.0;
    let animating = true;

    const loader = new GLTFLoader();
    loader.load(
    modeloRuta,
    gltf => {
    const modelo = gltf.scene;
    modelo.updateMatrixWorld(true);
    scene.add(modelo);

    const box = new THREE.Box3().setFromObject(modelo);
    const center = new THREE.Vector3();
    box.getCenter(center);
    modelo.position.sub(center);

    const size = box.getSize(new THREE.Vector3()).length();
    const scaleFactor = (4 / size) * 1.5;
    modelo.scale.setScalar(scaleFactor);

    const boxAfterScale = new THREE.Box3().setFromObject(modelo);
    const centerAfterScale = new THREE.Vector3();
    boxAfterScale.getCenter(centerAfterScale);
    modelo.position.sub(centerAfterScale);

    animate();
},
    undefined,
    error => console.error("Error cargando modelo:", error)
    );

    window.addEventListener('resize', () => {
    const width = container.clientWidth;
    const height = container.clientHeight;
    camera.aspect = width / height;
    camera.updateProjectionMatrix();
    renderer.setSize(width, height);
});

    function animate() {
    requestAnimationFrame(animate);

    if (animating) {
    animationProgress += clock.getDelta();
    const t = Math.min(animationProgress / animationDuration, 1);

    const easeInOut = t => t * t * (3 - 2 * t);
    const tSmooth = easeInOut(t);

    const angle = THREE.MathUtils.lerp(0, Math.PI / 9, tSmooth);
    const distance = THREE.MathUtils.lerp(10, 6, tSmooth);

    camera.position.x = Math.sin(angle) * distance;
    camera.position.z = Math.cos(angle) * distance;
    camera.position.y = 2;
    camera.lookAt(0, 0, 0);

    if (t >= 1) {
    animating = false;
    controls.enabled = true;
}
} else {
    controls.update();
}

    renderer.render(scene, camera);
}
}

