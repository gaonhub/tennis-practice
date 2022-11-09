
var startTime= "";
var endTime= "";

$('#matchingDate').daterangepicker({
    "timePicker": true,
    "timePicker24Hour": true,
    "timePickerIncrement": 10,
    "startDate": "11/03/2022",
    "endDate": "11/09/2022",
    locale: {
        format: 'M/DD hh:mm'
    }
    // zindex: 9999
}, function (start, end, label) {
    console.log('New date range selected: ' + start.format('YYMMDDHHmm') + ' to ' + end.format('YYMMDDHHmm') + ' (predefined range: ' + label + ')');

    startTime = start;
    endTime = end;
});


$(document).ready(function () {

    $("#submit_btn").click(function () {
        console.log(startTime);
        console.log(endTime);

        let matchingForm = {
            title: $("#matchingTitle").val(),
            // startDate: JSON.stringify(startTime),
            // endDate: JSON.stringify(endTime),
            place: $("#matchingPlace").val(),
            matchingType: $("#matchingType2").val(),
            courtType: $("#courtType2").val(),
        }
        console.log(matchingForm);

        $.ajax({
            type: "post",
            url: "/matching/new",
            data: $('#matchingTitle').val(),
            success: function (response) {
                alert("성공!")
            },
            error: function (response) {
                alert("서버 에러!");
            },
        });
    });


});


// $(document).ready(function () {
//     var now_utc = Date.now()
//     var timeOff = new Date().getTimezoneOffset() * 60000;
//     var today = new Date(now_utc - timeOff).toISOString().split("T")[0];
//     // document.getElementById("matchingDate").setAttribute("min", today);
//     $("#matchingDate").attr({"min" : today });
// });
//
//
// $('#matchingStartTime').timepicker({
//     timeFormat: 'HH:mm',
//     interval: 10,
//     minTime: '00:00',
//     maxTime: '23:50',
//     dynamic: false,
//     dropdown: true,
//     scrollbar: true,
//     zindex: 9999
// });
//
// $('#matchingEndTime').mouseover(function () {
//     // 시작 시간 선택 후 종료시간 최소 +10분 설정
//     var stime = $('#matchingStartTime').val();
//     var sgetTime = stime.split(":"); //split time by colon
//     var sminutes = parseInt(sgetTime[1])+10; //add two hours
//     var sNewTime = sgetTime[0] +":"+sminutes;
//     console.log(sNewTime);
//     $('#matchingEndTime').timepicker('option', 'minTime', sNewTime);
//     $('#matchingEndTime').timepicker('option', 'startTime', sNewTime);
// });
//
// $("#matchingEndTime").mouseover(function(){
//     // 시작 시간 선택 후 종료시간 최대 +2시간 설정
//     var time = $('#matchingStartTime').val();
//     var getTime = time.split(":"); //split time by colon
//     var hours = parseInt(getTime[0])+2; //add two hours
//     //set new time
//     var newTime = hours+":"+getTime[1];
//     //set time picker
//     console.log(newTime);
//     $("#matchingEndTime").timepicker('option','maxTime', newTime);
// })
//
// $('#matchingEndTime').timepicker({
//     timeFormat: 'HH:mm',
//     interval: 10,
//     dynamic: false,
//     dropdown: true,
//     scrollbar: true,
//     zindex: 9999
// });
//
//
