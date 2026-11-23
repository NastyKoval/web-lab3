"use strict";

const canvas = document.getElementById("plane");
const ctx = canvas.getContext("2d");
const C = 200, U = 120;            // центр canvas, длина R в px
const X_MIN = -5, X_MAX = 3, Y_MIN = -5, Y_MAX = 3;
const NUMBER = /^-?\d+(\.\d+)?$/;

// доли R -> пиксели
const px = u => C + u * U;
const py = u => C - u * U;

function currentR() {
  const box = document.querySelector('#r-group input:checked');
  return box ? Number(box.value) : null;
}

// точки из таблицы результатов
function savedPoints() {
  return [...document.querySelectorAll("#results-body tr")].map(tr => ({
    x: Number(tr.dataset.x), y: Number(tr.dataset.y),
    r: Number(tr.dataset.r), hit: tr.dataset.hit === "true"
  }));
}

function draw() {
  const r = currentR();
  ctx.clearRect(0, 0, 400, 400);

  ctx.fillStyle = "rgba(22, 181, 181, 0.30)";
  ctx.fillRect(px(0), py(0.5), U, U / 2);              // прямоугольник

  ctx.beginPath();                                     // треугольник
  ctx.moveTo(px(0), py(0)); ctx.lineTo(px(0.5), py(0)); ctx.lineTo(px(0), py(-1));
  ctx.fill();

  ctx.beginPath();                                     // четверть круга
  ctx.moveTo(C, C); ctx.arc(C, C, U / 2, 0.5 * Math.PI, Math.PI); ctx.fill();

  // оси и подписи
  ctx.strokeStyle = ctx.fillStyle = "#7a2447";
  ctx.beginPath();
  ctx.moveTo(0, C); ctx.lineTo(400, C); ctx.moveTo(C, 0); ctx.lineTo(C, 400);
  ctx.stroke();
  ctx.font = "12px Arial";
  ctx.fillText("x", 388, C + 14); ctx.fillText("y", C + 5, 12);
  [["R", 1], ["R/2", 0.5], ["-R/2", -0.5], ["-R", -1]].forEach(([t, u]) => {
    const label = r ? String(u * r) : t;
    ctx.fillText(label, px(u) - 8, C + 16);
    ctx.fillText(label, C + 6, py(u) + 4);
  });

  // точки: масштаб выбранного R, иначе своего
  savedPoints().forEach(p => {
    const scale = r || p.r;
    ctx.beginPath();
    ctx.arc(px(p.x / scale), py(p.y / scale), 4, 0, 2 * Math.PI);
    ctx.fillStyle = p.hit ? "#2e7d32" : "#b3121f";
    ctx.fill();
  });
}

// R: можно отметить только один
document.getElementById("r-group").addEventListener("change", e => {
  if (e.target.checked) {
    document.querySelectorAll("#r-group input").forEach(b => { if (b !== e.target) b.checked = false; });
  }
  document.getElementById("click-msg").textContent = "";
  draw();
});

// проверка формы
function validate() {
  const errors = [];
  const xs = document.querySelectorAll("#x-group input:checked");
  const yInput = document.getElementById("y-value");
  const y = yInput.value.trim().replace(",", ".");
  yInput.value = y;

  if (xs.length === 0) errors.push("Выберите хотя бы одно значение X.");
  if (y === "") errors.push("Введите Y.");
  else if (!NUMBER.test(y)) errors.push("Y должно быть числом, например 1.5.");
  else if (Number(y) <= Y_MIN || Number(y) >= Y_MAX) errors.push("Y должно быть строго между -5 и 3.");
  if (currentR() === null) errors.push("Выберите R.");
  return errors;
}

document.getElementById("point-form").addEventListener("submit", e => {
  const errors = validate();
  if (errors.length) {
    e.preventDefault();
    document.getElementById("error-box").textContent = errors.join("\n");
  }
});

document.getElementById("point-form").addEventListener("reset", () => {
  document.getElementById("error-box").textContent = "";
  setTimeout(draw);   // после сброса чекбоксов
});

// клик по графику -> координаты -> запрос
canvas.addEventListener("click", e => {
  const msg = document.getElementById("click-msg");
  const r = currentR();
  if (r === null) {
    msg.textContent = "Нельзя определить координаты: сначала выберите R.";
    return;
  }

  const cx = e.offsetX * canvas.width / canvas.clientWidth;
  const cy = e.offsetY * canvas.height / canvas.clientHeight;
  const x = Number(((cx - C) / U * r).toFixed(3));
  const y = Number(((C - cy) / U * r).toFixed(3));

  if (x < X_MIN || x > X_MAX || y <= Y_MIN || y >= Y_MAX) {
    msg.textContent = `Точка (${x}; ${y}) вне допустимых значений: X ∈ [-5; 3], Y ∈ (-5; 3).`;
    return;
  }
  window.location.href = "controller?" + new URLSearchParams({ x, y, r });
});

draw();
