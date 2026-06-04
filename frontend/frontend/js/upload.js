const imageInput =
document.getElementById("imageInput");

const preview =
document.getElementById("previewImage");

imageInput.addEventListener(
"change",
(e)=>{

    const file =
    e.target.files[0];

    if(!file) return;

    preview.src =
    URL.createObjectURL(file);

    preview.style.display =
    "block";
});

document
.getElementById("analyzeBtn")
.addEventListener("click",()=>{

    localStorage.setItem(
        "category",
        "한식"
    );

    localStorage.setItem(
        "confidence",
        "95%"
    );

    window.location.href =
        "result.html";
});