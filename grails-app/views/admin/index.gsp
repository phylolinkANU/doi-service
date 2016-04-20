<%--
  Created by IntelliJ IDEA.
  User: mol109
  Date: 13/04/2016
  Time: 6:04 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    %{--<meta name="section" content="home"/>--}%
    <title>DOI Service Administration | ${grailsApplication.config.skin.orgNameLong}</title>
    <r:require modules="doi"/>
</head>
<body>
%{--<div class="nav" role="navigation">--}%
    %{--<ul>--}%
        %{--<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>--}%
    %{--</ul>--}%
%{--</div>--}%

<div class="row-fluid">
    <h2 class="heading-medium">DOI Service Administration</h2>
    <div class="span12 panel panel-default" id="page-body" role="main">

        %{--<h1>DOI Service Administration</h1>--}%
        <ul>
            <li><g:link controller="admin" action="mintDoi">Mint a DOI</g:link></li>
        </ul>
    </div>
</div>
</body>
</html>
