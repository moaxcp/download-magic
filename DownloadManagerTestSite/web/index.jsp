<%-- 
    Document   : index
    Created on : Jun 22, 2011, 5:17:58 PM
    Author     : john
--%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="downloads.Util"%>
<%@page import="penny.download.Downloads"%>
<%@page import="javax.activation.FileDataSource"%>
<%@page import="java.io.File"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1><%= application.getRealPath("/downloads") %></h1>
        <%
            File directory = new File(application.getRealPath("/downloads"));
            List<String> urls = new ArrayList<String>();
            List<String> urlsSmall = new ArrayList<String>();
            for(String s : Util.getAllPaths(directory)) {
                File file = new File(s);
                String start = "/web/downloads/";
                int index = s.indexOf(start) + start.length();
                String print = s.substring(index);
                if(file.length() < 500 * 1014) {
                    urlsSmall.add(print);
                } else {
                    urls.add(print);
                }
            }
            %><h1>urlsSmall</h1><br><%
            for(String s : urlsSmall) {
                %>http://localhost:8084/DownloadManagerTestSite/downloads/<%= s %><br><%
            }
            for(String s : urlsSmall) {
                %>ftp://anonymous@localhost/DownloadManagerTestSite/downloads/<%= s %><br><%
            }
            
            %><h1>urlsLarge</h1><br><%
            for(String s : urls) {
                %>http://localhost:8084/DownloadManagerTestSite/downloads/<%= s %><br><%
            }
            for(String s : urls) {
                %>ftp://anonymous@localhost/DownloadManagerTestSite/downloads/<%= s %><br><%
            }
        %>
    </body>
</html>
