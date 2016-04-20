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
    <title>Mint DOI | ${grailsApplication.config.skin.orgNameLong}</title>
    <r:require modules="doi"/>
</head>

<body>
<g:include view="common/_envWarning.gsp"/>
<ala:systemMessage/>

<div class="col-sm-12 col-md-9 col-lg-9">
    <h1 class="hidden">Welcome the Atlas of Living Australia</h1>
    <ol class="breadcrumb hidden-print">
        <li><a class="font-xxsmall" href="${grailsApplication.config.ala.base.url}">Home</a></li>
        <li><a class="font-xxsmall" href="${request.contextPath}/">Admin</a></li>
        <li class="font-xxsmall active">Mint DOI</li>
    </ol>

    <h2 class="heading-medium">Mint DOI</h2>

    <div class="panel panel-default">
        <div class="panel-body">
            <div class="row">
                <div class="col-sm-6">
                    <form role="form">
                        <div class="form-group">
                            <label for="title">Title<span class="req-field"></span></label>
                            <input id="title" name="title" type="text" class="form-control" value="${doi?.title}"/>
                        </div>

                        <div class="form-group">
                            <label for="authors">Authors</label>
                            <input id="authors" name="authors" type="text" class="form-control"
                                   value="${doi?.authors}"/>
                        </div>

                        <div class="form-group">
                            <label for="description">Description</label>
                            <input id="description" name="description" type="text" class="form-control"
                                   value="${doi?.description}"/>
                        </div>

                        <div class="form-group">
                            <label for="applicationUrl">Application Url</label>
                            <input id="applicationUrl" name="applicationUrl" type="text" class="form-control"
                                   value="${doi?.applicationUrl}" data-validation-engine="validate[required]"/>
                        </div>

                        <div class="form-group">
                            <label for="customLandingPageUrl">Custom Landing Page Url</label>
                            <input id="customLandingPageUrl" name="customLandingPageUrl" type="text"
                                   class="form-control"
                                   value="${doi?.customLandingPageUrl}"
                                   data-validation-engine="validate[required"/>
                        </div>

                        <div class="form-group">
                            <label for="customLandingPageUrl">Custom Landing Page Url</label>
                            <input id="customLandingPageUrl" name="customLandingPageUrl" type="text"
                                   class="form-control"
                                   value="${doi?.customLandingPageUrl}"
                                   data-validation-engine="validate[required"/>
                        </div>

                        <div class="radio">
                            <label><input type="radio" id="newDoiRadio" name="newExistingDoiRadio" checked="checked">Mint New DOI</label>
                        </div>

                        <div class="radio">
                            <label><input type="radio" id="existingDoiRadio" name="newExistingDoiRadio">Register existing DOI</label>
                        </div>

                        <div class="form-group">
                            <label for="provider">Provider</label>
                            <g:select id="provider" name="provider" class="form-control"
                                      value="${doi?.provider}" data-validation-engine="validate[required]"
                                      keys="['ANDS']"
                                      from="['ANDS']"/>
                        </div>

                        <div class="form-group">
                            <label for="providerMetadata">Provider Metadata</label>
                            <textarea id="providerMetadata" name="providerMetadata" type="text" class="form-control"
                                      value="${doi?.providerMetadata}"
                                      rows="20">
                            </textarea>
                        </div>

                        <div class="form-group">
                            <label for="existingDoi">Existing DOI</label>
                            <input id="existingDoi" name="existingDoi" type="text"
                                   class="form-control"
                                   value="${doi?.existingDoi}"
                                   data-validation-engine="validate[required]"
                                   disabled/>
                        </div>

                        <div class="form-group">
                            <label for="fileUrl">File URL</label>
                            <input id="fileUrl" name="fileUrl" type="text" class="form-control"
                                   value="${doi?.fileUrl}"/>
                        </div>

                        <div class="form-group">
                            <label for="file">File</label>
                            <input id="file" name="file" type="file" class="form-control"/>
                        </div>

                        <div class="form-group">
                            <label for="applicationMetadata">Application Metadata</label>
                            <textarea id="applicationMetadata" name="applicationMetadata" type="text"
                                      class="form-control"
                                      rows="20"
                                      value="${doi?.applicationMetadata}">

                            </textarea>
                        </div>
                        <button id="mintDoiSubmit" class="btn btn-ala btn-primary">Mint DOI</button>
                    </form>
                </div>

                <div class="col-sm-6 well">
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
</body>
<r:script>
    $(function(){

        //$('.typeahead').typeahead();
        var usageOptions = [
            "Amateur naturalist","Amateur photographer","Biodiversity Research","Biogeographer",
            "Biologist","Botanist","Bush Regenerator","BushCare leader","Citizen scientist","Collection manager",
            "Collection technician","Communications","Conservation Planner","Consultant","Data manager",
            "Database Manager","Eco Tourism","Ecologist","Education","Education programs developer","Entomologist",
            "Environmental Officer","Environmental Scientist","Farming","Field Researcher","Forester","Geochemist",
            "GIS visualisation","Identification","IT specialist","Land manager","Land owner","Librarian","Mycologist",
            "Naturalist","Observer","Park Ranger","Pest control","Pest Identification","PhD Student","Policy developer",
            "Predicting distribution","Researcher","Science communicator","Scientific Illustrator","Scientist",
            "Student","Taxonomist","Teacher","Veterinary Pathologist","Volunteer","Volunteer Digitizer","Writer",
            "Zoologist"
        ];

        $(".usageAuto").autocomplete(usageOptions, {});
        //$('#updateAccountForm').validationEngine('attach', { scroll: false });
        $("#updateAccountSubmit").click(function(e) {

            $("#updateAccountSubmit").attr('disabled','disabled');

            var pm = $('#password').val() == $('#reenteredPassword').val();
            if(!pm){
                alert("The supplied passwords do not match!");
            }

            var valid = $('#updateAccountForm').validationEngine('validate');

            if (valid && pm) {
                $("form[name='updateAccountForm']").submit();
            } else {
                $('#updateAccountSubmit').removeAttr('disabled');
                e.preventDefault();
            }
        });

        $("#disableAccountSubmit").click(function(e) {

            $("#disableAccountSubmit").attr('disabled','disabled');

            var valid = confirm("${message(code: 'default.button.delete.user.confirm.message', default: "Are you sure want to disable your account? You won't be able to login again. You will have to contact support@ala.org.au in the future if you want to reactivate your account.")}");

            if (valid) {
                $('#updateAccountForm').validationEngine('detach');
                $("form[name='updateAccountForm']").attr('action','disableAccount');
                $("form[name='updateAccountForm']").submit();
            } else {
                $('#disableAccountSubmit').removeAttr('disabled');
                e.preventDefault();
            }
        });

        $('#existingDoiRadio').click(function()
        {
            $('#existingDoi').removeAttr("disabled");
            $('#providerMetadata').attr("disabled","disabled");
            $('#provider').attr("disabled","disabled");
        });

        $('#newDoiRadio').click(function()
        {
            $('#existingDoi').attr("disabled","disabled");
            $('#providerMetadata').removeAttr("disabled");
            $('#provider').removeAttr("disabled");
        });



    });
</r:script>
</html>
