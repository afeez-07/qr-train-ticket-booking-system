const BASE_URL = "http://localhost:8080";

let currentTicketId = null;


/* ================= PAGE SWITCH ================= */

function showLogin() {
    document.getElementById("registerPage").classList.add("hidden");
    document.getElementById("loginPage").classList.remove("hidden");
    document.getElementById("dashboard").classList.add("hidden");
}

function showDashboard() {
    document.getElementById("loginPage").classList.add("hidden");
    document.getElementById("registerPage").classList.add("hidden");
    document.getElementById("dashboard").classList.remove("hidden");
}

function showTrainSearch() {
    document.getElementById("trainSearch").classList.remove("hidden");
    document.getElementById("booking").classList.add("hidden");
    document.getElementById("adminPanel").classList.add("hidden");
}

function showBooking() {
    document.getElementById("booking").classList.remove("hidden");
    document.getElementById("trainSearch").classList.add("hidden");
    document.getElementById("adminPanel").classList.add("hidden");
}

function showAdmin() {

    const booking = document.getElementById("booking");
    const trainSearch = document.getElementById("trainSearch");
    const adminPanel = document.getElementById("adminPanel");
    const adminLoginBox = document.getElementById("adminLoginBox");

    if (booking) booking.classList.add("hidden");
    if (trainSearch) trainSearch.classList.add("hidden");
    if (adminPanel) adminPanel.classList.add("hidden");
    if (adminLoginBox) adminLoginBox.classList.remove("hidden");
}

function checkAdmin() {

    const user = document.getElementById("adminUser").value;
    const pass = document.getElementById("adminPass").value;

    if (user === "admin" && pass === "admin2005") {

        document.getElementById("adminLoginBox").classList.add("hidden");
        document.getElementById("adminPanel").classList.remove("hidden");

    } else {

        document.getElementById("adminLoginMsg").innerText = "Invalid Admin Credentials";
    }
}

function logout() {
    location.reload();
}


/* ================= REGISTER ================= */

function register() {

    fetch(BASE_URL + "/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },

        body: JSON.stringify({
            username: document.getElementById("regUsername").value,
            password: document.getElementById("regPassword").value
        })
    })
    .then(res => res.text())
    .then(data => {

        document.getElementById("registerMsg").innerText = data;

        document.getElementById("regUsername").value = "";
        document.getElementById("regPassword").value = "";

        showLogin();
    })
    .catch(() => alert("Register failed"));
}



/* ================= LOGIN ================= */

function login() {

    fetch(BASE_URL + "/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },

        body: JSON.stringify({
            username: document.getElementById("loginUsername").value,
            password: document.getElementById("loginPassword").value
        })
    })
    .then(res => {
        if (!res.ok) throw new Error();
        return res.text();
    })
    .then(() => showDashboard())
    .catch(() => {
        document.getElementById("loginMsg").innerText = "Invalid credentials";
    });
}



/* ================= TRAIN SEARCH ================= */

function searchTrains() {

    const source = document.getElementById("source").value;
    const destination = document.getElementById("destination").value;

    fetch(`${BASE_URL}/trains/search?source=${source}&destination=${destination}`)
    .then(res => res.json())
    .then(data => {

        if (data.length === 0) {
            document.getElementById("trainResults").innerHTML = "No trains found";
            return;
        }

        let html = "<table border='1'><tr><th>ID</th><th>Name</th><th>Fare</th><th>Seats</th></tr>";

        data.forEach(t => {
            html += `<tr>
                <td>${t.id}</td>
                <td>${t.trainName}</td>
                <td>${t.fare}</td>
                <td>${t.availableSeats}</td>
            </tr>`;
        });

        html += "</table>";

        document.getElementById("trainResults").innerHTML = html;
    })
    .catch(() => alert("Train search failed"));
}



/* ================= BOOK TICKET ================= */

function bookTicket() {

    fetch(BASE_URL + "/ticket/book", {

        method: "POST",
        headers: { "Content-Type": "application/json" },

        body: JSON.stringify({
            trainId: parseInt(trainId.value),
            seats: parseInt(seats.value)
        })

    })
    .then(res => {
        if (!res.ok) throw new Error("Booking failed");
        return res.json();
    })

    .then(ticket => {

        currentTicketId = ticket.id;

        bookingMsg.innerHTML =
            "✅ Ticket Booked Successfully <br>" +
            "Total Fare : ₹ " + ticket.totalFare;

        // QR
        qrImage.src =
            BASE_URL + "/ticket/qr/" + ticket.id + "?t=" + new Date().getTime();

        // SHOW PDF BUTTON
        downloadBtn.style.display = "block";
    })

    .catch(err => {
        console.log(err);
        alert("Ticket booking failed");
    });
}



/* ================= DOWNLOAD PDF ================= */

function downloadPDF() {

    if (!currentTicketId) {
        alert("Book ticket first!");
        return;
    }

    window.open(
        BASE_URL + "/ticket/pdf/" + currentTicketId,
        "_blank"
    );
}

/* ================= ADMIN PAGE LOAD ================= */
function loadAllTrains() {

    fetch(BASE_URL + "/admin/trains")
    .then(res => res.json())
    .then(data => {

        let html = "<table border='1'>";
        html += "<tr><th>ID</th><th>Name</th><th>Source</th><th>Destination</th><th>Fare</th><th>Seats</th><th>Action</th></tr>";

        data.forEach(train => {

            html += `
                <tr>
                    <td>${train.id}</td>
                    <td>${train.trainName}</td>
                    <td>${train.source}</td>
                    <td>${train.destination}</td>
                    <td>${train.fare}</td>
                    <td>${train.availableSeats}</td>
                    <td>
                        <button onclick="deleteTrain(${train.id})">
                            Delete
                        </button>
                    </td>
                </tr>
            `;
        });

        html += "</table>";

        document.getElementById("adminData").innerHTML = html;
    });
}

function deleteTrain(id) {

    if (!confirm("Are you sure you want to delete this train?")) {
        return;
    }

    fetch(BASE_URL + "/admin/delete-train/" + id, {
        method: "DELETE"
    })
    .then(res => res.text())
    .then(msg => {
        alert(msg);
        loadAllTrains(); // refresh table
    })
    .catch(() => alert("Delete failed"));
}

function loadAllTickets() {

    fetch(BASE_URL + "/admin/tickets")
    .then(res => res.json())
    .then(data => {

        let html = "<h4>All Tickets</h4>";

        data.forEach(t => {
            html += "Ticket ID: " + t.id +
                    " Train ID: " + t.trainId +
                    " Seats: " + t.seatsBooked + "<br>";
        });

        adminData.innerHTML = html;
    });
}

function addTrain() {

    fetch(BASE_URL + "/admin/add-train", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            trainName: document.getElementById("adminTrainName").value,
            source: document.getElementById("adminSource").value,
            destination: document.getElementById("adminDestination").value,
            departureTime: "06:00",
            arrivalTime: "12:00",
            fare: document.getElementById("adminFare").value,
            availableSeats: document.getElementById("adminSeats").value
        })
    })
    .then(res => res.json())
    .then(data => {
        alert("Train Added Successfully!");
        loadAllTrains();
    })
    .catch(() => alert("Add Train Failed"));
}

function cancelTicket() {

    const id = document.getElementById("cancelTicketId").value;

    if (!id) {
        alert("Enter Ticket ID");
        return;
    }

    fetch(BASE_URL + "/ticket/cancel/" + id, {
        method: "DELETE"
    })
    .then(res => res.text())
    .then(data => {
        document.getElementById("cancelMsg").innerText = data;
    })
    .catch(() => alert("Cancel failed"));
}

