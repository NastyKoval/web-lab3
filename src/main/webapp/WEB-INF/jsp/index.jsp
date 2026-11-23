<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- история из сессии (создаётся, если нет) --%>
<jsp:useBean id="history" class="lab3.model.ResultHistory" scope="session"/>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Лабораторная работа №3</title>
  <link rel="stylesheet" href="css/styles.css">
  <link rel="stylesheet" href="css/header.css">
</head>
<body>

<header>
  <h1 class="page-title">Определение попадания точки в область</h1>
  <p class="student-info">Ковалева Анастасия Петровна</p>
  <p class="student-info">Группа P3213 · Вариант ???</p>
</header>

<table class="layout">
  <tr>
    <td class="cell-canvas">
      <h2>Координатная плоскость</h2>
      <canvas id="plane" width="400" height="400"></canvas>
      <p id="click-msg" class="error"></p>
    </td>
    <td class="cell-form">
      <h2>Ввод данных</h2>
      <form id="point-form" class="form-area" action="controller" method="get">

        <p class="field-label">Координата X (можно несколько):</p>
        <div id="x-group" class="check-group">
          <c:forTokens items="-5,-4,-3,-2,-1,0,1,2,3" delims="," var="v">
            <label><input type="checkbox" name="x" value="${v}">${v}</label>
          </c:forTokens>
        </div>

        <p class="field-label"><label for="y-value">Координата Y (от -5 до 3):</label></p>
        <input type="text" id="y-value" name="y" placeholder="например, 1.5"
               autocomplete="off" maxlength="30">

        <p class="field-label">Радиус R:</p>
        <div id="r-group" class="check-group">
          <%-- отмечен R последней проверки --%>
          <c:forTokens items="1,2,3,4,5" delims="," var="v">
            <label><input type="checkbox" name="r" value="${v}"
                ${v == history.lastR ? 'checked' : ''}>${v}</label>
          </c:forTokens>
        </div>

        <p id="error-box" class="error"></p>
        <div class="controls">
          <button type="submit" class="btn-primary">Проверить</button>
          <button type="reset" class="btn-secondary">Сбросить</button>
        </div>
      </form>
    </td>
  </tr>
</table>

<table class="results">
  <caption>Результаты проверок</caption>
  <thead>
    <tr><th>X</th><th>Y</th><th>R</th><th>Результат</th>
        <th>Время запроса</th><th>Время работы, мкс</th></tr>
  </thead>
  <tbody id="results-body">
    <%-- data-* читает script.js для отрисовки точек --%>
    <c:forEach var="row" items="${history.results}">
      <tr data-x="${row.x}" data-y="${row.y}" data-r="${row.r}" data-hit="${row.hit}">
        <td>${row.x}</td>
        <td>${row.y}</td>
        <td>${row.r}</td>
        <td class="${row.hit ? 'hit' : 'miss'}">${row.hit ? 'Попадание' : 'Промах'}</td>
        <td>${row.time}</td>
        <td>${row.execMicros}</td>
      </tr>
    </c:forEach>
  </tbody>
</table>

<script src="js/script.js"></script>
</body>
</html>
