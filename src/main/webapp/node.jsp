<%@ page import="org.yt.jr.quest.ActiveGames" %>
<%@ page import="org.yt.jr.quest.GameInstance" %>
<%@ page import="org.yt.jr.quest.model.Game" %>
<%@ page import="org.yt.jr.quest.model.Node" %>
<%@ page import="org.yt.jr.quest.model.Question" %>
<%@ page import="org.yt.jr.quest.model.Answer" %>
<%@ page import="java.util.List" %>

<%
            final String player = (String) session.getAttribute("player");
            final GameInstance gameInstance = ActiveGames.ACTIVE_GAMES.findGame(player).get();
            final Game game = gameInstance.getGame();
            final String gameTitle = game.getTitle();

            final Node node = gameInstance.getCurrentNode();
            final Question question = node.getQuestion();
%>

<%@ include file="header.jsp" %>

<div id="welcome" class="block">
    Dear <b><%= player %></b>
    <br>
    You are playing <b><%= gameTitle %></b>
</div>

<%
    if (game.getStartNode().equals(node)) {
        final String gameIntro = game.getIntro();
%>
<div id="intro" class="block">
    <%= gameIntro %>
</div>
<%
    }
%>

<%
    if (gameInstance.getMessage() != null) {
%>
<div id="message" class="message" color="pink">
    <i><%= gameInstance.getMessage() %></i>
</div>
<%
    }
%>

<div id="banner" class="block" color="green">

    Now you are at <b><%= node.getName() %></b>

    <br>
    <i><%= node.getBanner() %></i>
</div>

<%
    if (!node.isFinal()) {
%>
<div id="questions" class="block">
    <b><%= question.getQuestionText() %></b>
    <br>
    <%
       for (final Answer a : question.getAnswers()) {
            out.print(String.format("<a href=/quest/main?node=%s>%s</a><br>",
                a.getNextNode().getId(),
                a.getAnswer()));
       }
    %>
</div>
<%
    } else {
    ActiveGames.ACTIVE_GAMES.finishGame(gameInstance);
%>
<div id="final" class="block">
    You are finishing this game!

    <br>
    <a href="/quest/select-game.jsp">Please, click here to go to the start and select new game!</a>
</div>
<%
    }
%>

<%@ include file="footer.jsp" %>