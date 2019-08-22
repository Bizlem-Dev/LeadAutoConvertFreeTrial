var remoteuser ;
var uploadedSubscriberCount=0; 	
$( document).ready(function() {
	  //  var remoteuser ='<%=request.getRemoteUser()%>';
   // alert(document.getElementById("schtime").innerHTML);
	var currentTime = new Date();

	var currentOffset = currentTime.getTimezoneOffset();

	var ISTOffset = 330;   // IST offset UTC +5:30 

	var ISTTime = new Date(currentTime.getTime() + (ISTOffset + currentOffset)*60000);

	// ISTTime now represents the time in IST coordinates

	var hoursIST = ISTTime.getHours();
	var minutesIST = ISTTime.getMinutes();
	var secIST = ISTTime.getSeconds();
	//document.getElementById("time").innerHTML = hoursIST+"::"+minutesIST+"::"+secIST;
	console.log("hoursIST = "+hoursIST+"::"+minutesIST+"::"+secIST);
	/*document.getElementById("hr").innerHTML =hoursIST;
	document.getElementById("min").innerHTML =minutesIST;
	document.getElementById("sec").innerHTML =secIST;*/
//	document.write("<b>" + hoursIST + ":" + minutesIST + " " + "</b>")
	
	   remoteuser =document.getElementById("remoteemail").value;//'viki@gmail.com';
	  localStorage.setItem('remoteuser',remoteuser);
	  
	    console.log("remoteuser : "+remoteuser);
	    console.log("storage = "+localStorage.getItem('remoteuser'));
	    
	    $.ajax({
			type : 'GET',
		
			url :'/portal/servlet/service/ui.grouplist',
			
			  data: {
                    email: remoteuser
                },
			success: function (response) {
		
			var json = JSON.parse(response);
			console.log("response json : "+json);
						console.log("groups : "+json.Groups);
						
						if(json.Groups.length==0){
							json.Groups.push("NA");
						}
						console.log("groups : "+json.Groups);
						if(json.Groups.length>0){
					 var newDiv = document.getElementById("grouplist");
				   var selectHTML = "";
	//<div class="row" ><div class="col-sm-9">
				    selectHTML='<select class="custom-btn form-control"  id="grlist">';
				    for(i = 0; i < json.Groups.length; i = i + 1) {
				        selectHTML += "<option value='" + json.Groups[i] + "'>" + json.Groups[i] + "</option>";
				    }

				 //   selectHTML += '</select></div><div class="col-sm-3 p-0"><a class="btn btn-danger copy-remove-btn sm-btn-custom sm-btn-custom2"><i class="fa fa-trash"></i></a></div>';

				    newDiv.innerHTML = selectHTML;	
			}	
				 }
			});  
	    getFunnelList();
	    
	   // init();
  });

/*function checkTime(i) {
	if (i < 10) {
	i = "0" + i;
	}
	return i;
	}

	function startTime() {
		console.log("in start time");
	var today = new Date();
	var h = today.getHours();
	var m = today.getMinutes();
	var s = today.getSeconds();
	// add a zero in front of numbers<10
	m = checkTime(m);
	s = checkTime(s);
	console.log(h + ":" + m + ":" + s);
	document.getElementById("time").innerHTML = h + ":" + m + ":" + s;
	
	t = setTimeout(function() {
	startTime()
	}, 500);
	}
	*/
	


/*
var d,h,m,s,animate;

function init(){
    d=new Date();
	  d.setHours(d.getHours()-5);
	  d.setMinutes(d.getMinutes()-30);
    h=d.getHours();
    m=d.getMinutes();
    s=d.getSeconds();
    clock();
    console.log("calling init");
};

function clock(){
    s++;
    if(s==60){
        s=0;
        m++;
        if(m==60){
            m=0;
            h++;
            if(h==24){
                h=0;
            }
        }
    }
    setClock('sec',s);
    setClock('min',m);
    setClock('hr',h);
    animate=setTimeout(clock,1000);
	//console.log("animate : "+animate);
};

function setClock(id,val){
    if(val<10){
        val='0'+val;
    }
    var name=id;
	console.log("id : "+id);
	console.log("val : "+val);
    document.getElementById(name).textContent=val;
};
*/
  
  $( document).ready(function() {
	  
	//  var remoteuser ='<%=request.getRemoteUser()%>';
	  //var remoteuser ='viki@gmail.com';
	  console.log("remoteuser : "+remoteuser);
		$.ajax({
			type : 'GET',
			//url :'<%=request.getContextPath()%>/servlet/service/CampaignReportApi.urlViewData',
			url :'/portal/servlet/service/ui.get_subscriber_status?rm_email='+remoteuser,//http://prod.bizlem.io:8082
			
			data: {
		    },
			success: function (response) {
						//alert("response : "+response);
						if(response.localeCompare('Free Trial is Active')==1){
							 console.log(response);
						}else if(response.localeCompare('Free Trial Date Expired')==1){
							 console.log(response);
							 window.location = "<%=request.getContextPath()%>/servlet/service/ui.free-trail-expire";
						}else if(response.localeCompare('Subscriber Count is More')==1){
							 console.log(response);
							 window.location = "<%=request.getContextPath()%>/servlet/service/ui.free-trail-expire";
						} else{
							 console.log("Something Went Wrong");
						}
						
				 }
			});
	     });		
  //btn btn-warning mb-2 set-up-btn open-content-set-up-compaign btn-block
  
  
  
  $(".drip-funnel-section").on( "click", function() {
	  $(this).find('.arrow').toggleClass('fa-angle-down fa-angle-up');
	});

	$(".manage-leades-section").on( "click", function() {
	  $(this).find('.arrow').toggleClass('fa-angle-down fa-angle-up');
	});

	$(".set-up-funnel-section").on( "click", function() {
	  $(this).find('.arrow').toggleClass('fa-angle-down fa-angle-up');
	});

	$("select.select-funnel-main").change(function(){
	    var selectedVal = $(this).children("option:selected").val();
	    if(selectedVal){
	        $('.select-catg').css('display','block');
	    }
	});

	$("select.select-catg").change(function(){
	    var selectedVal = $(this).children("option:selected").val();
	    if(selectedVal){
	        $('.select-campaign').css('display','block');
	    }
	});

	$("select.select-campaign").change(function(){
	    var selectedVal = $(this).children("option:selected").val();
	    if(selectedVal){
	        $('.current-server-box').css('display','block');
	    }
	});

	$("select.select-campaign-new").change(function(){
	    var selectedVal = $(this).children("option:selected").val();
	    if(selectedVal){
	        $('.campaign-name-box').css('display','block');
	    }
	});

	$('.add-btn-save').click(function(){
	    $('.show-save-btn').css('display','block');
	});

	// $(document).ready(function(){
	// $('body').on('change', '.select-oprater', function () {
