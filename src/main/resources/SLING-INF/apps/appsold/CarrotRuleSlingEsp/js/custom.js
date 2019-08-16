var email = "carrotrule444_gmail.com";
var choices=[]; // ["one", "two","three"];

var choices1=[];
var jsrl={};
var ruloptions=[];
var consodlink="";
var chjson;
$(document).ready(function() {
	getPrjDetailsTable();
	// getRawTable();
	
	console.log(window.location.href);

});

$("body").on("click", ".basic", function(e) {
	console.log("test12");
	  location.reload();
	e.preventDefault();
	});

var ruleng="";
var prjName="";
var jsonData={};
//Changes by Anagha
$("body").on("click", ".set-up", function(e) {
console.log("test");
$('.setupmain')
.trigger('click');
e.preventDefault();
var $this = $(this);

prjName = $this.attr("prj-name");

console.log("prjName: " + prjName);

ruleng = $this.attr("ruleEng");
console.log("ruleng = "+ruleng);	
getRule( prjName);
//	consol.log(resultRet);

});

//advanc
var prjadv="";
var ruleadv="";
$("body").on("click", ".advanc", function(e) {
console.log("test");
$('.advanctab')
.trigger('click');
e.preventDefault();
var $this = $(this);

prjName = $this.attr("prj-name");
prjadv=prjName;
console.log("prjName: " + prjName);

ruleng = $this.attr("ruleEng");
ruleadv=ruleng;
console.log("ruleadv = "+ruleadv);

});


var result='';

function getRule( prjName){	
jsonData["user_name"]= email;
jsonData["projectname"]= prjName;

console.log(jsonData);
$.ajax({
type: 'POST',
url: '/portal/servlet/service/RuleengineList.ruleenginelist',
async: false,
data:JSON.stringify(jsonData),
contentType: 'application/json',
success: function (dataa) {
console.log(JSON.stringify(dataa));

var json = JSON.parse(dataa);
console.log(json);	
chjson=JSON.parse(dataa);
choices= json.Rule_Engine;

  choices1=json.Rule_Engine;
  console.log("choices1 "+JSON.stringify(choices1));
console.log("rulArr "+JSON.stringify(choices));
addRuleSelect("ruleone","rullist1");



}
});


}


$('.runop').click( function(e) {
	var opjs={};//jsonData
	opjs["user_name"]=email;
	opjs["project_name"]=prjName;
	console.log("ruleng = "+ruleng);	
	opjs["Rule_Engine"]=ruleng;
	console.log("run= "+JSON.stringify(opjs));
	var fid="uexcel1";
	
	if(document.getElementById(fid).value !="")
					{
		
					var reader = new FileReader();

					var fname = document.getElementById(fid).value;
					 console.log("filename-- " + fname);
					var f = document.getElementById(fid).files;
					reader.readAsDataURL(f[0]);

					reader.onloadend = function() {
						var filejs={};
						var filedata = reader.result;

						var fd = filedata.substr(0, filedata.indexOf(",") + 1);
						var fdata = filedata.replace(fd, "");
//						 console.log("filedata-- " + fdata);

						filejs["filename"] = fname;
						filejs["filedata"] = fdata;

						opjs["OutPut_Management_Data"] = filejs;
						
						console.log("mainJson- " + JSON.stringify(opjs));
						/*
						$.ajax({
							type : 'POST',
							url : '/portal/servlet/service/OutputManagement.outputfile',
							async : false,
							data : JSON.stringify(opjs),
							contentType : "application/json",
							success : function(dataa) {
								// alert(dataa);
								console.log(dataa);
								
							}
						}).fail(function(err) {

						console.log("error" + err);
						}); */
					}}else{
						console.log("- else" );
					}

	e.preventDefault();
});


$('.dwldconso').click(function getconsoclink() {
	
	$.ajax({
		type : 'POST',
		url : '/portal/servlet/service/GenericConsolidator.GenericConsolidator',
		async : false,
		data : JSON.stringify(jsonData),
		contentType : "application/json",
		success : function(dataa) {
			// alert(dataa);
			console.log(dataa);

			var json = JSON.parse(dataa);
		
			// dwldlink
			consodlink = json.fileurl;
			
			console.log("consodlink link 1 "+ consodlink);
		
			var bs = document.getElementById('consolink'); // or grab it by tagname etc
			bs.href = "http://"+consodlink;


		}
	}).fail(function(err) {

	console.log("error" + err);
	});
	
//		"http://35.236.154.164:8082/carrotruleexcelfiles/Generic/DownloadFile/carrotrule444_gmail.com/carrotrule444_gmail.comconsolidator.xls";

	

});

$('.transformula1').click(function uploadconsoformula() {
	var filejs={};
	var fid="file1";
	var formulajs={};
	
	if(document.getElementById(fid).value !="")
					{
					var reader = new FileReader();

					var fname = document.getElementById(fid).value;
					 console.log("filename-- " + fname);
					var f = document.getElementById(fid).files;
					reader.readAsDataURL(f[0]);

					reader.onloadend = function() {

						var filedata = reader.result;

						var fd = filedata.substr(0, filedata.indexOf(",") + 1);
						var fdata = filedata.replace(fd, "");
//						 console.log("filedata-- " + fdata);

						filejs["filename"] = fname;
						filejs["filedata"] = fdata;

						formulajs["user_name"] = email;
						formulajs["projectname"] = jsonData.projectname;
						formulajs["Transform"] = filejs;
						formulajs["level"] = "level1";
						console.log("mainJson- " + JSON.stringify(formulajs));
						
						$.ajax({
							type : 'POST',
							url : '/portal/servlet/service/TransformFormula.formula',
							async : false,
							data : JSON.stringify(formulajs),
							contentType : "application/json",
							success : function(dataa) {
								// alert(dataa);
								console.log("formula set");
	

							}
						}).fail(function(err) {

						console.log("error" + err);
						}); 
					}}else{
						console.log("- else" );
					}

	
	
});

$('.transformula2').click(function uploadconsoformula() {
	var filejs={};
	var fid="file2";
	var formulajs={};
	
	if(document.getElementById(fid).value !="")
					{
					var reader = new FileReader();

					var fname = document.getElementById(fid).value;
					 console.log("filename-- " + fname);
					var f = document.getElementById(fid).files;
					reader.readAsDataURL(f[0]);

					reader.onloadend = function() {

						var filedata = reader.result;

						var fd = filedata.substr(0, filedata.indexOf(",") + 1);
						var fdata = filedata.replace(fd, "");
//						 console.log("filedata-- " + fdata);

						filejs["filename"] = fname;
						filejs["filedata"] = fdata;

						formulajs["user_name"] = email;
						formulajs["projectname"] = jsonData.projectname;
						formulajs["Transform"] = filejs;
						formulajs["level"] = "level2";
						console.log("mainJson- " + JSON.stringify(formulajs));
						
						$.ajax({
							type : 'POST',
							url : '/portal/servlet/service/TransformFormula.formula',
							async : false,
							data : JSON.stringify(formulajs),
							contentType : "application/json",
							success : function(dataa) {
								// alert(dataa);
//								console.log(dataa);
								console.log("formula set");
								
							}
						}).fail(function(err) {

						console.log("error" + err);
						});
					}}else{
						console.log("- else" );
					}

	
	
});

$('.consolform').click(function uploadconsoformula() {
	var filejs={};
	var fid="file3";
	var formulajs={};
	
	if(document.getElementById(fid).value !="")
					{
					var reader = new FileReader();

					var fname = document.getElementById(fid).value;
					 console.log("filename-- " + fname);
					var f = document.getElementById(fid).files;
					reader.readAsDataURL(f[0]);

					reader.onloadend = function() {

						var filedata = reader.result;

						var fd = filedata.substr(0, filedata.indexOf(",") + 1);
						var fdata = filedata.replace(fd, "");
//						 console.log("filedata-- " + fdata);

						filejs["filename"] = fname;
						filejs["filedata"] = fdata;

						formulajs["user_name"] = email;
						formulajs["projectname"] = jsonData.projectname;
						formulajs["Consolidator"] = filejs;
						
						console.log("mainJson- " + JSON.stringify(formulajs));
						
						$.ajax({
							type : 'POST',
							url : '/portal/servlet/service/ConsolidatorFormula.formula',
							async : false,
							data : JSON.stringify(formulajs),
							contentType : "application/json",
							success : function(dataa) {
								// alert(dataa);
//								console.log(dataa);
								console.log("formula set");
								

							}
						}).fail(function(err) {

						console.log("error" + err);
						});
					}}else{
						console.log("- else" );
					}

	
	
});



$('body').on('click', '#set-up .add-more-lavel1', function() {
//	getRulelistselect();
	addRuleSelect("level2rul1","rullistad2");
	$('#set-up .test-run-step-2').css('display', 'block');
	$('#set-up .test-run-step-c2').css('display', 'block');
	$(this).removeClass('add-more-lavel1');
	$(this).addClass('add-more-lavel2');
});

$('body').on('click', '#set-up .add-more-lavel2', function() {
	
	if(document.getElementById("lastrul") !=""){
	addRuleSelect("lastrul","lasrrul");
	}
	$('#set-up .test-run-step-3').css('display', 'block');
	$(this).attr('disabled', 'disabled');
});

$('body').on('click', '#test .add-more-lavel1', function() {
	$('#test .test-run-step-2').css('display', 'block');
	$('#test .test-run-step-c2').css('display', 'block');
	$(this).removeClass('add-more-lavel1');
	$(this).addClass('add-more-lavel2');
});

$('body').on('click', '#test .add-more-lavel2', function() {
	$('#test .test-run-step-3').css('display', 'block');
	$(this).attr('disabled', 'disabled');
});

