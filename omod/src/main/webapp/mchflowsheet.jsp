<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="java.util.*" %>
<%@ page import="org.openmrs.*" %>
<%@ page import="org.openmrs.module.mch.mapper.*" %>
<%@ page import="org.openmrs.module.mch.web.*" %>

<%@ include file="templates/include.jsp"%>
<%@ include file="templates/headerMCH.jsp"%>

<!-- Create htmlInclude tag that takes media -->

<%@ include file="fragments/popup.jspf"%>

<script type="text/javascript">

</script>

<%@ include file="fragments/load.jspf"%>

<!-- <span class="view-row"><a href="javascript:window.print()"><strong>Print flowsheet</strong></a></span> -->
<div id="form-header-section">
	<div class="form-header">Reproductive Maternal new-born and Child Health(RMNCH)</div>
	<div>
		<table id="demographics-table" width="100%">
		     <c:if test="${formData.otherIdentifiers != ''}">
				<tr>
					<td colspan="4"align="left">${formData.otherIdentifiers}</td>
				</tr>
			</c:if>
			<tr>
				<td width="25%"><span class="value-label-big">Family Name: </span><span class="value-data-big">${patient.personName.familyName}</span></td>
				<td width="25%"><span class="value-label-big">Given Name: </span><span class="value-data-big">${patient.personName.givenName}</span></td>
				<td width="25%"><span class="value-label"><span class="value-label">PC ID: </span><span class="value-data">${formData.tracnetId}</span></td>
				<td width="25%"><span class="value-label">Health Facility: </span><span class="value-data">${formData.healthCenter}</span></td>
			</tr>
			<tr>
				<td width="25%" colspan="2"><span class="value-label">D O B: </span><span class="value-date"><openmrs:formatDate date="${patient.birthdate}" format="${FormatDate_General}"/> (jj/mm/aaaa)</span></td>
				<td width="50%" colspan="2"><span class="value-label">Enrolement Date: </span> ${formData.programHistory} <span class="value-label"> (jj/mm/aaaa)</span></td>
			</tr>
			<tr>
				<td width="50%" colspan="2"><span class="value-label">Blood Group: </span> <font color="red"><span class="value-data"> ${formData.lastBloodGroup.name.name} </span></font></td>
				<td width="50%" colspan="4"><span class="value-label">Medical History:</span><span class="value-data">
				 <c:forEach var="conc" items="${formData.allMedicalHistory}">
                                    |<c:out value="${conc.name.name}"/>
                         </c:forEach>

				</span></td>
			</tr>
		</table>
	</div>
</div>

<!-- <%@ include file="fragments/tabs.jspf"%> -->


<div id="flowsheet-tabs">
	<ul>
			<c:if test="${formData.age > 12}">
			<li><a href="#ANC">ANC</a></li>
            <li><a href="#INTRAPARTUM">INTRAPARTUM</a></li>
            <li><a href="#PNC">PNC</a></li>
            </c:if>
            <c:if test="${formData.age <= 0}">
            <li><a href="#KMC">KMC</a></li>
            </c:if>

            <c:if test="${formData.age <= 12}">
            <li><a href="#EPI">EPI</a></li>
            </c:if>
            <c:if test="${formData.age < 5}">
            <li><a href="#IMNCI">IMNCI</a></li>
             </c:if>
            <c:if test="${formData.age <= 18}">
            <li><a href="#ECD">ECD</a></li>
                         </c:if>
            <c:if test="${formData.age <= 18 && formData.age >= 12}">

            <li><a href="#ASRH">ASRH</a></li>
                                     </c:if>

            <li><a href="#GBV">GBV</a></li>

            <c:if test="${formData.age >= 12}">

            <li><a href="#FP">FP</a></li>
              </c:if>
            <c:if test="${formData.age <= 12}">

            <li><a href="#GMP">GMP</a></li>
                          </c:if>

            <c:if test="${formData.age <= 0}">
            <li><a href="#NEWBORNT">NEW BORN Tr.</a></li>
            </c:if>

            <c:if test="${formData.age <= 0}">
                        <li><a href="#NEO">NEO</a></li>
            </c:if>

            <li><a href="#GRAPHS">GRAPHS</a></li>

	</ul>


<c:if test="${formData.age > 12}">
<div id="ANC" style="page-break-before: always">
	 <%@ include file="fragments/anc.jspf"%>
</div>
<div id="INTRAPARTUM" style="page-break-before: always">
	 <%@ include file="fragments/intrapartum.jspf"%>
</div>
<div id="PNC" style="page-break-before: always">
	 <%@ include file="fragments/pnc.jspf"%>
</div>
</c:if>
<c:if test="${formData.age <= 0}">
<div id="KMC" style="page-break-before: always">
	 <%@ include file="fragments/kmc.jspf"%>
</div>
</c:if>
<c:if test="${formData.age <= 12}">
<div id="EPI" style="page-break-before: always">
	 <%@ include file="fragments/epi.jspf"%>
</div>
</c:if>
<c:if test="${formData.age < 5}">
<div id="IMNCI" style="page-break-before: always">
	 <%@ include file="fragments/imnci.jspf"%>
</div>
</c:if>
<c:if test="${formData.age <= 18}">
<div id="ECD" style="page-break-before: always">
	 <%@ include file="fragments/ecd.jspf"%>
</div>
</c:if>
<c:if test="${formData.age <= 18 && formData.age >= 12}">
<div id="ASRH" style="page-break-before: always">
	 <%@ include file="fragments/asrh.jspf"%>
</div>
</c:if>
<div id="GBV" style="page-break-before: always">
	 <%@ include file="fragments/gbv.jspf"%>
</div>
<c:if test="${formData.age >= 12}">
<div id="FP" style="page-break-before: always">
	 <%@ include file="fragments/fp.jspf"%>
</div>
</c:if>
<c:if test="${formData.age <= 12}">
<div id="GMP" style="page-break-before: always">
	 <%@ include file="fragments/gmp.jspf"%>
</div>
</c:if>
<c:if test="${formData.age <= 0}">
<div id="NEWBORNT" style="page-break-before: always">
	 <%@ include file="fragments/newBornTransfer.jspf"%>
</div>
</c:if>

<c:if test="${formData.age <= 0}">
<div id="NEO" style="page-break-before: always">
	 <%@ include file="fragments/neo.jspf"%>
</div>
</c:if>

<div id="GRAPHS" style="page-break-before: always">
	 <%@ include file="fragments/graphs.jspf"%>
</div>

<%@ include file="templates/footer.jsp"%>
