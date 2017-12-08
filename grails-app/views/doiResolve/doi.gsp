<%@ page import="com.jakewharton.byteunits.BinaryByteUnit; com.google.common.io.BaseEncoding" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <meta name="breadcrumbParent" content="${grailsApplication.config.grails.serverURL},${message(code:"doi.homepage.title")}"/>
    <title>DOI Details</title>

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

    <asset:stylesheet src="doi.css"/>
</head>

<body>
<ala:systemMessage/>

<div class="col-sm-12 col-md-9 col-lg-9">
    <h2 class="heading-medium">${doi.title}</h2>
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="word-limit">
                        <h4>${doi.authors}</h4>
                        <div class="row">
                            <div class="col-md-10">
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
                        <div class="row">
                            <div class="col-md-10">

                                <div class="padding-bottom-10">${doi.description}</div>

                                <div class="padding-bottom-10"><a href="http://dx.doi.org/${doi.doi}" type="button" class="doi doi-sm"><span>DOI</span><span>${doi.doi}</a></div>
                                <div class="padding-bottom-10"><strong>Created:</strong> ${doi.dateMinted}</div>
                                <div class="padding-bottom-10"><strong>Licence:</strong> ${doi.licence}</div>
                                <g:if test="${doi.customLandingPageUrl}">
                                    <div class="padding-bottom-10"><strong>Landing page:</strong> This DOI was registered with an application-specific landing page. <a href="${doi.customLandingPageUrl}">View the application landing page.</a></div>
                                </g:if>
                                <g:if test="${doi.fileHash}">
                                    <div class="padding-bottom-10"><strong>File SHA-256:</strong> ${BaseEncoding.base16().encode(doi.fileHash)}</div>
                                </g:if>
                                <g:if test="${doi.fileSize}">
                                    <div class="padding-bottom-10"><strong>File size:</strong> ${BinaryByteUnit.format(doi.fileSize)}</div>
                                </g:if>

                                <g:render template="metadata" model="[metadata: doi.applicationMetadata]"/>


                                <div class="alert alert-info alert-dismissible" role="alert">
                                    <button type="button" class="close" data-dismiss="alert"
                                            aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                    If you are having trouble accessing this document, please <a
                                        href="https://www.ala.org.au/about-the-atlas/communications-centre/">contact the Atlas of Living Australia</a> to request a copy in a format that you can use.
                                </div>

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