$('body').on('click', '.copy-remove-btn-b', function() {
	$(this).parents('.addMore-sub-b').remove();
});
var cnt=0;
var lv2cnt=0;
var advcnt=0;
$('body').on('click', '.add-more-btn', function() {
	/* var copy = $(this).parents('.addMore').find('.addMore-copy').html();
	$(this).parents('.addMore').find('.addMore-main').append(copy); */
	
	
	var para_id;
	para_id= $(this).attr("id");


	console.log("if"+para_id);
	var $this= $(this);
	if(para_id=="addop"){
	cnt++;
	addClassnId1($this, "opfld", "op", cnt);
	}
	
//		var copy = $(this).parents('.addMore').find('.addMore-copy').html();
//		$(this).parents('.addMore').find('.addMore-main').append(copy); 
	else if(para_id=="addrul"){
			console.log("addrul = "+para_id);
			cnt++;
			
			addClassnId1($this, "ruleadd12", "rul", cnt);
			
			var Id=document.getElementsByClassName('ruleadd12')[cnt].id;
			
			if(Id=="rul"+cnt){
				console.log("cnt = "+cnt);
				console.log("addrul class =Id "+Id);
				if(cnt==1){
				var ruleName=document.getElementById("rullist1").value;	
				console.log("if id ruleName= "+ruleName);
				}else{
				var ruleName= $('#rul'+(cnt-1)).find("#rullist1").val();	
				console.log("ruleName:cnt "+ruleName);
				
				}
				

//				  console.log("ruleName:choices org 115"+chjson.Rule_Engine);
				  var index = choices1.indexOf(ruleName);
				    if (index > -1) {
				    	choices1.splice(index, 1);
				    }

				    console.log("ruleName:choices1 "+choices1);
				    choices2=chjson.Rule_Engine;
				    console.log("ruleName:choices1 44444444"+choices2);
//				    console.log("ruleName:choices org333 "+chjson.Rule_Engine);
				   var newDiv = document.getElementById(Id);
				   var selectHTML = "";

				    selectHTML='<div class="row" ><div class="col-sm-9"><select class="form-control  bg-gray" id="rullist1">';
				    for(i = 0; i < choices1.length; i = i + 1) {
				        selectHTML += "<option value='" + choices1[i] + "'>" + choices1[i] + "</option>";
				    }

				    selectHTML += '</select></div><div class="col-sm-3 p-0"><a class="btn btn-danger copy-remove-btn sm-btn-custom sm-btn-custom2"><i class="fa fa-trash"></i></a></div>';

				    newDiv.innerHTML = selectHTML;
//				addRuleSelect("rultwo");
			}
			
			}
	else if(para_id=="level2ruladd"){
		console.log("level2ruladd = "+para_id);
		lv2cnt++;
			
		
		addClassnId1($this, "lev2rul12", "rul2plr", lv2cnt);
		
		var Id=document.getElementsByClassName('lev2rul12')[lv2cnt].id;
		
		 console.log("choices2 56"+JSON.stringify(choices2));
		
		if(Id=="rul2plr"+lv2cnt){
			console.log("addrul class =Id "+Id);
			if(lv2cnt==1){
				var ruleName=document.getElementById("rullistad2").value;	
				console.log("if id ruleName= "+ruleName);
				}else{
				var ruleName= $('#rul2plr'+(lv2cnt-1)).find("#rullist2").val();	
				console.log("ruleName:cnt "+ruleName);
				
				}
				 var index = choices2.indexOf(ruleName);
				 
				    if (index > -1) {
				    	choices2.splice(index, 1);
				    }
				    console.log("ruleName:choices2 "+choices2);
			   var newDiv = document.getElementById(Id);

			    var selectHTML = "";

			    selectHTML='<div class="row" ><div class="col-sm-9"><select class="form-control  bg-gray" id="rullist2">';
			    for(i = 0; i < choices2.length; i = i + 1) {
			    	
			        selectHTML += "<option value='" + choices2[i] + "'>" + choices2[i] + "</option>";
			    }

			    selectHTML += '</select></div><div class="col-sm-3 p-0"><a class="btn btn-danger copy-remove-btn sm-btn-custom sm-btn-custom2"><i class="fa fa-trash"></i></a></div>';

			    newDiv.innerHTML = selectHTML;

		}
		
		}else if(para_id=="advplus"){
//			var copy = $(this).parents('.addMore').find('.addMore-copy').html();
//			$(this).parents('.addMore').find('.addMore-main').append(copy); 
			advcnt++;
			
//			addRuleeng("ruleengone");
			console.log("advplus = "+para_id+advcnt);
			addClassnId1($this, "ruleengadd12", "ruleeng", advcnt);
			console.log("advcnt == "+advcnt);
			addClassnId1($this, "ruleengaddplus", "ruleengplus", advcnt);
			getrlrngadv();
			

			
		}
	else{
		var copy = $(this).parents('.addMore').find('.addMore-copy').html();
		$(this).parents('.addMore').find('.addMore-main').append(copy); 
	}
		

});

function addClassnId1($this, class_add, id_add, cntr){
	//console.log("copy: "+copy+" $this: "+$this+" class_add: "+class_add+" id_add: "+id_add+" cntr: "+cntr);
	$this.parents('.addMore').find('.addMore-copy').find('.addMore-sub').addClass(class_add).attr('id', id_add+cntr);
	var copy=$this.parents('.addMore').find('.addMore-copy').html();	
	$this.parents('.addMore').find('.addMore-main').append(copy);
	$this.parents('.addMore').find('.addMore-copy').find('.addMore-sub').removeClass(class_add).removeAttr('id', id_add+cntr);
	$('.selectpicker').selectpicker('refresh');
	$this.parents('.addMore').find('.addMore-sub:last-child').find(".dropdown-toggle:first").css("display","none");
	$this.parents('.addMore').find('.addMore-sub:last-child').find('.form-group').find(".dropdown-toggle:first").css("display","none");
	}

$('body').on('click', '.copy-remove-btn', function() {
	$(this).parents('.addMore-sub').remove();
});

$('.more-tr .add')
		.click(
				function() {
					var addTr = $(this).parents('.tab-pane.active').find(
							'#draggable table tbody tr:last-child').html();
					addTr = '<tr>' + addTr + '</tr>';
					$(this).parents('.tab-pane.active').find(
							'#draggable table tbody').append(addTr);
					var a = $(this).parents('.tab-pane.active').find(
							'#draggable table tbody tr:last-child').find(
							'td:first-child').html();
					a = (parseInt(a) + 1)
					$(this).parents('.tab-pane.active').find(
							'#draggable table tbody tr:last-child').find('td')
							.text('');
					$(this).parents('.tab-pane.active').find(
							'#draggable table tbody tr:last-child').find(
							'td:first-child').text(a);
				});

$('.more-tr .remove').click(
		function() {
			var firstTr = $(this).parents('.tab-pane.active').find(
					'#draggable table tbody tr:last-child').attr('class');
			if (firstTr != 'firstTr') {
				$(this).parents('.tab-pane.active').find(
						'#draggable table tbody tr:last-child').remove();
			}
		});

$('.more-td .add')
		.click(
				function() {
					$(this)
							.parents('.tab-pane.active')
							.find('#draggable table thead tr')
							.append(
									'<th>Added New <i class="fa fa-times text-danger remove-added-new-added-rule"></i></th>');
					$(this).parents('.tab-pane.active').find(
							'#draggable table tbody tr').append('<td></td>');

					var scrollLeft = $(this).parents('.tab-pane.active').find(
							'#draggable .draggable-table').width();
					$(this).parents('.tab-pane.active').find(
							'#draggable .draggable-table').scrollLeft(
							scrollLeft);
				});

$('.more-td .remove').click(
		function() {
			var lastTD = $(this).parents('.tab-pane.active').find(
					'#draggable table thead tr th:last-child').attr(
					'data-class');
			if (lastTD != 'lastTD') {
				$(this).parents('.tab-pane.active').find(
						'#draggable table thead tr th:last-child').remove();
				$(this).parents('.tab-pane.active').find(
						'#draggable table tbody tr td:last-child').remove();
			}
		});

$('#transformation .more-tdT .add')
		.click(
				function() {
					$(this).parents('.tab-pane.active').find(
							'#draggable table thead tr:nth-child(3)').append(
							'<th class="bg-info"></th>');
					$(this)
							.parents('.tab-pane.active')
							.find('#draggable table thead tr:nth-child(2)')
							.append(
									'<th>Added New <i class="fa fa-times text-danger remove-added-new"></i></th>');
					$(this).parents('.tab-pane.active').find(
							'#draggable table tbody tr').append('<td></td>');

					var scrollLeft = $(this).parents('.tab-pane.active').find(
							'#draggable .draggable-table').width();
					$(this).parents('.tab-pane.active').find(
							'#draggable .draggable-table').scrollLeft(
							scrollLeft);
				});

$('#transformation .more-tdT .remove').click(
		function() {
			var lastTD = $(this).parents('.tab-pane.active').find(
					'#draggable table thead tr th:last-child').attr(
					'data-class');
			if (lastTD != 'lastTD') {
				$(this).parents('.tab-pane.active').find(
						'#draggable table thead tr:nth-child(2) th:last-child')
						.remove();
				$(this).parents('.tab-pane.active').find(
						'#draggable table thead tr:nth-child(3) th:last-child')
						.remove();
				$(this).parents('.tab-pane.active').find(
						'#draggable table tbody tr td:last-child').remove();
			}
		});

$('#calculationBuilder .more-tdT .add')
		.click(
				function() {
					var colspan = $(this).parents('.tab-pane.active').find(
							'table thead .transformed-data-th').attr('colspan');
					colspan++;
					$(this).parents('.tab-pane.active').find(
							'table thead .transformed-data-th').attr('colspan',
							colspan);
					
					
					$(this)
							.parents('.tab-pane.active')
							.find('table thead tr:nth-child(2)')
							.append(
									'<th>Added New <i class="fa fa-times text-danger remove-added-new-calc-builder"></i></th>');
					$(this).parents('.tab-pane.active').find('table tbody tr')
							.append('<td></td>');

					var scrollLeft = $(this).parents('.tab-pane.active').find(
							'.draggable-table').width();
					$(this).parents('.tab-pane.active')
							.find('.draggable-table').scrollLeft(scrollLeft);
				});

$('#calculationBuilder .more-tdT .remove').click(
		function() {
			var colspan = $(this).parents('.tab-pane.active').find(
					'table thead .transformed-data-th').attr('colspan');
			colspan--;
			$(this).parents('.tab-pane.active').find(
					'table thead .transformed-data-th')
					.attr('colspan', colspan);
			var lastTD = $(this).parents('.tab-pane.active').find(
					'table thead tr th:last-child').attr('data-class');
			if (lastTD != 'lastTD') {
				$(this).parents('.tab-pane.active').find(
						'table thead tr:nth-child(2) th:last-child').remove();
				$(this).parents('.tab-pane.active').find(
						'table tbody tr td:last-child').remove();
			}
		});

$('body').on(
		'click',
		'.remove-added-new',
		function() {
			var index = $(this).parent('th').index();
			index++;
			$(this).parents('.table').find(
					"thead tr:nth-child(2) th:nth-child(" + index
							+ "), thead tr:nth-child(3) th:nth-child(" + index
							+ "), tbody tr td:nth-child(" + index + ")")
					.remove();
		});

$('body').on(
		'click',
		'.remove-added-new-trans-data',
		function() {
			var index = $(this).parent('th').index();
			index++;
			$(this).parents('.table').find(
					"thead tr:nth-child(2) th:nth-child(" + index
							+ "), thead tr:nth-child(3) th:nth-child(" + index
							+ "), tbody tr td:nth-child(" + index + ")")
					.remove();
		});

$('body').on(
		'click',
		'.remove-added-new-added-rule',
		function() {
			var index = $(this).parent('th').index();
			index++;
			$(this).parents('.table').find(
					"thead tr th:nth-child(" + index
							+ "), tbody tr td:nth-child(" + index + ")")
					.remove();
		});

$('body').on(
		'click',
		'.remove-added-new-calc-builder',
		function() {
			var index = $(this).parent('th').index();
			index++;
			$(this).parents('.table').find(
					"thead tr th:nth-child(" + index
							+ "), tbody tr td:nth-child(" + index + ")")
					.remove();
		});

$('#transformation .more-trT .add')
		.click(
				function() {
					var addTr = $(this).parents('.tab-pane.active').find(
							'#draggable table tbody tr:last-child').html();
					addTr = '<tr>' + addTr + '</tr>';
					$(this).parents('.tab-pane.active').find(
							'#draggable table tbody').append(addTr);
					var a = $(this).parents('.tab-pane.active').find(
							'#draggable table tbody tr:last-child').find(
							'td:first-child').html();
					a = (parseInt(a) + 1)
					$(this).parents('.tab-pane.active').find(
							'#draggable table tbody tr:last-child').find('td')
							.text('');
					$(this).parents('.tab-pane.active').find(
							'#draggable table tbody tr:last-child').find(
							'td:first-child').text(a);
				});

$('#transformation .more-trT .remove').click(
		function() {
			var firstTr = $(this).parents('.tab-pane.active').find(
					'#draggable table tbody tr:last-child').attr('class');
			if (firstTr != 'firstTr') {
				$(this).parents('.tab-pane.active').find(
						'#draggable table tbody tr:last-child').remove();
			}
		});

$('.plus-minus .plus').click(function() {
	var copy = $(this).parents('.plus-minus').find('.copy-plus-minus').html();
	$(this).parents('.plus-minus').find('.plus-minus-sub').append(copy);
});

$('.plus-minus .minus').click(
		function() {
			var first = $(this).parents('.plus-minus').find(
					'.plus-minus-sub .form-group:last-child .form-control')
					.attr('data-class');
			if (first != 'first') {
				$(this).parents('.plus-minus').find(
						'.plus-minus-sub .form-group:last-child').remove();
			}
		});

