const API_BASE_URL =
    "http://13.125.232.98:8080";

const imageInput =
document.getElementById("imageInput");

const preview =
document.getElementById("previewImage");

const analyzeBtn =
document.getElementById("analyzeBtn");


// 이미지 미리보기

imageInput.addEventListener(
    "change",
    (e) => {

        const file =
        e.target.files[0];

        if (!file) return;

        preview.src =
        URL.createObjectURL(file);

        preview.style.display =
        "block";
    }
);


// 분석 요청

analyzeBtn.addEventListener(
    "click",
    async () => {

        const file =
        imageInput.files[0];

        if (!file) {

            alert(
                "이미지를 선택해주세요."
            );

            return;
        }

        try {

            analyzeBtn.disabled = true;

            analyzeBtn.innerText =
            "분석 중...";

            const formData =
            new FormData();

            formData.append(
                "file",
                file
            );

            // 선택 입력값

            const memoInput =
            document.getElementById("memo");

            if (
                memoInput &&
                memoInput.value.trim()
            ) {

                formData.append(
                    "memo",
                    memoInput.value
                );
            }

            const regionInput =
            document.getElementById("region");

            if (
                regionInput &&
                regionInput.value.trim()
            ) {

                formData.append(
                    "regionName",
                    regionInput.value
                );
            }

            // API 호출

            
            const response =
            await fetch(
                `${API_BASE_URL}/api/food/uploads`,
                {
                    method: "POST",
                    body: formData
                }
            );

            if (!response.ok) {

                throw new Error(
                    `HTTP ${response.status}`
                );
            }

            const result =
            await response.json();

            console.log(
                "분석 결과",
                result
            );

            // 결과 저장

            localStorage.setItem(
                "uploadResult",
                JSON.stringify(result)
            );

            // 결과 페이지 이동

            window.location.href =
            "result.html";

        }

        catch (error) {

            console.error(error);

            alert(
                "서버 연결 또는 분석에 실패했습니다."
            );
        }

        finally {

            analyzeBtn.disabled =
            false;

            analyzeBtn.innerText =
            "분석하기";
        }
    }
);