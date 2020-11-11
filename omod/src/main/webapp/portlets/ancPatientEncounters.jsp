<%@ include file="/WEB-INF/template/include.jsp" %>
<openmrs:htmlInclude file="/scripts/easyAjax.js" />

<openmrs:htmlInclude file="/scripts/jquery/dataTables/css/dataTables.css" />
<openmrs:htmlInclude file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js" />
<!--


<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" />
<link href="<openmrs:contextPath/>/scripts/jquery-ui/css/<spring:theme code='jqueryui.theme.name' />/jquery-ui.custom.css" type="text/css" rel="stylesheet" />
-->
<openmrs:globalProperty key="dashboard.encounters.showViewLink" var="showViewLink" defaultValue="true"/>
<openmrs:globalProperty key="dashboard.encounters.showEditLink" var="showEditLink" defaultValue="true"/>

<!-- <div id="displayEncounterPopup">
	<div id="displayEncounterPopupLoading"><openmrs:message code="general.loading"/></div>
	<iframe id="displayEncounterPopupIframe" width="100%" height="100%" marginWidth="0" marginHeight="0" frameBorder="0" scrolling="auto"></iframe>
</div> -->

<script type="text/javascript">
	$j(document).ready(function() {
		$j('#displayEncounterPopup').dialog({
				title: 'dynamic',
				autoOpen: false,
				draggable: false,
				resizable: false,
				width: '95%',
				modal: true,
				open: function(a, b) { $j('#displayEncounterPopupLoading').show(); }
		});
	});

	function loadUrlIntoEncounterPopup(title, urlToLoad) {
		$j("#displayEncounterPopupIframe").attr("src", urlToLoad);
		$j('#displayEncounterPopup')
			.dialog('option', 'title', title)
			.dialog('option', 'height', $j(window).height() - 50) 
			.dialog('open');

	}
</script>

<c:if test="${model.showPagination == 'true'}">
<script type="text/javascript">
	$j(document).ready(function() {
		$j('#portlet${model.portletUUID} #patientEncountersTable').dataTable({
			"sPaginationType": "two_button",
			"bAutoWidth": false,
			"bFilter": false,
			"aaSorting": [[3,'asc']], // initial sorting uses the samer order given by ForEachEncounter (Encounter.datetime by default)
			"iDisplayLength": 20,
			"aoColumns": [
				{ "bVisible": false, "sType": "numeric" },
				{ "bVisible": ${showViewLink}, "iDataSort": 0 }, // sort this column by using the first invisible column for encounterIds,
            	{ "iDataSort": 3 }, // sort the date in this column by using the next invisible column for time in milliseconds
            	{ "bVisible": false, "sType": "numeric" },
            	null,
            	null
        	],
			"oLanguage": {
					"sLengthMenu": 'Show <select><option value="5">5</option><option value="20">20</option><option value="50">50</option><option value="100">100</option></select> entries',
					"sZeroRecords": '<openmrs:message code="Encounter.no.previous"/>'
			}
		} );
		$j("#displayEncounterPopupIframe").load(function() { $j('#displayEncounterPopupLoading').hide(); });
	} );
</script>
</c:if>

<%--
Parameters
	model.num == \d  limits the number of encounters shown to the value given
	model.showPagination == 'true' lists off the encounters in a paginated table
	model.hideHeader == 'true' hides the 'All Encounter' header above the table listing
	model.hideFormEntry == 'true' does not show the "Enter Forms" popup no matter what the gp has
	model.formEntryReturnUrl == what URL to return to when a form has been cancelled or successfully filled out
--%>

