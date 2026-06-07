const result =
    JSON.parse(localStorage.getItem("uploadResult"));

if (!result) {
    alert("분석 결과가 없습니다.");
    window.location.href = "upload.html";
}

// =========================
// 기본 정보 출력
// =========================

document.getElementById("category").innerText =
    result.category || "-";

document.getElementById("confidence").innerText =
    `신뢰도 ${result.topConfidence || 0}%`;

document.getElementById("foodType").innerText =
    result.foodType || "-";

document.getElementById("searchKeyword").innerText =
    result.searchKeyword || "-";

// =========================
// Rekognition 태그 출력
// =========================

const labelList = document.getElementById("labelList");
labelList.innerHTML = "";

if (result.labels && result.labels.length > 0) {

    result.labels.forEach(label => {

        const tag = document.createElement("div");
        tag.classList.add("label-tag");

        tag.innerText =
            `${label.name} (${label.confidence.toFixed(1)}%)`;

        labelList.appendChild(tag);
    });

} else {
    labelList.innerHTML = "<p>분석 태그가 없습니다.</p>";
}

// =========================
// 추천 맛집 출력
// =========================

const restaurantList = document.getElementById("restaurantList");
restaurantList.innerHTML = "";

if (result.places && result.places.length > 0) {

    result.places.forEach(place => {

        const card = document.createElement("div");
        card.classList.add("restaurant-card");

        card.innerHTML = `
            <h3>${place.name}</h3>

            <p><strong>카테고리 :</strong> ${place.categoryName || "-"}</p>
            <p><strong>주소 :</strong> ${place.roadAddressName || place.addressName || "-"}</p>
            <p><strong>거리 :</strong> ${place.distance || "-"}m</p>

            ${
                place.phone
                    ? `<p><strong>전화 :</strong> ${place.phone}</p>`
                    : ""
            }

            ${
                place.placeUrl
                    ? `<a href="${place.placeUrl}" target="_blank" class="map-link">
                        카카오맵에서 보기
                      </a>`
                    : ""
            }
        `;

        restaurantList.appendChild(card);
    });

} else {
    restaurantList.innerHTML =
        "<p>추천 맛집이 없습니다.</p>";
}