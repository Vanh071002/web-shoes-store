
function follow(brandName){
    $.ajax({
        url: "/demo_war_exploded/followBrand",
        type: "get",
        data: {
            brandName: brandName
        },
        success: function() {

        },
        error: function(xhr){
            console.log("Error:" + xhr.responseText);
        }
    });
}
function unfollow(brandName){
    $.ajax({
        url: "/demo_war_exploded/unfollowBrand",
        type: "get",
        data: {
            brandName: brandName
        },
        success: function() {

        },
        error: function(xhr){
            console.log("Error:" + xhr.responseText);
        }
    });
}
function changeFollow(element) {
    let isFollowed = element.innerText;
    let brandName = deleteLetter(element.getAttribute("id"), "button-id-");

    if(isFollowed == "Unfollow"){
        unfollow(brandName);
        element.innerText = "Follow";
    }
    else {
        follow(brandName);
        element.innerText = "Unfollow";
    }
}
function deleteLetter(str, letter) {
    return str.replace(new RegExp(letter, "g"), "");
}