<%@ include file="header.jsp" %>
<%@ page import="org.yt.jr.quest.service.KnownGames" %>
<%@ page import="org.yt.jr.quest.model.Game" %>

<div class="block">
    Welcome to the simple textual quest system!
</div>

<div class="block">
    <form method="post" action="/quest/login">
        Say your name and your game, please
        <input type="text" id="player" name="player" placeholder="Type your name">
        <br>
        <select id="game" name="game">
            <%
                for (final String gameName : KnownGames.getInstance().getNames()) {
                    out.print(String.format("<option value=\"%s\">%s</option>",gameName,gameName));
                }
            %>
        </select>
        <button type="submit">Go!</button>
    </form>
</div>

<%@ include file="footer.jsp" %>