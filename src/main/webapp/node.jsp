<%@ page import="org.yt.jr.quest.model.Node" %>
<%@ page import="org.yt.jr.quest.model.Question" %>
<%@ page import="org.yt.jr.quest.model.Answer" %>
<%@ page import="java.util.List" %>

<%
            final Node node = (Node) request.getAttribute("node");
            final Question question = node.getQuestion();
%>

<%@ include file="header.jsp" %>

<div id="welcome" class="block">
    Dear <b><%= request.getAttribute("player") %></b>
    <br>
    You are playing <b><%= request.getAttribute("gameTitle") %></b>
</div>

<div id="banner" class="block">

    Now you are at <b><%= node.getName() %></b>
    <br>
    <i><%= node.getBanner() %></i>
</div>

<%
    if (!node.isFinal()) {
%>
<div id="questions" class="block">
    <b><%= question.getQuestion() %></b>
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
    }
%>

<%@ include file="footer.jsp" %>