    <%
    final String errorMessage = (String) session.getAttribute("errorMessage");
    session.removeAttribute("errorMessage");
    if (errorMessage != null) {
    %>
<div class="err_block">
    <%
        out.print(errorMessage);
    %>
</div>
    <%
    }
    %>
</body>
</html>