$('.cal-main .plus').click(
		function() {
			var copy = $(this).parents('.cal-main').find('.copy-cal').html();
			$(this).parents('.cal-main').find('.cal-sub').append(copy);

			$('.selectpicker').selectpicker('refresh');
			$(this).parents('.cal-main').find('.cal-sub').find(
					".custom-cal-sub:last").find(".bs-placeholder:first").css(
					"display", "none");
			$(this).parents('.cal-main').find('.cal-sub').find(
					".custom-cal-sub:last").find(".sf-remove").find(
					".bs-placeholder:first").css("display", "none");
		});

$('.cal-main .minus').click(
		function() {
			var first = $(this).parents('.cal-main').find(
					'.cal-sub .row:last-child').attr('data-class');
			if (first != 'first') {
				$(this).parents('.cal-main').find('.cal-sub .row:last-child')
						.remove();
			}
		});

$('.comunication-sectioin-main-last .plus').click(
		function() {
			var copy = $(this).parents('.comunication-sectioin-main-last')
					.find('.comunication-sectioin-main-copy-sub').html();
			$(this).parents('.comunication-sectioin-main-last').find(
					'.comunication-sectioin-main-last-sub').append(copy);
		});

$('.comunication-sectioin-main-last .minus')
		.click(
				function() {
					var first = $(this)
							.parents('.comunication-sectioin-main-last')
							.find(
									'.comunication-sectioin-main-last-sub .row:last-child')
							.attr('data-class');
					if (first != 'first') {
						$(this)
								.parents('.comunication-sectioin-main-last')
								.find(
										'.comunication-sectioin-main-last-sub .row:last-child')
								.remove();
					}
				});

$('body').on(
		'dblclick',
		'table tr td',
		function() {
			if (!$(this).hasClass('simple-td')) {
				$(this).html(
						"<input type='text' class='inp' value='"
								+ $(this).text() + "' />");
			}
		});

$('body')
		.on(
				'dblclick',
				'.table-transform tr th',
				function() {
					if (!$(this).hasClass('simple-th')) {
						$(this)
								.html(
										"<input type='text' class='inp' value='"
												+ $(this).text()
												+ "' /><i class='fa fa-times text-danger remove-added-new-trans-data'></i>");
					}
				});

$('body')
		.on(
				'focusout',
				'.table-transform tr th .inp',
				function() {
					$(this)
							.parent("th")
							.html(
									$(this).val()
											+ "<i class='fa fa-times text-danger remove-added-new-trans-data'></i>");
				});

$('body').on('focusout', 'table tr td .inp', function() {
	$(this).parent("td").html($(this).val());
});

$('body')
		.on(
				'dblclick',
				'.table-add-rules tr th',
				function() {
					if (!$(this).hasClass('simple-th')) {
						$(this)
								.html(
										"<input type='text' class='inp' value='"
												+ $(this).text()
												+ "' /><i class='fa fa-times text-danger remove-added-new-added-rule'></i>");
					}
				});

$('body')
		.on(
				'focusout',
				'.table-add-rules tr th .inp',
				function() {
					$(this)
							.parent("th")
							.html(
									$(this).val()
											+ "<i class='fa fa-times text-danger remove-added-new-added-rule'></i>");
				});

$('body')
		.on(
				'dblclick',
				'.table-calc-builder tr th',
				function() {
					if (!$(this).hasClass('simple-th')) {
						$(this)
								.html(
										"<input type='text' class='inp' value='"
												+ $(this).text()
												+ "' /><i class='fa fa-times text-danger remove-added-new-calc-builder'></i>");
					}
				});

$('body')
		.on(
				'focusout',
				'.table-calc-builder tr th .inp',
				function() {
					$(this)
							.parent("th")
							.html(
									$(this).val()
											+ "<i class='fa fa-times text-danger remove-added-new-calc-builder'></i>");
				});

$("body").on("change", ".eventnew .schedule_push", function() {
	var val = $(this).attr('value');

	if (val == "push") {
		$('.schedule_push_hideShow').css('display', 'none');
	} else {
		$('.schedule_push_hideShow').css('display', 'block');
	}
});
$("body").on("change", ".selectpicker-output", function() {
	var val = $(this).val();
	$(this).parents('.custom-cal-sub').find('.upfront').val(val);
	$(this).parents('.custom-cal-sub').find('.annual').val(val);
});

$("body").on(
		"click",
		".dropdown-toggle",
		function() {
			var Sheight = $(this).parent('.bootstrap-select').find(
					'.dropdown-menu.open').height();
			Sheight = Sheight + 30;
			$('.tab-content').css('padding-bottom', Sheight);
		});
$("body").on("focusout", ".dropdown-toggle", function() {
	$('.tab-content').css('padding-bottom', '15');
});

var items = {
	'Account' : [ 'AccountId', 'AccountName' ],
	'Case' : [ 'CaseId', 'CaseSubject' ],
	'Lead' : [ 'LeadId', 'LeadName' ],
	'Oppurtunity' : [ 'OppurtunityId', 'OppurtunityName' ]
};

$("body")
		.on(
				"change",
				".sf-object",
				function() {
					var heading = $(this).val();
					console.log(heading);
					if ($(".box-left").find('h3[data-name="' + heading + '"]')
							.text() == '') {
						$(".box-left").append(
								'<div class="list-part"><h3 data-name="'
										+ heading + '">' + heading
										+ '</h3><ul><li>' + items[heading]['0']
										+ '</li><li>' + items[heading]['1']
										+ '</li></ul></div>');
					}
					console.log(items[heading]);
				});

$("body").on("click", ".box li", function() {
	$(this).toggleClass('selected');
});

$("body")
		.on(
				"click",
				".click-right",
				function() {
					$(".box li.selected")
							.each(
									function() {
										var heading = $(this).parent("ul")
												.parent("div").find("h3")
												.text();
										var liText = $(this).text();

										if ($(".box-right").find(
												'h3[data-name="' + heading
														+ '"]').text() != '') {
											$(".box-right").find(
													'h3[data-name="' + heading
															+ '"]').parent(
													"div").find("ul").append(
													"<li>" + liText + "</li>");
										} else {
											$(".box-right")
													.append(
															'<div class="list-part"><h3 data-name="'
																	+ heading
																	+ '">'
																	+ heading
																	+ '</h3><ul><li>'
																	+ liText
																	+ '</li></ul></div>');
										}

										if ($(this).parent("ul").parent("div")
												.find("ul li").length == 1) {
											$(this).parent("ul").parent("div")
													.remove();
										} else {
											$(this).remove();
										}
									});
				});

$("body")
		.on(
				"click",
				".click-left",
				function() {
					$(".box li.selected")
							.each(
									function() {
										var heading = $(this).parent("ul")
												.parent("div").find("h3")
												.text();
										var liText = $(this).text();

										if ($(".box-left").find(
												'h3[data-name="' + heading
														+ '"]').text() != '') {
											$(".box-left").find(
													'h3[data-name="' + heading
															+ '"]').parent(
													"div").find("ul").append(
													"<li>" + liText + "</li>");
										} else {
											$(".box-left")
													.append(
															'<div class="list-part"><h3 data-name="'
																	+ heading
																	+ '">'
																	+ heading
																	+ '</h3><ul><li>'
																	+ liText
																	+ '</li></ul></div>');
										}

										if ($(this).parent("ul").parent("div")
												.find("ul li").length == 1) {
											$(this).parent("ul").parent("div")
													.remove();
										} else {
											$(this).remove();
										}
									});
				});

$("body")
		.on(
				"click",
				".click-right-primary-key",
				function() {
					$(".box-right li.selected")
							.each(
									function() {
										var heading = $(this).parent("ul")
												.parent("div").find("h3")
												.text();
										var liText = $(this).text();
										$('.primary-key-external-data').attr(
												'value', liText);

										if ($(".box-primary-key").find(
												'h3[data-name="' + heading
														+ '"]').text() != '') {
											$(".box-primary-key").find(
													'h3[data-name="' + heading
															+ '"]').parent(
													"div").find("ul").html(
													"<li>" + liText + "</li>");
										} else {
											$(".box-primary-key")
													.html(
															'<h2 class="box-title">Select Primary Key</h2><div class="list-part"><h3 data-name="'
																	+ heading
																	+ '">'
																	+ heading
																	+ '</h3><ul><li>'
																	+ liText
																	+ '</li></ul></div>');
										}
									});
				});

$("body").on("click", ".click-left-primary-key", function() {
	$(".box-primary-key li.selected").each(function() {
		var heading = $(this).parent("ul").parent("div").find("h3").text();
		$('.primary-key-external-data').attr('value', '');

		if ($(this).parent("ul").parent("div").find("ul li").length == 1) {
			$(this).parent("ul").parent("div").remove();
		} else {
			$(this).remove();
		}
	});
});

var input = document.getElementById('file-upload');
var infoArea = document.getElementById('file-upload-filename');
if (input) {
	input.addEventListener('change', showFileName);
}
function showFileName(event) {
	var input = event.srcElement;
	var fileName = input.files[0].name;
	infoArea.textContent = fileName;
}

var input2 = document.getElementById('xls');
var infoArea2 = document.getElementById('xls-filename');
if (input2) {
	input2.addEventListener('change', showFileName2);
}
function showFileName2(event) {
	var input2 = event.srcElement;
	var fileName2 = input2.files[0].name;
	infoArea2.textContent = fileName2;
}

var input3 = document.getElementById('file-upload-cal');
var infoArea3 = document.getElementById('file-upload-filename-cal');
if (input3) {
	input3.addEventListener('change', showFileName3);
}
function showFileName3(event) {
	var input3 = event.srcElement;
	var fileName3 = input3.files[0].name;
	infoArea3.textContent = fileName3;
}

var input4 = document.getElementById('uexcel1');
var infoArea4 = document.getElementById('uexcel1-filename');
if (input4) {
	input4.addEventListener('change', showFileName4);
}
function showFileName4(event) {
	var input4 = event.srcElement;
	var fileName4 = input4.files[0].name;
	infoArea4.textContent = fileName4;
}

$('.web-services-add')
		.click(
				function() {
					$('.web-services').toggleClass('display-block',
							'display-none');

					var webUrl = document.getElementById("web-services-url").value;

					if (webUrl != '') {
						var a = '';
						a = a + '<div class="row form-group">';
						a = a + '<div class="col-sm-3">';
						a = a
								+ '<input type="text" class="form-control" value="'
								+ webUrl + '" placeholder="URL Name">';
						a = a + '</div>';
						a = a + '<div class="col-sm-3">';
						a = a
								+ '<button class="btn btn-warning sm-btn-custom"><i class="fa fa-edit"></i></button> ';
						a = a
								+ '<button class="btn btn-danger sm-btn-custom"><i class="fa fa-trash"></i></button>';
						a = a + '</div>';
						a = a + '</div>';

						$('.add-url-name-main').append(a);
						$('.web-services input').val('');
						$('.web-services select').val('');
					}
				});

$('.basic-pro-edit').click(function() {
	$('.sf-objects').trigger('click');
});

$(".dropdown-toggle").mouseenter(function() {
	$(this).parents('#transformation').find('.float-right').addClass('open');
}).mouseleave(
		function() {
			$(this).parents('#transformation').find('.float-right')
					.removeClass('open');
		});

$(".dropdown-menu").mouseenter(function() {
	$(this).parents('#transformation').find('.float-right').addClass('open');
}).mouseleave(
		function() {
			$(this).parents('#transformation').find('.float-right')
					.removeClass('open');
		});

$(".dropdown-toggle-addRule").mouseenter(function() {
	$(this).parent('.float-right').addClass('open');
}).mouseleave(function() {
	$(this).parent('.float-right').removeClass('open');
});

$(".dropdown-menu-addRule").mouseenter(function() {
	$(this).parent('.float-right').addClass('open');
}).mouseleave(function() {
	$(this).parent('.float-right').removeClass('open');
});

