<%@ include file="header.jsp" %>
<%@ page import="org.yt.jr.quest.KnownGames" %>
<%@ page import="org.yt.jr.quest.model.Game" %>

<div class="block">
    Welcome to the simple textual quest system!
</div>

<div class="block">
    <form method="post" action="/quest/main">
        Ok, <%= session.getAttribute("player") %>
        <br>
        Select the game
        <br>
        <select id="game" name="game">
            <%
                for (final String gameName : KnownGames.getInstance().getAllGameNames()) {
                    out.print(String.format("<option value=\"%s\">%s</option>",gameName,gameName));
                }
            %>
        </select>
        <button type="submit">Go!</button>
    </form>
</div>

<%@ include file="footer.jsp" %>