<div id="portlet${model.portletUUID}">
<div id="encounterPortlet">

	<openmrs:globalProperty var="enableFormEntryInEncounters" key="FormEntry.enableOnEncounterTab" defaultValue="false"/>

	<c:if test="${enableFormEntryInEncounters && !model.hideFormEntry}">
		<openmrs:hasPrivilege privilege="Form Entry">
			<div id="formEntryDialog">
				<openmrs:portlet url="personFormEntry" personId="${patient.personId}" id="encounterTabFormEntryPopup" parameters="showLastThreeEncounters=false|returnUrl=${model.formEntryReturnUrl}"/>
			</div>

			<button class="showFormEntryDialog" style="margin-left: 2em; margin-bottom: 0.5em"><openmrs:message code="FormEntry.fillOutForm"/></button>
			
			<script type="text/javascript">
				$j(document).ready(function() {
					$j("#formEntryDialog").dialog({
						title: '<openmrs:message code="FormEntry.fillOutForm" javaScriptEscape="true"/>',
						autoOpen: false,
						draggable: false,
						resizable: false,
						width: '90%',
						modal: true
					});
					$j('button.showFormEntryDialog').click(function() {
						$j('#formEntryDialog').dialog('open');
					});
				});
			</script>

		</openmrs:hasPrivilege>
	</c:if>

	<openmrs:hasPrivilege privilege="View Encounters">
		<div id="encounters">
			<div class="boxHeader${model.patientVariation}" style="background-color: #5c9ccc;" >Previous Visits</div>
			<div class="box${model.patientVariation}">
				<div>
					<table cellspacing="0" cellpadding="2" id="patientEncountersTable" width="100%">
						<thead>
							<tr>
								<th class="hidden"> hidden Encounter id </th>
								<th class="encounterView" align="center"><c:if test="${showViewLink == 'true'}">
								 	View/Edit
								</c:if></th>
								<th class="encounterDatetimeHeader"> Visit Date </th>
								<th class="hidden"> hidden Sorting Order (by Encounter.datetime) </th>
								<!-- <th class="encounterTypeHeader"> <openmrs:message code="Encounter.type"/> </th> -->
								<!-- <th class="encounterVisitHeader"><openmrs:message code="Encounter.visit"/></th> -->
								<th class="encounterProviderHeader"> Clinician </th>
								<th class="encounterFormHeader"> Visit type     </th>
							    <!-- <th class="encounterLocationHeader"> <openmrs:message code="Encounter.location"/> </th>  -->
								<!-- <th class="encounterEntererHeader"> <openmrs:message code="Encounter.enterer"/>  </th>  -->
							</tr>
						</thead>
						<tbody>
							<%-- WARNING: if sortBy="encounterDatetime" is changed, update the hidden Sorting Order column, in order to sort the encounterDatetime column too --%>
							<openmrs:globalProperty var="EnrolmentFormId" key="mch.Form_ANC_Enrolment"  />
                            <openmrs:globalProperty var="RoutineFormId" key="mch.Form_ANC_Routine"  />
                            <openmrs:globalProperty var="EmergencyFormId" key="mch.Form_ANC_Emergency"  />
                            <openmrs:globalProperty var="ReferralFormId" key="mch.Form_ANC_Referral"  />
                            <openmrs:globalProperty var="intraWomanInLabourFormId" key="mch.Form_INTRAPARTUM_Assessment_and_care_of_woman_in_labour"  />
                            <openmrs:globalProperty var="intraDeliveryAndNewBornRegFormId" key="mch.Form_INTRAPARTUM_Delivery_outcome_and_New_Born_Registration"  />


                          <openmrs:globalProperty var="ASRHEnrolment" key="mch.ASRH_Enrolment"  />
                          <openmrs:globalProperty var="ECDEnrolmentFormId" key="mch.ECD_Enrolment"  />
                          <openmrs:globalProperty var="ECDFollowUPFormId" key="mch.ECD_FollowUP"  />
                          <openmrs:globalProperty var="EPIVaccinationRecordsFormId" key="mch.EPI_Vaccination_Records"  />
                          <openmrs:globalProperty var="EPIExaminationFormId" key="mch.EPI_Examination"  />
                          <openmrs:globalProperty var="FPEnrolment" key="mch.FP_Enrolment"  />
                          <openmrs:globalProperty var="FPMonitoringsheetandexamsfollowup" key="mch.FP_Monitoring_sheet_and_exams_followup"  />
                           <openmrs:globalProperty var="GBVEnrolmentChild" key="mch.GBV_Enrolment_Child"  />
                           <openmrs:globalProperty var="GBVEnrolmentAdult" key="mch.GBV_Enrolment_Adult"  />
                           <openmrs:globalProperty var="GBVfollowup" key="mch.GBV_followup"  />
                           <openmrs:globalProperty var="GMP_6M_History_and_followupFormId" key="mch.GMP_6M_History_and_followup"  />
                          <openmrs:globalProperty var="GMP_6M_59M_History_and_followupFormId" key="mch.GMP_6M_59M_History_and_followup"  />
                          <openmrs:globalProperty var="GMP_5_9Y_History_and_followupFormId" key="mch.GMP_5_9Y_History_and_followup"  />
						  <openmrs:globalProperty var="GMP_10_18Y_History_and_followupFormId" key="mch.GMP_10_18Y_History_and_followup" />
						  <openmrs:globalProperty var="GMP_GT_18_Examination_History_nutritionFormId" key="mch.GMP_GT_18_Examination_History_nutrition"  />

                          <openmrs:globalProperty var="IMNCI07DayFormId" key="mch.IMNCI_0-7_Day"  />
                           <openmrs:globalProperty var="IMNCI1W2MFormId" key="mch.IMNCI_1W-2M"  />
                          <openmrs:globalProperty var="IMNCI2M5YFormId" key="mch.IMNCI_2M-5Y"  />
                          <openmrs:globalProperty var="IMNCIFolloUpFormId" key="mch.IMNCI_FollowUP"  />
                           <openmrs:globalProperty var="KMCManagementFormId" key="mch.Form_KMC_Management"  />
                           <openmrs:globalProperty var="EMCExitFormId" key="mch.Form_KMC_Exit"  />
                           <openmrs:globalProperty var="NEWBORNTransfer" key="mch.NEWBORN_Transfer"  />
                           <openmrs:globalProperty var="FormIdOf024H" key="mch.Form_PNC_0_24H"  />
                           <openmrs:globalProperty var="FormIdOf14" key="mch.Form_PNC_1_4"  />
                           <openmrs:globalProperty var="GMP_GT_18_Examination_History_nutritionFormId" key="mch.GMP_GT_18_Examination_History_nutrition"  />


                              <openmrs:globalProperty var="Form_Neo_admission" key="mch.Form_Neo_admission"  />
                              <openmrs:globalProperty var="Form_Neo_admission_infant_bellow_1_month" key="mch.Form_Neo_admission_infant_bellow_1_month"  />
                               <openmrs:globalProperty var="Form_Neo_daily_evaluation_and_orders" key="mch.Form_Neo_daily_evaluation_and_orders"  />
                               <openmrs:globalProperty var="Form_Neo_nursing_assessment" key="mch.Form_Neo_nursing_assessment"  />
                                <openmrs:globalProperty var="Form_Neo_Progress_note" key="mch.Form_Neo_Progress_note"  />
                               <openmrs:globalProperty var="Form_Neo_education" key="mch.Form_Neo_education"  />


                            <c:set var="countRoutineForm" value="0" scope="page" />
							<openmrs:forEachEncounter encounters="${model.patientEncounters}" sortBy="encounterDatetime" descending="false" var="enc" num="${model.num}" >
							<c:if test="${enc.form.formId == EnrolmentFormId || enc.form.formId == RoutineFormId || enc.form.formId == EmergencyFormId || enc.form.formId == ReferralFormId  || enc.form.formId == Form_INTRAPARTUM_Assessment_and_care_of_woman_in_labour  || enc.form.formId == Form_INTRAPARTUM_Delivery_outcome_and_New_Born_Registration || enc.form.formId == ASRHEnrolment || enc.form.formId == ECDEnrolmentFormId || enc.form.formId == ECDFollowUPFormId || enc.form.formId == EPIVaccinationRecordsFormId || enc.form.formId == EPIExaminationFormId || enc.form.formId == FPEnrolment || enc.form.formId == FPMonitoringsheetandexamsfollowup || enc.form.formId == GBVEnrolmentChild || enc.form.formId == GBVEnrolmentAdult || enc.form.formId == GBVfollowup || enc.form.formId == GMP_6M_History_and_followupFormId || enc.form.formId == GMP_6M_59M_History_and_followupFormId || enc.form.formId == GMP_5_9Y_History_and_followupFormId || enc.form.formId == GMP_10_18Y_History_and_followupFormId || enc.form.formId == GMP_GT_18_Examination_History_nutritionFormId || enc.form.formId == IMNCI07DayFormId || enc.form.formId == IMNCI1W2MFormId || enc.form.formId == IMNCI2M5YFormId || enc.form.formId == IMNCIFolloUpFormId || enc.form.formId == KMCManagementFormId || enc.form.formId == EMCExitFormId || enc.form.formId == NEWBORNTransfer || enc.form.formId == FormIdOf024H || enc.form.formId == FormIdOf14}">
								<tr class='${status.index % 2 == 0 ? "evenRow" : "oddRow"}'>
									 <td class="hidden">
										<%--  this column contains the encounter id and will be used for sorting in the dataTable's encounter edit column --%>
										${enc.encounterId}
									</td>
									<td class="encounterView">
										<c:if test="${showViewLink}">
											<c:set var="viewEncounterUrl" value="${pageContext.request.contextPath}/admin/encounters/encounter.form?encounterId=${enc.encounterId}"/>
											<c:choose>
												<c:when test="${ model.formToViewUrlMap[enc.form] != null }">
													<c:url var="viewEncounterUrl" value="${model.formToViewUrlMap[enc.form]}">
														<c:param name="encounterId" value="${enc.encounterId}"/>
													</c:url>
												</c:when>
												<c:when test="${ model.formToEditUrlMap[enc.form] != null }">
													<c:url var="viewEncounterUrl" value="${model.formToEditUrlMap[enc.form]}">
														<c:param name="encounterId" value="${enc.encounterId}"/>
													</c:url>
												</c:when>
											</c:choose>
											<%-- <a href="${viewEncounterUrl}">
												<img src="${pageContext.request.contextPath}/images/file.gif" title="<openmrs:message code="general.view"/>" border="0" />
											</a> --%>
											<a href="javascript:void(0)" onClick="showViewAppendPopup('${enc.form.name}', '${enc.form.formId}','${enc.encounterId}'); return false;">
                                            <img src="${pageContext.request.contextPath}/images/file.gif" title="<openmrs:message code="general.view"/>" border="0" /></a>

                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp


                                            <a href="javascript:void(0)" onClick="showEntryAppendPopup('${enc.form.name}', '${enc.form.formId}','${enc.encounterId}'); return false;">
                                            <img src="${pageContext.request.contextPath}/images/edit.gif" title="<openmrs:message code="general.edit"/>" border="0" /></a>
											<%-- <img src="${pageContext.request.contextPath}/images/file.gif" title="<openmrs:message code="general.view"/>" border="0" /> --%>

										</c:if>
									</td>
									<td class="encounterDatetime">
										<openmrs:formatDate date="${enc.encounterDatetime}" type="small" />
									</td>
									<td class="hidden">
									<%--  this column contains the sorting order provided by ForEachEncounterTag (by encounterDatetime) --%>
									<%--  and will be used for the initial sorting and sorting in the dataTable's encounterDatetime column --%>
										${count}
									</td>
					 				<%-- <td class="encounterType"><openmrs:format encounterType="${enc.encounterType}"/></td> --%>
					 				<%-- <td class="encounterVisit">
					 					<c:if test="${enc.visit != null}"><openmrs:format visitType="${enc.visit.visitType}"/></c:if>
					 				</td> --%>
					 				<td class="encounterProvider"><openmrs:format encounterProviders="${enc.providersByRoles}"/></td>
					 				<c:choose>
					 				<c:when test="${enc.form.formId == RoutineFormId}">
					 				<c:set var="countRoutineForm" value="${countRoutineForm + 1}"/>
					 				<td class="encounterForm">${enc.form.name} Visit: ${countRoutineForm}</td>
					 				</c:when>
					 				<c:otherwise>
					 				<td class="encounterForm">${enc.form.name}</td>
					 				</c:otherwise>
					 				</c:choose>
					 				<%-- <td class="encounterLocation"><openmrs:format location="${enc.location}"/></td> --%>
					 				<%-- <td class="encounterEnterer">${enc.creator.personName}</td>  --%>
								</tr>
							</c:if>
							</openmrs:forEachEncounter>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		
		<c:if test="${model.showPagination != 'true'}">
			<script type="text/javascript">
				// hide the columns in the above table if datatable isn't doing it already 
				$j(".hidden").hide();
			</script>
		</c:if>
	</openmrs:hasPrivilege>
	
	<openmrs:htmlInclude file="/dwr/interface/DWRObsService.js" />
	<openmrs:htmlInclude file="/dwr/interface/DWRPatientService.js" />
	<openmrs:htmlInclude file="/dwr/engine.js" />
	<openmrs:htmlInclude file="/dwr/util.js" />
	<script type="text/javascript">
		<!-- // begin

		<%--
		var obsTableCellFunctions = [
			function(data) { return "" + data.encounter; },
			function(data) { return "" + data.conceptName; },
			function(data) { return "" + data.value; },
			function(data) { return "" + data.datetime; }
		];
		--%>


		function handleGetObservations(encounterId) { 
			<%--
			DWRObsService.getObservations(encounterId, handleRefreshObsData);
			document.getElementById("encounterId").value = encounterId;
			<c:choose>
				<c:when test="${viewEncounterWhere == 'newWindow'}">
					var formWindow = window.open('${pageContext.request.contextPath}/admin/encounters/encounterDisplay.list?encounterId=' + encounterId, '${enc.encounterId}', 'toolbar=no,width=800,height=600,resizable=yes,scrollbars=yes');
					formWindow.focus();
				</c:when>
				<c:when test="${viewEncounterWhere == 'oneNewWindow'}">
					var formWindow = window.open('${pageContext.request.contextPath}/admin/encounters/encounterDisplay.list?encounterId=' + encounterId, 'formWindow', 'toolbar=no,width=800,height=600,resizable=yes,scrollbars=yes');
					formWindow.focus();
				</c:when>
				<c:otherwise>
					window.location = '${pageContext.request.contextPath}/admin/encounters/encounterDisplay.list?encounterId=' + encounterId;
				</c:otherwise>
			</c:choose>
			--%>
			loadUrlIntoEncounterPopup('Test title', '${pageContext.request.contextPath}/admin/encounters/encounterDisplay.list?encounterId=' + encounterId);
		}

		<%--
		function handleRefreshObsData(data) {
  			handleRefreshTable('obsTable', data, obsTableCellFunctions);
		}
		--%>

		function handleRefreshTable(id, data, func) {
			dwr.util.removeAllRows(id);
			dwr.util.addRows(id, data, func, {
				cellCreator:function(options) {
				    var td = document.createElement("td");
				    return td;
				},
				escapeHtml:false
			});
		}

		function showHideDiv(divId) {
			var div = document.getElementById(divId);
			if ( div ) {
				if (div.style.display != "") { 
					div.style.display = "";
				} else { 
					div.style.display = "none";
				}				
			}
		}
		
		function handleAddObs(encounterField, conceptField, valueTextField, obsDateField) {
			var encounterId = dwr.util.getValue($(encounterField));
			var conceptId = dwr.util.getValue($(conceptField));
			var valueText = dwr.util.getValue($(valueTextField));
			var obsDate = dwr.util.getValue($(obsDateField));
			var patientId = ${model.patient.patientId};			
			//alert("Adding obs for encounter (" + encounterId + "): " + conceptId + " = " + valueText + " " + obsDate);  
			DWRObsService.createObs(patientId, encounterId, conceptId, valueText, obsDate);
			handleGetObservations(encounterId);
		}
			
	
		//refreshObsTable();

		// end -->
		
	</script>
</div>
</div>