$('.admin').click(function() {
	$('.createNewF').trigger('click');
});
$('.save-next-createNew').click(
		function() {
			if ($('#externalData').is(':checked')) {
				$('.externalDataF').trigger('click');
			} else if ($('#inputTransformation').is(':checked')) {
				$('.transformationF').trigger('click');
				$('.externalDataF').addClass('tab-diactive');
				$('.transformationF').removeClass('tab-diactive');
			} else if ($('#inputTransformation').not(':checked')
					&& $('#externalData').not(':checked')) {
				$('.addRulesF').trigger('click');
				$('.externalDataF').addClass('tab-diactive');
				$('.transformationF').addClass('tab-diactive');
			}
		});

$('.createNew-pre').click(function() {
	$('.basic').trigger('click');
});

$('.createNew-next')
		.click(
				function() {
					var mainJson = {};
					var project_name = document.getElementById("project_name").value;
					var Project_Description = document
							.getElementById("Project_Description").value;
					var Rule_Engine = document.getElementById("Rule_Engine").value;
					var Rule_Engine_Description = document
							.getElementById("Rule_Engine_Description").value;

					mainJson["user_name"] = email;
					mainJson["project_name"] = project_name;
					mainJson["Project_Description"] = Project_Description;
					mainJson["Rule_Engine"] = Rule_Engine;
					mainJson["Rule_Engine_Description"] = Rule_Engine_Description;
					document.getElementById('myprj').innerHTML = document
							.getElementById("project_name").value;
					
					document.getElementById("prjName_RE").innerHTML=project_name;
					document.getElementById("REName").innerHTML=Rule_Engine;
					document.getElementById("myprj3").innerHTML=project_name;
					document.getElementById("REName3").innerHTML=Rule_Engine;
					
					
					console.log(JSON.stringify(mainJson));
					$
							.ajax(
									{
										type : 'POST',
										url : '/portal/servlet/service/Create_New_page.createNode',
										async : true,
										data : JSON.stringify(mainJson),
										contentType : "application/json",
										success : function(dataa) {
											// alert(dataa);
											console.log(dataa);

											console
													.log("in ext- "
															+ document
																	.getElementById("project_name").value);
											// var json=JSON.parse(dataa);
										
											
										}
									}).fail(function(err) {

								console.log("error" + err);
							});
					$('.externalDataF')
					.trigger('click');

				});

$('.externalData-pre').click(function() {
	$('.sf-objects').trigger('click');
});
var dwldlink = "";
var excelfields = [];
$('.externalData-next')
		.click(
				function() {
					var mainJson = {};
				
					document.getElementById('myprj1').innerHTML = document
							.getElementById("project_name").value;
					mainJson["user_name"] = email;
					mainJson["project_name"] = document.getElementById('myprj').innerHTML;
					//get external obj
					var webserviceJson={};
					var prjName=document.getElementById("myprj").value;
					var url= document.getElementById("web-services-url").value;
					console.log(url);
					var token=document.getElementById("token").value;
					var username=document.getElementById("username").value;
					var password=document.getElementById("Password").value;
					var urlJson={};
					var urlArr=[];
					var exDataJson={};
					var opFieldArr=[];

					urlJson["url"]=url;
					urlJson["token"]=token;
					urlJson["username"]=username;
					urlJson["password"]=password;

					urlArr.push(urlJson);

					opFieldArr=getExparamObject();

					exDataJson["URL"]=urlArr;
					exDataJson["output_field"]=opFieldArr;
					var extejs={};
					extejs["EXTERNAL_DATA"]=exDataJson;
					console.log("extejson :: "+extejs);
					mainJson["WebServices"] = extejs;
					//end
					var filearr = [];
					var fid = "xls";
					// get base64data of file
					var filejs = {};
					if(document.getElementById(fid).value !="")
					{
					var reader = new FileReader();

					var fname = document.getElementById(fid).value;
					// console.log("filename-- " + fname);
					var f = document.getElementById(fid).files;
					reader.readAsDataURL(f[0]);

					reader.onloadend = function() {

						var filedata = reader.result;

						var fd = filedata.substr(0, filedata.indexOf(",") + 1);
						var fdata = filedata.replace(fd, "");
						// console.log("filedata-- " + fdata);

						filejs["filename"] = fname;
						filejs["filedata"] = fdata;

						// console.log("json file- " + JSON.stringify(filejs));

						// new
						var far = [];

						far.push(filejs);

						mainJson["File_Data"] = far;
						console.log("mainJson- " + JSON.stringify(mainJson));

					
						$
								.ajax(
										{
											type : 'POST',
											url : '/portal/servlet/service/Add_External_Parameter.External',
											async : true,
											data : JSON.stringify(mainJson),
											contentType : "application/json",
											success : function(dataa) {
												// alert(dataa);
												console.log(dataa);
	
												var json = JSON.parse(dataa);
											
												// dwldlink
												dwldlink = json.Download_Excel;
												console.log("dwld link :: "
														+ dwldlink);

//												console.log(" fields length :: "+ json.Message.EXCEL_DATA.length);

												for (var i = 0; i < json.Message.EXCEL_DATA.length; i++) {
													excelfields
															.push(json.Message.EXCEL_DATA[i].field);
													
												}
//												console.log("array= "	+ excelfields);

												getRawTable(excelfields);
											}
										}).fail(function(err) {

									console.log("error" + err);
								});

					} 
				}

					$('.transformationF').trigger('click');
				});

$('.transform-pre').click(function() {
	$('.externalDataF').trigger('click');
});
var fnames=[];
var transheader=[];
$('.transform-next').click(function() {
	var mainJson = {};
	var fid = "file-upload";

	// get base64data of file
	var filejs = {};
	var reader = new FileReader();
	var transjs = {};
	var fname = document.getElementById(fid).value;  
	 var columnCount = excelfields.length;
	if(fname !=""){
//	 console.log("filename-- " + fname);
	var f = document.getElementById(fid).files;
	reader.readAsDataURL(f[0]);

	reader.onloadend = function() {

		var filedata = reader.result;

		var fd = filedata.substr(0, filedata.indexOf(",") + 1);
		var fdata = filedata.replace(fd, "");
		

		filejs["filename"] = fname;
		filejs["filedata"] = fdata;
	
		mainJson["user_name"] = email;
		mainJson["project_name"] = document.getElementById('myprj1').innerHTML;
		mainJson["Transform_File_Data"] = filejs;
		console.log("MainJSON:: " + JSON.stringify(mainJson));
		
		// to display header
//		var flds=[];
		
//    	console.log("columnCount= ="+columnCount);
    	
      

	
	 $.ajax({ type : 'POST',
		 url :	 '/portal/servlet/service/Transform_validation.form', async : true, data :
	  JSON.stringify(mainJson), 
	  contentType : "application/json",
	  success :
	  function(dataa) { 
		  
		  console.log(dataa);
		
		  var json = JSON.parse(dataa); 
//		  console.log("json :: " +	  JSON.stringify(json)); // dwldlink
		 
		
		  for(var i=0;i<json.Save_Data.length;i++){
			  
			  transheader.push(json.Save_Data[i].fieldname);
		
		  }
//		  console.log("transheader :: " +	  JSON.stringify(transheader));
		  
			 for (var i = 0; i < transheader.length; i++) {
		  		   var row = document.getElementById("rulerow");
		  		   var x = row.insertCell(-1);

		  	    	  x.className += "bg-success operators-td simple-th";
		  	    	 
		  	    	   x.innerHTML=transheader[i]+'<select name="operators" ><option value="= Equals" >= Equals</option><option value="!= Not Equals">!= Not Equals</option><option value="> Greater Than">> Greater Than</option><option value="< Less Than">< Less Than</option><option value=">= Greater Than Equals">>= Greater Than Equals</option><option value="<= Less Than Equals"><= Less Than Equals</option><option value="Is Null">Is Null</option><option value="Is Not Null">Is Not Null</option></select>';
		  	    	   //insert td
		  	    	   var row1 = document.getElementById("firsttr");
			  		   var x1 = row1.insertCell(-1);
						 
				    	  x1.innerHTML = '<td></td><td></td><td></td>';
				    	
				    	
				    	   var row2 = document.getElementById("secondtr");
				  		   var x2 = row2.insertCell(-1);
							 
					       x2.innerHTML = '<td></td><td></td><td></td>';
					    	
				    	  
					    	  var row3 = document.getElementById("thirdtr");
					  		   var x3 = row3.insertCell(-1);
								 
						    	  x3.innerHTML = '<td></td><td></td><td></td>';
						    
				    	  
				
			 
			 }
			 

			
		  
		  
	   } }).fail(function(err) {
	  
	  console.log("error" + err); });
	 

	}
	}else{
		
		 for (var i = 0; i <columnCount; i++) {
  		   var row = document.getElementById("rulerow");
  		   var x = row.insertCell(-1);

  	    	  x.className += "bg-success operators-td simple-th";
//  	    	   console.log("columnCount-i= "+columnCount-i);

  	    	  x.innerHTML=excelfields[i]+'<select name="operators" ><option value="= Equals" >= Equals</option><option value="!= Not Equals">!= Not Equals</option><option value="> Greater Than">> Greater Than</option><option value="< Less Than">< Less Than</option><option value=">= Greater Than Equals">>= Greater Than Equals</option><option value="<= Less Than Equals"><= Less Than Equals</option><option value="Is Null">Is Null</option><option value="Is Not Null">Is Not Null</option></select>';

  	    	//insert td
  	    	   var row1 = document.getElementById("firsttr");
	  		   var x1 = row1.insertCell(-1);
				 
		       x1.innerHTML = '<td></td><td></td><td></td>';
		       var row2 = document.getElementById("secondtr");
		  	   var x2 = row2.insertCell(-1);
			   x2.innerHTML = '<td></td><td></td><td></td>';
			   var row3 = document.getElementById("thirdtr");
			   var x3 = row3.insertCell(-1);
			   x3.innerHTML = '<td></td><td></td><td></td>';
  	    	   
  	    	   
		 } 
		 
		 
//		 fnames.push("SR No");
//		   fnames.push("Rule Name");
////		    console.log("fnames = "+fnames);
//	
////	   console.log("fnames[j.length= "+fnames.length);
//	   for(var j=1;j<=fnames.length;j++){
//		   var row = document.getElementById("rulerow");
//		   var x = row.insertCell(0);
////			   console.log("fnames[j.length-j] = "+fnames[fnames.length-j]);
//	    	  x.innerHTML = fnames[fnames.length-j];
//	    	  x.className += "simple-th";
//		   }  
	   
		   
    
	
  		  
  	  } 
		

	$('.addRulesF').trigger('click');
});

