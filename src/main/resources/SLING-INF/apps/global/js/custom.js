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

$('body').on('click', '.open-content-set-up-compaign',function(){
  $('.set-up-campaign-form-box').slideDown();  
  $(this).parents('.funnel-section').find('.funnel-sub .open-content-set-up-compaign').removeClass('active');
  $(this).addClass('active');
});

$(function () {
  $("#datepicker").datepicker({ 
        autoclose: true, 
        todayHighlight: true
  }).datepicker('update', new Date());
});

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

$('.create-new-list').click(function(){
  $('.create-new-list-box').slideDown();  
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
