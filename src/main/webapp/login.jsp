<%@ include file="header.jsp" %>

<div class="block">
    Welcome to the simple textual quest system!
</div>

<div class="block">
    <form method="post" action="/quest/main">
        Say your name, please
        <input type="text" id="player" name="player" placeholder="Type your name">
        <button type="submit">Continue</button>
    </form>
</div>

<%@ include file="footer.jsp" %>