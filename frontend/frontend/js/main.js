const API_BASE_URL =
    "http://13.125.232.98:8080";

document
.getElementById("uploadBtn")
.addEventListener("click", () => {

    window.location.href =
        "upload.html";
});

document.addEventListener(
    "DOMContentLoaded",
    loadRecentRestaurants
);

async function loadRecentRestaurants(){

    try{

        const response =
        await fetch(
            `${API_BASE_URL}/api/food/preference/recommend`
        );

        const data =
        await response.json();

        renderRestaurants(
            data.places.slice(0,5)
        );

    }catch(error){

        console.error(error);

        document.getElementById(
            "recentRestaurantList"
        ).innerHTML =
        "<p>추천 맛집을 불러오지 못했습니다.</p>";
    }
}

function renderRestaurants(places){

    const container =
    document.getElementById(
        "recentRestaurantList"
    );

    container.innerHTML = "";

    places.forEach(place => {

        const card =
        document.createElement("div");

        card.className =
        "restaurant-card";

        card.innerHTML = `

            <div class="card-body">

                <span class="badge">
                    추천 맛집
                </span>

                <h3>
                    ${place.name}
                </h3>



                <p class="address">
                    ${place.roadAddressName}
                </p>

                <p class="phone">
                    ${
                        place.phone
                        ? place.phone
                        : "전화번호 없음"
                    }
                </p>

                <a
                    href="${place.placeUrl}"
                    target="_blank"
                    class="map-btn"
                >
                    카카오맵 보기
                </a>

            </div>

        `;

        container.appendChild(card);
    });
}