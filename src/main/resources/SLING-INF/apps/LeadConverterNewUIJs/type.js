function typefunction() {
	// CKEDITOR.instances.editor1.getData();
	var typename = document.getElementById("typeid2").value;
	alert(typename)
	if (typename == "Explore") {
		//alert("Explore");
		var editor = document.getElementById("editor1").innerHTML;
		// alert(editor);
		var type = "Explore";
		var subjectvalue = document.getElementById("subject").value;
		//alert(subjectvalue);
		//var datetimepicker = document.getElementById("datetimepicker").value;
		var urlParams = new URLSearchParams(window.location.search);
		document.getElementById("list_id").value = urlParams.get('list_id');
		var list_id = document.getElementById("list_id").value;
		var campaignvalue=document.getElementById("usercampaignvalueexplore1").value;
		//var currentdate=document.getElementById("datetimepicker").value;
		//var noofdays=document.getElementById("noofdays").value;
		$.ajax({
					type : 'POST',
					url : 'http://35.221.160.146:8082/portal/servlet/service/LEAD_CONVERTER_NodeAdd_Email.CampaignNodeAdd',
					data : {
						ckcontent : editor,
						type : type,
						subject : subjectvalue,
					//	date : datetimepicker,
						list_id : list_id,
						campaignvalue  : campaignvalue
				//		   currentdate : currentdate,
					//	   noofdays : noofdays

					},
					success : function(dataa) {

						alert(dataa);

					}

				});		
			
	} 
	else if (typename == "Inform") {
		alert("Inform");
		var editor = document.getElementById("editor1").innerHTML;
	//	alert(editor);
		var type = "Inform";
		var subjectvalue = document.getElementById("subject").value;

		//alert(subjectvalue);
		//var datetimepicker = document.getElementById("datetimepicker").value;
		var urlParams = new URLSearchParams(window.location.search);
		document.getElementById("list_id").value = urlParams.get('list_id');
		var list_id = document.getElementById("list_id").value;
		//var campaignvalue=document.getElementById("usercampaignvalue1").value;
		//var currentdate=document.getElementById("datetimepicker").value;
		//var noofdays=document.getElementById("noofdays").value;


		$.ajax({
					type : 'POST',
					url : 'http://35.201.178.201:8082/portal/servlet/service/LEAD_CONVERTER_NodeAdd_Email.CampaignNodeAdd',
					data : {
						ckcontent : editor,
						type : type,
						subject : subjectvalue,
					//	date : datetimepicker,
						list_id : list_id
					//	campaignvalue  : campaignvalue,
				//		   currentdate : currentdate,
					//	   noofdays : noofdays

					},
					success : function(dataa) {

						alert(dataa);

					}

				});
	}

	else if (typename =="Warm") {
		var editor = document.getElementById("editor1").innerHTML;
		// alert(editor);
		var type = "Warm";
		var subjectvalue = document.getElementById("subject").value;
		//alert(subjectvalue);
		//var datetimepicker = document.getElementById("datetimepicker").value;
		var urlParams = new URLSearchParams(window.location.search);
		document.getElementById("list_id").value = urlParams.get('list_id');
		var list_id = document.getElementById("list_id").value;
		//var campaignvalue=document.getElementById("usercampaignvalue1").value;
		//var currentdate=document.getElementById("datetimepicker").value;
		//var noofdays=document.getElementById("noofdays").value;
		$.ajax({
					type : 'POST',
					url : 'http://35.201.178.201:8082/portal/servlet/service/LEAD_CONVERTER_NodeAdd_Email.CampaignNodeAdd',
					data : {
						ckcontent : editor,
						type : type,
						subject : subjectvalue,
					//	date : datetimepicker,
						list_id : list_id
					//	campaignvalue  : campaignvalue,
				//		   currentdate : currentdate,
					//	   noofdays : noofdays

					},
					success : function(dataa) {

						alert(dataa);

					}

				});
	}

	else if (typename =="Entice") {
		var editor = document.getElementById("editor1").innerHTML;
		// alert(editor);
		var type = "Entice";
		var subjectvalue = document.getElementById("subject").value;
		//alert(subjectvalue);
		//var datetimepicker = document.getElementById("datetimepicker").value;
		var urlParams = new URLSearchParams(window.location.search);
		document.getElementById("list_id").value = urlParams.get('list_id');
		var list_id = document.getElementById("list_id").value;
		//var campaignvalue=document.getElementById("usercampaignvalue1").value;
		//var currentdate=document.getElementById("datetimepicker").value;
		//var noofdays=document.getElementById("noofdays").value;
		$.ajax({
					type : 'POST',
					url : 'http://35.201.178.201:8082/portal/servlet/service/LEAD_CONVERTER_NodeAdd_Email.CampaignNodeAdd',
					data : {
						ckcontent : editor,
						type : type,
						subject : subjectvalue,
					//	date : datetimepicker,
						list_id : list_id
					//	campaignvalue  : campaignvalue,
				//		   currentdate : currentdate,
					//	   noofdays : noofdays

					},
					success : function(dataa) {

						alert(dataa);

					}



				});
	}
	else if (typename == "Convert") {
		var editor = document.getElementById("editor1").innerHTML;
		// alert(editor);
		var type = "Convert";
		var subjectvalue = document.getElementById("subject").value;
		//alert(subjectvalue);
		//var datetimepicker = document.getElementById("datetimepicker").value;
		var urlParams = new URLSearchParams(window.location.search);
		document.getElementById("list_id").value = urlParams.get('list_id');
		var list_id = document.getElementById("list_id").value;
		//var campaignvalue=document.getElementById("usercampaignvalue1").value;
		//var currentdate=document.getElementById("datetimepicker").value;
		//var noofdays=document.getElementById("noofdays").value;
		$.ajax({
					type : 'POST',
					url : 'http://35.201.178.201:8082/portal/servlet/service/LEAD_CONVERTER_NodeAdd_Email.CampaignNodeAdd',
					data : {
						ckcontent : editor,
						type : type,
						subject : subjectvalue,
					//	date : datetimepicker,
						list_id : list_id
					//	campaignvalue  : cmpaignvalue,
				//		   currentdate : currentdate,
					//	   noofdays : noofdays

					},
					success : function(dataa) {

						alert(dataa);

					}



				});
	}

}