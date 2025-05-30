let timeRemaining = 60 * 60; // 60 minutos en segundos
let timerInterval;
let currentAcertijo = 0; // 0 para la pantalla de inicio, 1 para el primer acertijo, etc.
const totalAcertijos = 5; // Número de acertijos antes del final

const respuestasCorrectas = {
    acertijo1: "1",
    acertijo2: "3",
    acertijo3: "9",
    acertijo4: "6",
    acertijo5: "ONU"
};

let cifrasObtenidas = []; // Para las 4 primeras cifras
let palabraClaveFinal = ""; // Para la última palabra clave

// --- Funciones de control de pantalla ---
function showScreen(screenId) {
    const screens = document.querySelectorAll('.screen');
    screens.forEach(screen => {
        screen.classList.remove('active');
    });
    document.getElementById(screenId).classList.add('active');
}

function updateTimerDisplay() {
    const minutes = Math.floor(timeRemaining / 60);
    const seconds = timeRemaining % 60;
    document.getElementById('countdown').textContent = 
        `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
}

function startTimer() {
    timerInterval = setInterval(() => {
        timeRemaining--;
        updateTimerDisplay();
        if (timeRemaining <= 0) {
            clearInterval(timerInterval);
            showScreen('game-over-screen');
        }
    }, 1000);
}

function startGame() {
    showScreen('acertijo1'); // Muestra el primer acertijo
    currentAcertijo = 1;
    startTimer(); // Inicia el cronómetro
}

// --- Funciones de verificación de acertijos ---

function verificarAcertijo(acertijoNum, inputId, feedbackId, respuestaCorrecta, nextAcertijoId) {
    const inputElement = document.getElementById(inputId);
    const feedbackElement = document.getElementById(feedbackId);
    const respuestaUsuario = inputElement.value.trim().toUpperCase();

    if (respuestaUsuario === respuestaCorrecta.toUpperCase()) {
        feedbackElement.textContent = "¡Correcto! Has encontrado parte de la combinación.";
        feedbackElement.className = "feedback correct";
        inputElement.disabled = true; // Deshabilita el input una vez correcta

        if (acertijoNum <= 4) {
            cifrasObtenidas[acertijoNum - 1] = respuestaUsuario; // Guardar la cifra
        } else if (acertijoNum === 5) {
            palabraClaveFinal = respuestaUsuario; // Guardar la palabra clave
        }
        
        // Muestra el siguiente acertijo después de un breve retraso
        setTimeout(() => {
            if (acertijoNum < totalAcertijos) {
                currentAcertijo++;
                showScreen(`acertijo${currentAcertijo}`);
            } else {
                showScreen(nextAcertijoId); // Muestra la pantalla final del acertijo si todos están resueltos
            }
        }, 1500); // Espera 1.5 segundos antes de avanzar
    } else {
        feedbackElement.textContent = "Respuesta incorrecta. Inténtalo de nuevo.";
        feedbackElement.className = "feedback incorrect";
    }
    inputElement.value = ""; // Limpia el input para el siguiente intento
}

function verificarAcertijo1() {
    verificarAcertijo(1, 'respuesta1', 'feedback1', respuestasCorrectas.acertijo1, 'acertijo2');
}

function verificarAcertijo2() {
    verificarAcertijo(2, 'respuesta2', 'feedback2', respuestasCorrectas.acertijo2, 'acertijo3');
}

function verificarAcertijo3() {
    verificarAcertijo(3, 'respuesta3', 'feedback3', respuestasCorrectas.acertijo3, 'acertijo4');
}

function verificarAcertijo4() {
    verificarAcertijo(4, 'respuesta4', 'feedback4', respuestasCorrectas.acertijo4, 'acertijo5');
}

function verificarAcertijo5() {
    verificarAcertijo(5, 'respuesta5', 'feedback5', respuestasCorrectas.acertijo5, 'final-acertijo');
}


function verificarCombinacionFinal() {
    const inputElement = document.getElementById('respuestaFinal');
    const feedbackElement = document.getElementById('feedbackFinal');
    const respuestaUsuario = inputElement.value.trim().toUpperCase();

    const combinacionEsperada = cifrasObtenidas.join("") + palabraClaveFinal;

    if (respuestaUsuario === combinacionEsperada) {
        feedbackElement.textContent = "¡Combinación Correcta! La caja se abre...";
        feedbackElement.className = "feedback correct";
        clearInterval(timerInterval); // Detiene el cronómetro al resolver
        setTimeout(() => {
            showScreen('success-screen'); // Muestra la pantalla de éxito
        }, 2000);
    } else {
        feedbackElement.textContent = "Combinación incorrecta. ¡Sigue buscando!";
        feedbackElement.className = "feedback incorrect";
    }
}

// Función para permitir Enter en los campos de texto
function checkEnter(event, functionName) {
    if (event.key === 'Enter') {
        // Ejecuta la función de verificación correspondiente
        window[functionName](); 
    }
}

// Inicializa la pantalla de inicio al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    showScreen('intro-screen');
    updateTimerDisplay(); // Asegura que el tiempo inicial se muestre correctamente
});
