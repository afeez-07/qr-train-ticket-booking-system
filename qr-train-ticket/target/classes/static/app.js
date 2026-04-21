const BASE_URL = "http://localhost:8080";

let currentTicketId = null;
let isLogin = true;
let selectedTrainFare = 0;

window.onload = function(){

    document.getElementById("dashboard").classList.add("hidden");
    document.getElementById("adminLoginPage").classList.add("hidden");
    document.getElementById("adminSection").classList.add("hidden");
    document.getElementById("adminPanel").classList.add("hidden");

    document.getElementById("authSection").classList.remove("hidden");

}

/* ================= AUTH SWITCH ================= */

function toggleAuth() {

    isLogin = !isLogin;

    document.getElementById("authTitle").innerText =
        isLogin ? "Login" : "Register";

    document.getElementById("authBtn").innerText =
        isLogin ? "Login" : "Register";

    document.querySelector(".switch-link").innerText =
        isLogin
        ? "Don't have an account? Register"
        : "Already have an account? Login";

    // Clear inputs
    const user = document.getElementById("username");
    const pass = document.getElementById("password");

    if(user) user.value = "";
    if(pass) pass.value = "";

    // Clear message
    const msg = document.getElementById("authMsg");
    if(msg) msg.innerText = "";
}

/* ================= AUTH HANDLE ================= */

function handleAuth(event) {

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    if(!username || !password)
    {
       document.getElementById("authMsg").innerText = "Enter username and password";
       return;
    }

    const url = isLogin ? "/auth/login" : "/auth/register";

    fetch(BASE_URL + url,{
        method:"POST",
        headers:{"Content-Type":"application/json"},
        body:JSON.stringify({username,password})
    })

    .then(res=>{
        if(!res.ok) throw new Error();
        return res.text();
    })

    .then(msg => {

        if(isLogin){

            if(msg === "Login Success"){

                document.getElementById("searchSection").classList.add("hidden");
                document.getElementById("bookingSection").classList.add("hidden");
                document.getElementById("cancelSection").classList.add("hidden");
                document.getElementById("adminSection").classList.add("hidden");

                document.getElementById("adminPanel").classList.add("hidden");

                document.getElementById("mainMenu").classList.remove("hidden");

                document.getElementById("authSection").classList.add("hidden");
                document.getElementById("dashboard").classList.remove("hidden");

            } else {

                document.getElementById("authMsg").innerText = msg;

            }

        }

        else{

            document.getElementById("authMsg").innerText =
            "Registration successful! Please login.";

            toggleAuth();
        }

    })
}

function openAdminLogin(){

    // hide user login
    document.getElementById("authSection").classList.add("hidden");

    document.getElementById("adminData").innerHTML = "";

    // reset admin UI
    document.getElementById("adminSection").classList.add("hidden");
    document.getElementById("adminPanel").classList.add("hidden");

    // clear admin inputs
    document.getElementById("adminUser").value = "";
    document.getElementById("adminPass").value = "";
    document.getElementById("adminLoginMsg").innerText = "";

    // show admin login page
    document.getElementById("adminLoginPage").classList.remove("hidden");

}

function backToUserLogin(){

    document.getElementById("adminLoginPage").classList.add("hidden");
    document.getElementById("adminSection").classList.add("hidden");
    document.getElementById("adminPanel").classList.add("hidden");
    document.getElementById("dashboard").classList.add("hidden");
    document.getElementById("adminData").innerHTML = "";
    document.getElementById("authSection").classList.remove("hidden");

    document.getElementById("username").value = "";
    document.getElementById("password").value = "";

}

function logout(){

    // hide dashboard
    document.getElementById("dashboard").classList.add("hidden");

    // reset all sections
    document.getElementById("searchSection").classList.add("hidden");
    document.getElementById("bookingSection").classList.add("hidden");
    document.getElementById("cancelSection").classList.add("hidden");
    document.getElementById("adminSection").classList.add("hidden");
    document.getElementById("adminPanel").classList.add("hidden");

    // show menu next time
    document.getElementById("mainMenu").classList.remove("hidden");

    // show login
    document.getElementById("authSection").classList.remove("hidden");

    // clear inputs
    document.getElementById("username").value="";
    document.getElementById("password").value="";
}

