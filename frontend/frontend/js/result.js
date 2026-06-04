const category =
localStorage.getItem("category");

const confidence =
localStorage.getItem("confidence");

document
.getElementById("category")
.innerText =
category;

document
.getElementById("confidence")
.innerText =
`신뢰도 ${confidence}`;

const restaurants = {

    "한식":[
        "백반집",
        "국밥집",
        "한정식 맛집"
    ],

    "일식":[
        "스시 전문점",
        "돈카츠 전문점",
        "라멘 전문점"
    ]
};

const list =
document.getElementById(
"restaurantList"
);

(restaurants[category] || [])
.forEach(item=>{

    const li =
    document.createElement("li");

    li.innerText = item;

    list.appendChild(li);

});