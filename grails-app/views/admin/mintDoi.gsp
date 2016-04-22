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
    <title>Mint/Register DOI | ${grailsApplication.config.skin.orgNameLong}</title>
    <r:require modules="doi, jqueryValidationEngine"/>
</head>

<body>
<ala:systemMessage/>

<div class="col-sm-12 col-md-10 col-lg-10">
    <h1 class="hidden">Welcome the Atlas of Living Australia</h1>
    <ol class="breadcrumb hidden-print">
        <li><a class="font-xxsmall" href="${grailsApplication.config.grails.serverURL}">Home</a></li>
        <li><g:link class="font-xxsmall" controller="admin" action="index">Admin</g:link></li>
        <li class="font-xxsmall active">Mint/Register DOI</li>
    </ol>

    <g:if test="${status == 'error'}">
        <div class="alert alert-danger">
            ${errorMessage}
        </div>
    </g:if>

    <h2 class="heading-medium">Mint/Register DOI</h2>

    <div class="panel panel-default">
        <div class="panel-body">
            <div class="row">
                <div class="col-sm-6">
                    <g:uploadForm name="mintDoiForm" role="form" controller="admin" action="createDoi" method="POST">
                        <div class="form-group">
                            <label for="title">Title<span class="req-field"/></label>
                            <input id="title" name="title" type="text" class="form-control"
                                   value="${mintParameters?.title}"
                                   data-validation-engine="validate[required]"/>
                        </div>

                        <div class="form-group">
                            <label for="authors">Authors<span class="req-field"/></label>
                            <input id="authors" name="authors" type="text" class="form-control"
                                   value="${mintParameters?.authors}"
                                   data-validation-engine="validate[required]"/>
                        </div>

                        <div class="form-group">
                            <label for="description">Description<span class="req-field"/></label>
                            <input id="description" name="description" type="text" class="form-control"
                                   value="${mintParameters?.description}"
                                   data-validation-engine="validate[required]"/>
                        </div>

                        <div class="form-group">
                            <label for="applicationUrl">Application Url<span class="req-field"/></label>
                            <input id="applicationUrl" name="applicationUrl" type="text" class="form-control"
                                   value="${mintParameters?.applicationUrl}"
                                   data-validation-engine="validate[required,custom[url]]"/>
                        </div>

                        <div class="panel panel-default">
                            <div class="panel-body">
                                <div class="radio">
                                    <label>
                                        <input type="radio" id="newDoiRadio" name="newExistingDoiRadio"
                                               value="new"
                                               checked="checked">Mint New DOI</label>
                                </div>

                                <div class="radio">
                                    <label><input type="radio" id="existingDoiRadio"
                                                  name="newExistingDoiRadio"
                                                  value="existing">Register existing DOI</label>
                                </div>

                                <div class="form-group">
                                    <label for="provider">Provider</label>
                                    <g:select id="provider" name="provider" class="form-control"
                                              value="${mintParameters?.provider}"
                                              data-validation-engine="validate[required]"
                                              keys="['ANDS']"
                                              from="['ANDS']"/>
                                </div>

                                <div class="form-group">
                                    <label for="providerMetadata">Provider Metadata</label>
                                    <g:textArea name="providerMetadata" type="text" class="form-control"
                                                value="${mintParameters?.providerMetadata}"
                                                rows="20"
                                                data-validation-engine="validate[required,funcCall[isJson]]"/>
                                </div>

                                <div class="form-group">
                                    <label for="existingDoi">Existing DOI</label>
                                    <input id="existingDoi" name="existingDoi" type="text"
                                           class="form-control"
                                           value="${mintParameters?.existingDoi}"
                                           data-validation-engine="validate[required]"
                                           disabled/>
                                </div>
                            </div>
                        </div>

                        <div class="panel panel-default">
                            <div class="panel-body">

                                <div class="form-group">
                                    <label for="fileUrl">File URL</label>
                                    <input id="fileUrl" name="fileUrl" type="text" class="form-control"
                                           value="${mintParameters?.fileUrl}"
                                           data-validation-engine="validate[groupRequired[file],custom[url]]"/>
                                </div>

                                <div class="form-group">
                                    <label for="file">File</label>
                                    <input id="file" name="file" type="file" class="form-control"
                                           data-validation-engine="validate[groupRequired[file]]"/>
                                </div>

                            </div>
                        </div>

                        <div class=" form-group">
                            <label for="customLandingPageUrl">Custom Landing Page Url</label>
                            <input id="customLandingPageUrl" name="customLandingPageUrl" type="text"
                                   class="form-control"
                                   value="${mintParameters?.customLandingPageUrl}"
                                   data-validation-engine="validate[custom[url]]"/>
                        </div>

                        <div class="form-group">
                            <label for="applicationMetadata">Application Metadata</label>
                            <g:textArea id="applicationMetadata" name="applicationMetadata" type="text"
                                        class="form-control"
                                        rows="20"
                                        value="${mintParameters?.applicationMetadata}"
                                        data-validation-engine="validate[funcCall[isJson]]"/>
                        </div>
                        <button id="mintDoiSubmit" class="btn btn-ala btn-primary">Mint DOI</button>
                    </g:uploadForm>
                </div>

                <div class="col-sm-6">
                    <div class="col-sm-12 well">
                        <p>
                            To mint a new DOI, fill in the required fields and click "Mint DOI".
                        </p>

                        <p>
                            You will have to provide either a DOI for existing entries not recorded in the DOI service or the application
                            metadata for new records that need to be minted.
                        </p>

                        <p>
                            The provider and application metadata fields, if required, need to be filled in with a JSON document.
                        </p>

                        <p>You can use the template below to populate the ANDS provider metadata:</p>

                        <p/>
                        <code>
                            {<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;"authors" : [<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"&lt;Author&gt;"<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;],<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;"contributors" : [{<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"name" : "&lt;Contributor&gt;",<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"type" : "&lt;Editor|etc&gt;"<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;],<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;"title" : "&lt;Title&gt;",<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;"subjects" : [<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"&lt;Subjects&gt;"<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;],<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;"subtitle" : "&lt;Subtitle&gt;",<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;"publicationYear" : &lt;Year&gt;,<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;"createdDate" : "YYY-MM-ddThh:mm:ssZ",<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;"descriptions" : [{<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"text" : "&lt;Description&gt;",<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"type" : "&lt;Other|etc&gt;"<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;],<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;"resourceText" : "&lt;Species information|etc&gt;",<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;"resourceType" : "&lt;Text|etc&gt;",<br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;"publisher" : "&lt;Publisher&gt;"<br/>
                            }<br/>

                        </code>

                        <p/>

                        <p>You need to provide an existing resource for File Url or upload a File stored locally.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<r:script>
    $(function () {
        $('#mintDoiSubmit').removeAttr('disabled');
        $('#mintDoiForm').validationEngine('attach', {scroll: false});

        $("#mintDoiSubmit").click(function (e) {

            $("#mintDoiSubmit").attr('disabled', 'disabled');

            var valid = $('#mintDoiForm').validationEngine('validate');

            if (valid) {
                $("form[name='mintDoiForm']").submit();
            } else {
                $('#mintDoiSubmit').removeAttr('disabled');
                e.preventDefault();
            }
        });

        $('#existingDoiRadio').click(function () {
            $('#existingDoi').removeAttr("disabled");
            $('#providerMetadata').attr("disabled", "disabled");
            $('#provider').attr("disabled", "disabled");
        });

        $('#newDoiRadio').click(function () {
            $('#existingDoi').attr("disabled", "disabled");
            $('#providerMetadata').removeAttr("disabled");
            $('#provider').removeAttr("disabled");
        });
    });

    function isJson(field, rules, i, options) {
        try {
            if (field.val().trim()) {
                JSON.parse(field.val());
            }
        }
        catch (err) {
            console.warn(err.message);
            console.warn('Field:"' + field.val().trim() + '"');
            return "This field has to be a valid JSON document";
        }
    }
</r:script>
</html>