function showSection(sectionId) {

    document.getElementById("mainMenu").classList.add("hidden");

    const sections = [
        "searchSection",
        "bookingSection",
        "cancelSection",
        "adminSection"
    ];

    sections.forEach(id => {
        document.getElementById(id).classList.add("hidden");
    });

    document.getElementById(sectionId).classList.remove("hidden");

    if(sectionId === "bookingSection"){
        loadAllTrainsForBooking();
    }
}

function loadAllTrainsForBooking(){

    fetch(BASE_URL + "/admin/trains")
    .then(res => res.json())
    .then(data => {

        let html = `
        <div class="table-container">
        <table>
        <thead>
        <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Source</th>
        <th>Destination</th>
        <th>Arrival Time</th>
        <th>Fare</th>
        <th>Seats</th>
        </tr>
        </thead>
        <tbody>
        `;

        data.forEach(t => {
            html += `
            <tr onclick="selectTrain(${t.id}, ${t.fare})">
            <td>${t.id}</td>
            <td>${t.trainName}</td>
            <td>${t.source}</td>
            <td>${t.destination}</td>
            <td>${t.arrivalTime}</td>
            <td>${t.fare}</td>
            <td>${t.availableSeats}</td>
            </tr>
            `;
        });

        html += `
        </tbody>
        </table>
        </div>
        `;

        document.getElementById("bookingTrainTable").innerHTML = html;

    })
    .catch(() => {
        document.getElementById("bookingTrainTable").innerHTML =
        "Unable to load trains";
    });
}

function selectTrain(id, fare){

    document.getElementById("trainId").value = id;

    selectedTrainFare = fare;

}

function goHome() {

    const sections = [
        "searchSection",
        "bookingSection",
        "cancelSection",
        "adminSection"
    ];

    sections.forEach(id => {
        document.getElementById(id).classList.add("hidden");
    });

    document.getElementById("mainMenu").classList.remove("hidden");

    // ⭐ CLEAR BOOKING FORM
    document.getElementById("passengerName").value = "";
    document.getElementById("passengerAge").value = "";
    document.getElementById("trainId").value = "";
    document.getElementById("seats").value = "";
    selectedTrainFare = 0;

    // ⭐ CLEAR BOOKING RESULT
    document.getElementById("bookingMsg").innerHTML = "";
    document.getElementById("qrImage").src = "";

    // Hide download button
    document.getElementById("downloadBtn").classList.add("hidden");
    document.getElementById("cancelTicketId").value = "";
    document.getElementById("cancelMsg").innerText = "";
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

        let html = `
        <div class="table-container">
        <table>
        <thead>
        <tr>
        <th>ID</th>
        <th>Train Name</th>
        <th>Source</th>
        <th>Destination</th>
        <th>Arrival Time</th>
        <th>Fare (₹)</th>
        <th>Available Seats</th>
        </tr>
        </thead>
        <tbody>
        `;

        data.forEach(t => {
        html += `
        <tr onclick="selectTrain(${t.id}, ${t.fare}); showSection('bookingSection')">
        <td>${t.id}</td>
        <td>${t.trainName}</td>
        <td>${t.source}</td>
        <td>${t.destination}</td>
        <td>${t.arrivalTime}</td>
        <td>${t.fare}</td>
        <td>${t.availableSeats}</td>
        </tr>
        `;
        });

        html += `
        </tbody>
        </table>
        </div>
        `;

        document.getElementById("trainResults").innerHTML = html;
    })
    .catch(() => alert("Train search failed"));
}


/* ================= MAKE PAYMENT =================== */

function makePayment(){

    const trainId = document.getElementById("trainId").value;
    const seatsBooked = parseInt(document.getElementById("seats").value);
    const passengerName = document.getElementById("passengerName").value;
    const passengerAge = document.getElementById("passengerAge").value;

    if(!trainId || !seatsBooked || !passengerName || !passengerAge){
        alert("Enter all booking details");
        return;
    }

    // Get train details from backend
    fetch(BASE_URL + "/admin/trains")
    .then(res => res.json())
    .then(data => {

        const train = data.find(t => t.id == trainId);

        if(!train){
            alert("Invalid Train ID");
            return;
        }

        const totalFare = seatsBooked * train.fare;

        const confirmPay = confirm(
            "Train ID: " + trainId +
            "\nSeats: " + seatsBooked +
            "\nPassenger: " + passengerName +
            "\nTotal Fare: ₹" + totalFare +
            "\nProceed to payment?"
        );

        if(confirmPay){
            alert("Payment Successful!");
            bookTicket();
        }

    })
    .catch(() => {
        alert("Unable to fetch train data");
    });

}