$('.addRule-pre').click(function() {
	$('.transformationF').trigger('click');
});
var caldwldlink="";
$('.addRule-next').click(function() {


	    var sfdata=[];
		var arrfields = [];
		// get all operators
		var fieldnamenew = document.getElementsByName("operators");
		if(fieldnamenew!=""){
//		console.log("fieldnamenew.length= " + fieldnamenew.length);
		for (var i = 0; i < fieldnamenew.length ; i++) {
			var fieldsjs= {};
			if(document.getElementById("file-upload").value !=""){
				fieldsjs["field"]=transheader[i];
			}else {
			
				if(excelfields.length>0){
			fieldsjs["field"]=excelfields[i];
		
				}
			}

			fieldsjs["Category"]=fieldnamenew[i].value;
//			console.log("fieldsjs json= " + fieldsjs);
//			arrfields.push(fieldnamenew[i].value);
			sfdata.push(fieldsjs);
		}
		console.log("all operators :: " + JSON.stringify(sfdata));

	}

	var oTable = document.getElementById('ruleheadertab');
	
    //gets rows of table
    var rowLength = oTable.rows.length;
    var datarrnew=[];
    var firstrow=[];
    //loops through rows   
    
    var datajsnew={};
    
//    console.log("iiii= "+i);
    for (var i = 0; i < rowLength; i++)
    {
     
       var rowsarr=[];
    	var sfdarr=[];  
      //gets cells of current row  
       var oCells = oTable.rows.item(i).cells;
       var ruljs={};

       //gets amount of cells of current row
       var cellLength = oCells.length;


       var checkrule="";
       checkrule=oCells.item(1).innerHTML.replace(/<[^>]*>/g, '');
	   
	   if(checkrule !="" )
	   				{
       //loops through each cell in current row
		   				for(var j = 0; j < cellLength; j++)
		   				{
		   				  
              // get your cell info here
		   						var headerjs={};
		   						var cellVal ="";
		   						cellVal = oCells.item(j).innerHTML.replace(/<[^>]*>/g, '');
		   						
//		   						var booleanValue = true; 
//		   						var numericalValue = 354;
//		   						var stringValue = "This is a String";
//		   						var stringObject = new String( "This is a String Object" );
//		   						alert(typeof booleanValue) // displays "boolean"
//		   						alert(typeof numericalValue) // displays "number"
//		   						alert(typeof stringValue) // displays "string"
//		   						alert(typeof stringObject) // displays "object"
		   						
		   							
//		   						console.log("jjjjjjjj=  "+j+" :: cellVal= "+cellVal);
              
		   						if(i==0){
            	  
            	 
		   							headerjs["field"]=  cellVal;
              
		   							firstrow.push(headerjs);
		   							console.log("headerjs ====  "+JSON.stringify(headerjs));
		   					
		   							     }
		   						else{

//		   						if(cellVal != ""){
		   							
		   							var getfieldval=firstrow[j];
		   						
		   							getfieldval["value"]=  cellVal;
//		   							console.log("getfieldval =::::::::: --- "+JSON.stringify(getfieldval));
            	  
		   										if(getfieldval.field == "Rule Name"){
              		  
		   											ruljs["rulename"] =getfieldval.value;
//		   											console.log("ruljs if --- "+JSON.stringify(ruljs));
		   																	}
		   										
		   										if(j>=2){
		   											var getSffieldval={};
		   											
		   											if(getfieldval.field.includes("=") ){
//		   												console.log("sfdata--- --- ");
		   											var str=getfieldval.field;
		   											

														str=str.substring(0,str.indexOf("="));
//														console.log("str = "+str);

//	   													console.log("getSffieldvalsf" +getfieldval.field);
	   											
	   												getSffieldval["field"]=str;
	   												if(isNaN(cellVal) == "true"){
	   													typ="string";
	   												}else{
	   													
	   													
	   						   							if( cellVal % 1 === 0){
	   						   								typ="Integer";
//	   						   							console.log("checktyp11 --------- = "+typ);
	   						   							}
	   						   							if(cellVal  % 1 !== 0){
	   						   							typ="Float";
//	   						   						console.log("checktyp22 --------- = "+typ);
	   						   							}
	   						   							
	   						   						}
	   						   					
	   												getSffieldval["type"]=typ; 
	   												getSffieldval["value"]=cellVal;
	   					//= Equals != Not Equals  > Greater Than	< Less Than   >= Greater Than Equals  <= Less Than Equals
	   												var cat="";
	   												var symb="";
	   												 if(sfdata[j-2].Category =="= Equals"){
	   													cat="Equals";
	   													symb="=";
	   												}	else if(sfdata[j-2].Category =="!= Not Equals"){
	   													cat="Not Equals";
	   													symb="!=";
	   												}	else if(sfdata[j-2].Category =="> Greater Than"){
	   													cat="Greater Than";
	   													symb=">";
	   												}	else if(sfdata[j-2].Category =="< Less Than"){
	   													cat="Less Than";
	   													symb="<";
	   												}	else if(sfdata[j-2].Category ==">= Greater Than Equals"){
	   													cat="Greater Than Equals";
	   													symb=">=";
	   												}	else if(sfdata[j-2].Category =="<= Less Than Equals"){
	   													cat="Less Than Equals";
	   													symb="<=";
	   												}else{
	   													cat=sfdata[j-2].Category;
	   													symb=sfdata[j-2].Category;
	   												}
	   												
	   												getSffieldval["Category"]=cat;
	   												getSffieldval["symbol_category"]=symb;
	   											
//	   												console.log("getSffieldvalsf =::::::::: 12--- "+JSON.stringify(getSffieldval));
	   												sfdarr.push(getSffieldval);

	   												
	   												
	   														}
		   											}
	   											

		   										
		   										if(!getfieldval.field.includes("=") ){
		   											if(getfieldval.field !="SR No" && getfieldval.field !="Rule Name" ){
//		   												console.log("getSffieldval.field" +getfieldval.field);
											    		var getOPfieldval={};
											    		getOPfieldval["field"]=getfieldval.field;
		   											 
											    		getOPfieldval["value"]=cellVal;		
											    		rowsarr.push(getOPfieldval);
//		   												console.log("getOPfieldval =::::::::: --- "+JSON.stringify(getOPfieldval));
		   											  }
											    	}else{
											    	
											    		 }
              
              
//		   						             } //cellval is empty close
		   					     	}		
		   				}


//		   			 console.log("rowsarr:: "+i+"i ="+JSON.stringify(rowsarr));
//		   			 console.log("sfdarr:: "+i+"i ="+JSON.stringify(sfdarr));
		   			ruljs["SFdata"] =sfdarr;
		   		    ruljs["Outputdata"] =rowsarr;
	   		
	
//                  console.log("final ruljs data--"+JSON.stringify(ruljs));

		   		    if(i!=0){
   
		   		    	datarrnew.push(JSON.parse(JSON.stringify(ruljs)));
		   		    }
      
//      			 console.log("datarr:: "+i+"i ="+JSON.stringify(datarrnew));
     	
        }
    }
    
    datajsnew["data"]=datarrnew;
//    console.log("datajs ------ ALL: "+JSON.stringify(datajsnew));

    datajsnew["user_name"]=email;
//	document.getElementById("prjName_RE").innerHTML=prjadv;
//	document.getElementById("REName").innerHTML=ruleng;
//    $("#myLabel").text()
    datajsnew["project_name"]=$("#prjName_RE").text();;
    datajsnew["ruleengine_name"]=$("#REName").text();;

    console.log("Main JSON: "+JSON.stringify(datajsnew));
    var storeDataArr=[];
    var table;
    var count;
    
	$
	.ajax(
			{
				type : 'POST',
				url : '/portal/servlet/service/AddRule.postaddRule',
				async : true,
				data : JSON.stringify(datajsnew),
				contentType : "application/json",
				success : function(dataa) {
					// alert(dataa);
					console.log(dataa);

					 var json=JSON.parse(dataa);
//						console.log("json :: "+json);
						caldwldlink = json.Download_url;
						  console.log("caldwldlink: "+caldwldlink);
						  

						  
						  //code by anagha

				  	 storeDataArr= json.storedata;
				  	 console.log(storeDataArr);

				  	 
				  	 count= storeDataArr.length;
				  	 console.log(count);

				  	 table = document.getElementById("calBuildtab"); 
				  	 totalRows = table.rows.length;
				  	 console.log(totalRows);
				  	 
				  	limit= count-totalRows+2;

				  	if(count<totalRows){
				  	console.log("***");
				  	addTableDataCal();
				  	}else {
				  	for(var i=0; i<limit; i++){
				  	var row= table.insertRow(-1);
				  	var cell1 = row.insertCell(0);
				  	var cell2 = row.insertCell(1);

				  	cell1.innerHTML = totalRows+i;
				  	}
				  	addTableDataCal();
				  	}
				
						  
						  
						  
				}
			}).fail(function(err) {

		console.log("error" + err);
	}); 
    
    
    
//    {
//    	"field":"id",
//    	"type":"Integer",
//    	"Category":"Equals",
//    	"symbol_category":"=",
//    	"value":"10"
//    	}
 
	function addTableDataCal(){
		for(var i=0; i<count; i++){
		console.log(i+"_"+storeDataArr[i].rulename);
		table.rows[i+2].cells[1].innerHTML=storeDataArr[i].rulename;
		fieldDataArr= storeDataArr[i].SFdata;
		opfieldArr= storeDataArr[i].Outputdata;
		calBuildArrlen= (table.rows[1].cells.length)-2+fieldDataArr.length+opfieldArr.length;

		console.log("calBuildArrlen: "+calBuildArrlen);
		for(var j=0; j<fieldDataArr.length; j++){
		field= fieldDataArr[j].field;
		val= fieldDataArr[j].value;
		Category= fieldDataArr[j].Category;

		console.log(field);
		if(i==0){
		createCell(table.rows[1].insertCell(table.rows[1].cells.length), field);

		table.rows[1].cells[table.rows[1].cells.length-1].innerHTML= field+'<select name="operators"><option value="= Equals" id="Equals">= Equals</option><option value="!= Not Equals" id="Not Equals">!= Not Equals</option><option value="> Greater Than" id="Greater Than">> Greater Than</option><option value="< Less Than" id="Less Than">< Less Than</option><option value=">= Greater Than Equals" id="Greater Than Equals">>= Greater Than Equals</option><option value="<= Less Than Equals" id="Less Than Equals"><= Less Than Equals</option><option value="Is Null" id="Is Null">Is Null</option><option value="Is Not Null" id="Is Not Null">Is Not Null</option></select>';
		console.log("****"+document.getElementById(Category).value);
		table.rows[1].cells[table.rows[1].cells.length-1].id=j;
		console.log(Category);
		table.rows[1].cells[j+2].className ="bg-success operators-td simple-th";
		}	
		$('#'+j).find('select[name="operators"]').find('option[value="'+Category+'"]').attr("selected",true);
		createCell(table.rows[i+2].insertCell(table.rows[i+2].cells.length), val);	
		}

		for(var k=0; k<opfieldArr.length; k++){
		field= opfieldArr[k].field;
		val= opfieldArr[k].value;
		console.log(field);
		if(i==0){
		createCell(table.rows[1].insertCell(table.rows[1].cells.length), field);	
		table.rows[1].cells[2+fieldDataArr.length+k].className= "bg-info text-center simple-th";
		}
		createCell(table.rows[i+2].insertCell(table.rows[i+2].cells.length), val);	
		}
		}

		table.rows[0].cells[0].setAttribute("colspan", fieldDataArr.length+2);
		if(opfieldArr.length>0){
		createCell(table.rows[0].insertCell(1), "Output Data");
		table.rows[0].cells[1].id="op2";
		table.rows[0].cells[1].className= "bg-info text-center simple-th";
		table.rows[0].cells[1].setAttribute("colspan", opfieldArr.length);
		table.rows[0].cells[2].setAttribute("colspan", calBuildArrlen);
		}else{
		table.rows[0].cells[1].setAttribute("colspan", calBuildArrlen);
		}

		}
	
	
	$('.calculationBuilder').trigger('click');
});

