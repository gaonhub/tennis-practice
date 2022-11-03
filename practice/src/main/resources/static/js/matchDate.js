$(document).ready(function () {
    var now_utc = Date.now()
    var timeOff = new Date().getTimezoneOffset()*60000;
    var today = new Date(now_utc-timeOff).toISOString().split("T")[0];
    $("#matchDate").attr({"min" : today});
});


$("#startTime").timepicker({
    timeFormat: 'HH:mm p',
    interval: 10,
    startTime: '00:00',
    dynamic: false,
    dropdown: true,
    scrollbar: true
});


$('.timepicker').data("timepicker").getTime();

$("#endTime").mouseover(function (){
    var startTime = $('#startTime').val() + 10;
    var maxHour = $('#startTime').data('timepicker').hour;
    var maxMinute = $('#startTime').data('timepicker').minute;
    console.log(maxHour)
    console.log(maxMinute)
    $('#endTime').timepicker('option','minTime',startTime);
});


// $("#endTime").mouseover(function (){
//     var endTime = $('#startTime').val() + 30;
//     $('#endTime').timepicker('option','maxTime',endTime);
// });

$("#endTime").timepicker({
    timeFormat: 'HH:mm p',
    interval: 10,
    dynamic: false,
    dropdown: true,
    scrollbar: true
});




// $('#startTime').change(function(){
//
//     var nexttime = $('li.ui-timepicker-selected').next('li').text();
//
//     $('#endTime').timepicker('option','minTime',nexttime);
//
// });

// $(document).ready(function() {
//     fn_egov_init_date()
// })
//
// function fn_egov_init_date() {
//     var $startTime = $("#startTime");
//     var $endTime = $('#endTime');
//     $startTime.timepicker({
//         lang: 'ko',
//         timeFormat: 'HH:mm p',
//         startTime: '00:00',
//         dynamic: false,
//         dropdown: true,
//         scrollbar: true,
//         onShow: function (ct) {
//             this.setOptions({
//                 max: $endTime.val() ? $endTime.val() : false
//             })
//         },
//     });
//
//     $endDate.timepicker({
//         lang: 'ko',
//         timeFormat: 'HH:mm p',
//         startTime: '00:00',
//         dynamic: false,
//         dropdown: true,
//         scrollbar: true,
//         onShow: function (ct) {
//             this.setOptions({
//                 min: $startTime.val() ? $startTime.val() : false
//             })
//         }
//     });
//
// }





