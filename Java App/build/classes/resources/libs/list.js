
// no jQuery, just for "fun". It was no fun at all.
var addListItem, animationEndEvent, i, /*input,*/ item, itemClickHandler, len, prefix, ref, removeListItem;

prefix = (function () {
    var dom, pre, styles;
    styles = window.getComputedStyle(document.documentElement, "");
    pre = (Array.prototype.slice.call(styles).join("").match(/-(moz|webkit|ms)-/) || (styles.OLink === "" && ["", "o"]))[1];
    dom = "WebKit|Moz|MS|O".match(new RegExp("(" + pre + ")", "i"))[1];
    return {
        dom: dom,
        lowercase: pre,
        css: "-" + pre + "-",
        js: pre[0].toUpperCase() + pre.substr(1)
    };
})();

animationEndEvent = prefix.lowercase + "AnimationEnd";

if (prefix.lowercase === "moz") {
    animationEndEvent = "animationend";
}

console.log(animationEndEvent);

window.insertAnimation = function (text, list) {
    var closeIcon, firstItem, i, item, items, len, newItem, ref;
    items = list.querySelectorAll(".list__item");
    ref = list.querySelectorAll(".list__item");
    for (i = 0, len = ref.length; i < len; i++) {
        item = ref[i];
        item.classList.add("list__item--inserting");
        item.addEventListener(animationEndEvent, function (event) {
            return event.target.classList.remove("list__item--inserting");
        });
    }
    newItem = document.createElement("li");
    newItem.classList.add("list__item");
    newItem.classList.add("list__item--inserting-new");
    newItem.innerHTML = text;
    closeIcon = document.createElement("i");
    closeIcon.classList.add("icon-close");
    newItem.appendChild(closeIcon);
    closeIcon.addEventListener("click", itemClickHandler);
    firstItem = items[0];
    list.insertBefore(newItem, firstItem);
    return newItem.addEventListener(animationEndEvent, function (event) {
        return event.target.classList.remove("list__item--inserting-new");
    });
};

window.removeAnimation = function (index, list) {
    var handler, i, item, items, len, postCount, postItem, postItems;
    items = list.querySelectorAll(".list__item");
    item = items.item(index);
    item.classList.add("list__item--inserting-removed");
    postItems = document.querySelectorAll(`.list__item:nth-child(1n+${index + 2})`);
    postCount = 0;
    handler = function (event) {
        var i, len, removeItem, results;
        event.target.removeEventListener(animationEndEvent, arguments.callee);
        postCount++;
        if (postCount === postItems.length || postCount > 5) {
            list.removeChild(item);
            results = [];
            for (i = 0, len = postItems.length; i < len; i++) {
                removeItem = postItems[i];
                results.push(removeItem.classList.remove("list__item--removing-sibling"));
            }
            return results;
        }
    };
    for (i = 0, len = postItems.length; i < len; i++) {
        postItem = postItems[i];
        postItem.classList.add("list__item--removing-sibling");
        postItem.addEventListener(animationEndEvent, handler);
    }
    if (postItems.length === 0) {
        return item.addEventListener(animationEndEvent, function () {
            return list.removeChild(item);
        });
    }
};

addListItem = function (data) {
    var list = document.querySelectorAll(".list").item(0);
    if (data !== "") {
        return insertAnimation(data, list);
    }
};

getCurrentData = function () {
    var arr = document.getElementsByClassName("list__item");
    var final = [];
    console.log("lenght" + arr.length);
    for (index = 0; index < arr.length; ++index) {
        console.log(arr[index].textContent.trim() + " idx : " + index);
        final.push(arr[index].textContent.trim());
    }
    console.log("final" + final );
    return final;
    
};

removeListItem = function (index) {
    var list;
    list = document.querySelectorAll(".list").item(0);
    return removeAnimation(index, list);
};

//input = document.querySelectorAll(".list-input")[0];
//
//input.addEventListener("keyup", function (event) {
//    if (event.keyCode === 13) {
//        return addListItem();
//    }
//});

itemClickHandler = function (event) {
    console.log("delete");
    var index, itemsArray, itemsNodeList, listItem;
    listItem = event.target.parentNode;
    event.target.removeEventListener("click", arguments.callee);
    itemsNodeList = document.querySelectorAll(".list .list__item");
    itemsArray = Array.prototype.slice.call(itemsNodeList);
    index = itemsArray.indexOf(listItem);
    return removeListItem(index);
};

ref = document.querySelectorAll(".list .list__item .icon-close");
for (i = 0, len = ref.length; i < len; i++) {
    item = ref[i];
    item.addEventListener("click", itemClickHandler);
}

//  input.style.marginBottom = String(input.offsetHeight) + "px";