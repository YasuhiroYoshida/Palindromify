<%@ page import="java.util.*" %>
<html>
<head>
    <title>Palindromify</title>
</head>
<body>
<table border="0">
  <tr><td align="center" colspan="2"><font size="+2">Palindromify</font></td></tr>
  <tr><td align="center" colspan="2"><font size="+1">Hall of Fame [Total Score]</font></td></tr>
<% 
int listSize = ocjp6.Palindrome.records.size();
int toIndex = 5 < listSize ? 5 : listSize;

if (toIndex != 0 && ocjp6.Palindrome.records.get(0)[0] != "") {
  List<String[]> subList = ocjp6.Palindrome.records.subList(0, toIndex);
  for (int i = 0; i < subList.size(); i++) { 
%>
<tr><td align="left"><%= subList.get(i)[0] %></td><td align="right"> <%= subList.get(i)[2] %> pts</td></tr>
<%
  }
} 
%>
<tr><td align="right"><br></td></tr>
<tr><td align="center" colspan="2"><font size="+1">Hall of Fame [Highest Score]</font></td></tr>
<% 
int toIndexH = toIndex;

if (toIndexH != 0 && ocjp6.Palindrome.recordsH.get(0)[0] != "") {
  List<String[]> subListH = ocjp6.Palindrome.recordsH.subList(0, toIndex);
  for (int i = 0; i < subListH.size(); i++) { 
%>
<tr><td align="left"><%= subListH.get(i)[0] %></td><td align="right"> <%= subListH.get(i)[3] %> pts</td></tr>
<%
  }
} 
%>
<tr><td valign="top" align="center" colspan="2">
  <br>
  <input type="button" value="Try Again" onClick="location.href='<%= ocjp6.Palindrome.contextPath %>/Palindrome'">
</td></tr>
</table>
</body>
</html>