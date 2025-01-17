<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <asset:stylesheet src="doi.css"/>
</head>

<body>
<ala:systemMessage/>

<div class="col-sm-12 col-md-9 col-lg-9">
    <div class="col-md-12" id="doiTitle">
        <h2><a href="https://doi.org/${doi.doi}" type="button" class="doi"><span>DOI</span><span>${doi.doi}</span></a></h2>
		<h3 class="heading-medium ${doi.active? '': 'text-muted'}">${doi.title}
			<g:if test="${!doi.active}">
				<small class="badge badge-secondary"><g:message code="doi.inactive"/></small>
			</g:if>
		</h3>
    </div>

    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="word-limit break-word">
                        <div class="row">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <div class="well">
                                    <p style="margin-top: 4px; margin-bottom: 4px;">To access this resource, you can 
                                        <a class="btn btn-default" href="${doi.applicationUrl}"
										   style="margin-left: 4px; margin-top: -14px; margin-bottom: -12px;"

                                           title="Go to source">Go to the source</a>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">

                                <div class="row padding-bottom-10">
                                    <div class="col-md-3">
                                        <strong>Application URL:</strong>

                                    </div>

                                    <div class="col-md-9">

                                        <a href="${doi.applicationUrl}">${doi.applicationUrl}</a>                                        

                                    </div>

                                </div>

                                <div class="row padding-bottom-10">
                                    <div class="col-md-3">
                                        <strong>Date Created:</strong>
                                    </div>
                                    <div class="col-md-9">
                                        ${doi.dateMinted}
                                    </div>
                                </div>
                                <div class="row padding-bottom-10">
                                    <div class="col-md-3">
                                        <strong>Authors:</strong>
                                    </div>
                                    <div class="col-md-9">
                                        ${doi.authors}
                                    </div>
                                </div>
                                <div class="row padding-bottom-10">
                                    <div class="col-md-3">
                                        <strong>Description:</strong>
                                    </div>
                                    <div class="col-md-9">
                                        ${doi.description}
                                    </div>
                                </div>
                                <div class="row padding-bottom-10">
                                    <div class="col-md-3">
                                        <strong>Licence:</strong>
                                    </div>
                                    <div class="col-md-9">
                                        <g:if test="${doi.licence}">
                                            <ul style="margin-left: -22px;">
                                            <g:each in="${doi.licence}" var="licence" >
                                                <li>${licence}</li>
                                            </g:each>
                                            </ul>
                                        </g:if>
                                    </div>
                                </div>
                                <div class="row padding-bottom-10">
                                    <div class="col-md-3">
                                        <strong>Genus:</strong>
                                    </div>
                                    <div class="col-md-9">
                                        <g:if test="${doi.applicationMetadata.Genus}">
                                            <ul style="margin-left: -22px;">
                                            <g:each in="${doi.applicationMetadata.Genus}" var="v" >
                                                <li>${v}</li>
                                            </g:each>
                                            </ul>
                                        </g:if>
                                    </div>
                                </div>
                                <div class="row padding-bottom-10">
                                    <div class="col-md-3">
                                        <strong>Species:</strong>
                                    </div>
                                    <div class="col-md-9">
                                        <g:if test="${doi.applicationMetadata.Species}">
                                            <ol style="margin-left: -22px;">
                                            <g:each in="${doi.applicationMetadata.Species}" var="v" >
                                                <li>${v}</li>
                                            </g:each>
                                            </ol>
                                        </g:if>
                                    </div>
                                </div>
                                <div class="row padding-bottom-10">
                                    <div class="col-md-3">
                                        <strong>Creators:</strong>
                                    </div>
                                    <div class="col-md-9">
                                        <g:if test="${doi.applicationMetadata.Creators}">
                                            <ul style="margin-left: -22px;">
                                            <g:each in="${doi.applicationMetadata.Creators}" var="v" >
                                                <li>${v.name}</li>
                                            </g:each>
                                            </ul>
                                        </g:if>
                                    </div>
                                </div>
                                <div class="row padding-bottom-10">
                                    <div class="col-md-3">
                                        <strong>Citation URL:</strong>
                                    </div>
                                    <div class="col-md-9">
                                        <a href="${grailsApplication.config.doi.resolverUrl}${doi.doi}">${grailsApplication.config.doi.resolverUrl}${doi.doi}</a>
                                    </div>
                                </div>
                                <g:if test="${doi.customLandingPageUrl}">
                                    <div class="row padding-bottom-10">
                                        <div class="col-md-3">
                                            <strong>Landing page:</strong>
                                        </div>
                                        <div class="col-md-9">
                                            This DOI was registered with an application-specific landing page. <a href="${doi.customLandingPageUrl}">View the application landing page.</a>
                                        </div>
                                    </div>
                                </g:if>

                                <g:if test="${isAdmin}">
                                    <div class=" padding-top-10">
                                        <h3>Admin only fields</h3>
                                    </div>
                                    <div class="row padding-bottom-10">
                                        <div class="col-md-3">
                                            <strong>User Id:</strong>
                                        </div>
                                        <div class="col-md-9">
                                            ${doi.userId}
                                        </div>
                                    </div>

                                    <g:if test="${doi.authorisedRoles}">
                                        <div>
                                            The DOI contains sensitive data. The file can only be accessed by users that has all of the roles below
                                        </div>
                                        <div class="row padding-bottom-10">
                                            <div class="col-md-3">
                                                <strong>Sensitive roles:</strong>
                                            </div>
                                            <div class="col-md-9">
                                                <ul>
                                                    <g:each var="role" in="${doi.authorisedRoles}">
                                                        <li>${role}</li>
                                                    </g:each>
                                                </ul>
                                            </div>
                                        </div>
                                    </g:if>
                                </g:if>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <div class="alert alert-info alert-dismissible" role="alert" style="margin-top: 10px; margin-bottom: 0px;">
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