$('.calculationBuilder-pre').click(function() {
	$('.addRulesF').trigger('click');
});
$('.calculationBuilder-next').click(function() {
	
	
	var CalJson = {};
	var fid = "file-upload-cal";

	
	
	
	// for table
var oTable = document.getElementById('calBuildtab');
	

var sfcount=document.getElementById("raw1").colSpan-1;

var opcount=document.getElementById("op2").colSpan;
var calccount=document.getElementById("calbuil").colSpan
console.log("sfcount = "+sfcount);
console.log("opcount = "+opcount);
console.log("calccount = "+calccount);

var opcn=sfcount+opcount;
var calbcol=opcn+1;
console.log("opcnnew  = "+opcn);
//gets table


//gets rows of table
var rowLength = oTable.rows.length;
var datajsnew={};
var datarrnew=[];
var firstrow=[];
var calbarr=[];
//loops through rows    
for (i = 0; i < rowLength; i++){

   //gets cells of current row
   var oCells = oTable.rows.item(i).cells;

   //gets amount of cells of current row
   var cellLength = oCells.length;

   var rowsarr=[];
	var sfdarr=[];  
 //gets cells of current row  
  var oCells = oTable.rows.item(i).cells;
  var ruljs={};

  //gets amount of cells of current row
  var cellLength = oCells.length;

  console.log("ii=  iiiiiiiiiiiiiiiiiiiiiiiiiiiii="+i);
  var checkrule="";
  if(i>0){
  checkrule= oCells.item(1).innerHTML.replace(/<[^>]*>/g, '');

  }else{}
if(checkrule!=""){
   //loops through each cell in current row
   for(var j = 0; j < cellLength; j++){
      /* get your cell info here */
      var cellVal = oCells.item(j).innerHTML;
      var headerjs={};
			var cellVal ="";
			cellVal = oCells.item(j).innerHTML.replace(/<[^>]*>/g, '');
//			if(cellVal!=""){
//			console.log("in cellVal =  "+cellVal);
			if(cellVal != ""){
			console.log("in for  jjj=  "+j+"i="+i);

			if(i==1){
//				console.log("jjjjjjjj=  "+j+" :: cellVal= "+cellVal);
				headerjs["field"]=  cellVal;

				firstrow.push(headerjs);
				console.log("headerjs ====  "+JSON.stringify(firstrow));
		
				     }else{
				    	if(i>=2){
				    		var cellVal ="";
							cellVal = oCells.item(j).innerHTML.replace(/<[^>]*>/g, '');
//							console.log("cellval= = "+cellVal);
				    		
							var getfieldval=firstrow[j];
	   						
   							getfieldval["value"]=  cellVal;
   							console.log("getfieldval =::::::::: --- "+JSON.stringify(getfieldval));
    	  
   										if(getfieldval.field == "Rule Name"){
      		  
   											ruljs["rulename"] =getfieldval.value;
   											console.log("ruljs if --- "+JSON.stringify(ruljs));
   																	}
   										
				    	
						if(j>=2 && j<=sfcount){
							console.log("sfcount= = "+sfcount);
							console.log("cellval= = "+cellVal+j);
								var getSffieldval={};
								
								if(getfieldval.field.includes("=") ){
									console.log("sfdata--- --- ");
								var str=getfieldval.field;
								

								str=str.substring(0,str.indexOf("="));
								console.log("str = "+str);

									console.log("getSffieldvalsf" +getfieldval.field);
								
								getSffieldval["field"]=str;
								getSffieldval["type"]="Integer"; 
								getSffieldval["value"]=cellVal;
	//= Equals != Not Equals  > Greater Than	< Less Than   >= Greater Than Equals  <= Less Than Equals
								var cat="";
								var symb="";
							
								
								getSffieldval["Category"]=cat;
								getSffieldval["symbol_category"]=symb;
							
//								console.log("getSffieldvalsf =::::::::: 12--- "+JSON.stringify(getSffieldval));
								sfdarr.push(getSffieldval);
								console.log("sfdarr =::::::::: 12--- "+JSON.stringify(sfdarr));
								
								
										}
								}
						
						if(j>sfcount && j<=opcn){
							console.log("sfcount= = "+sfcount+":: opcount="+opcn);
							console.log("getSffieldval.field" +getfieldval.field);
				    		var getOPfieldval={};
				    		getOPfieldval["field"]=getfieldval.field;
							 
				    		getOPfieldval["value"]=cellVal;		
				    		rowsarr.push(getOPfieldval);
								console.log("getOPfieldval =::::::::: --- "+JSON.stringify(getOPfieldval));
							
						}
						
						if(j > calbcol){
							console.log("sfcount= = "+sfcount+":: opcount="+opcn);
							console.log("getSffieldval.field" +getfieldval.field);
				    		var getOPfieldval={};
				    		getOPfieldval["field"]=getfieldval.field;
							 
				    		getOPfieldval["value"]=cellVal;		
				    		calbarr.push(getOPfieldval);
								console.log("getOPfieldval =::::::::: --- "+JSON.stringify(getOPfieldval));
							
						}
				    	}
				    	 
				    	 
				     }
			}
			
      
      
   }
}
   
//   console.log("rowsarr:: "+i+"i ="+JSON.stringify(rowsarr));
		 if(sfdarr.length>0){
		ruljs["SFdata"] =sfdarr;
		 }
		 if(rowsarr.length>0){
	    ruljs["Outputdata"] =rowsarr;
	    console.log("sfdarr:: "+i+"i ="+JSON.stringify(ruljs));
		 }
		 if(calbarr.length>0){
		 ruljs["CalculationBuilder"] =calbarr;
	     console.log("sfdarr:: "+i+"i ="+JSON.stringify(ruljs));
			 
		 }
	    if(i>1){
	    datarrnew.push(JSON.parse(JSON.stringify(ruljs)));
	    }
}
datajsnew["data"]=datarrnew;
console.log("datajs ------ ALL: "+JSON.stringify(datajsnew));

    //gets rows of table

	//table end
	
	// get base64data of file
	var filejs = {};
	var reader = new FileReader();
	var transjs = {};
	var fname = document.getElementById(fid).value;
	if(fname!=""){
	// console.log("filename-- " + fname);
	var f = document.getElementById(fid).files;
	reader.readAsDataURL(f[0]);

	reader.onloadend = function() {

		var filedata = reader.result;

		var fd = filedata.substr(0, filedata.indexOf(",") + 1);
		var fdata = filedata.replace(fd, "");
		// console.log("filedata-- " + fdata);Transform_File_Data
		filejs["rulename"] =  document.getElementById("Rule_Engine").value;
		filejs["filename"] = fname;
		filejs["filedata"] = fdata;
		// transjs["Transform_File_Data"]=filejs;
		CalJson["user_name"] = email;
		CalJson["project_name"] = document.getElementById('myprj1').innerHTML;
		CalJson["ruleengine_name"] = document.getElementById("Rule_Engine").value;
		CalJson["Calculation_File_Data"] = filejs;
		console.log(" CalJson - " + JSON.stringify(CalJson));
		
		
		$
		.ajax(
				{
					type : 'POST',
					url : '/portal/servlet/service/CalBuilder.postaddCal',
					async : true,
					data : JSON.stringify(CalJson),
					contentType : "application/json",
					success : function(dataa) {
				
						console.log(dataa);
//						 var json=JSON.parse(dataa);
//						 console.log("json :: "+json);
						
					}
				}).fail(function(err) {

			console.log("error" + err);
		});
		
		
	
	
	}
	
	
	}
	
	
	
//	$('.outputManagement').trigger('click');
});

$('.outputManagement-pre').click(function() {
	$('.calculationBuilder').trigger('click');
});

$('.createNew-btn').click(function() {
	$('.createNewF').trigger('click');
});

$('.operators-td .dropdown-menu a').click(
		function() {
			var op = $(this).attr('value');
			$(this).parents('.operators-td').find('a.btn').html(
					op + ' <span class="caret"></span>');
		});

$('.xls-add-more-btn')
		.click(
				function() {
					var fileName = $('#xls').val().split('\\').pop();

					if (fileName) {
						var a = '';
						a = a + '<div class="row form-group">';
						a = a + '<div class="col-sm-3">';
						a = a
								+ '<input type="text" class="form-control" value="'
								+ fileName + '" placeholder="URL Name">';
						a = a + '</div>';
						a = a + '<div class="col-sm-3">';
						a = a
								+ '<button class="btn btn-warning sm-btn-custom"><i class="fa fa-edit"></i></button> ';
						a = a
								+ '<button class="btn btn-danger sm-btn-custom"><i class="fa fa-trash"></i></button>';
						a = a + '</div>';
						a = a + '</div>';

						$('.add-xls-name-main').append(a);
						$('#xls').val('');
						$('#xls-filename').text('');
					}
				});
$('#project_name').blur(
		function() {
			$('input[name=template2]').attr('value',
					$('#template2').val() + $('#project_name').val());
		});


var outputArr=[];
var count=0;
function getExparamObject(){
var opcls= document.getElementsByClassName("opfld");
for(var i=0; i<opcls.length; i++){
var Id=document.getElementsByClassName('opfld')[i].id;
console.log(Id);	
if(Id=="op" || Id=="op"+i){
console.log(count);
count++;
console.log(count);
if(count==1){
console.log("count1= "+count);
if(Id=="op"){
//	console.log("if id op= "+Id);
var fieldname=document.getElementById("fieldname").value;
var fieldtype=document.getElementById("op_fieldtype").value;
var fieldlength=parseInt(document.getElementById("fieldlength").value);	
}
var op_row={};
op_row["fieldname"]=fieldname;
op_row["fieldtype"]=fieldtype;
op_row["fieldlength"]=fieldlength;
outputArr.push(op_row);
}else{
console.log("else id op= "+Id);
if(Id=="op"+i){
	var fieldname= $('#op'+i).find("#fieldnamecpy").val();
	var fieldtype=$('#op'+i).find("#op_fieldtypecpy").val();
	var fieldlength=$('#op'+i).find("#fieldlengthcpy").val();	
	
console.log(fieldname+" "+fieldtype+" "+fieldlength);
}var op_row={};
op_row["fieldname"]=fieldname;
op_row["fieldtype"]=fieldtype;
op_row["fieldlength"]=fieldlength;
outputArr.push(op_row);
}
//outputArr.push(op_row);
document.getElementById("opArr").value=JSON.stringify(outputArr);
console.log(outputArr);
console.log(document.getElementById("opArr").value);
}
}


return outputArr;
}


// xls-filename
var data = "";
function getBase64ofFile(fid) {
	var filejs = {};
	var reader = new FileReader();

	var fname = document.getElementById(fid).value;
	// console.log("filename-- " + fname);
	var f = document.getElementById(fid).files;
	reader.readAsDataURL(f[0]);

	reader.onloadend = function() {

		var filedata = reader.result;

		var fd = filedata.substr(0, filedata.indexOf(",") + 1);
		var fdata = filedata.replace(fd, "");
		// console.log("filedata-- " + fdata);

		filejs["filename"] = fname;
		filejs["filedata"] = fdata;

		console.log("json file- " + JSON.stringify(filejs));

		// new
		var far = [];
		// for(var i=0;i<1,i++){
		far.push(filejs);
		// }
		var fj = {};
		// fj["File_Data"]=far;
		// mainJsonext["File_Data"]=far;
		// console.log("final -- "+JSON.stringify(fj));
		data = JSON.stringify(fj);
		// console.log("data-- "+data);
	}
	console.log("json file- out call ajax in onload" + JSON.stringify(filejs));
	return filejs;
}
// id="dwldlink"
$('.downloadexcel').click(function getlink() {
	console.log("link dwl= " + dwldlink);

	var a = document.getElementById('downldlink'); // or grab it by tagname etc
	a.href = dwldlink;

});
//downloadcalexcel
$('.downloadcalexcel').click(function getcalclink() {
	console.log("calculation link dwl= " + caldwldlink);

	var a = document.getElementById('downldcalclink'); // or grab it by tagname etc
	a.href = caldwldlink;

});


