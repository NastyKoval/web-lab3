<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Результат проверки</title>
  <link rel="stylesheet" href="css/styles.css">
  <link rel="stylesheet" href="css/header.css">
</head>
<body>

<header>
  <h1 class="page-title">Результат проверки</h1>
  <p class="student-info">Ковалева Анастасия Петровна</p>
  <p class="student-info">Группа P3213 · Вариант ???</p>
</header>

<div class="card">
  <c:choose>
    <%-- errors / results задаёт AreaCheckServlet --%>
    <c:when test="${not empty errors}">
      <h2>Данные не прошли проверку</h2>
      <ul class="error-list">
        <c:forEach var="e" items="${errors}">
          <li>${e}</li>
        </c:forEach>
      </ul>
    </c:when>
    <c:otherwise>
      <h2>Полученные параметры</h2>
      <table class="results">
        <thead>
          <tr><th>X</th><th>Y</th><th>R</th><th>Результат</th>
              <th>Время запроса</th><th>Время работы, мкс</th></tr>
        </thead>
        <tbody>
          <c:forEach var="row" items="${results}">
            <tr>
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
    </c:otherwise>
  </c:choose>

  <p><a class="btn-primary back-link" href="controller">← Вернуться к форме</a></p>
</div>

</body>
</html>
