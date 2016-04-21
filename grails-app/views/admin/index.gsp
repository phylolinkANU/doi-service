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

<div class="col-sm-12 col-md-9 col-lg-9">
    <h1 class="hidden">Welcome the Atlas of Living Australia</h1>
    <ol class="breadcrumb hidden-print">
        <li><a class="font-xxsmall" href="${grailsApplication.config.grails.serverURL}">Home</a></li>
        <li class="font-xxsmall active">Admin</li>
    </ol>
    <h2 class="heading-medium">DOI Service Administration</h2>
    <div class="span12 panel panel-default" id="page-body" role="main">
        <ul>
            <li><g:link controller="admin" action="mintDoi">Mint a DOI</g:link></li>
        </ul>
    </div>
</div>
</body>
</html>
