<%@ page import="org.apache.commons.lang.StringUtils" %>
<g:if test="${metadata}">
        <g:if test="${metadata instanceof Map}">
            <g:each in="${((Map)metadata).entrySet()}" var="item">
                <g:render template="metadata" model="[metadata: item]"/>
            </g:each>
        </g:if>
        <g:elseif test="${metadata instanceof Map.Entry}">
            <div class="padding-bottom-10"><strong>${org.apache.commons.lang.StringUtils.capitalize(metadata.key?.replaceAll("([a-z])([A-Z])", '$1 $2'))}:</strong> ${metadata.value}</div>
        </g:elseif>
        <g:elseif test="${metadata instanceof Collection}">
            <g:each in="${metadata}" var="item" status="index">
                <g:render template="metadata" model="[metadata: item]"/><g:if test="${index < metadata.size() - 1}">,</g:if>
            </g:each>
        </g:elseif>
        <g:else>
            ${metadata}
        </g:else>
</g:if>
