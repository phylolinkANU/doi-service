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
        window.ga = window.ga || function () {
                    (ga.q = ga.q || []).push(arguments)
                };
        ga.l = +new Date;
        ga('create', '${grailsApplication.config.googleAnalyticsId}', 'auto');
        ga('send', 'pageview');
    </script>
    <script async src='//www.google-analytics.com/analytics.js'></script>
    %{--End Google Analytics--}%

    <r:require modules="doi"/>
</head>

<body>

<div class="panel">
    <div class="panel-body">
        <h1>${doi.title}</h1>

        <div class="citation">${doi.authors}</div>

        <p>${doi.description}</p>

        <div>
            <h4>Date Created</h4>
            ${doi.dateMinted}
        </div>


        <g:if test="${doi.customLandingPageUrl}">
            <div>
                <h4>Application-specific landing page</h4>
                This DOI was registered with an application-specific landing page. <a href="${doi.customLandingPageUrl}"
                                                                                      title="Application landing page">View the application landing page</a>.
            </div>
        </g:if>

        <g:if test="${doi.applicationMetadata}">
            <h4>Additional information</h4>
            <g:render template="metadata" model="[metadata: doi.applicationMetadata]"/>
        </g:if>
        <p/>

        <div class="padding-top-1 center-block">
            <a class="btn btn-primary btn-lg" href="${request.contextPath}/api/v1/doi/${doi.uuid}/download"
               title="Download file"><span class="fa fa-download">&nbsp;</span>Download</a>

            <a class="btn btn-primary btn-lg" href="${doi.applicationUrl}" title="Go to source"><span
                    class="fa fa-forward">&nbsp;</span>Go to source</a>
        </div>
    </div>
</div>
</body>
</html>
