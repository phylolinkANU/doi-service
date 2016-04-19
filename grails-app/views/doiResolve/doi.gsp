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
<g:include view="common/_envWarning.gsp"/>
<ala:systemMessage/>

<div class="col-sm-12 col-md-9 col-lg-9">
    <h1 class="hidden">Welcome the Atlas of Living Australia</h1>
    <ol class="breadcrumb hidden-print">
        <li><a class="font-xxsmall" href="${grailsApplication.config.ala.base.url}">Home</a></li>
        <li><a class="font-xxsmall" href="${request.contextPath}/">DOI Search Index</a></li>
        <li class="font-xxsmall active">DOI entry</li>
    </ol>

    <h2 class="heading-medium">${doi.title}</h2>

    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="panel panel-default">
                <div class="panel-body">

                    <div class="word-limit">
                        <h1 class="heading-xlarge">${doi.title} <small>&mdash; ${doi.authors}</small></h1>

                        <div class="row">
                            <div class="col-md-offset-1 col-md-10">

                                <p class="help-block">${doi.description}</p>

                                <!-- Tabular data -->
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <tbody>
                                        <tr>
                                            <th scope="row">Creation Date</th>
                                            <td>${doi.dateMinted}</td>
                                        </tr>
                                        <g:if test="${doi.customLandingPageUrl}">
                                            <tr>
                                                <th scope="row">Landing page</th>
                                                <td>This DOI was registered with an application-specific landing page.
                                                    <br/>
                                                    <a href="${doi.customLandingPageUrl}">View the application landing page.</a>
                                                </td>
                                            </tr>
                                        </g:if>
                                        <g:render template="metadata" model="[metadata: doi.applicationMetadata]"/>

                                        </tbody>
                                    </table>

                                </div>

                                <div class="alert alert-info alert-dismissible" role="alert">
                                    <button type="button" class="close" data-dismiss="alert"
                                            aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                    If you are having trouble accessing this document, please <a
                                        href="https://www.ala.org.au/about-the-atlas/communications-centre/">contact the Atlas of Living Australia</a> to request a copy in a format that you can use.
                                </div>

                                <h4>To access this resource, you can</h4>

                                <p>
                                    <a class="btn btn-default" href="${doi.applicationUrl}"
                                       title="Go to source">Go to the source</a>
                                    <a class="btn btn-primary"
                                       href="${request.contextPath}/doi/${doi.uuid}/download"
                                       title="Download file">Download file</a>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