function getPrjDetailsTable() {
	$
			.ajax({
				type : 'GET',
				url : '/portal/servlet/service/carrotrule.editing?username='
						+ email,
				async : false,
				success : function(dataa) {
					try {
						var json = JSON.parse(dataa);
						console.log(json);
						var prjArr = [];
						prjArr = json.Data;
						console.log(prjArr);
						/* {"Data":[{"Created_Date":"20-08-2018","Project_Name":"Bizlem_project","Rule_Engine":"Bizlem_rule"},{"Created_Date":"15-03-2019","Project_Name":"cvdf","Rule_Engine":"dfg"},{"Created_Date":"15-03-2019","Project_Name":"pj12","Rule_Engine":"rule12"},{"Created_Date":"15-03-2019","Project_Name":"ggrr","Rule_Engine":"tt"},{"Created_Date":"15-03-2019","Project_Name":"dfde22","Rule_Engine":"sdf"},{"Created_Date":"15-03-2019","Project_Name":"prjtest4","Rule_Engine":"fd"},{"Created_Date":"15-03-2019","Project_Name":"pj22","Rule_Engine":"rl2"},{"Created_Date":"15-03-2019","Project_Name":"prj3451","Rule_Engine":"fgd"},{"Created_Date":"15-03-2019","Project_Name":"opo","Rule_Engine":"fgf"},{"Created_Date":"15-03-2019","Project_Name":"toioi11","Rule_Engine":"rif"},{"Created_Date":"15-03-2019","Project_Name":"moto","Rule_Engine":"vb"},{"Created_Date":"15-03-2019","Project_Name":"c11","Rule_Engine":"ds"}]} */
						var table = document.getElementById("prjDetails");

						for (var i = 0; i < prjArr.length; i++) {
							// for(var i=prjArr.length;i>0;i--){
							console.log(prjArr.length);
							var prjName = prjArr[i].Project_Name;
							var ruleName = prjArr[i].Rule_Engine;
							var creDate = prjArr[i].Created_Date;

							var row = table.insertRow(-1);

							row.id = 'row' + i;
							table.rows[i].className = "prj-tr";
							var cell1 = row.insertCell(0);
							var cell2 = row.insertCell(1);
							var cell3 = row.insertCell(2);
							var cell4 = row.insertCell(3);
							var cell5 = row.insertCell(4);

							cell5.className = "action-btn";
							cell1.innerHTML = i + 1;
							cell2.innerHTML = prjName;
							cell3.innerHTML = ruleName;
							cell4.innerHTML = creDate;
							// cell5.innerHTML = '<a class="btn btn-info btn
							// sm0-btn-custom basic-pro-edit"><i class="fa
							// fa-edit"></i></a> <a href="" class="btn
							// btn-danger btn sm0-btn-custom"><i class="fa
							// fa-trash"></i></a> <a
							// href="<%=request.getContextPath()%>/apps/static/advanced.esp"
							// target="_blank" class="btn btn-success
							// sm0-btn-custom" onclick="advance()">A</a> <a
							// href="<%=request.getContextPath()%>/apps/static/setUp.esp"
							// target="_blank" class="btn btn-success
							// sm0-btn-custom" onclick="setup()">Set Up</a>';
//					http://35.236.154.164:8082/portal/servlet/service/CallCarrotRule.Setup  target="_blank"
							cell5.innerHTML = '<a href="" class="btn btn-info btn sm0-btn-custom basic-pro-edit edit-project" id="edit' + i + '" prj-name="' + prjName + '"><i class="fa fa-edit"></i></a> <a href="" class="btn btn-danger btn sm0-btn-custom delete-prj" id="del' + i + '"prj-name="' + prjName + '"><i class="fa fa-trash"></i></a> <a href=""	target="_blank" class="btn btn-success sm0-btn-custom advanc" prj-name="' + prjName +'"  ruleEng="' + ruleName +'">A</a> <a href=""   class="btn btn-success sm0-btn-custom set-up" prj-name="' + prjName +'" ruleEng="' + ruleName +'">Set Up</a>';
//							cell5.innerHTML = '<a class="btn btn-info btn sm0-btn-custom basic-pro-edit edit-project"><i class="fa fa-edit"></i></a> <a href="" class="btn btn-danger btn sm0-btn-custom delete-prj" id="del"'
//									+ i
//									+ '" prj-name="'
//									+ prjName
//									+ '"><i class="fa fa-trash"></i></a> <a href="<%=request.getContextPath()%>/apps/static/advanced.esp"	target="_blank" class="btn btn-success sm0-btn-custom" onclick="advance()">A</a> <a href="<%=request.getContextPath()%>/apps/static/setUp.esp" target="_blank" class="btn btn-success sm0-btn-custom" onclick="setup()">Set Up</a>';
						
						
						
						}
						
						   fnames.push("SR No");
						   fnames.push("Rule Name");
						   console.log("fnames = "+fnames);
					

				  	   for(var j=0;j<fnames.length;j++){
				  		   var row = document.getElementById("rulerow");
				  		   var x = row.insertCell(-1);
//							   console.log("fnames[j.length-j] = "+fnames[fnames.length-j]);
					    	  x.innerHTML = fnames[j];
					    	  x.className += "simple-th";
					    	  
					
						   }
				      
					} catch (err) {
						console.log(err);
					}
				}
			});
}

var mainJson = {};
$("body").on("click", ".delete-prj", function() {
	console.log("test");
	var $this = $(this);
	console.log("$this: " + $this);
	var prjName = $this.attr("prj-name");

	console.log(prjName);
	mainJson["user_name"] = email;
	mainJson["project_name"] = prjName;

	$.ajax({
		type : 'POST',
		url : '/portal/servlet/service/Delete.delete', // Delete_new.delete',
		async : true,
		data : JSON.stringify(mainJson),
		contentType : "application/json",
		success : function(dataa) {
			console.log(dataa);
			var json = JSON.parse(dataa);
			console.log(json);
		}
	});
});

function getRawTable(flds) {
	// display headers on transform
	var columnCount = flds.length;
//	console.log(" getRawTable columnCount= " + columnCount);
	var table = document.getElementById("rawtable");
	// Add the header row.
	var row = table.insertRow(-1);
//	console.log("flds[0][i] = " + flds);
	for (var i = 0; i < columnCount; i++) {
		var headerCell = document.createElement("TH");
// document.getElementById('TH').className = "bg-info simple-th";
		headerCell.innerHTML = flds[i];

		row.appendChild(headerCell);
	}

	

}

$('body').on('click', '.edit-project', function(e) {
	console.log("on click Edit");
//	 fnames.push("SR No");
//	   fnames.push("Rule Name");
//	    console.log("fnames = "+fnames);
//
// console.log("fnames[j.length= "+fnames.length);
// for(var j=1;j<=fnames.length;j++){
//	   var row = document.getElementById("rulerow");
//	   var x = row.insertCell(0);
//		   console.log("fnames[j.length-j] = "+fnames[fnames.length-j]);
//  	  x.innerHTML = fnames[fnames.length-j];
//  	  x.className += "simple-th";
//	   }  
	$('.createNewF').trigger('click');
	e.preventDefault(); // to stop the form submitting.

	var $this = $(this);
	console.log("$this: " + $this);
	var prjName = $this.attr("prj-name");

	console.log(prjName);
	var Rul_Engine;
	$.ajax({
	type: 'GET',
	url: '/portal/servlet/service/EditProject_new?username=' + email+'&prjName='+prjName,
	async: false,
	success: function(dataa) {
	try {
	var json = JSON.parse(dataa);
	console.log(json);

	var prj_Desc=json.prj_Desc;
	Rul_Engine=json.ruleName;
	var Rule_Desc=json.rule_Desc;

	document.getElementById("project_name").value=prjName;
	document.getElementById("Project_Description").value=prj_Desc;
	document.getElementById("Rule_Engine").value=Rul_Engine;
	document.getElementById("Rule_Engine_Description").value=Rule_Desc;
	} catch (err) {
	console.log(err);
	}
	}
	});

	var ruleEngineArr;
	var count;
	var fieldDataArr;
	var field;
	var opFieldArr;
	var opField;
	var val;
	var table;
	var totalRows;
	var Category;

	console.log(Rul_Engine);
	$.ajax({
	type: 'GET',
	url: '/portal/servlet/service/carrotrule.carrotrulefulldetails?username='+email+'&projectname='+prjName+'&ruleenginename='+Rul_Engine,
	async: false,
	success: function(dataa) {
	try {
	var json = JSON.parse(dataa);
	console.log(json);
	console.log("*"+Rul_Engine);
	document.getElementById("prjName_RE").innerHTML=prjName;
	document.getElementById("REName").innerHTML=Rul_Engine;
	
	//show prj name and rule
	document.getElementById("myprj").innerHTML=prjName;
	document.getElementById("myprj1").innerHTML=prjName;
	document.getElementById("myprj3").innerHTML=prjName;
	
	document.getElementById("REName3").innerHTML=Rul_Engine;
	

	ruleEngineArr= json.Rule_Engine;
	count= ruleEngineArr.length;
	console.log(count);
	table = document.getElementById("ruleheadertab"); 
	totalRows = table.rows.length;
	console.log(totalRows);
	var limit= count-totalRows+1;

	if(count<totalRows){
	console.log("***");
	addTableData();
	}else {
	for(var i=0; i<limit; i++){
	var row= table.insertRow(-1);
	var cell1 = row.insertCell(0);
	var cell2 = row.insertCell(1);

	cell1.innerHTML = totalRows+i;
	}
	addTableData();
	}
	} catch (err) {
	console.log(err);
	}
	}
	});

	function addTableData(){
		
	console.log("In addTableData");
	for(var i=0; i<count; i++){
	console.log(ruleEngineArr[i].Rule_Name);
	table.rows[i+1].cells[1].innerHTML=ruleEngineArr[i].Rule_Name;
	fieldDataArr= ruleEngineArr[i].FieldData;
	opfieldArr= ruleEngineArr[i].OutputField;

	for(var j=0; j<fieldDataArr.length; j++){
	field= fieldDataArr[j].field;
	val= fieldDataArr[j].value;
	Category= fieldDataArr[j].Category;
	
	 
	
	console.log(field);
	if(i==0){
	createCell(table.rows[0].insertCell(table.rows[0].cells.length), field);
	
	table.rows[0].cells[table.rows[0].cells.length-1].innerHTML= field+'<select name="operators"><option value="= Equals" id="Equals">= Equals</option><option value="!= Not Equals" id="Not Equals">!= Not Equals</option><option value="> Greater Than" id="Greater Than">> Greater Than</option><option value="< Less Than" id="Less Than">< Less Than</option><option value=">= Greater Than Equals" id="Greater Than Equals">>= Greater Than Equals</option><option value="<= Less Than Equals" id="Less Than Equals"><= Less Than Equals</option><option value="Is Null" id="Is Null">Is Null</option><option value="Is Not Null" id="Is Not Null">Is Not Null</option></select>';
	console.log("****"+document.getElementById(Category).value);
	table.rows[0].cells[table.rows[0].cells.length-1].id=j;
	console.log(Category);
	}	
	$('#'+j).find('select[name="operators"]').find('option[value="'+Category+'"]').attr("selected",true);
	createCell(table.rows[i+1].insertCell(table.rows[i+1].cells.length), val);	
	}

	for(var k=0; k<opfieldArr.length; k++){
	field= opfieldArr[k].field;
	val= opfieldArr[k].value;
	console.log(field);
	if(i==0){
	createCell(table.rows[0].insertCell(table.rows[0].cells.length), field);	
	}
	createCell(table.rows[i+1].insertCell(table.rows[i+1].cells.length), val);	
	}
	}
	console.log("**"+Category);
	}

	
	});
function createCell(cell, text) {
	var div = document.createElement('div'), // create DIV element
	txt = document.createTextNode(text); // create text node
	div.appendChild(txt); // append text node to the DIV
	cell.appendChild(div); // append DIV to the table cell
	}


function addRuleSelect(divid,rullis) {
    var newDiv = document.getElementById(divid);
    
    var selectHTML = "";
    console.log("choces "+JSON.stringify(chjson.Rule_Engine));
//      console.log("choces i"+chjson.Rule_Engine);
    
    selectHTML="<select class='form-control  bg-gray' id='"+rullis+"'>";
    for(i = 0; i < chjson.Rule_Engine.length; i = i + 1) {
    	 
        selectHTML += "<option value='" + chjson.Rule_Engine[i] + "'>" + chjson.Rule_Engine[i] + "</option>";
    }

    selectHTML += '</select>';

    newDiv.innerHTML = selectHTML;
    
       
}

