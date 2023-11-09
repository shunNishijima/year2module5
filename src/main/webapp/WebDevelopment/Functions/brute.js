function fetchBruteCount() {
    fetch('http://localhost:8080/SAFEty/api/user/brute', { method: 'POST' })
        .then(response => response.text())
        .then(data => {
            document.getElementById('bcount').innerHTML = data;
        })
        .catch(error => console.error('Error:', error));
}

function clearBruteTable() {
    fetch('http://localhost:8080/SAFEty/api/user/brute', { method: 'DELETE' })
        .then(response => response.text())
        .then(data => {
            console.log(data);
        })
        .catch(error => console.error('Error:', error));
    window.location.href = window.location.pathname;
}

fetchBruteCount();