const API_BASE_URL =
    "http://13.125.232.98:8080";

document.addEventListener(
    "DOMContentLoaded",
    loadRecommend
);

async function loadRecommend() {

    try {

        const response =
        await fetch(
            `${API_BASE_URL}/api/food/preference/recommend`
        );

        if (!response.ok) {
            throw new Error(
                `HTTP ${response.status}`
            );
        }

        const data =
        await response.json();

        console.log(data);

        renderRecommend(data);

    }
    catch (error) {

        console.error(error);

        document.getElementById(
            "foodStats"
        ).innerHTML =
        "<p>추천 정보를 불러오지 못했습니다.</p>";

        document.getElementById(
            "restaurantList"
        ).innerHTML =
        "<p>추천 정보를 불러오지 못했습니다.</p>";
    }
}

function renderRecommend(data) {

    // 상단 요약

    document.getElementById(
        "totalUploads"
    ).innerText =
    data.totalUploadCount ?? "-";

    document.getElementById(
        "favoriteCategory"
    ).innerText =
    data.favoriteCategory ?? "-";

    document.getElementById(
        "favoriteFoodType"
    ).innerText =
    data.favoriteFoodType ?? "-";

    document.getElementById(
        "favoriteCount"
    ).innerText =
    data.favoriteFoodTypeCount ?? "-";

    document.getElementById(
        "searchKeyword"
    ).innerText =
    data.searchKeyword ?? "-";


    // 업로드 통계

    const statsContainer =
    document.getElementById(
        "foodStats"
    );

    statsContainer.innerHTML = "";

    data.foodTypeCounts.forEach(item => {

        const row =
        document.createElement("div");

        row.className =
        "stat-row";

        row.innerHTML = `
            <span>${item.foodType}</span>
            <span>${item.count}회</span>
        `;

        statsContainer.appendChild(row);
    });


    // 추천 맛집

    const restaurantContainer =
    document.getElementById(
        "restaurantList"
    );

    restaurantContainer.innerHTML = "";

    data.places.forEach(place => {

        const card =
        document.createElement("div");

        card.className =
        "restaurant-card";

        card.innerHTML = `
            <h3>${place.name}</h3>

            <p>
                ${place.categoryName}
            </p>

            <p>
                ${place.roadAddressName}
            </p>

            <p>
                ${place.phone || "전화번호 없음"}
            </p>

            <a
                href="${place.placeUrl}"
                target="_blank"
            >
                카카오맵 보기
            </a>
        `;

        restaurantContainer.appendChild(card);
    });
}