function addRuleeng(divid) {
    var newDiv = document.getElementById(divid);
    
    var selectHTML = "";
 
    selectHTML='<a href="#" class="rule1">Rule 11</a>';
   

    newDiv.innerHTML = selectHTML;
    
       
}

var rulselectarr=[];

function getRulelistselect(){
var count=0;
var rulcls= document.getElementsByClassName("ruleadd12");
for(var i=0; i<rulcls.length; i++){
var Id=document.getElementsByClassName('ruleadd12')[i].id;
console.log(Id);	
if(Id=="rul" || Id=="rul"+i){
console.log(count);
count++;
console.log(count);
if(count==1){
console.log("count1= "+count);
if(Id=="rul"){
	console.log("if id rul= "+Id);

var rul12=parseInt(document.getElementById("rullist1").value);	
console.log("if id rul12= "+rul12);
}
var rul_row={};

rul_row["rulname"]=rul12;
rulselectarr.push(rul_row);
}else{
console.log("else id rul= "+Id);
if(Id=="rul"+i){
	
	var fieldtype=$('#rul'+i).find("#rullistcpy").val();
		
	
console.log("in rull "+fieldtype);
}
var rul_row={};

rul_row["rulname"]=fieldtype;

rulselectarr.push(rul_row);
}
//rulselectarr.push(rul_row);
document.getElementById("rularr").value=JSON.stringify(rulselectarr);
console.log("rulselectarr:: "+rulselectarr);
console.log(document.getElementById("rularr").value);
}
}


return rulselectarr;
}


//downloadcalexcel

/*{"user_name":"carrotrule444@gmail.com","projectname":"Bizlem_project","Level":[{"Level1":["Bizlem_rule"],"Level2":[],"Level3":[]}]}
*/
var ipData={};
var lvlJsonArr= [];
var lvlJsonArr1= [];

//code by anagha
$("body").on("click", ".down-set-up", function() {
	console.log("id c1 down-set-up: clicked------------");
var $this = $(this);
console.log($this);
var id_dwn= $(this).closest('div').attr('id');
console.log(id_dwn);

ipData["user_name"]= email;
ipData["projectname"]= prjName;

if(id_dwn=="c1"){
	var lvlArr= getLevelData(id_dwn);
	console.log("id c1 lvlArr: "+lvlArr);
	var lvlJson={};
	lvlJson["Level1"]=lvlArr;
	lvlJson["Level2"]=[];
	lvlJson["Level3"]=[];
	lvlJsonArr.push(lvlJson);
	ipData["Level"]= lvlJsonArr;
	console.log("console.log(JSON.stringify(lvlJsonArr))== "+JSON.stringify(ipData));
	}else if(id_dwn=="c2"){
	var lvlArr1= getLevelData1(id_dwn);
	console.log("lvlArr id c2: "+lvlArr1);
	var lvlJson={};
	lvlJson["Level1"]=[];
	lvlJson["Level2"]=lvlArr1;
	lvlJson["Level3"]=[];
//	console.log("console.log(JSON.stringify(lvlJson))== "+JSON.stringify(lvlJson));
//	console.log("console.log(JSON.stringify(lvlJsonArr))== "+JSON.stringify(lvlJsonArr1));
	lvlJsonArr1.push(lvlJson);
	ipData["Level"]= lvlJsonArr1;
	console.log("console.log(JSON.stringify(lvlJsonArr))== "+JSON.stringify(ipData));
	}



console.log(JSON.stringify(ipData));
$.ajax({
type: 'POST',
url: '/portal/servlet/service/ExcelCreationTransform.ExcelCreation',
async: false,
data:JSON.stringify(ipData),
contentType: 'application/json',
success: function (dataa) {
console.log("ipData=="+JSON.stringify(ipData));
console.log(JSON.stringify(dataa));
var json = JSON.parse(dataa);
console.log(json);	
console.log(json.fileurl);
window.location.href='http://'+json.fileurl;
}
}).fail(function(err) {

console.log("error" + err);
});

});


var lvlArr=[];
function getLevelData(id_dwn){
var lvlCnt=0;

var level1= document.getElementsByClassName("ruleadd12");
console.log(level1.length);
var len= level1.length;
for(var i=0; i<len; i++){
console.log("i: "+i);
var Id=document.getElementsByClassName('ruleadd12')[i].id;
console.log(Id);	
console.log(lvlCnt);
lvlCnt++;
console.log(lvlCnt);
var ruleName="";
if(lvlCnt==1){
if(Id=="rul"){
ruleName= $('#rul').find("#rullist1").val();	
console.log("rulName: "+ruleName);
}
lvlArr.push(ruleName);
}else{
if(Id=="rul"+(lvlCnt-1)){
ruleName= $('#rul'+(lvlCnt-1)).find("#rullist1").val();	
console.log("ruleName: "+ruleName);
}
lvlArr.push(ruleName);
}
console.log("lvlArr_fun: "+lvlArr);	
}


return lvlArr;
}

var lvlArr1=[];
function getLevelData1(id_dwn){
var lvlCnt=0;
var level2= document.getElementsByClassName("lev2rul12");
console.log(level2.length);
var len= level2.length;
for(var i=0; i<len-1; i++){
console.log("i: "+i);
var Id=document.getElementsByClassName('lev2rul12')[i].id;
console.log(Id);	
console.log(lvlCnt);
lvlCnt++;
console.log(lvlCnt);
var ruleName="";
if(lvlCnt==1){
if(Id=="rul2plr"){
ruleName= $('#rul2plr').find("#rullistad2").val();	
console.log("rulName: "+ruleName);
}
lvlArr1.push(ruleName);
}else{
if(Id=="rul2plr"+(lvlCnt-1)){
ruleName= $('#rul2plr'+(lvlCnt-1)).find("#rullist2").val();	
console.log("ruleName: "+ruleName);
}
lvlArr1.push(ruleName);
}
console.log("lvlArr_fun: "+lvlArr1);	
}


return lvlArr1;
}
//rule1

$("body").on("click", ".rule1", function(e) {
	console.log("test rule111");
	$('.addRulesF')
	.trigger('click');
	e.preventDefault();
//	ruleEngad
	 var keys = [];
	console.log("prjadv1= : " + prjadv);
	var $this = $(this);
	var ruleng = $this.attr("ruleEngad");
	console.log("test ruleng :: "+ruleng);
	document.getElementById("prjName_RE").innerHTML=prjadv;
	document.getElementById("REName").innerHTML=ruleng;
	 var mainjson={};
	 mainjson["user_name"]=email;
	 mainjson["project_name"]=prjadv;
	 mainjson["Rule_Engine"]=ruleadv;
	 console.log("mainjson:: "+JSON.stringify(mainjson));
		$.ajax({
			type : 'POST',
			url : '/portal/servlet/service/AdvancedPage.fulldetails',
			async : false,
			data : JSON.stringify(mainjson),
			contentType : "application/json",
			success : function(dataa) {
				// alert(dataa);
				console.log(dataa);

				
				var json = JSON.parse(dataa);
			
				for(var i=0;i<json.TRANSFORM_DATA.Raw_Data.length;i++){
					var transf=json.TRANSFORM_DATA.Raw_Data[i];
					 console.log("keys transf= "+transf);
					
					   for(var k in transf) keys.push(k);
					
				}
				 
				
				for(var i=0;i<json.TRANSFORM_DATA.Transform.length;i++){
					var transf=json.TRANSFORM_DATA.Transform[i];
					 console.log("keys transf= "+transf);
					   for(var k in transf) keys.push(k);
					
				}
				console.log("keys transform= "+keys);
				 
			}
		}).fail(function(err) {

		console.log("error" + err);
		});
	
	
var excelfl=keys; //["id","name","dis"];
console.log("keys excelfl= "+excelfl);
	 for (var i = 0; i <excelfl.length; i++) {
		   var row = document.getElementById("rulerow");
		   var x = row.insertCell(-1);

	    	  x.className += "bg-success operators-td simple-th";
//	    	   console.log("columnCount-i= "+columnCount-i);

	    	  x.innerHTML=excelfl[i]+'<select name="operators" ><option value="= Equals" >= Equals</option><option value="!= Not Equals">!= Not Equals</option><option value="> Greater Than">> Greater Than</option><option value="< Less Than">< Less Than</option><option value=">= Greater Than Equals">>= Greater Than Equals</option><option value="<= Less Than Equals"><= Less Than Equals</option><option value="Is Null">Is Null</option><option value="Is Not Null">Is Not Null</option></select>';

	  	    	//insert td
	  	    	   var row1 = document.getElementById("firsttr");
		  		   var x1 = row1.insertCell(-1);
					 
			       x1.innerHTML = '<td></td><td></td><td></td>';
			       var row2 = document.getElementById("secondtr");
			  	   var x2 = row2.insertCell(-1);
				   x2.innerHTML = '<td></td><td></td><td></td>';
				   var row3 = document.getElementById("thirdtr");
				   var x3 = row3.insertCell(-1);
				   x3.innerHTML = '<td></td><td></td><td></td>';
	  	    	   
	 }
	console.log("prjadv= : " + prjadv);

//		consol.log(resultRet);

	});
// advplus
var counta=0;
function getrlrngadv(){
	var opcls= document.getElementsByClassName("ruleengadd12");
	for(var i=0; i<opcls.length; i++){
	var Id=document.getElementsByClassName('ruleengadd12')[i].id;
	console.log(Id);	
	if(Id=="ruleeng" || Id=="ruleeng"+i){
	
	counta++;
//	console.log(counta);
	if(counta==1){
//	console.log("count1= "+counta);
	if(Id=="ruleeng"){
//		console.log("if id ruleeng= "+Id);
	
	var rlgn=document.getElementById("rulengfir").value;
	console.log(":rulengfir:"+rlgn);
	var newDiv = document.getElementById("ruleengone");   
	var selectHTML = "";    
    selectHTML='<div class="col-sm-12 text-left"><a href="#" class="rule1" id="ruleng12" ruleEngad="' + rlgn +'">'+rlgn+'</a></div>';
	newDiv.innerHTML = selectHTML;
	 saveadvruleng(rlgn);
	
	}

	}else{
//	console.log("else id ruleeng= "+Id);
	if(Id=="ruleeng"+i){
		var rlgn= $('#ruleeng'+i).find("#rulengcp").val();
		console.log(":in ruleeng :"+rlgn);
//		ruleengplus
		
		var newDiv = document.getElementById("ruleengplus"+i);   
		var selectHTML = "";    
	    selectHTML='<div class="col-sm-12 text-left"><a href="#" class="rule1" id="ruleng12" ruleEngad="' + rlgn +'">'+rlgn+'</a></div>';
		newDiv.innerHTML = selectHTML;	
		saveadvruleng(rlgn);
	
	}

	}
	//outputArr.push(op_row);
	}
	}

}

function saveadvruleng(rlg){
 var mainjson={};
 mainjson["user_name"]=email;
 mainjson["project_name"]=prjadv;
 mainjson["Rule_Engine"]=rlg;
 console.log("mainjson:: "+JSON.stringify(mainjson));
 if(rlg!=""){
	$.ajax({
		type : 'POST',
		url : '/portal/servlet/service/AdvancedPage.advanced',
		async : false,
		data : JSON.stringify(mainjson),
		contentType : "application/json",
		success : function(dataa) {
			// alert(dataa);
			console.log(dataa);

//			var json = JSON.parse(dataa);
		
			
		}
	}).fail(function(err) {

	console.log("error" + err);
	});
 }
}

//save-adv
$('.save-adv').click(function() {
//	getrlrngadv();a
});