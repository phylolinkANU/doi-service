<%@ page import="org.apache.commons.lang.StringUtils" %>
<g:if test="${metadata}">
        <g:if test="${metadata instanceof Map}">
            <g:each in="${((Map)metadata).entrySet()}" var="item">
                <g:render template="metadata" model="[metadata: item]"/>
            </g:each>
        </g:if>
        <g:elseif test="${metadata instanceof Map.Entry}">
            <div class="row padding-bottom-10">
                <div class="col-md-2">
                    <strong>${org.apache.commons.lang.StringUtils.capitalize(metadata.key?.replaceAll("([a-z])([A-Z])", '$1 $2'))}:</strong>
                </div>
                <div class="col-md-10">
                    <g:render template="metadata" model="[metadata: metadata.value]"/>
                </div>
            </div>
        </g:elseif>
        <g:elseif test="${metadata instanceof Collection}">
            <g:each in="${metadata}" var="item" status="index">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <g:render template="metadata" model="[metadata: item]"/>
                    </div>
                </div>
            </g:each>
        </g:elseif>
        <g:else>
            ${metadata}
        </g:else>
</g:if>
