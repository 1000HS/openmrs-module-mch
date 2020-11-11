<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="java.util.*" %>
<%@ page import="org.openmrs.*" %>
<%@ page import="org.openmrs.module.mch.mapper.*" %>
<%@ page import="org.openmrs.module.mch.web.*" %>

<%@ include file="templates/include.jsp"%>
<%@ include file="templates/headerAdult.jsp"%>

<!-- Create htmlInclude tag that takes media -->

<openmrs:globalProperty key="rwandaadulthivflowsheet.ConceptId_cd4count" var="ConceptId_cd4count"/>
<openmrs:globalProperty key="rwandaadulthivflowsheet.ConceptId_ViralLoad" var="ConceptId_ViralLoad"/>

<%@ include file="fragments/popup.jspf"%> 

<script type="text/javascript">
		var data${ConceptId_cd4count} = [];
		<%
		//List<Obs> cd4List = formData.getCd4ObsList();
		//for(int index = 0; index < cd4List.size(); index++) {
		//	out.println("data"+AdultHivFlowsheetFormData.ConceptId_CD4+".push([ "+index+" , "+ cd4List.get(index).getValueNumeric()+" ]);");
		//}
		%>
		<c:forEach var="o" items="${formData.obsMapper.cd4ObsList}">
			data${ConceptId_cd4count}.push([ ${o.date.time} , ${o.cd4.valueNumeric} ]);
		</c:forEach>

		// setup background areas
	    var markings${ConceptId_cd4count} = [
//	        { color: '#f6f6f6', yaxis: { from: 2000 } },
	        { color: '#FFD2F2', yaxis: { to: 350 } }
//	        { color: '#000', lineWidth: 1, yaxis: { from: 2, to: 2 } },
//	        { color: '#000', lineWidth: 1, yaxis: { from: 8, to: 8 } }
	    ];

		var options${ConceptId_cd4count} = {
			<%= UIHelper.getCd4XAxis(formData) %>,
			//xaxis: { mode: "time", timeformat: "%d/%m/%y", },
			//xaxis: { 
			//	min: 0,
			//	max: 13,
			//	ticks: [
					<%
			//			for(int index = 0; index < 14; index++) {
			//				if(index > 0)
			//					out.print(", ");
			//				if(index < cd4List.size())
			//					out.print("["+index+", \""+formData.formatDate(AdultHivFlowsheetFormData.FormatDate_General, cd4List.get(index).getObsDatetime())+"\"]");
			//				else
			//					out.print("["+index+", \""+AdultHivFlowsheetFormData.DateTextPlaceHolder+"\"]");
			//			}
					%>
			//		] 
			//},
			yaxis: { 
				min: 0, 
				max: 3000, 
				ticks: [[0, "0"], [100, ""], [200, ""], [300, ""], [400, ""], [500, "500"], [600, ""], [700, ""], [800, ""], [900, ""], [1000, "1000"], [1100, ""], [1200, ""], [1300, ""], [1400, ""], [1500, "1500"], [1600, ""], [1700, ""], [1800, ""], [1900, ""], [2000, "2000"], [2100, ""], [2200, ""], [2300, ""], [2400, ""], [2500, "2500"], [2600, ""], [2700, ""], [2800, ""], [2900, ""], [3000, "3000"]]
			},
			grid: { markings: markings${ConceptId_cd4count} },
			points: { show: true },
			lines: { show: true }
		};

		var dataBMI = [];
		var index = 1;
		<c:forEach var="o" items="${formData.obsMapper.weightObsList}">
			dataBMI.push([ ${o.obsDatetime.time} , ${o.valueNumeric} ]);
		</c:forEach>

	
		// setup background areas
    	var markingsBMI = [
    	   <c:if test="${!empty  formData.obsMapper.latestHeight.valueNumeric}">
			{ color: '#FFD2F2', yaxis: { from: 16 * ${formData.obsMapper.latestHeight.valueNumeric} /100 * ${formData.obsMapper.latestHeight.valueNumeric} /100, to: 18.5 * ${formData.obsMapper.latestHeight.valueNumeric} /100 * ${formData.obsMapper.latestHeight.valueNumeric} /100  } },
	        { color: '#ff6347', yaxis: { to: 16 * ${formData.obsMapper.latestHeight.valueNumeric} /100 * ${formData.obsMapper.latestHeight.valueNumeric} /100  } }
			</c:if>
		];
		

		var optionsBMI = {
				<%= UIHelper.getBMIXAxis(formData) %>,
//				xaxis: { 
//					min: 1,
//					max: 21,
//					ticks: [[1, ""], [2, ""], [3, ""], [4, ""], [5, ""], [6, ""], [7, ""], [8, ""], [9, ""], [10, ""], [11, ""], [12, ""], [13, ""], [14, ""], [15, ""], [16, ""], [17, ""], [18, ""], [19, ""], [20, ""], [21, ""]]
//				},
			yaxis: { 
				min: 20, 
				max: 100, 
				ticks: [[20, ""], [22.5, ""], [25, "25"], [27.5, ""], [30, "30"], [32.5, ""], [35, "35"], [37.5, ""], [40, "40"], [42.5, ""], [45, "45"], [47.5, ""], [50, "50"], [52.5, ""], [55, "55"], [57.5, ""], [60, "60"], [62.5, ""], [65, "65"], [67.5, ""], [70, "70"], [72.5, ""], [75, "75"], [77.5, ""], [80, "80"], [82.5, ""], [85, "85"], [87.5, ""], [90, "90"], [92.5, ""], [95, "95"], [97.5, ""], [100, ""]<c:if test="${!empty  formData.obsMapper.latestHeight.valueNumeric}">, [16 * ${formData.obsMapper.latestHeight.valueNumeric} /100 * ${formData.obsMapper.latestHeight.valueNumeric} /100, "BMI 16&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"], [18.5 * ${formData.obsMapper.latestHeight.valueNumeric} /100 * ${formData.obsMapper.latestHeight.valueNumeric} /100, "BMI 18.5&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]</c:if>]
		},
		grid: { markings: markingsBMI },
		points: { show: true },
		lines: { show: true }
	};


		function plotCd4Graph() {
			var p =$j.plot($j("#obsGraph${ConceptId_cd4count}"), [{ label: "", data: data${ConceptId_cd4count} }], options${ConceptId_cd4count});
//			$j.each(p.getData()[0].data, function(i, el){
//				  var o = p.pointOffset({x: el[0], y: el[1]});
//				  $j('<div class="data-point-label">' + el[1] + '</div>').css( {
//				    position: 'absolute',
//				    left: o.left + 4,
//				    top: o.top - 43,
//				    display: 'none'
//				  }).appendTo(p.getPlaceholder()).fadeIn('slow');
//				});
		}

		function plotBMIGraph() {
			var p =$j.plot($j("#bmiGraphGraph"), [{ label: "", data: dataBMI }], optionsBMI);
		}

		function loadGraphs() {
			$j("#graphs").removeClass('ui-tabs-hide');
			plotCd4Graph();
			plotBMIGraph();
			$j("#graphs").addClass('ui-tabs-hide');					
		}

		//function registerTabListener() {
		//	$j('#flowsheet-tabs').bind('tabsshow', function(event, ui) {
		//        if(ui.index == 4)
		//        	loadGraphs();
		//	});
		//}

		window.setTimeout(loadGraphs, 1000);
		//window.setTimeout(registerTabListener, 500);
</script>

<%@ include file="fragments/load.jspf"%>

<div id="form-header-section">
<span class="view-row"><a href="javascript:window.print()"><strong>Print flowsheet</strong></a></span>
	<div class="form-header">MNCH/ FP – ASRH AND NUTRITION</div>
	<div>
		<table id="demographics-table" width="100%">
		    <c:if test="${formData.otherIdentifiers != ''}">
				<tr>
					<td colspan="4"align="left">${formData.otherIdentifiers}</td>
				</tr>
			</c:if>
			<tr>
				<td width="25%"><span class="value-label-big">NOM: </span><span class="value-data-big">${patient.personName.familyName}</span></td>
				<td width="25%"><span class="value-label-big">PRENOM: </span><span class="value-data-big">${patient.personName.givenName}</span></td>
				<td width="25%"><span class="value-label"><span class="value-label">TRACnet#: </span><span class="value-data">${formData.tracnetId}</span></td>
				<td width="25%"><span class="value-label">Nom de la FOSA: </span><span class="value-data">${formData.healthCenter}</span></td>
			</tr>
			<tr>
				<td width="25%" colspan="1"><span class="value-label">Date de naissance: </span><span class="value-date"><openmrs:formatDate date="${patient.birthdate}" format="${FormatDate_General}"/></span></td>
				<td width="25%" colspan="1">
					<c:if test="${!empty formData.arvTreatmentGroup}">
							<span class="value-label">Treatment Group:</span> <span class="value-data">${formData.arvTreatmentGroup}</span>
			        </c:if>
			    </td>
				<td width="25%" colspan="1">
					<span class="value-label">Taille: </span><span class="value-data"><openmrs:format obsValue="${formData.obsMapper.latestHeight}"/> cm </span><span class="value-date"><openmrs:formatDate date="${formData.obsMapper.latestHeight.obsDatetime}" format="${FormatDate_General}"/></span>
				</td>
			</tr>
			<tr>
				<td width="50%" colspan="2"><span class="value-label">Date d’enrôlement: </span> ${formData.programHistory}  <span class="value-label"> (jj/mm/aaaa)</span></td>
				<td width="25%" colspan="1"><span class="value-label">Date début ARV:  </span><span class="value-date"><openmrs:formatDate date="${formData.arvStartDate}" format="${FormatDate_General}"/></span> <span class="value-label"> (jj/mm/aaaa)</span></td>
			</tr>
		</table>
	</div>
</div>

<%@ include file="fragments/tabs.jspf"%>

<div id="regimen-section">
	
	<%@ include file="fragments/arvRegimen.jspf"%>
	
	<div id="tb-regimen-section"> <!-- TRAITEMENT ANTITUBERCULEUX -->
		<table class="section-table">
			<thead>
				<tr>
					<th scope="col" id="col-tb-emr-1" class="section-emr">EMR</th>
					<th scope="col" id="col-tb-info">TRAITEMENT ANTITUBERCULEUX</th>
					<th scope="col" id="col-tb-emr-2" class="section-emr">EMR</th>
					<th scope="col" id="col-tb-phase"></th>
					<th scope="col" id="col-tb-emr-3" class="section-emr">EMR</th>
					<th scope="col" id="col-tb-stop"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="episode" items="${formData.tuberculosisEpisodes}">
				<jsp:useBean id="episode" type="org.openmrs.module.mch.impl.pih.TbEpisodeMapping" />
					<tr>
						<td class="section-emr"><c:if test="${!empty episode.episodeDate}">&#x2713;</c:if></td>
						<td>
							<span class="value-label">Commencer: </span>
							<span class="value-date">
							<c:choose>
									<c:when test="${!empty episode.episodeDate}">
										<openmrs:formatDate date="${episode.episodeDate}" format="${FormatDate_General}"/>
									</c:when>
									<c:otherwise>
										${DateTextPlaceHolder}
									</c:otherwise>
								</c:choose>
						    </span>
							<span class="value-label">Crachats: </span>
								<%= UIHelper.getCheckBoxWidget(episode.isSmearPositive(), "Positif") %>
								<%= UIHelper.getCheckBoxWidget(episode.isSmearNegative(), "Négatif") %>
							<br/>
							<span class="value-label">Schéma thérapeutique: </span>
								<%= UIHelper.getCheckBoxWidget(episode.isInitialTreatment(), "Premier") %>
								<%= UIHelper.getCheckBoxWidget(episode.isRetreatment(), "Retraitement") %>
								<%= UIHelper.getCheckBoxWidget(episode.isMdrtb(), "MDR-TB") %>
							<br/>
							
							<span class="value-label">Marquer le traitement Anti-TB: </span><span class="value-data"><%= UIHelper.formatRegimenDisplaySummary(episode.getInitialDrugOrder()) %></span></td>
						<td class="section-emr"></td>
						<td>
							<span class="value-label">Phase de continuation</span><br/>
							
								<c:if test="${!empty episode.continuationPhaseDrugOrder}">
									<span class="value-data"><openmrs:formatDate date="${episode.continuationPhaseDrugOrder.startDate}" format="${FormatDate_General}"/><br/>
									(<%= UIHelper.formatRegimenDisplaySummary(episode.getContinuationPhaseDrugOrder()) %>)</span>
								</c:if>
								<c:if test="${empty  episode.continuationPhaseDrugOrder}"><span class="value-date">${DateTextPlaceHolder}</span></c:if>
						</td>
						<td class="section-emr"><c:if test="${drugOrder.discontinued}">&#x2713;</c:if></td>
						<td>
							<span class="value-label">Arrêt: </span><span class="value-date">
								<c:choose>
									<c:when test="${!empty episode.continuationPhaseDrugOrder}">
										<openmrs:formatDate date="${episode.continuationPhaseDrugOrder.discontinuedDate}" format="${FormatDate_General}"/>
									</c:when>
									<c:otherwise>
										${DateTextPlaceHolder}
									</c:otherwise>
								</c:choose>
							</span>&nbsp;&nbsp;&nbsp;
							<span class="value-label">ISSUE DE TRAITEMENT: </span>
							<br/>
							<%= UIHelper.getCheckBoxWidget(episode.isCured(), "Guéri ") %> (crachat positif au début, date crachat négatif: <c:if test="${empty episode.firstNegativeSmear}">${DateTextPlaceHolder}</c:if><c:if test="${!empty episode.firstNegativeSmear}"><openmrs:formatDate date="${episode.firstNegativeSmear.obsDatetime}" format="${FormatDate_General}"/></c:if>)
							<%= UIHelper.getCheckBoxWidget(episode.isFailed(), "Echec de traitement") %>
							<br/>
							<%= UIHelper.getCheckBoxWidget(episode.isComplete(), "Traitement complet") %>
							<%= UIHelper.getCheckBoxWidget(episode.isAbandoned(), "Abandonné") %>
							<%= UIHelper.getCheckBoxWidget(episode.isTransfered(), "Transféré") %>
							<%= UIHelper.getCheckBoxWidget(episode.isDead(), "Décès") %>
							<%= UIHelper.getCheckBoxWidget(episode.isOther(), "Autre:") %><c:if test="${episode.other}"><span class="value-data">${episode.outcomeOther}</span></c:if>
						</td> 
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div id="prophylacticRegimen"> <!-- TRAITEMENT PROPHYLACTIQUE -->
		<table class="section-table">
			<thead>
				<tr>
					<th scope="col" id="col-prophylactic-emr-1" class="section-emr">EMR</th>
					<th scope="col" colspan="2" id="col-prophylactic-info">TRAITEMENT PROPHYLACTIQUE</th>
					<th scope="col" id="col-prophylactic-emr-2" class="section-emr">EMR</th>
					<th scope="col" colspan="2" id="col-prophylactic-stop"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="prophylaxisepisode" items="${formData.prophylaxisEpisodes}">
				<jsp:useBean id="prophylaxisepisode" type="org.openmrs.module.mch.impl.pih.ProphylaxisMapping" />
					<tr>
						<td class="section-emr"><c:if test="${!empty prophylaxisepisode.startDate}">&#x2713;</c:if><c:if test="${empty prophylaxisepisode.startDate}">&nbsp;</c:if></td>
						<td>
							<span class="value-label">Medicament:</span>
							<%= UIHelper.getCheckBoxWidget(prophylaxisepisode.isCotrimoxisole(), "Cotrimoxazole") %>
							<%= UIHelper.getCheckBoxWidget(prophylaxisepisode.isFluconazole(), "Fluconazole") %>
							<%= UIHelper.getCheckBoxWidget(prophylaxisepisode.isDapsone(), "Dapsone") %>
							<br/>
							<%= UIHelper.getCheckBoxWidget(prophylaxisepisode.isProphylaxisOther(), "Autre: ") %> <span class="value-data"><%= UIHelper.formatRegimenDisplaySummary(prophylaxisepisode.getDrugOrder()) %> </span>
						</td>
						<td>
							<span class="value-label">Date de début</span><br/>
							<span class="value-date"><c:if test="${empty prophylaxisepisode.startDate}">${DateTextPlaceHolder}</c:if><c:if test="${!empty prophylaxisepisode.startDate}"><openmrs:formatDate date="${prophylaxisepisode.startDate}" format="${FormatDate_General}"/></c:if></span>
						</td>
						<td class="section-emr"><c:if test="${!empty prophylaxisepisode.stopDate}">&#x2713;</c:if><c:if test="${empty prophylaxisepisode.stopDate}">&nbsp;</c:if></td>
						<td>
							<span class="value-label">Date d’arrêt</span><br/>
							<span class="value-date"><c:if test="${empty prophylaxisepisode.stopDate}">${DateTextPlaceHolder}</c:if><c:if test="${!empty prophylaxisepisode.stopDate}"><openmrs:formatDate date="${prophylaxisepisode.stopDate}" format="${FormatDate_General}"/></c:if></span>
						</td>
						<td>
							<span class="value-label">Raison d’arrêt</span>
							<%= UIHelper.getCheckBoxWidget(prophylaxisepisode.isToxicity(), "Toxicité") %>
							<%= UIHelper.getCheckBoxWidget(prophylaxisepisode.isAbandoned(), "Abandonné") %>
							<%= UIHelper.getCheckBoxWidget(prophylaxisepisode.isOutOfStock(), "Rupture de stock") %>
							<br/>
							<%= UIHelper.getCheckBoxWidget(prophylaxisepisode.isCd4Improved(), "Amelioration de CD4") %>
							<%= UIHelper.getCheckBoxWidget(prophylaxisepisode.isReasonForStoppingOther(), "Autre:") %> <c:if test="${prophylaxisepisode.reasonForStoppingOther}"><span class="value-data">${prophylaxisepisode.discontinueReasonOther}</span></c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<%@ include file="fragments/chronicRegimens.jspf"%>
</div>
<div id="history" style="page-break-before: always">

<!--%@ include file="fragments/allergy.jspf"%>

<%@ include file="fragments/hospitalisations.jspf"%>

<%@ include file="fragments/oi.jspf"%-->
<dv id="flowsheetId"></dv>

<div id="Misc"> <!-- Misc -->
		<table class="section-table">
			<thead>
				<tr>
					<th colspan="5">CHRONIQUE</th>
				</tr>
				
				<%@ include file="fragments/chronic.jspf"%>		
</div>

<%@ include file="fragments/visit_adult.jspf"%>

<div style="page-break-before: always" id="orders">

	<%@ include file="fragments/lab_adult.jspf"%>
	<%@ include file="fragments/images_adult.jspf"%>

</div>
<div id="graphs">
	<div style="page-break-before: always" id="cd4Graph"> <!-- COURBE D'EVOLUTION DES CD4 -->
		<table class="section-graph">
			<thead>
				<tr>
					<th align="left">CD4 (cells/mm3)</th>				
					<th align="left">COURBE D'EVOLUTION DES CD4</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="2" align="center">
						<div id="obsGraph${ConceptId_cd4count}" class="cd4Graph"></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div style="height: 10px"></div>
	<div style="page-break-before: always" id="bmiGraph"> <!-- Courbe d’évolution du BMI -->
 		<table border="0" cellspacing="0">
 			<tr>
 				<td>
			<table border="1" cellspacing="0" class="bmiLegend">
				<tr>
					<th>
						BMI<br/>
						(kg/m<sup style="font-size: 6px">2</sup>)
					</th>
					<th colspan="2">
						= <u style="font-size: 6px">poids en kilogrammes</u><br/>
						&nbsp;&nbsp;&nbsp;Taille en metres<sup style="font-size: 6px">2</sup>
					</th>
				</tr>
				<tr>
					<th>
					</th>
					<th colspan="2">
						BMI
					</th>
				</tr>
				<tr>
					<td></td><td>16</td><td>18.5</td>
				</tr>
				<tr>
					<td>Taille (cm)</td><td colspan="2">Poids en Kg</td>
				</tr>
				<TR>
					<TD>140</TD>
					<TD>31</TD>
					<TD>36</TD>
				</TR>
				<TR>
					<TD>141</TD>
					<TD>32</TD>
					<TD>37</TD>
				</TR>
				<TR>
					<TD>142</TD>
					<TD>32</TD>
					<TD>37</TD>
				</TR>
				<TR>
					<TD>143</TD>
					<TD>33</TD>
					<TD>38</TD>
				</TR>
				<TR>
					<TD>144</TD>
					<TD>33</TD>
					<TD>38</TD>
				</TR>
				<TR>
					<TD>145</TD>
					<TD>34</TD>
					<TD>39</TD>
				</TR>
				<TR>
					<TD>146</TD>
					<TD>34</TD>
					<TD>39</TD>
				</TR>
				<TR>
					<TD>147</TD>
					<TD>35</TD>
					<TD>40</TD>
				</TR>
				<TR>
					<TD>148</TD>
					<TD>35</TD>
					<TD>41</TD>
				</TR>
				<TR>
					<TD>149</TD>
					<TD>36</TD>
					<TD>41</TD>
				</TR>
				<TR>
					<TD>150</TD>
					<TD>36</TD>
					<TD>42</TD>
				</TR>
				<TR>
					<TD>151</TD>
					<TD>36</TD>
					<TD>42</TD>
				</TR>
				<TR>
					<TD>152</TD>
					<TD>37</TD>
					<TD>43</TD>
				</TR>
				<TR>
					<TD>153</TD>
					<TD>37</TD>
					<TD>43</TD>
				</TR>
				<TR>
					<TD>154</TD>
					<TD>38</TD>
					<TD>44</TD>
				</TR>
				<TR>
					<TD>155</TD>
					<TD>38</TD>
					<TD>44</TD>
				</TR>
				<TR>
					<TD>156</TD>
					<TD>39</TD>
					<TD>45</TD>
				</TR>
				<TR>
					<TD>157</TD>
					<TD>39</TD>
					<TD>46</TD>
				</TR>
				<TR>
					<TD>158</TD>
					<TD>40</TD>
					<TD>46</TD>
				</TR>
				<TR>
					<TD>159</TD>
					<TD>40</TD>
					<TD>47</TD>
				</TR>
				<TR>
					<TD>160</TD>
					<TD>41</TD>
					<TD>47</TD>
				</TR>
				<TR>
					<TD>161</TD>
					<TD>41</TD>
					<TD>48</TD>
				</TR>
				<TR>
					<TD>162</TD>
					<TD>42</TD>
					<TD>49</TD>
				</TR>
				<TR>
					<TD>163</TD>
					<TD>43</TD>
					<TD>49</TD>
				</TR>
				<TR>
					<TD>164</TD>
					<TD>43</TD>
					<TD>50</TD>
				</TR>
				<TR>
					<TD>165</TD>
					<TD>44</TD>
					<TD>50</TD>
				</TR>
				<TR>
					<TD>166</TD>
					<TD>44</TD>
					<TD>51</TD>
				</TR>
				<TR>
					<TD>167</TD>
					<TD>45</TD>
					<TD>52</TD>
				</TR>
				<TR>
					<TD>168</TD>
					<TD>45</TD>
					<TD>52</TD>
				</TR>
				<TR>
					<TD>169</TD>
					<TD>46</TD>
					<TD>53</TD>
				</TR>
				<TR>
					<TD>170</TD>
					<TD>46</TD>
					<TD>53</TD>
				</TR>
				<TR>
					<TD>171</TD>
					<TD>47</TD>
					<TD>54</TD>
				</TR>
				<TR>
					<TD>172</TD>
					<TD>47</TD>
					<TD>55</TD>
				</TR>
				<TR>
					<TD>173</TD>
					<TD>48</TD>
					<TD>55</TD>
				</TR>
				<TR>
					<TD>174</TD>
					<TD>48</TD>
					<TD>56</TD>
				</TR>
				<TR>
					<TD>175</TD>
					<TD>49</TD>
					<TD>57</TD>
				</TR>
				<TR>
					<TD>176</TD>
					<TD>50</TD>
					<TD>57</TD>
				</TR>
				<TR>
					<TD>177</TD>
					<TD>50</TD>
					<TD>58</TD>
				</TR>
				<TR>
					<TD>178</TD>
					<TD>51</TD>
					<TD>59</TD>
				</TR>
				<TR>
					<TD>179</TD>
					<TD>51</TD>
					<TD>59</TD>
				</TR>
				<TR>
					<TD>180</TD>
					<TD>52</TD>
					<TD>60</TD>
				</TR>
				<TR>
					<TD>181</TD>
					<TD>52</TD>
					<TD>61</TD>
				</TR>
				<TR>
					<TD>182</TD>
					<TD>53</TD>
					<TD>61</TD>
				</TR>
				<TR>
					<TD>183</TD>
					<TD>54</TD>
					<TD>62</TD>
				</TR>
				<TR>
					<TD>184</TD>
					<TD>54</TD>
					<TD>63</TD>
				</TR>
				<TR>
					<TD>185</TD>
					<TD>55</TD>
					<TD>63</TD>
				</TR>
				<TR>
					<TD>186</TD>
					<TD>55</TD>
					<TD>64</TD>
				</TR>
				<TR>
					<TD>187</TD>
					<TD>56</TD>
					<TD>65</TD>
				</TR>
				<TR>
					<TD>188</TD>
					<TD>57</TD>
					<TD>65</TD>
				</TR>
				<TR>
					<TD>189</TD>
					<TD>57</TD>
					<TD>66</TD>
				</TR>
				<TR>
					<TD>190</TD>
					<TD>58</TD>
					<TD>67</TD>
				</TR>
				<TR>
					<TD>191</TD>
					<TD>58</TD>
					<TD>67</TD>
				</TR>
				<TR>
					<TD>192</TD>
					<TD>59</TD>
					<TD>68</TD>
				</TR>
				<TR>
					<TD>193</TD>
					<TD>60</TD>
					<TD>69</TD>
				</TR>
				<TR>
					<TD>194</TD>
					<TD>60</TD>
					<TD>70</TD>
				</TR>
				<TR>
					<TD>195</TD>
					<TD>61</TD>
					<TD>70</TD>
				</TR>
				<TR>
					<TD>196</TD>
					<TD>61</TD>
					<TD>71</TD>
				</TR>
				<TR>
					<TD>197</TD>
					<TD>62</TD>
					<TD>72</TD>
				</TR>
				<TR>
					<TD>198</TD>
					<TD>63</TD>
					<TD>73</TD>
				</TR>
				<TR>
					<TD>199</TD>
					<TD>63</TD>
					<TD>73</TD>
				</TR>
				<TR>
					<TD>200</TD>
					<TD>64</TD>
					<TD>74</TD>
				</TR>
			</table>
			</td>
			<td>
			<table class="section-graph">
				<tr>
						<td>
							<table class="section-graph">
								<thead>
									<tr>
										<th align="left" width="5%"></th>				
										<th width="99%"><center><u>Courbe d’évolution du BMI</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Taille <u>&nbsp;&nbsp;&nbsp;<openmrs:format obsValue="${formData.obsMapper.latestHeight}"/>&nbsp;&nbsp;&nbsp;</u> cm</center></th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>Poids (kg)</td>										
										<td>
											<div id="bmiGraphGraph" class="bmiGraph"></div>
										</td>
										<td></td>
									</tr>
									<tr>
									<td></td><td>Date (jj/mm/aaaa)</td><td></td>
								</tbody>
							</table>
						</td>
				</tr>
				<tr>
						<td style="border: 1px solid #000000; font-size: 8px;">
								<b><u>Instructions pour dessiner les courbes:</u></b><br/>
									1. Prend la taille du patient à sa première visite.<br/>
									2. Regarde dans le tableau le poids correspondant à cette taille dans la première colonne du BMI 16.<br/>
									3. Dessine une ligne horizontale dans la courbe du BMI partant du poids trouvé et mentionne le BMI 16 à la fin de cette ligne.<br/>
									4. Répète les opérations 2 et 3 maintenant avec le BMI 18.5<br/>
									5. Les deux lignes doivent être de différente couleur.<br/>
									6. A chaque visite il faut prendre le poids du patient et marquer les signes dans la courbe suivant les dates de visite.<br/>
									7. A partir de la variation de la courbe formée par ces signes maintenant tu peux connaitre si le BMI du patient est supérieur à 18.5 ou à 16, ou s’il est inferieur à ces valeurs.<br/>
									8. BMI &lt; 18.5 soutien nutritionnel nécessaire, BMI &lt;16 malnutrition sévère, envisager l'admission et un soutien nutritionnel.<br/>
						</td>
					</tr>
			</table>
			</td>
			</tr>
		</table>
	</div>
</div>
</div>

<%@ include file="templates/footer.jsp"%>
