$(document).ready(function () {
    $(".category").click(function () {
        const card = document.getElementsByClassName("card_area");
        const selectedCategory = $(this).attr("title");

        Array.from(document.getElementsByClassName("category")).forEach(category => category.classList.remove("active"));
        document.getElementById($(this).attr("id")).classList.add("active");

        $(card).hide();

        if (selectedCategory === $("#list_all").attr("title")) {
            $(card).show();
        } else {
            $(".card_area[title=" + selectedCategory + "]").show();
        }
    });

    let no = $('.rankNo').text();
    console.log(no);
    let plusNo = no + 1;
    console.log(plusNo);
    $('.rankNo').attr("text", plusNo)
});



