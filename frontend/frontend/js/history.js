const API_BASE_URL = "http://13.125.232.98:8080";

document.addEventListener("DOMContentLoaded", loadHistory);

async function loadHistory() {
    try {
        const response = await fetch(
            `${API_BASE_URL}/api/food/uploads`
        );

        if (!response.ok) {
            throw new Error("업로드 기록 조회 실패");
        }

        const uploads = await response.json();
        renderHistory(uploads);

    } catch (error) {
        console.error(error);

        document.getElementById("historyContainer").innerHTML = `
            <div class="empty">
                데이터를 불러올 수 없습니다.
            </div>
        `;
    }
}

function renderHistory(data) {
    const container = document.getElementById("historyContainer");
    container.innerHTML = "";

    if (data.length === 0) {
        container.innerHTML = `
            <div class="empty">
                업로드 기록이 없습니다.
            </div>
        `;
        return;
    }

    data.forEach(item => {

        const labels = item.labels
            ?.map(label =>
                `<span class="tag">${label.name}</span>`
            )
            .join("");

        const card = document.createElement("div");
        card.className = "history-card";

        card.innerHTML = `
            <h3>${item.foodType ?? "미분류"}</h3>

            <div class="info">카테고리 : ${item.category ?? "-"}</div>
            <div class="info">파일명 : ${item.originalFileName ?? "-"}</div>
            <div class="info">메모 : ${item.memo ?? "-"}</div>
            <div class="info">
                신뢰도 : ${
                    item.topConfidence
                        ? item.topConfidence.toFixed(1)
                        : "-"
                }%
            </div>
            <div class="info">업로드 시간 : ${item.createdAt ?? "-"}</div>

            <div class="tag-list">
                ${labels}
            </div>
        `;

        container.appendChild(card);
    });
}