<%@ include file="templates/include.jsp"%>
<c:set var="hideBreadcrumbs" value="true"/>
<%@ include file="templates/header.jsp"%>

<div id="homepage-logo">
	<img src="${pageContext.request.contextPath}/moduleResources/rwandamoh/images/rwanda300.png"/>
</div>

<!--  DT:  this jsp doesn't seem to be used at all in this module, i.e., there's no controller, and it points to the rwandamoh module.  Does this even still exist?
by commening this out, it allows removal of ui.springmvc dependency
<ui:appList apps="${apps}"/>
-->


<div id="footer">
	<div id="powered-by">
		Powered by
		<a href="http://www.openmrs.org">
			<img src="${pageContext.request.contextPath}/moduleResources/rwandamoh/images/openmrs.png"/>
		</a>
	</div>
</div>

<%@ include file="templates/footer.jsp"%>