//	         var data = $(this).val();
//	         if(data == 'greter-than-equals' || data == 'less-than-equals' || data == 'is-null' || data == 'is-not-null'){
//	             $(this).parents('tr').find('td').find('.remove-slect').css('display','none');
//	             $(this).parents('tr').find('td').find('.remove-input').css('display','none');
//	         }else{
//	             $(this).parents('tr').find('td').find('.remove-slect').css('display','block');
//	             $(this).parents('tr').find('td').find('.remove-input').css('display','block');
//	         }
//	     });
	// });

	$(document).ready(function(){
	$('body').on('change','.click-select', function () {
	        var data = $(this).val();
	        if(data == 'category'){
	            $(this).parents('tr').find('td').find('.open-select').css('display','block');
	            $(this).parents('tr').find('td').find('.open-input').css('display','none');
	        }else if(data == 'url'){
	            $(this).parents('tr').find('td').find('.open-select').css('display','none');
	            $(this).parents('tr').find('td').find('.open-input').css('display','block');
	        }else{
	            $(this).parents('tr').find('td').find('.open-select').css('display','none');
	            $(this).parents('tr').find('td').find('.open-input').css('display','none');

	        }
	    });
	});

	$(document).ready(function(){
	$('body').on('change', '.select-oprater', function () {
	        var data = $(this).val();
	        if(data == 'eqals' || data == 'not-equals' || data == 'is-null' || data == 'is-not-null'){
	            $(this).parents('tr').find('td').find('.remove-slect').css('display','none');
	            $(this).parents('tr').find('td').find('.remove-input').css('display','none');
	        }else{
	            $(this).parents('tr').find('td').find('.remove-slect').css('display','block');
	            $(this).parents('tr').find('td').find('.remove-input').css('display','block');
	        }
	    });
	});


	$(".hide-create-new-list-box").on( "click", function() {
	    $('.create-new-list-main').slideUp();
	});

	$(".hide-meege-list-box").on( "click", function() {
	    $('.import-from-csv-file').slideUp();
	});

	$(".merge").on( "click", function() {
	    $('.import-from-csv-file').slideDown();
	});

	$(".filter-list-1-section-btn").on( "click", function() {
	    $(this).parents('.container-main').find('.filter-section-list-1').css('display','block');
	    $(this).parents('.container-main').find('.filter-right-list-1-section').removeClass('col-md-12').addClass('col-md-8');
	});

	$('input[name=radio]').click(function () {
	    if (this.id == "radio1-section") {
	        $(".show-import-from-csv-file").slideDown();
	    } else {
	        $(".show-import-from-csv-file").slideUp();
	    }
	});

	$('input[name=radio]').click(function () {
	    if (this.id == "radio3") {
	        $(".up-import-from-csv-file").slideDown();
	    } else {
	        $(".up-import-from-csv-file").slideUp();
	    }
	});

	$('input[name=radio]').click(function () {
	    if (this.id == "radio4") {
	        $(".show-import-from-csv-file2").slideDown();
	    } else {
	        $(".show-import-from-csv-file2").slideUp();
	    }
	});

	$('input[name=radio]').click(function () {
	    if (this.id == "merge-existing") {
	        $(".show-merge-existing-list").slideDown();
	    } else {
	        $(".show-merge-existing-list").slideUp();
	    }
	});

	// append set up button start
	$('body').on('click', '.add-more-btn', function(){
	  var copy = $(this).parents('.funnel-sub').find('.addMore-copy').html();
	  $(this).parents('.funnel-sub').find('.add-more-sub-copy').append(copy);
	  var sp = $(this).parents('.funnel-sub').find('.addMore-sub:last-child .set-up-btn span').html();
	  var sp1 = Number(sp)+1;
	  $(this).parents('.funnel-sub').find('.addMore-sub:last-child .set-up-btn span').html(sp1);
	});

	$('body').on('click', '.copy-remove-btn', function() { 
	  $(this).parents('.addMore-sub').remove();
	});
	// append set up button end



	//
	$('body').on('click', '.open-content-set-up-compaign',function(){
	  $('.set-up-campaign-form-box').slideDown();  
	  $(this).parents('.funnel-section').find('.funnel-sub .open-content-set-up-compaign').removeClass('active');
	  $(this).addClass('active');
	});

	/*$(function () {
	  $(".datepicker").datepicker({ 
	        autoclose: true, 
	        todayHighlight: true
	  }).datepicker('update', new Date());
	});*/

	$('input[name=radio]').click(function () {
	    if (this.id == "radio-time") {
	        $(".date-time-section").slideDown();
	    } else {
	        $(".date-time-section").slideUp();
	    }
	});

	$('.open-import-contact-main-section').click(function(){
	  $('.import-contact-main-form').slideUp();
	  $('.import-contact-main-section').slideDown();  
	});

	// custom select box

	$('input[name=radio-list]').click(function () {
	    if (this.id == "list1") {
	        $(".entire-list-section").slideDown();
	    } else {
	        $(".entire-list-section").slideUp();
	    }
	});
	$('input[name=radio-list]').click(function () {
	    if (this.id == "list2") {
	        $(".part-list-section").slideDown();
	    } else {
	        $(".part-list-section").slideUp();
	    }
	});


	$('input[name=radio-list1]').click(function () {
	    if (this.id == "list5") {
	        $(".entire-list-section").slideDown();
	    } else {
	        $(".entire-list-section").slideUp();
	    }
	});
	$('input[name=radio-list1]').click(function () {
	    if (this.id == "list6") {
	        $(".part-list-section").slideDown();
	    } else {
	        $(".part-list-section").slideUp();
	    }
	});

	$(document).ready(function () {
	    $('.to-from-list-part-section tr').click(function () {
	        if(this.style.background == "" || this.style.background =="white") {
	            $(this).css('background', '#6E000B').css('color', '#fff');
	        }
	        else {
	            $(this).css('background', 'white').css('color', '#000');
	        }
	    });
	});

	$('.creat-new-campaigns-menu').on('click' , function(){
	  $("#collapseExample2").slideDown();
	  $(".set-up-campaign-form-box").slideDown();
	});

	$(".creat-new-campaigns-menu").click(function() {
	    $('html, body').animate({
	        scrollTop: $("#op").offset().top
	    }, 2000);
	});

	$('.create-new-list').on('click' , function(){
	  $('.create-new-list-main').slideDown();  
	});

	$('body').on('click', '.add-new-row', function(){
	    var addRow = $('.create-new-list-table tbody .append-row-tr').html();
	    var noRow = $('.create-new-list-table tbody tr:last-child th:first-child').html();
	    var noRow1 = Number(noRow)+1;
	    $('.create-new-list-table tbody').append('<tr><th scope="row">'+noRow1+'</th>'+addRow+'</tr>');
	});

	$('input[name=radio-msg]').click(function () {
	    if (this.id == "radio") {
	        $(".comment-box-part").slideUp();
	    } else {
	        $(".comment-box-part").slideDown();
	    }
	});

	$('input[name=radio-msg]').click(function () {
	    if (this.id == "radio2") {
	        $(".aend-a-webpage-section").slideUp();
	    } else {
	        $(".aend-a-webpage-section").slideDown();
	    }
	});

	$('input[name=radio-tag]').click(function () {
	    if (this.id == "radio-tag") {
	        $(".drop-down-part").slideUp();
	    } else {
	        $(".drop-down-part").slideDown();
	    }
	});

	$('input[name=radio-tag]').click(function () {
	    if (this.id == "radio-tag-2") {
	        $(".drop-down-part-second").slideUp();
	    } else {
	        $(".drop-down-part-second").slideDown();
	    }
	});

	$(".drip-funnel-save").on( "click", function() {
	    $(".drip-funnel-section").trigger("click");
	    $(".manage-leades-section").trigger("click");
	});

	$(".select-existing-upload-save").on( "click", function() {
	    $(".manage-leades-section").trigger("click");
	    $(".set-up-funnel-section").trigger("click");
	});

	$(".manage-leads-create-new-list-save").on( "click", function() {
	    $(".manage-leades-section").trigger("click");
	    $(".set-up-funnel-section").trigger("click");
	});

	$(".manage-leads-existing-list-save").on( "click", function() {
	    $(".manage-leades-section").trigger("click");
	    $(".set-up-funnel-section").trigger("click");
	});

	// $('#op').trigger( "click" );

	$('input[name=checkbox-apply-funnel]').click(function () {
	    $('.show-apply-to-all-subfunnel-section').slideToggle();
	});

	$('input[name=checkbox-default-mass]').click(function () {
	    $('.show-checkbox-default-mass').slideToggle();
	});

	$('input[name=table-check-1]').click(function () {
	    $('.show-table-check-1').slideToggle();
	});

	$('input[name=table-check-2]').click(function () {
	    $('.show-table-check-2').slideToggle();
	});

	$('input[name=table-check-3]').click(function () {
	    $('.show-table-check-3').slideToggle();
	});

	$('input[name=table-check-4]').click(function () {
	    $('.show-table-check-4').slideToggle();
	});

	$('.plsu-append-row').click(function(){
	    var copy = $('.get-row-append').html();
	    $('.append-row-part').append(copy);
	});

	function toggleIcon(e) {
	    $(e.target)
	        .prev('.panel-heading')
	        .find(".more-less")
	        .toggleClass('glyphicon-plus glyphicon-minus');
	}
	$('.panel-group').on('hidden.bs.collapse', toggleIcon);
	$('.panel-group').on('shown.bs.collapse', toggleIcon);

	$('body').on('click', '.add-row-btn', function(){
	    var addRow = $('.table-set-up-funnel tbody tr:last-child').html();
	    var noRow = $(this).parents('.main-tbl').find('tbody tr:last-child th:first-child').html();
	    var noRow1 = Number(noRow)+1;

	    var id1 = $('.table-set-up-funnel tbody tr:last-child').find(".field-ID-select").attr('id');
	    var id2 = $('.table-set-up-funnel tbody tr:last-child').find(".type-ID-select").attr('id');
	    var id3 = $('.table-set-up-funnel tbody tr:last-child').find(".oprater-ID-select").attr('id');
	    var id4 = $('.table-set-up-funnel tbody tr:last-child').find(".opraters-ID-select").attr('id');
		
		var id5 = $('.table-set-up-funnel tbody tr:last-child').find(".field-name-input").attr('id');
		var id6 = $('.table-set-up-funnel tbody tr:last-child').find(".opt1-input").attr('id');
		var id7 = $('.table-set-up-funnel tbody tr:last-child').find(".opt2-input").attr('id');

	    var res = addRow.replace(id1, "fieldID"+noRow1);
	    res = res.replace(id2, "typeID"+noRow1);
	    res = res.replace(id3, "operatorID"+noRow1);
	    res = res.replace(id4, "operatorIDs"+noRow1);
		
		res = res.replace(id5, "fieldName"+noRow1);
		res = res.replace(id6, "opt1Val"+noRow1);
		res = res.replace(id7, "opt2Val"+noRow1);

	    $(this).parents('.main-tbl').find('.table-set-up-funnel tbody').append('<tr class="new">'+res+'</tr>');
	    $(this).parents('.main-tbl').find('.table-set-up-funnel tbody tr:last-child th').html(noRow1);
	});

	$('.add-new-form').click(function(){
	    var getBox = $('.append-div-box').html();
	    $('.main-box-section').append(getBox);
	});

	$('.remove-new-form').click(function(){
	    $('.main-box-section').find('li:last-child').remove();
	});

	$('body').on('click', '.remove-row-btn', function(){
	    var firstTr = $(this).parents('.main-tbl').find('tbody tr:last-child').attr('class');
	    if(firstTr == 'new'){
	        $(this).parents('.main-tbl').find('tbody tr:last-child').remove();
	    }
	});

	$('body').on('click', '.add-row-btn-second-tbl', function(){
	    var addRowNew = $('.second-tbl tbody tr:last-child').html();
	    $(this).parents('.second-tbl').find('.second-tbl-sub tbody').append('<tr class="new">'+addRowNew+'</tr>');
	});

	$('body').on('click', '.remove-row-second-tbl', function(){
	    var firstTr = $(this).parents('.second-tbl').find('.second-tbl-sub tbody tr:last-child').attr('class');
	    if(firstTr == 'new'){
	        $(this).parents('.second-tbl').find('tbody tr:last-child').remove();
	    }
	});





  
    $('#save_txt_lead').on('click', getSubscribersListFromTextArea);
	
	function getSubscribersListFromTextArea(){
	    var entire_list_name=document.getElementById("enter-the-txt-list-name").value;
			if(entire_list_name=="" || entire_list_name==null){
                alert('plz enter list_name');
				return false;
			}
				//alert('entire_list_name : '+entire_list_name);
			
	    var lesdArr=[];
		var lines = $('textarea').val().split('\n');
		for(var i = 0;i < lines.length;i++){
		    var line=lines[i].trim();
		    //alert("Lines : "+line);
		     // By tabs
			 
            //alert(line.length);			 
			var tabs = line.split('\t');
			//alert(tabs.length);
			if (line.length > 0) {
			            
						if(tabs[0].includes("@")){
						var leadObj={};
            			for(var j = 0; j < 5; j++){  
						            if(j==0){
									   if(tabs[j]==undefined){
									    leadObj['EmailAddress'] = 'NA';
									   }
									   else{
									    leadObj['EmailAddress'] = tabs[j];
									   }
									   
									}
									if(j==1){
									   //leadObj['FirstName'] = tabs[j];
									   if(tabs[j]==undefined){
									    leadObj['FirstName'] = 'NA';
									   }
									   else{
									    leadObj['FirstName'] = tabs[j];
									   }
									}
									if(j==2){
									   //leadObj['LastName'] = tabs[j];
									   if(tabs[j]==undefined){
									    leadObj['LastName'] = 'NA';
									   }
									   else{
									    leadObj['LastName'] = tabs[j];
									   }
									}
									if(j==3){
									   //leadObj['PhoneNumber'] = tabs[j];
									   if(tabs[j]==undefined){
									    leadObj['PhoneNumber'] = 'NA';
									   }
									   else{
									    leadObj['PhoneNumber'] = tabs[j];
									   }
									   
									   //alert("tabs[j] : "+tabs[j]);
									}
									if(j==4){
									   //leadObj['Address'] = tabs[j];
									   if(tabs[j]==undefined){
									    leadObj['Address'] = 'NA';
									   }
									   else{
									    leadObj['Address'] = tabs[j];
									   }
						             }
						}
						lesdArr.push(leadObj);
						}
				
				
			}
			
			
			//code here using lines[i] which will give you each line
		}
		
		    	
		    var finalleadObj={};
			finalleadObj['ListName'] = entire_list_name;
			finalleadObj['ListData'] = lesdArr;
			//alert(JSON.stringify(finalleadObj));
			console.log(JSON.stringify(finalleadObj));
			   uploadedSubscriberCount=lesdArr.length;
			     console.log("uploadedSubscriberCount list: "+uploadedSubscriberCount);
			saveLeadData(lesdArr,entire_list_name);
	}
	
	$('#process_queue').on('click', processQueue);
	function processQueue(event){
	     	//Time is 10:42, Saturday 2 February 2019 16:12 -05:30
			 $.ajax({
						url: '/servlet/service/createCampaign.processQueue',//createListAndSubscribers
						type: 'POST',
						async:false,
						data: {
				                campid:'450',
						      },
						cache: false,
						//dataType: 'json',
						//processData: false, 
						//contentType: false, 
						success: function(data)
						{
							//console.log('--------------markup---------start');
		                    console.log('data : '+data+ ' : Campaign Sent Sucessfully!');
		                    //console.log('--------------markup---------end');
		                    alert(' Campaign Sent Sucessfully!');
						}
				    });
			//alert("schedule_campaign date : 3");
	
	}
  

      
	$(document).on('click', '.open-content-set-up-compaign', function() {
	    //alert($(this).text());
		var sub_funnel_name=$(this).text();
		//alert(sub_funnel_name);// EC-Explore EnC-Entice IC-Inform WC-Warm CC-Connect
		if(sub_funnel_name.includes("EC")==true){
              localStorage.setItem('SubFunnelName',"Explore");
			  localStorage.setItem('DistanceBtnCampaign',document.getElementById("explore").value);
			  //alert('document.getElementById("explore").value; : '+document.getElementById("explore").value);
        }else if(sub_funnel_name.includes("EnC")==true){
              localStorage.setItem('SubFunnelName',"Entice");
			  localStorage.setItem('DistanceBtnCampaign',document.getElementById("entice").value);
        }else if(sub_funnel_name.includes("IC")==true){
              localStorage.setItem('SubFunnelName',"Inform");
			  localStorage.setItem('DistanceBtnCampaign',document.getElementById("inform").value);
        }else if(sub_funnel_name.includes("WC")==true){
              localStorage.setItem('SubFunnelName',"Warm");
			  localStorage.setItem('DistanceBtnCampaign',document.getElementById("warm").value);
        }else if(sub_funnel_name.includes("CC")==true){
              localStorage.setItem('SubFunnelName',"Connect");
			  localStorage.setItem('DistanceBtnCampaign',document.getElementById("connect").value);
        }
		//document.getElementById("campaign-name").campaign-name
		$("#campaign-name").attr("placeholder", "Enter the Campaign Name For "+sub_funnel_name);
  
	});
	  
    function addQueryString(){
	  //alert("inside addQueryString");
	  var campaignName = document.getElementById("campaign-name").value;
	  localStorage.setItem('campaignName',campaignName);
	  //var fromName = document.getElementById("from-name").value;
	  //var fromEmailAddress = document.getElementById("from-email-address").value;
	  //alert("localStorage.getItem('listid') "+localStorage.getItem('listid'));
	  var queryString = "?campaignName=" + campaignName + "&fromName=" + localStorage.getItem('fromName') + "&fromEmailAddress=" + localStorage.getItem('fromEmailAddress')+"&funnelName=" + localStorage.getItem('funnelName')+"&SubFunnelName=" + localStorage.getItem('SubFunnelName')+"&DistanceBtnCampaign=" + localStorage.getItem('DistanceBtnCampaign')+"&listid=" + localStorage.getItem('listid');
	  //alert('queryString : '+queryString);
	  //document.getElementById("campaign-name-nxt").setAttribute("href", "set-up-campaign.html"+queryString);
	  document.getElementById("campaign-name-nxt").setAttribute("href","/portal/servlet/service/ui.set-up-campaign");
	  //window.location.href = "page2.html" + queryString;
	  //<%=request.getContextPath()%>/servlet/service/ui.set-up-campaign
	}
  //Global Variable For Displaying Subscribers List In Table
  var entire_list_tbody="";
  var part_of_list_tbody="";
  //Global Variables for funnel setup		
	
	var funnelName;
	var fromName;
	var fromEmailAddress;
  
    $('input[type=radio][name=radio]').change(function() {
		if (this.value == 'csv_file_upload') {
			//alert("csv_file_upload is selected");
		}
		else if (this.value == 'integrated_ervices') {
			//alert("integrated_ervices is selected");
			//alert('Hello Akhilesh: 2 '+decodeURIComponent(document.cookie));
			//var cookieValue = $.cookie("funnelName");
			//alert('cookieValue: '+cookieValue);
			//alert("remoteuser : "+$.cookie("remoteuser")+"\n"+" funnelName : "+$.cookie("funnelName")+"\n"+" fromName : "+$.cookie("fromName")+"\n"+" fromEmailAddress : "+$.cookie("fromEmailAddress"));
			/*
			alert('remoteuser: '+localStorage.getItem('remoteuser'));
			alert('funnelName: '+localStorage.getItem('funnelName'));
			alert('fromName: '+localStorage.getItem('fromName'));
			alert('fromEmailAddress: '+localStorage.getItem('fromEmailAddress'));
			*/
			/*
			localStorage.getItem('remoteuser');
		    localStorage.getItem('funnelName');
		    localStorage.getItem('fromName');
		    localStorage.getItem('fromEmailAddress');
			*/
		
		
		}else if (this.value == 'create_new_list') {
			//alert("create_new_list is selected");
		}else if (this.value == 'create_from_existing_list') {
			//alert("create_new_list is selected");
			getSubscribersList();
		}else if (this.value == 'merge_existing') {
			//alert("merge_existing is selected");
			getMergeSubscribersList();
		}else{
		    //alert("You have selected something else");
		}
	});
	
	$('input[type=radio][name=radio-list1]').change(function() {
	    //alert("this.value : "+this.value);
		if (this.value == 'list5') {
			//alert("Entire List is selected");
			var e = document.getElementById("listdropbox");
			var struser = e.options[e.selectedIndex].value;
			if(struser=='Choose list'){
			    alert('Please Select List');
				document.getElementById("leads_list").innerHTML  ="";
				//return;
			}else{
			    document.getElementById("leads_list").innerHTML  =entire_list_tbody;
			}
			//getSubscribersList();
		}
		else if (this.value == 'list6') {
			//alert("Part Of List is selected part_of_leads_list");
			var e = document.getElementById("listdropbox");
			var struser = e.options[e.selectedIndex].value;
			if(struser=='Choose list'){
			    alert('Please Select List');
				document.getElementById("part_of_leads_list").innerHTML  ="";
				$( "#list_select_all" ).hide();
				$( "#list_form" ).hide();
				return;
			}else{
			    $( "#list_select_all" ).show();
				$( "#list_form" ).show();
				document.getElementById("part_of_leads_list").innerHTML  =part_of_list_tbody;
				
			}
		}else{
		    alert("You have selected something else");
		}
	});
	
	$(document).ready(function(){
		$("#listdropbox").change(function(){
		    //alert('hi : ');
			var e = document.getElementById("listdropbox");
			var list_type = $("input[name='radio-list1']:checked").val();
			var list_id = e.options[e.selectedIndex].value; 
			//alert('list_id : '+list_id);
			var list="";
			if(list_id!='Choose list'){
				entire_list_tbody="<tr><th>Sr No.</th><th>EmailAddress</th><th>First Name</th><th>Last Name</th><th>Phone Number</th><th>Address</th></tr>";
				part_of_list_tbody="<tr><th>Sr No.</th><th>Select</th><th>EmailAddress</th><th>First Name</th><th>Last Name</th><th>Phone Number</th><th>Address</th></tr>";
				$.ajax({
					type : 'POST',
					dataType: 'json',
					//contentType: false, 
					url :'/portal/servlet/service/Searchlist.getlist_data_new',
					data: {
						   selectedlistid : list_id
						 },
					success: function (data) {
								var tbody="";
								for(var i=0;i<data.length;i++){
								   var lead_info=data[i];
									//Creating Rows for PartOf List
									part_of_list_tbody=part_of_list_tbody+"<tr id='part_of_list_leadtr"+(i+1)+"' name='part_of_list_leadtr'><td>"+(i+1)+"</td><td><input id='part_of_list_leadchk"+(i+1)+"' name='part_of_list_sub_chk_boxes' type='checkbox'/></td><td>"+lead_info.Email_Name+"</td><td>"+lead_info.Name+"</td><td>NA</td><td>NA</td><td>NA</td></tr>";
									//Creating Rows for Entire List
									entire_list_tbody=entire_list_tbody+"<tr id='entire_list_leadtr"+(i+1)+"' name='entire_list_leadtr'><td>"+(i+1)+"</td><td>"+lead_info.Email_Name+"</td><td>"+lead_info.Name+"</td><td>NA</td><td>NA</td><td>NA</td></tr>";
								}
								if (list_type == 'list5') {
									document.getElementById("leads_list").innerHTML  =entire_list_tbody;
								}
								else if (list_type == 'list6') {
								    $( "#list_select_all" ).show();
				                    $( "#list_form" ).show();
									document.getElementById("part_of_leads_list").innerHTML  =part_of_list_tbody;
								}else{
									alert("You have selected something else");
								}
							}	
					});	
				}else{
				   document.getElementById("leads_list").innerHTML  ="";
				   document.getElementById("part_of_leads_list").innerHTML  ="";
				   $( "#list_select_all" ).hide();
				   $( "#list_form" ).hide();
				
				}
		    });
		});
	
	function getSubscribersList(){
	 //   var remoteuser ='viki@gmail.com';
	 	//
		console.log("remoteuser : "+remoteuser);
		$.ajax({
			type : 'POST',
			url :'/portal/servlet/service/Searchlist.listsearch',
		    data: {
			      remoteuser : remoteuser,
				 },
			success: function (dataa) {
				//alert	("Searchlist.listsearch : "+dataa);
				var jsonData = JSON.parse(dataa);
				var optionMap = "";
					for(var i=0;i<jsonData.JsonArray.length;i++){
						 var array=jsonData.JsonArray[i];
						 optionMap = optionMap + "<option value="+array.List_id+">"+array.List_Name+"</option>";
					}
				    document.getElementById("listdropbox").innerHTML  = "<option>Choose list</option>" + optionMap;
					//document.getElementById("mergelistdropbox1").innerHTML  = "<option>Choose list</option>" + optionMap;
					//document.getElementById("mergelistdropbox2").innerHTML  = "<option>Choose list</option>" + optionMap;
				}
	    });
	}
	function getMergeSubscribersList(){
	 //   var remoteuser ='viki@gmail.com';
	//	var remoteuser ='<%=request.getRemoteUser()%>';
		console.log("remoteuser : "+remoteuser);
		$.ajax({
			type : 'POST',
			url :'/portal/servlet/service/Searchlist.listsearch',
		    data: {
			      remoteuser : remoteuser,
				 },
			success: function (dataa) {
				//alert	("Searchlist.listsearch : "+dataa);
				var jsonData = JSON.parse(dataa);
				var optionMap = "";
					for(var i=0;i<jsonData.JsonArray.length;i++){
						 var array=jsonData.JsonArray[i];
						 optionMap = optionMap + "<option value="+array.List_id+">"+array.List_Name+"</option>";
					}
				    document.getElementById("mergelistdropbox1").innerHTML  = "<option>Choose list</option>" + optionMap;
					document.getElementById("mergelistdropbox2").innerHTML  = "<option>Choose list</option>" + optionMap;
				}
	    });
	}
	//save_list_merge
	$('#save_list_merge').on('click', saveMergeListLead);
	function saveMergeListLead(event)
		{   
		    //var list_type = $("input[name='radio-list1']:checked").val();
		    var e1 = document.getElementById("mergelistdropbox1");
			var mergelist1 = e1.options[e1.selectedIndex].value; 
			
			var mergelistName = e1.options[e1.selectedIndex].text; 
			//alert("mergelistName : "+mergelistName);
			//.text()
			
			var e2 = document.getElementById("mergelistdropbox2");
			var mergelist2 = e2.options[e2.selectedIndex].value; 
			
			//alert('mergelist1 : '+mergelist1+'  mergelist2 : '+mergelist2);
			
			if(mergelist1!='Choose list' && mergelist2!='Choose list'){
			    if(mergelist1==mergelist2){
				  alert("You Have Selected Same List !");
				  return;
				}
			    
				$.ajax({
					type : 'POST',
					dataType: 'json',
					async:false,
					//contentType: false, 
					url :'/portal/servlet/service/Searchlist.getlist_data_new',
					data: {
						   selectedlistid : mergelist2
						 },
					success: function (data) {
					            
								var lesdArr=[];
								for(var i=0;i<data.length;i++){
								   var lead_info=data[i];
								   var leadObj={};
									   leadObj['EmailAddress'] = lead_info.Email_Name;
									   leadObj['FirstName'] = lead_info.Name;
									   leadObj['LastName'] = 'NA';
									   leadObj['PhoneNumber'] = 'NA';
									   leadObj['Address'] = 'NA';
								   lesdArr.push(leadObj);
								}
								var finalleadObj={};
								finalleadObj['ListId'] = mergelist1;
							    finalleadObj['ListName'] = mergelistName;
							    finalleadObj['ListData'] = lesdArr;
								//alert("data : "+JSON.stringify(finalleadObj));
								saveMergeLeadData(lesdArr,mergelistName,mergelist1);
								
							}	
					});	
					
				}else{
				   alert("You have selected something else");
				    /*
				   document.getElementById("leads_list").innerHTML  ="";
				   document.getElementById("part_of_leads_list").innerHTML  ="";
				   $( "#list_select_all" ).hide();
				   $( "#list_form" ).hide();
				   */
				
				}
		}
	function saveMergeLeadData(lesdArr,listname,listid){
			    //alert(JSON.stringify(lesdArr));
				//createlistPlusLead createListAndSubscribers
				//alert('listname: '+listname);
				//alert('lesdArr: '+JSON.stringify(lesdArr));
				//console.log('lesdArr: '+JSON.stringify(lesdArr));
				//localStorage.setItem('lesdArr',JSON.stringify(lesdArr));
				
				//alert('funnelCreationStatus: '+localStorage.getItem('funnelCreationStatus'));
				if(localStorage.getItem('funnelCreationStatus')=='Funnel Created'){
				   alert('Funnel: '+localStorage.getItem('funnelName')+" Created");
				}else if(localStorage.getItem('funnelCreationStatus')=='Funnel Exists'){
				   alert('Funnel: '+localStorage.getItem('funnelName')+" Exists");
				}
				//alert('remoteuser: '+localStorage.getItem('remoteuser'));
			    //alert('funnelName: '+localStorage.getItem('funnelName'));
			    //alert('fromName: '+localStorage.getItem('fromName'));
			    //alert('fromEmailAddress: '+localStorage.getItem('fromEmailAddress'));
				
				$.ajax({
			       	//url: '<%=request.getContextPath()%>/servlet/service/uidata.createLead',
					url: '/portal/servlet/service/createListAndSubscribers.mergeList',//createListAndSubscribers
					type: 'POST',
					async: false,
                    cache: false,
                    //timeout: 30000,
					data: {
					       jsonArr:JSON.stringify(lesdArr),
					       listname:listname,
						   list_id:listid,
						   remoteuser:localStorage.getItem('remoteuser'),
						   funnelName:localStorage.getItem('funnelName'),
						   fromName:localStorage.getItem('fromName'),
						   fromEmailAddress:localStorage.getItem('fromEmailAddress'),
					      },
					//cache: false,
					//dataType: 'json',
					//processData: false, 
					//contentType: false, 
					success: function(data)
					{
					    var jsonData = JSON.parse(data);
						//alert("data: listid"+jsonData.listid);
						localStorage.setItem('listid',jsonData.listid);
						//console.log("-------------------------subscribers start-----------------------");
						//console.log("data: "+data);
						//console.log("-------------------------subscribers edn-----------------------");
					},
					//timeout: 30000 // sets timeout to 30 seconds
					
					});
			    		
					//alert('Hello Akhilesh: ');
					
			}
  
    $('#save_list_lead').on('click', saveListLead);
	function saveListLead(event)
		{   
		    var list_type = $("input[name='radio-list1']:checked").val();
			if (list_type == 'list5') {
			    var entire_list_leadtrs=document.getElementsByName("entire_list_leadtr");
				var entire_list_name=document.getElementById("entire_list_name").value;
				if(entire_list_name=="" || entire_list_name==null){
					alert('plz entire_list_name');
					return false;
				}
				//alert('entire_list_name : '+entire_list_name);
				//alert(entire_list_leadtrs.length);
				
				
						var lesdArr=[];
						for(var m=0;m<entire_list_leadtrs.length;m++){
							var lead_chk_box=entire_list_leadtrs[m];
							var leadtr=document.getElementById($(lead_chk_box).attr('id').replace("part_of_list_leadchk", "part_of_list_leadtr"));
							//alert(leadtr);  
							var leadObj={};
							for(var j=0;j<leadtr.cells.length;j++){
								//alert(leadtr.cells.item(j).innerHTML);
								if(j==1){
								   leadObj['EmailAddress'] = leadtr.cells.item(1).innerHTML;
								}
								if(j==2){
								   leadObj['FirstName'] = leadtr.cells.item(2).innerHTML;
								}
								if(j==3){
								   leadObj['LastName'] = leadtr.cells.item(3).innerHTML;
								}
								if(j==4){
								   leadObj['PhoneNumber'] = leadtr.cells.item(4).innerHTML;
								}
								if(j==5){
								   leadObj['Address'] = leadtr.cells.item(5).innerHTML;
								}
							}
							lesdArr.push(leadObj);
							var finalleadObj={};
							finalleadObj['ListName'] = entire_list_name;
							finalleadObj['ListData'] = lesdArr;
							
						}
						//alert(JSON.stringify(lesdArr));
						console.log(JSON.stringify(finalleadObj));
						saveLeadData(lesdArr,entire_list_name); 
				
				
			}
			else if (list_type == 'list6') {
				var part_of_list_leadtrs=document.getElementsByName("part_of_list_leadtr");
				//alert(part_of_list_leadtrs.length);
				
				var part_of_list_name=document.getElementById("part_of_list_name").value;
				//alert('part_of_list_name : '+part_of_list_name);
				
				if ($('input[name="part_of_list_sub_chk_boxes"]:checked').length == 0) {
						 alert('Please Select At Least One Lead.');
				} else {
						var lead_chk_boxes=$('input[name="part_of_list_sub_chk_boxes"]:checked');
						var lesdArr=[];
						for(var m=0;m<lead_chk_boxes.length;m++){
							var lead_chk_box=lead_chk_boxes[m];
							var leadtr=document.getElementById($(lead_chk_box).attr('id').replace("part_of_list_leadchk", "part_of_list_leadtr"));
							//alert(leadtr);  
							var leadObj={};
							for(var j=0;j<leadtr.cells.length;j++){
								//alert(leadtr.cells.item(j).innerHTML);
								if(j==2){
								   leadObj['EmailAddress'] = leadtr.cells.item(2).innerHTML;
								}
								if(j==3){
								   leadObj['FirstName'] = leadtr.cells.item(3).innerHTML;
								}
								if(j==4){
								   leadObj['LastName'] = leadtr.cells.item(4).innerHTML;
								}
								if(j==5){
								   leadObj['PhoneNumber'] = leadtr.cells.item(5).innerHTML;
								}
								if(j==6){
								   leadObj['Address'] = leadtr.cells.item(6).innerHTML;
								}
							}
							
							lesdArr.push(leadObj);
							var finalleadObj={};
							finalleadObj['ListName'] = part_of_list_name;
							finalleadObj['ListData'] = lesdArr;
							
						}
						//alert(JSON.stringify(lesdArr));
						console.log(JSON.stringify(finalleadObj));
						saveLeadData(lesdArr,part_of_list_name);  
				}
			}
		    
			
		 }
				
	  $('#fileUploader').on('change', uploadFile);
	    function uploadFile(event)
			{
			    event.stopPropagation(); 
				event.preventDefault(); 
				var files = event.target.files; 
				var data = new FormData();
				$.each(files, function(key, value)
				{  
				  data.append(key, value);
				});
				postFilesData(data); 
			 }
			
		function postFilesData(data)
			{
			//alert("data : "+data);
			console.log("uicsvdata : "+data);
			console.log("uicsvdata data: "+JSON.stringify(data));
			var list_type = $("input[name='radio-list']:checked").val();
			entire_list_tbody="";
            part_of_list_tbody="";
			 $.ajax({
				url: '/portal/servlet/service/uicsvdata',
				type: 'POST',
				data: data,
				cache: false,
				dataType: 'json',
				processData: false, 
				contentType: false, 
				success: function(data, textStatus, jqXHR)
				{
					//alert(JSON.stringify(data));
				    console.log("uicsvdata : "+JSON.stringify(data));
				    uploadedSubscriberCount=data.length;
				     console.log("uploadedSubscriberCount : "+uploadedSubscriberCount);
					var tbody="";
				/*	for(var i=0;i<data.length;i++){
					   var lead_info=data[i];
                       part_of_list_tbody=part_of_list_tbody+"<tr id='csv_part_of_list_leadtr"+(i+1)+"' name='csv_part_of_list_leadtr'><td>"+(i+1)+"</td><td><input id='csv_part_of_list_leadchk"+(i+1)+"' name='csv_part_of_list_sub_chk_boxes' type='checkbox'/></td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td><td>"+lead_info.CompanyName+"</td><td>"+lead_info.CompanyHeadCount+"</td><td>"+lead_info.Industry+"</td><td>"+lead_info.Institute+"</td><td>"+lead_info.Source+"</td></tr>";
					   
					   entire_list_tbody=entire_list_tbody+"<tr id='csv_entire_list_leadtr"+(i+1)+"' name='csv_entire_list_leadtr'><td>"+(i+1)+"</td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td><td>"+lead_info.CompanyName+"</td><td>"+lead_info.CompanyHeadCount+"</td><td>"+lead_info.Industry+"</td><td>"+lead_info.Institute+"</td><td>"+lead_info.Source+"</td></tr>";
					   //part_of_list_tbody=part_of_list_tbody+"<tr id='csv_part_of_list_leadtr"+(i+1)+"' name='csv_part_of_list_leadtr'><td>"+(i+1)+"</td><td><input id='csv_part_of_list_leadchk"+(i+1)+"' name='csv_part_of_list_sub_chk_boxes' type='checkbox'/></td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td></tr>";
					   
					   //entire_list_tbody=entire_list_tbody+"<tr id='csv_entire_list_leadtr"+(i+1)+"' name='csv_entire_list_leadtr'><td>"+(i+1)+"</td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td></tr>";
					}*/
					
					for(var i=0;i<data.length;i++){
						var lead_info=data[i];
						console.log("uicsvdata : i "+i);
						console.log("uicsvdata : i "+JSON.stringify(lead_info));
						//var emailStatusExist= JSON.parse(lead_info);
						if(lead_info.Email_Status){
							console.log("uicsvdata : i checked ");
						var email_info=JSON.parse(lead_info.Email_Status);
						//alert('email_info : '+JSON.stringify(email_info))
						//alert('email_info.smtp_check==true : '+email_info.smtp_check==true);
						//alert('email_info.smtp_check==true : '+email_info.smtp_check=="true");
						if(email_info.smtp_check==true){
						part_of_list_tbody=part_of_list_tbody+"<tr class='true-mail' id='csv_part_of_list_leadtr"+(i+1)+"' name='csv_part_of_list_leadtr'><td>"+(i+1)+"</td><td><input id='csv_part_of_list_leadchk"+(i+1)+"' name='csv_part_of_list_sub_chk_boxes' type='checkbox'/></td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td><td>"+lead_info.CompanyName+"</td><td>"+lead_info.CompanyHeadCount+"</td><td>"+lead_info.Industry+"</td><td>"+lead_info.Institute+"</td><td>"+lead_info.Source+"</td><td>"+email_info.smtp_check+"</td></tr>";

						entire_list_tbody=entire_list_tbody+"<tr class='true-mail' id='csv_entire_list_leadtr"+(i+1)+"' name='csv_entire_list_leadtr'><td>"+(i+1)+"</td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td><td>"+lead_info.CompanyName+"</td><td>"+lead_info.CompanyHeadCount+"</td><td>"+lead_info.Industry+"</td><td>"+lead_info.Institute+"</td><td>"+lead_info.Source+"</td><td>"+email_info.smtp_check+"</td></tr>";
						//part_of_list_tbody=part_of_list_tbody+"<tr id='csv_part_of_list_leadtr"+(i+1)+"' name='csv_part_of_list_leadtr'><td>"+(i+1)+"</td><td><input id='csv_part_of_list_leadchk"+(i+1)+"' name='csv_part_of_list_sub_chk_boxes' type='checkbox'/></td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td></tr>";
						}else{
						part_of_list_tbody=part_of_list_tbody+"<tr class='false-mail' id='csv_part_of_list_leadtr"+(i+1)+"' name='csv_part_of_list_leadtr'><td>"+(i+1)+"</td><td><input id='csv_part_of_list_leadchk"+(i+1)+"' name='csv_part_of_list_sub_chk_boxes' type='checkbox'/></td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td><td>"+lead_info.CompanyName+"</td><td>"+lead_info.CompanyHeadCount+"</td><td>"+lead_info.Industry+"</td><td>"+lead_info.Institute+"</td><td>"+lead_info.Source+"</td><td>"+email_info.smtp_check+"</td></tr>";
						entire_list_tbody=entire_list_tbody+"<tr class='false-mail' id='csv_entire_list_leadtr"+(i+1)+"' name='csv_entire_list_leadtr'><td>"+(i+1)+"</td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td><td>"+lead_info.CompanyName+"</td><td>"+lead_info.CompanyHeadCount+"</td><td>"+lead_info.Industry+"</td><td>"+lead_info.Institute+"</td><td>"+lead_info.Source+"</td><td>"+email_info.smtp_check+"</td></tr>";
						}
						
						}else{
							part_of_list_tbody=part_of_list_tbody+"<tr class='false-mail' id='csv_part_of_list_leadtr"+(i+1)+"' name='csv_part_of_list_leadtr'><td>"+(i+1)+"</td><td><input id='csv_part_of_list_leadchk"+(i+1)+"' name='csv_part_of_list_sub_chk_boxes' type='checkbox'/></td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td><td>"+lead_info.CompanyName+"</td><td>"+lead_info.CompanyHeadCount+"</td><td>"+lead_info.Industry+"</td><td>"+lead_info.Institute+"</td><td>"+lead_info.Source+"</td><td>false</td></tr>";
							entire_list_tbody=entire_list_tbody+"<tr class='false-mail' id='csv_entire_list_leadtr"+(i+1)+"' name='csv_entire_list_leadtr'><td>"+(i+1)+"</td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td><td>"+lead_info.CompanyName+"</td><td>"+lead_info.CompanyHeadCount+"</td><td>"+lead_info.Industry+"</td><td>"+lead_info.Institute+"</td><td>"+lead_info.Source+"</td><td>false</td></tr>";
						}
						//entire_list_tbody=entire_list_tbody+"<tr id='csv_entire_list_leadtr"+(i+1)+"' name='csv_entire_list_leadtr'><td>"+(i+1)+"</td><td>"+lead_info.EmailAddress+"</td><td>"+lead_info.FirstName+"</td><td>"+lead_info.LastName+"</td><td>"+lead_info.PhoneNumber+"</td><td>"+lead_info.Address+"</td></tr>";
						}
					
		            if (list_type == 'list1') {
						document.getElementById("csv_leads_list").innerHTML  =entire_list_tbody;
						//getSubscribersList();
					}
					else if (list_type == 'list2') {
					     $( "#csv_select_all" ).show();
				         $( "#csv_form" ).show();
						document.getElementById("csv_part_of_leads_list").innerHTML  =part_of_list_tbody;
					}else{
						alert("You have selected something else");
					}
					//$(listdropbox).val('Choose list');
					$('[name=listdropbox]').val('Choose list');
					
					
				},
				error: function(jqXHR, textStatus, errorThrown)
				{
					console.log('ERRORS: ' + textStatus);
					console.log('errorThrown: ' + errorThrown);
					//alert("textStatus : "+textStatus);
					alert("ERRORS : "+data);
				}
				});
			}
	$('input[type=radio][name=radio-list]').change(function() {
	    //alert("this.value : "+this.value);
		if (this.value == 'list1') {
		    if( document.getElementById("fileUploader").files.length == 0 ){
			      document.getElementById("leads_list").innerHTML  ="";
                  alert("no files selected");
            }else{
			     document.getElementById("csv_leads_list").innerHTML  =entire_list_tbody;
			}
		}
		else if (this.value == 'list2') {
		    if( document.getElementById("fileUploader").files.length == 0 ){
			      document.getElementById("csv_part_of_leads_list").innerHTML  ="";
				$( "#csv_select_all" ).hide();
				$( "#csv_form" ).hide();
                  alert("no files selected");
            }else{
			    $( "#csv_select_all" ).show();
				$( "#csv_form" ).show();
				document.getElementById("csv_part_of_leads_list").innerHTML  =part_of_list_tbody;
			}
			
		}else{
		    alert("You have selected something else");
		}
	});
	
	
	
  $('#save_csv_lead').on('click', saveLead);
	function saveLead(event)
		{   
		    var list_type = $("input[name='radio-list']:checked").val();
			if (list_type == 'list1') {
			    var entire_list_leadtrs=document.getElementsByName("csv_entire_list_leadtr");
				//alert(entire_list_leadtrs.length);
				
				var csv_entire_list_name=document.getElementById("csv_entire_list_name").value;
				if(csv_entire_list_name=="" || csv_entire_list_name==null){
                alert('plz entire_list_name');
				return false;
			}
				//alert('csv_entire_list_name : '+csv_entire_list_name);
				
				
						var lesdArr=[];
						for(var m=0;m<entire_list_leadtrs.length;m++){
							var lead_chk_box=entire_list_leadtrs[m];
							var leadtr=document.getElementById($(lead_chk_box).attr('id').replace("csv_part_of_list_leadchk", "csv_part_of_list_leadtr"));
							//alert(leadtr);  
							var leadObj={};
							for(var j=0;j<leadtr.cells.length;j++){
								//alert(leadtr.cells.item(j).innerHTML);
								if(j==1){
									   leadObj['EmailAddress'] = leadtr.cells.item(1).innerHTML;
									}
									if(j==2){
									   leadObj['FirstName'] = leadtr.cells.item(2).innerHTML;
									}
									if(j==3){
									   leadObj['LastName'] = leadtr.cells.item(3).innerHTML;
									}
									if(j==4){
									   leadObj['PhoneNumber'] = leadtr.cells.item(4).innerHTML;
									}
									if(j==5){
									   leadObj['Address'] = leadtr.cells.item(5).innerHTML;
									}
									if(j==6){
									   leadObj['CompanyName'] = leadtr.cells.item(6).innerHTML;
									}
									if(j==7){
									   leadObj['CompanyHeadCount'] = leadtr.cells.item(7).innerHTML;
									}
									if(j==8){
									   leadObj['Industry'] = leadtr.cells.item(8).innerHTML;
									}
									if(j==9){
									   leadObj['Institute'] = leadtr.cells.item(9).innerHTML;
									}
									if(j==10){
									   leadObj['Source'] = leadtr.cells.item(10).innerHTML;
									}
									/*
								if(j==1){
								   leadObj['EmailAddress'] = leadtr.cells.item(1).innerHTML;
								}
								if(j==2){
								   leadObj['FirstName'] = leadtr.cells.item(2).innerHTML;
								}
								if(j==3){
								   leadObj['LastName'] = leadtr.cells.item(3).innerHTML;
								}
								if(j==4){
								   leadObj['PhoneNumber'] = leadtr.cells.item(4).innerHTML;
								}
								if(j==5){
								   leadObj['Address'] = leadtr.cells.item(5).innerHTML;
								}
								*/
							}
							lesdArr.push(leadObj);
							var finalleadObj={};
							finalleadObj['ListName'] = csv_entire_list_name;
							finalleadObj['ListData'] = lesdArr;
							
						}
						//alert(JSON.stringify(lesdArr));
						console.log(JSON.stringify(finalleadObj));
						saveLeadData(lesdArr,csv_entire_list_name);  
				
				
			}
			else if (list_type == 'list2') {
				var part_of_list_leadtrs=document.getElementsByName("csv_part_of_list_leadtr");
				//alert(part_of_list_leadtrs.length);
				var csv_part_of_list_name=document.getElementById("csv_part_of_list_name").value;
				console.log('csv_part_of_list_name : '+csv_part_of_list_name);
			//	alert('csv_part_of_list_name : '+csv_part_of_list_name);
				
				if ($('input[name="csv_part_of_list_sub_chk_boxes"]:checked').length == 0) {
						 alert('Please Select At Least One Lead.');
				} else {
						var lead_chk_boxes=$('input[name="csv_part_of_list_sub_chk_boxes"]:checked');
						var lesdArr=[];
						for(var m=0;m<lead_chk_boxes.length;m++){
							var lead_chk_box=lead_chk_boxes[m];
							var leadtr=document.getElementById($(lead_chk_box).attr('id').replace("csv_part_of_list_leadchk", "csv_part_of_list_leadtr"));
							//alert(leadtr);  
							var leadObj={};
							for(var j=0;j<leadtr.cells.length;j++){
								//alert(leadtr.cells.item(j).innerHTML);
								if(j==2){
									   leadObj['EmailAddress'] = leadtr.cells.item(2).innerHTML;
									}
									if(j==3){
									   leadObj['FirstName'] = leadtr.cells.item(3).innerHTML;
									}
									if(j==4){
									   leadObj['LastName'] = leadtr.cells.item(4).innerHTML;
									}
									if(j==5){
									   leadObj['PhoneNumber'] = leadtr.cells.item(5).innerHTML;
									}
									if(j==6){
									   leadObj['Address'] = leadtr.cells.item(6).innerHTML;
									}
									if(j==7){
									   leadObj['CompanyName'] = leadtr.cells.item(7).innerHTML;
									}
									if(j==8){
									   leadObj['CompanyHeadCount'] = leadtr.cells.item(8).innerHTML;
									}
									if(j==9){
									   leadObj['Industry'] = leadtr.cells.item(9).innerHTML;
									}
									if(j==10){
									   leadObj['Institute'] = leadtr.cells.item(10).innerHTML;
									}
									if(j==11){
									   leadObj['Source'] = leadtr.cells.item(11).innerHTML;
									}
								/*
								if(j==2){
								   leadObj['EmailAddress'] = leadtr.cells.item(2).innerHTML;
								}
								if(j==3){
								   leadObj['FirstName'] = leadtr.cells.item(3).innerHTML;
								}
								if(j==4){
								   leadObj['LastName'] = leadtr.cells.item(4).innerHTML;
								}
								if(j==5){
								   leadObj['PhoneNumber'] = leadtr.cells.item(5).innerHTML;
								}
								if(j==6){
								   leadObj['Address'] = leadtr.cells.item(6).innerHTML;
								}
								*/
							}
							lesdArr.push(leadObj);
							var finalleadObj={};
							finalleadObj['ListName'] = csv_part_of_list_name;
							finalleadObj['ListData'] = lesdArr;
							
						}
						//alert(JSON.stringify(lesdArr));
						console.log(JSON.stringify(finalleadObj));
						saveLeadData(lesdArr,csv_part_of_list_name);
				}
			}
		    
			
		 }
		 
		 
	
	 function saveLeadData(lesdArr,listname){
			    //alert(JSON.stringify(lesdArr));
				//createlistPlusLead createListAndSubscribers
				//alert('listname: '+listname);
				//alert('lesdArr: '+JSON.stringify(lesdArr));
				//console.log('lesdArr: '+JSON.stringify(lesdArr));
				//localStorage.setItem('lesdArr',JSON.stringify(lesdArr));
				
		 console.log('funnelCreationStatus: '+localStorage.getItem('funnelCreationStatus'));
				if(localStorage.getItem('funnelCreationStatus')=='Funnel Created'){
				   alert('Funnel: '+localStorage.getItem('funnelName')+" Created");
				}else if(localStorage.getItem('funnelCreationStatus')=='Funnel Exists'){
				   alert('Funnel: '+localStorage.getItem('funnelName')+" Exists");
				}else{
					
				}
				//alert('remoteuser: '+localStorage.getItem('remoteuser'));
			    //alert('funnelName: '+localStorage.getItem('funnelName'));
			    //alert('fromName: '+localStorage.getItem('fromName'));
			    //alert('fromEmailAddress: '+localStorage.getItem('fromEmailAddress'));
				console.log("JSON.stringify(lesdArr) :: "+JSON.stringify(lesdArr));
				console.log("uploadedSubscriberCount :: "+uploadedSubscriberCount);
				var gr=localStorage.getItem('groupname');
				$.ajax({
			       	//url: '<%=request.getContextPath()%>/servlet/service/uidata.createLead',
					url: '/portal/servlet/service/createListAndSubscribers.createlistPlusLead',//createListAndSubscribers
					type: 'POST',
					async: false,
                    cache: false,
                    //timeout: 30000,
					data: {
					       jsonArr:JSON.stringify(lesdArr),
					       listname:listname,
						   remoteuser:localStorage.getItem('remoteuser'),
						   funnelName:localStorage.getItem('funnelName'),
						   fromName:localStorage.getItem('fromName'),
						   fromEmailAddress:localStorage.getItem('fromEmailAddress'),
						   SubscriberCount:uploadedSubscriberCount,
						   group:gr,
					      },
					//cache: false,
					//dataType: 'json',
					//processData: false, 
					//contentType: false, 
					success: function(data)
					{
						console.log("jsonData1 = "+data);
					    var jsonData = JSON.parse(data);
					    console.log("jsonData = "+jsonData);
						//alert("data: listid"+jsonData.listid);
					  
						if(jsonData.quantity){
						console.log("jsonData.quantity = "+jsonData.quantity);
						alert("You can not upload leads more than "+jsonData.quantity);
						 var body = '<h3>You can not upload leads more than '+jsonData.quantity+'</h3>';
						 $("#MyPopup .modal-body").html(body);
						}else{
						localStorage.setItem('listid',jsonData.listid);
						console.log("jsonData: "+jsonData);
						//alert("Subscribers List with id "+jsonData.listid+" created Sucessfully.");
						 var body = '<h4>Subscribers List created Sucessfully</h4>';
						 $("#MyPopup .modal-body").html(body);
						}
						
					   

	                    
	                  
	                   $("#MyPopup").modal("show");
						//console.log("-------------------------subscribers start-----------------------");
						
						//console.log("-------------------------subscribers edn-----------------------");
					},
					//timeout: 30000 // sets timeout to 30 seconds
					
					});
			    		
					
			}
	
	function saveFunnelData(funnelId){
		//var remoteuser ='<%=request.getRemoteUser()%>';
		//localStorage.setItem('tst_lcl',"Testing Local Storage on server");
		//alert('localStorage : '+localStorage.getItem('tst_lcl'));
		//var remoteuser ='<%=request.getRemoteUser()%>';
		//remoteuser='viki@gmail.com';
		console.log("funnel user ="+remoteuser);
		funnelName = document.getElementById("funnel-name").value;
		fromName = document.getElementById("from-name").value;
		fromEmailAddress = document.getElementById("from-email-address").value;
		//alert('Hello Akhilesh: ');
		//document.cookie = remoteuser + "=" + remoteuser + ";" + expires + ";path=/";
		//document.cookie = "remoteuser=" + remoteuser + ";"+"funnelName=" + funnelName + ";"+"fromName=" + fromName + ";"+"fromEmailAddress=" + //fromEmailAddress;
		/*
		document.cookie = remoteuser + "=" + remoteuser;
		$.cookie('remoteuser',remoteuser,{ path: '/' });
		$.cookie('funnelName',funnelName,{ path: '/' });
		$.cookie('fromName',fromName,{ path: '/' });
		$.cookie('fromEmailAddress',fromEmailAddress,{ path: '/' });
		*/
		//localStorage.setItem('remoteuser',remoteuser);
		localStorage.setItem('funnelName',funnelName);
		localStorage.setItem('fromName',fromName);
		localStorage.setItem('fromEmailAddress',fromEmailAddress);
		var groupname="";
		if(document.getElementById("grlist") && document.getElementById("grlist").value){
		
		groupname=	document.getElementById("grlist").value;
		console.log("groupname local = "+groupname);
		}else{
			console.log("groupname local = "+groupname);
		}
		localStorage.setItem('groupname',groupname);
		console.log("groupname local = "+groupname);
        //alert("remoteuser : "+remoteuser+"\n"+" funnelName : "+funnelName+"\n"+" fromName : "+fromName+"\n"+" fromEmailAddress : "+fromEmailAddress);
		var trackOpens = "true";//document.getElementById("styled-checkbox-1").checked;
		var trackClicks = "true";//document.getElementById("track-clicks").checked;
		var trackPlainTextClicks = "true";//document.getElementById("styled-checkbox-2").checked;
		var googleAnalyticssLinkTracking ="true"; //document.getElementById("styled-checkbox-3").checked;

		var autoTweetAfterSending = "true";//document.getElementById("auto").checked;
		var autoPost2SocialMedia = "true";// document.getElementById("Auto-post").checked;
		/*
		var text = '{ "employees" : [' +
				'{ "firstName":"John" , "lastName":"Doe" },' +
				'{ "firstName":"Anna" , "lastName":"Smith" },' +
				'{ "firstName":"Peter" , "lastName":"Jones" } ]}';
		*/
		var text = '{"remoteuser":"'+remoteuser+'", "funnelName":"'+funnelName+'", "fromName":"'+fromName+'", "fromEmailAddress":"'+fromEmailAddress+'", "trackOpens":"'+trackOpens+'", "trackClicks":"'+trackClicks+'", "trackPlainTextClicks":"'+trackPlainTextClicks+'", "googleAnalyticssLinkTracking":"'+googleAnalyticssLinkTracking+'", "autoTweetAfterSending":"'+autoTweetAfterSending+'", "autoPost2SocialMedia":"'+autoPost2SocialMedia+'", "group":"'+groupname+'"}';
        
		/*
		if(remoteuser=='anonymous'){
			alert('Please Login 2 http://Current Sever IP OR Domain Name/portal/login');
			return;
		}
		*/
		//alert("remoteuser : "+remoteuser);
		//alert("fromEmailAddress : "+fromEmailAddress);
		//alert("autoTweetAfterSending : "+autoTweetAfterSending);
		//alert("autoPost2SocialMedia : "+autoPost2SocialMedia);
		//alert(text);
		
		$.ajax({
	        type : 'POST',
	        url :'/portal/servlet/service/uidata.indexData',
	        async: false,
	        data: {
	    	      data : text
	             },
			success: function (dataa) {
					//subFunnelJsonObj = JSON.parse(dataa);
				//		var jsonresp = JSON.parse(dataa);
					console.log("jsonresp= "+dataa);
					if(dataa == "False"){
					alert("Funnel not creted. User does not exist ");
					}else{
					localStorage.setItem('funnelCreationStatus',dataa);
					//alert("Funnel is creted sucessfully ");
					console.log("Funnel is creted sucessfully : "+dataa)
					
					 var body = '<h3>Funnel is created successfully</h3>';

                    
                    $("#MyPopup .modal-body").html(body);
                    $("#MyPopup").modal("show");
					}
					/*
					for(var i=0;i<subFunnelJsonArray.length;i++){
					   funnel_thead=funnel_thead+"<th><a href='<%=request.getContextPath()%>/servlet/service/CampaignReportApi.campaignView?subfunnel_name="+subFunnelJsonArray[i]+"&funnel_name="+funnelId+"' target='_blank'>"+subFunnelJsonArray[i]+"</th>";
					}
					*/
					
				}
	        });	
	        
	}
	

	function getFunnelList(){
	   // var remoteuser ='viki_gmail.com';
		console.log("remoteuser : "+remoteuser);
		$.ajax({
			type : 'GET',
			url :'/portal/servlet/service/createCampaign.getFunnel?userName='+remoteuser,
			//http://prod.bizlem.io:8082/portal/servlet/service/createCampaign.getFunnel?userName=viki_gmail.com
		    data: {},
			success: function (dataa) {
				console.log	("Searchlist.listsearch : "+dataa);
				var funnel_list_json_arr = JSON.parse(dataa);
				var optionMap = "";
					for(var i=0;i<funnel_list_json_arr.length;i++){
						 var funnel_list_json_obj=funnel_list_json_arr[i];
						 //alert	("funnel_list_json_obj : "+funnel_list_json_obj);
						 optionMap = optionMap + "<option value='"+funnel_list_json_obj+"'>"+funnel_list_json_obj+"</option>";
					}
					//alert("optionMap : "+optionMap);
				    document.getElementById("funnel_list").innerHTML  = "<option>Choose list</option>" + optionMap;
					//document.getElementById("mergelistdropbox1").innerHTML  = "<option>Choose list</option>" + optionMap;
					//document.getElementById("mergelistdropbox2").innerHTML  = "<option>Choose list</option>" + optionMap;
				}
	    });
	}
	$(document).ready(function(){
		$("#funnel_list").change(function(){
		  //  var remoteuser ='viki_gmail.com';
		    //alert('hi : ');
			var e = document.getElementById("funnel_list");
			var funnel_name = e.options[e.selectedIndex].value; 
			//alert('list_id : '+list_id);
			var list="";
			if(funnel_name!='Choose list'){
				
				getFunnel(funnel_name,remoteuser);	
				}else{
				   
				
				}
		    });
	});
		
	function getFunnel(funnel_name,remoteuser) {
	    //"Rule_Engine" "SFDC_SELECTDATA" "External_Data" "TRANSFORM_DATA"
		console.log("inside getFunnel getFunnel : "+funnel_name);
		 console.log("funnel_name : "+funnel_name);
	    var username="carrotrule@xyz.com";
		var projectname="June27F01";
		var ruleenginename="June27F01_RE";
		$.ajax({
			       	//url: '<%=request.getContextPath()%>/servlet/service/uidata.createLead',
					url: '/portal/servlet/service/createCampaign.getListFunnelDetail?userName='+remoteuser+'&funnelName='+funnel_name,//July17F02',//createListAndSubscribers
					type: 'GET',
					async: false,
                    cache: false,
                    //timeout: 30000,
					data: {
					       username:username,
						   projectname:projectname,
						   ruleenginename:ruleenginename,
					       },
					success: function(data)
					{
					   console.log("getFunnel Ajax : "+data);
					   var funnel_details_json_obj = JSON.parse(data);
					   var funnelName=funnel_details_json_obj["funnelName"];
					   
					   var ExploreJsonArr=funnel_details_json_obj["Explore"];
					   var ExploreJsonObj=funnel_details_json_obj["Connect"];
					   
					   var EnticeJsonArr=funnel_details_json_obj["Entice"];
					   var EnticeJsonObj=funnel_details_json_obj["Connect"];
					   
					   var InformJsonArr=funnel_details_json_obj["Inform"];
					   var InformJsonObj=funnel_details_json_obj["Connect"];
					   
					   var WarmJsonArr=funnel_details_json_obj["Warm"];
					   var WarmJsonObj=funnel_details_json_obj["Connect"];
					   
					   var ConnectJsonArr=funnel_details_json_obj["Connect"];
					   
					   
					   
					   
					   var CampaignJsonObj=funnel_details_json_obj["Campaign"];
					   var CampaignExploreJsonArr=CampaignJsonObj["Explore"];
					   var CampaignEnticeJsonArr=CampaignJsonObj["Entice"];
					   var CampaignInformJsonArr=CampaignJsonObj["Inform"];
					   var CampaignWarmJsonArr=CampaignJsonObj["Warm"];
					   var CampaignConnectJsonArr=CampaignJsonObj["Connect"];
					   
					   var ConnectJsonObj=null;
					   if(ConnectJsonArr.length>0){
					      ConnectJsonObj=ConnectJsonArr[ConnectJsonArr.length-1];
						  //"ListStatus":"active"
						  var list_status=ConnectJsonObj.ListStatus;
						  if(list_status=='active'){
						     var list_campaign_json_arr=ConnectJsonObj.list_campaign_json_arr;
							 
							 for(var i=0;i<CampaignExploreJsonArr.length;i++){
							 
							 
							 }
						  
						  }
						 // alert();
						  console.log("Greater than Zeo");
					   }else{
						   console.log("Less than Zeo");
					   }
					   /*
					   var ConnectJsonObj=null;
					   if(CampaignExploreJsonArr.length>0){
					      ConnectJsonObj=CampaignExploreJsonArr[ConnectJsonArr.length-1];
						  alert("Greater than Zeo");
						  alert(ConnectJsonObj);
					   }else{
					      alert("Less than Zeo");
					   }
					   */
					   
					   console.log("CampaignExploreJsonArr : "+CampaignExploreJsonArr);
					   
					   var sub_funnel_heading='<tr style="text-align: center;"><td colspan="15"><strong>Sub Funnel</strong></td></tr>';

                       var category_heading='<tr style="text-align: center;"><td colspan="3"><h3>Unknown</h3></td><td colspan="3"><h3>Cold</h3></td><td colspan="3"><h3>Warm</h3></td><td colspan="3"><h3>Hot</h3></td><td colspan="3"><h3>Connect </h3></td></tr><tr><th>Explore</th><th>Date</th><th>Leads</th><th>Entice</th><th>Date</th><th>Leads</th><th>Inform</th><th>Date</th><th>Leads</th><th>Warm</th><th>Date</th><th>Leads</th><th>Connect</th><th>Date </th><th>Leads </th><tr>';

                       var final_tbl_body='';
					   
					   var tbl_body='';
					   
					   var exp_tbl_body='';
					   var ent_tbl_body='';
					   var inf_tbl_body='';
					   var warm_tbl_body='';
					   var connect_tbl_body='';
					   
					   
					   
					   var max_row_length=CampaignExploreJsonArr.length;
					   console.log('max_row_length : '+max_row_length);
					
					  /* if(i==0){
							 exp_tbl_body='<tr><td>Explore</td><td>Date</td><td>Leads</td></tr>'; 
						 }
							 ent_tbl_body='<td>Entice</td><td>Date</td><td>Leads</td>';
							 inf_tbl_body='<td>Inform</td><td>Date</td><td>Leads</td>';
							 warm_tbl_body='<td>Warm</td><td>Date</td><td>Leads</td>';
							 connect_tbl_body='<td>Connect</td><td>Date</td><td>Leads</td>';*/
							 
					   
					   for(var i=0;i<CampaignExploreJsonArr.length;i++){
						 var CampaignExploreJsonObj=CampaignExploreJsonArr[i];
						 console.log("CampaignExploreJsonObj : "+JSON.stringify(CampaignExploreJsonObj));
						 //JSON.stringify(arr)
						
						 exp_tbl_body='<td>'+CampaignExploreJsonObj.CampaignName+'</td><td>'+CampaignExploreJsonObj.Campaign_Date+'</td><td>'+CampaignExploreJsonObj.Subscribers_Count+'</td>';
						 
						  if(CampaignEnticeJsonArr[i]!=undefined){
						    var CampaignEnticeJsonObj=CampaignEnticeJsonArr[i];
							ent_tbl_body='<td>'+CampaignEnticeJsonObj.CampaignName+'</td><td>'+CampaignEnticeJsonObj.Campaign_Date+'</td><td>'+CampaignEnticeJsonObj.Subscribers_Count+'</td>';
						  }else{
						    ent_tbl_body='<td></td><td></td><td></td>';
						
						  }
						  if(CampaignInformJsonArr[i]!=undefined){
						    var CampaignInformJsonObj=CampaignInformJsonArr[i];
							inf_tbl_body='<td>'+CampaignInformJsonObj.CampaignName+'</td><td>'+CampaignInformJsonObj.Campaign_Date+'</td><td>'+CampaignInformJsonObj.Subscribers_Count+'</td>';
						  }else{
						    inf_tbl_body='<td></td><td></td><td></td>';
						
						  }
						  if(CampaignWarmJsonArr[i]!=undefined){
						    var CampaignWarmJsonObj=CampaignWarmJsonArr[i];
							warm_tbl_body='<td>'+CampaignWarmJsonObj.CampaignName+'</td><td>'+CampaignWarmJsonObj.Campaign_Date+'</td><td>'+CampaignWarmJsonObj.Subscribers_Count+'</td>';
						  }else{
						    warm_tbl_body='<td></td><td></td><td></td>';
						
						  }
						  if(CampaignConnectJsonArr[i]!=undefined){
						    var CampaignConnectJsonObj=CampaignConnectJsonArr[i];
						    connect_tbl_body='<td>'+CampaignConnectJsonObj.CampaignName+'</td><td>'+CampaignConnectJsonObj.Campaign_Date+'</td><td>'+CampaignConnectJsonObj.Subscribers_Count+'</td>';
						  }else{
						    connect_tbl_body='<td></td><td></td><td></td>';
						
						  }
						  
						  tbl_body=tbl_body+'<tr>'+exp_tbl_body+ent_tbl_body+inf_tbl_body+warm_tbl_body+connect_tbl_body+'</tr>';
						 
						
					   }
					   
					   document.getElementById("sub_funnel_tbl_body").innerHTML  = sub_funnel_heading + category_heading + tbl_body;
					   console.log("ExploreJsonArr : "+ExploreJsonArr);
					   console.log("ConnectJsonArr : "+ConnectJsonArr);
					   var campaign_json_obj=null;
					   var CreatedBy=null;
					   var funnelName=null;
					   var SubFunnelName=null;
					   var CampaignNodeNameInSling=null;
					   var FromName=null;
					   var FromEmailAddress=null;
					   var CampaignName=null;
					   var Subject=null;
					   var Body=null;
					   var Type=null;
					   var Campaign_Id=null;
					   var List_Id=null;
					   var campaign_status=null;
					   var Campaign_Date=null;
					   
					   var explore_row_add_more_sub_copy="";
					   var entice_row_add_more_sub_copy="";
					   var inform_row_add_more_sub_copy="";
					   var warm_row_add_more_sub_copy="";
					   var connect_row_add_more_sub_copy="";
					   
					}
	    });
	}
	
	// $('#schedule_campaign').on('click', scheduleCampaign);
	
	function scheduleCampaign(event){
		
		var today = new Date();
		var h = today.getHours();
		var m = today.getMinutes();
		var s = today.getSeconds();
		// add a zero in front of numbers<10
		m = checkTime(m);
		s = checkTime(s);
		//console.log(h + ":" + m + ":" + s);
		//alert(document.getElementById("schtime"));
	
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth() + 1; //January is 0!

		var yyyy = today.getFullYear();
		if (dd < 10) {
		  dd = '0' + dd;
		} 
		if (mm < 10) {
		  mm = '0' + mm;
		} 
		var today = yyyy + '-' + mm + '-' + dd ;
		document.getElementById("campaign_shedule_date").innerHTML =today;
	     var campaign_shedule_date = today;
	     console.log("today= "+today);
		 if(document.getElementById('radio-time').checked) {
			  //Male radio button is checked
			  var campaign_shedule_hr = document.getElementById("campaign_shedule_hr").value;
			  var campaign_shedule_minute = document.getElementById("campaign_shedule_minute").value;
			//  var campaign_shedule_AP = document.getElementById("campaign_shedule_AP").value;
			  //alert("campaign_shedule_hr : "+campaign_shedule_hr+"\n"+"campaign_shedule_minute : "+campaign_shedule_minute+"\n"+"campaign_shedule_AP : "+campaign_shedule_AP);
			  //campaign_shedule_date=campaign_shedule_date+" "+campaign_shedule_hr+":"+campaign_shedule_minute+":00 "+campaign_shedule_AP;
			  campaign_shedule_date=campaign_shedule_date+" "+campaign_shedule_hr+":"+campaign_shedule_minute+":00";
			  
			  //2019-01-07 06:43:02 PM
			  //2019-01-11 02:11:00 PM
			  //01-11-2019 01:01:00 am
		 }else{
		 
		     campaign_shedule_date=campaign_shedule_date+" 00:00:00";
		 }
		 console.log("campaign_shedule_date = "+campaign_shedule_date);
		 //alert("schedule_campaign date : "+campaign_shedule_date);
		    var campaignid=localStorage.getItem('Expcampaignid');
		    console.log("campaignid = "+campaignid);
			//var campaignid='593';
			var markup = "";
		    var campaignName;
			var fromName;
			var fromEmailAddress;
			
			var funnelName;
			var SubFunnelName;
			var DistanceBtnCampaign;
			var listid;
			var subjectName;
			//Time is 10:42, Saturday 2 February 2019 16:12 -05:30
			var funnelName=localStorage.getItem('funnelName');
			var SubFunnelName=localStorage.getItem('SubFunnelName');
			var group=localStorage.getItem('groupname');
			console.log("group= "+group);
			var listid=localStorage.getItem('listid');
			var remoteuser =localStorage.getItem('remoteuser');
			 $.ajax({
						//url: '<%=request.getContextPath()%>/servlet/service/uidata.createLead',
						url: '/portal/servlet/service/createCampaign.updateEmbargo',//createListAndSubscribers
						type: 'POST',
						async:false,
						data: {
				               campaignid:campaignid,
						       embargo:campaign_shedule_date,
						       funnelName:funnelName,
						       camp_catogery:SubFunnelName,
						       listid:listid,
						       remoteuser:remoteuser,
						       group:group,
							  },
						cache: false,
						//dataType: 'json',
						//processData: false, 
						//contentType: false, 
						success: function(data)
						{
							//console.log('--------------markup---------start');
		                    console.log('data : '+data);
		                    //console.log('--------------markup---------end');
		                  //  alert('Embargo Updated Sucessfully!');
		                    
		                    var body = '<h4>Embargo Updated Successfully</h4>';

		                    
		                    $("#MyPopup .modal-body").html(body);
		                    $("#MyPopup").modal("show");
						}
				    });
			//alert("schedule_campaign date : 3");

	}
	$(function () {
		  
	      $("#btnClosePopup").click(function () {
	          $("#MyPopup").modal("hide");
	      });
	  });
   
