$(document).ready(function(){
		document.getElementsByName('uploadnew')[0].checked=true;

        $("input[name='uploadnew']").click(function(){
            var radioValue = $("input[name='uploadnew']:checked").val();
            if(radioValue=="existing"){
                $(".existing-tab").removeClass("hidden");
				$(".upload-new-class").addClass("hidden");
				document.getElementsByName('uploadnew2')[1].checked=true;
            }else if(radioValue=="uploadnew"){
				$(".upload-new-class").removeClass("hidden");
				$(".existing-tab").addClass("hidden");
				document.getElementsByName('uploadnew')[0].checked=true;
			}
        });
		$("input[name='uploadnew2']").click(function(){
            var radioValue = $("input[name='uploadnew2']:checked").val();
            if(radioValue=="existing2"){
                $(".existing-tab").removeClass("hidden");
				$(".upload-new-class").addClass("hidden");
				document.getElementsByName('uploadnew2')[1].checked=true;
            }else if(radioValue=="new2"){
				$(".upload-new-class").removeClass("hidden");
				$(".existing-tab").addClass("hidden");
				document.getElementsByName('uploadnew')[0].checked=true;
			}
        });
    });
	
	 $(document).ready(function(){
        $("input[name='uploadnew-sms']").click(function(){
            var radioValue = $("input[name='uploadnew-sms']:checked").val();
            if(radioValue=="existingsms"){
                $(".existing-tab-sms").removeClass("hidden");
				$(".upload-new-class-sms").addClass("hidden");
				document.getElementsByName('uploadnew2-sms')[1].checked=true;
            }else if(radioValue=="uploadnewsms"){
				$(".upload-new-class-sms").removeClass("hidden");
				$(".existing-tab-sms").addClass("hidden");
				document.getElementsByName('uploadnew-sms')[0].checked=true;
			}
        });
		$("input[name='uploadnew2-sms']").click(function(){
            var radioValue = $("input[name='uploadnew2-sms']:checked").val();
            if(radioValue=="existing2sms"){
                $(".existing-tab-sms").removeClass("hidden");
				$(".upload-new-class-sms").addClass("hidden");
				document.getElementsByName('uploadnew2-sms')[1].checked=true;
            }else if(radioValue=="new2sms"){
				$(".upload-new-class-sms").removeClass("hidden");
				$(".existing-tab-sms").addClass("hidden");
				document.getElementsByName('uploadnew-sms')[0].checked=true;
			}
			
        });
		
		$('#uploadBtn').on("change", function(){
			$("#input-uploadBtn").val($(this).val())
			});
			
		$('#uploadBtn-search').on("change", function(){
			$("#input-uploadBtn-search").val($(this).val())
			});
			
		$('#uploadBtn-sms').on("change", function(){
			$("#input-uploadBtn-sms").val($(this).val())
			});
			
		$('#uploadBtn-sms-search').on("change", function(){
			$("#input-uploadBtn-sms-search").val($(this).val())
			});			

    });
	 $(document).ready(function(){
       $('iframe').bind('mouseover', function(){
          var iframeID = $(this).attr('id');
          $(this).contents().bind('click', function(){
			var h =$("#"+iframeID).contents().find("body").html()
			 var ref_this = $('ul.tabs li.active');
			if(ref_this.attr("id")=="explore"){
				CKEDITOR.instances['editorexplore1'].setData(h);
			}else if(ref_this.attr("id")=="inform"){
				CKEDITOR.instances['editorexplore2'].setData(h);
			}else if(ref_this.attr("id")=="warm"){
				CKEDITOR.instances['editorexplore3'].setData(h);
			}
			else if(ref_this.attr("id")=="entice"){
				CKEDITOR.instances['editorexplore4'].setData(h);
			}
			else if(ref_this.attr("id")=="convert"){
				CKEDITOR.instances['editorexplore5'].setData(h);
			}
			  
          });
       }); 

    });

