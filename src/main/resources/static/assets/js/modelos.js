import * as THREE from 'https://esm.sh/three@0.160.0';
import { GLTFLoader } from 'https://esm.sh/three@0.160.0/examples/jsm/loaders/GLTFLoader.js';
import { OrbitControls } from 'https://esm.sh/three@0.160.0/examples/jsm/controls/OrbitControls.js';



// Seleccionar todas las previews
document.querySelectorAll(".preview-img").forEach((img) => {
    img.addEventListener("click", () => {
        // Ocultar la imagen preview
        img.style.display = "none";

        // Mostrar el visor asociado
        const visor = img.parentElement.querySelector(".visor3d");
        visor.style.display = "block";

        // Ruta del modelo (desde data-model)
        const modeloRuta = img.dataset.model;

        // Iniciar visor en este contenedor
        iniciarVisor3D(visor, modeloRuta);
    });
});

function iniciarVisor3D(container, modeloRuta) {

    console.log("Iniciando visor en este contenedor:", container);
    console.log("Cargando modelo desde:", modeloRuta);

    // Escena
    const scene = new THREE.Scene();


    // Renderizador transparente
    const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setSize(container.offsetWidth, container.offsetHeight, false);
    renderer.domElement.style.width = "100%";
    renderer.domElement.style.height = "100%";
    container.appendChild(renderer.domElement);

    // Cámara
    const camera = new THREE.PerspectiveCamera(
        75,
        container.clientWidth / container.clientHeight,
        0.1,
        1000
    );
    camera.position.set(0, 2, 10);
    camera.lookAt(0, 0, 0);


    // Controles
    const controls = new OrbitControls(camera, renderer.domElement);
    controls.enableDamping = true;
    controls.dampingFactor = 0.05;
    controls.zoomSpeed = 0.5;
    controls.enabled = false; // desactivado hasta que termine la animación

    // Crear un GridHelper como suelo
    const gridHelper = new THREE.GridHelper(20, 20, 0xffffff, 0xffffff)
    gridHelper.material.opacity = 0.3;       // Transparencia
    gridHelper.material.transparent = true;  // Habilitar transparencia
    scene.add(gridHelper);

    // Luces
    const ambientLight = new THREE.AmbientLight(0xffffff, 0.8);
    scene.add(ambientLight);

    const directionalLight = new THREE.DirectionalLight(0xffffff, 1);
    directionalLight.position.set(10, 20, 10);
    scene.add(directionalLight);

    // Reloj y variables de animación
    const clock = new THREE.Clock();
    let animationProgress = 0;
    const animationDuration = 2.0;
    let animating = true;

    // Cargar modelo
    const loader = new GLTFLoader();
    loader.load(modeloRuta,
        function (gltf) {
            console.log("Modelo cargado:", modeloRuta);
            const modelo = gltf.scene;
            modelo.updateMatrixWorld(true);
            scene.add(modelo);

            const box = new THREE.Box3().setFromObject(modelo);
            const center = new THREE.Vector3();
            box.getCenter(center);
            modelo.position.sub(center);

            // Escalar el modelo
            const size = box.getSize(new THREE.Vector3()).length();
            const scaleFactor = (4 / size) * 1.5;
            modelo.scale.setScalar(scaleFactor);

            // Volver a centrar tras el escalado
            const boxAfterScale = new THREE.Box3().setFromObject(modelo);
            const centerAfterScale = new THREE.Vector3();
            boxAfterScale.getCenter(centerAfterScale);
            modelo.position.sub(centerAfterScale);
        },
        undefined,
        function (error) {
            console.error('Error al cargar el modelo:', error);
        }
    );

    // Redimensionamiento
    window.addEventListener('resize', () => {
        const width = container.clientWidth;
        const height = container.clientHeight;
        camera.aspect = width / height;
        camera.updateProjectionMatrix();
        renderer.setSize(width, height);
    });

    // Animación
    function animate() {
        requestAnimationFrame(animate);

        if (animating) {
            animationProgress += clock.getDelta();
            const t = Math.min(animationProgress / animationDuration, 1);

            const easeInOut = t => t * t * (3 - 2 * t);
            const tSmooth = easeInOut(t);

            const angle = THREE.MathUtils.lerp(0, Math.PI / 9, tSmooth); // hacia la derecha
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

    animate();
}
