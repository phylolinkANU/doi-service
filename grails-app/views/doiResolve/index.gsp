<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>ALA DOI Repository</title>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    %{-- Google Analytics --}%
    <script>
        window.ga=window.ga||function(){(ga.q=ga.q||[]).push(arguments)};ga.l=+new Date;
        ga('create', '${grailsApplication.config.googleAnalyticsId}', 'auto');
        ga('send', 'pageview');
    </script>
    <script async src='//www.google-analytics.com/analytics.js'></script>
    %{--End Google Analytics--}%

    <r:require modules="doi"/>
</head>

<body>
<g:include view="common/_envWarning.gsp"/>

<div class="panel">
    <div class="panel-heading">
        <h1>ALA DOI Repository</h1>
    </div>
    <div class="panel-body">
        <g:if test="${dois.list}">
            <div class="pull-right">Showing ${offset + 1} to ${Math.min(dois.totalCount, offset + pageSize)} of ${dois.totalCount}</div>
            <div class="clearfix clear"></div>
            <div class="table-responsive">
                <table class="doi-table">
                    <thead>
                    <th width="25%">DOI</th>
                    <th width="25%">Title</th>
                    <th width="25%">Author(s)</th>
                    <th width="20%">Date</th>
                    <th width="5%"></th>
                    </thead>
                    <tbody>
                    <g:each in="${dois}" var="doi">
                        <tr>
                            <td>${doi.doi}</td>
                            <td>${doi.title}</td>
                            <td>${doi.authors}</td>
                            <td>${doi.dateMinted}</td>
                            <td><a href="${request.contextPath}/doi/${doi.uuid}" title="View DOI">View</a></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="padding-top-1">
                <g:if test="${offset > 0}">
                    <a class="btn btn-sm btn-primary" href="${request.contextPath}?offset=${offset - pageSize}&pageSize=${pageSize}">Prev</a>
                </g:if>
                <g:if test="${offset + pageSize < dois.totalCount}">
                    <a class="btn btn-sm btn-primary pull-right" href="${request.contextPath}?offset=${offset + pageSize}&pageSize=${pageSize}">Next</a>
                </g:if>
            </div>
        </g:if>
        <g:else>
            <p>There are no DOIs recorded in the ALA DOI Repository</p>
        </g:else>
    </div>
</div>



</body>
</html>
