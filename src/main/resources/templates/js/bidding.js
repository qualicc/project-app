function fetchDataGet(){
    fetch('http://localhost:8080/bidding')
        .then(response => response.json())
        .then(data => console.log(data));
}

fetchDataGet();