/* ================= BOOK TICKET ================= */

function bookTicket() {

    fetch(BASE_URL + "/ticket/book", {

        method: "POST",
        headers: { "Content-Type": "application/json" },

        body: JSON.stringify({
            trainId: parseInt(trainId.value),
            seats: parseInt(seats.value),
            passengerName: passengerName.value,
            passengerAge: parseInt(passengerAge.value)
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
            "Passenger: " + ticket.passengerName + "<br>" +
            "Total Fare : ₹ " + ticket.totalFare + "<br>" +
            "Remaining Seats : " + ticket.remainingSeats;

        // QR
        qrImage.src =
            BASE_URL + "/ticket/qr/" + ticket.id + "?t=" + new Date().getTime();

        // SHOW PDF BUTTON
        document.getElementById("downloadBtn").classList.remove("hidden");
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

function openAdmin() {

    // hide main menu
    document.getElementById("mainMenu").classList.add("hidden");

    // hide other sections
    document.getElementById("searchSection").classList.add("hidden");
    document.getElementById("bookingSection").classList.add("hidden");
    document.getElementById("cancelSection").classList.add("hidden");

    // show admin section
    document.getElementById("adminSection").classList.remove("hidden");

}

/* ================= ADMIN PAGE LOAD ================= */
function loadAllTrains() {

    fetch(BASE_URL + "/admin/trains")
    .then(res => res.json())
    .then(data => {

        let html = "<table border='1'>";
        html += "<tr><th>ID</th><th>Name</th><th>Source</th><th>Destination</th><th>Arrival Time</th><th>Fare</th><th>Seats</th><th>Action</th></tr>";

        data.forEach(train => {

            html += `
                <tr>
                    <td>${train.id}</td>
                    <td>${train.trainName}</td>
                    <td>${train.source}</td>
                    <td>${train.destination}</td>
                    <td>${train.arrivalTime}</td>
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

function loadAllTickets(){

fetch(BASE_URL + "/admin/tickets")
.then(res => res.json())
.then(data => {

let html = `
<h4>Booked Tickets</h4>

<div class="table-container">
<table>
<thead>
<tr>
<th>Ticket ID</th>
<th>Passenger Name</th>
<th>Passenger Age</th>
<th>Train Name</th>
<th>Train ID</th>
<th>Seats Booked</th>
<th>Total Fare</th>
<th>Action</th>
</tr>
</thead>
<tbody>
`;

data.forEach(t => {
html += `
<tr>
<td>${t.id}</td>
<td>${t.passengerName || "N/A"}</td>
<td>${t.passengerAge || "-"}</td>
<td>${t.trainName}</td>
<td>${t.trainId}</td>
<td>${t.seatsBooked}</td>
<td>${t.totalFare}</td>

<td>
<button class="admin-download" onclick="downloadAdminPDF(${t.id})">
Download PDF
</button>
</td>

</tr>
`;
});

html += `
</tbody>
</table>
</div>
`;

document.getElementById("adminData").innerHTML = html;

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

        // Clear input fields
        document.getElementById("adminTrainName").value = "";
        document.getElementById("adminSource").value = "";
        document.getElementById("adminDestination").value = "";
        document.getElementById("adminFare").value = "";
        document.getElementById("adminSeats").value = "";

        // Reload train table
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

/* ================= ADMIN LOGIN ================= */

function checkAdmin() {

    const user = document.getElementById("adminUser").value;
    const pass = document.getElementById("adminPass").value;

    if (user === "admin" && pass === "admin2005") {

        document.getElementById("adminData").innerHTML = "";
        document.getElementById("adminLoginPage").classList.add("hidden");
        document.getElementById("authSection").classList.add("hidden");
        document.getElementById("dashboard").classList.remove("hidden");
        document.getElementById("mainMenu").classList.add("hidden");
        document.getElementById("adminSection").classList.remove("hidden");
        document.getElementById("adminPanel").classList.remove("hidden");

    }

    else {

        document.getElementById("adminLoginMsg").innerText =
            "Invalid Admin Credentials";
    }
}

function downloadAdminPDF(ticketId){

    window.open(
        BASE_URL + "/ticket/pdf/" + ticketId,
        "_blank"
    );

}