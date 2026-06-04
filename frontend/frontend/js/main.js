const restaurants = [
{
    name:"성수 돈카츠",
    desc:"인기 일식 맛집",
    image:"https://images.unsplash.com/photo-1555396273-367ea4eb4db5"
},
{
    name:"한강 국밥",
    desc:"든든한 한식 맛집",
    image:"https://images.unsplash.com/photo-1504674900247-0877df9cc836"
},
{
    name:"브런치 카페",
    desc:"감성 카페 추천",
    image:"https://images.unsplash.com/photo-1554118811-1e0d58224f24"
}
];

const container =
document.getElementById("restaurantContainer");

restaurants.forEach(item=>{

    container.innerHTML += `
        <div class="card">

            <img src="${item.image}">

            <div class="card-content">
                <h3>${item.name}</h3>
                <p>${item.desc}</p>
            </div>

        </div>
    `;
});