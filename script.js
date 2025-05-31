let timeRemaining = 10 * 60; // 10 minutos en segundos
let timerInterval;
let currentAcertijo = 0;
const totalAcertijos = 5;

const respuestasCorrectas = {
    acertijo1: "1",
    acertijo2: "3",
    acertijo3: "9",
    acertijo4: "6",
    acertijo5: "ONU"
};

let cifrasObtenidas = [];
let palabraClaveFinal = "";

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
    showScreen('acertijo1');
    currentAcertijo = 1;
    startTimer();
}

function verificarAcertijo(acertijoNum, inputId, feedbackId, respuestaCorrecta, nextAcertijoId) {
    const inputElement = document.getElementById(inputId);
    const feedbackElement = document.getElementById(feedbackId);
    const respuestaUsuario = inputElement.value.trim().toUpperCase();

    if (respuestaUsuario === respuestaCorrecta.toUpperCase()) {
        feedbackElement.textContent = "¡Correcto! Has encontrado parte de la combinación.";
        feedbackElement.className = "feedback correct";
        inputElement.disabled = true;

        if (acertijoNum <= 4) {
            cifrasObtenidas[acertijoNum - 1] = respuestaUsuario;
        } else if (acertijoNum === 5) {
            palabraClaveFinal = respuestaUsuario;
        }
        setTimeout(() => {
            if (acertijoNum < totalAcertijos) {
                currentAcertijo++;
                showScreen(`acertijo${currentAcertijo}`);
            } else {
                showScreen(nextAcertijoId);
            }
        }, 1500);
    } else {
        feedbackElement.textContent = "Respuesta incorrecta. Inténtalo de nuevo.";
        feedbackElement.className = "feedback incorrect";
    }
    inputElement.value = "";
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
        clearInterval(timerInterval);
        setTimeout(() => {
            showScreen('success-screen');
        }, 2000);
    } else {
        feedbackElement.textContent = "Combinación incorrecta. ¡Sigue buscando!";
        feedbackElement.className = "feedback incorrect";
    }
}

function checkEnter(event, functionName) {
    if (event.key === 'Enter') {
        window[functionName](); 
    }
}

document.addEventListener('DOMContentLoaded', () => {
    showScreen('intro-screen');
    updateTimerDisplay();
});