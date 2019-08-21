/*$(".drip-funnel-section").on( "click", function() {
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
//         var data = $(this).val();
//         if(data == 'greter-than-equals' || data == 'less-than-equals' || data == 'is-null' || data == 'is-not-null'){
//             $(this).parents('tr').find('td').find('.remove-slect').css('display','none');
//             $(this).parents('tr').find('td').find('.remove-input').css('display','none');
//         }else{
//             $(this).parents('tr').find('td').find('.remove-slect').css('display','block');
//             $(this).parents('tr').find('td').find('.remove-input').css('display','block');
//         }
//     });
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

$(function () {
  $(".datepicker").datepicker({ 
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




*/