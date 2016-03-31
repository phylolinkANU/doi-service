<g:if test="${grailsApplication.config.deployment_env && grailsApplication.config.deployment_env?.toLowerCase() != "prod" && grailsApplication.config.deployment_env?.toLowerCase() != "production"}">
    <div class="padding-top-1" ng-cloak>
        <div class="alert-warning admin-message">
            <div class="admin-message-text">This is a ${grailsApplication.config.deployment_env.toUpperCase()} site.</div>
        </div>
    </div>
</g:if>