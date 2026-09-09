const form = document.getElementById("nominationForm");
const message = document.getElementById("message");

form.addEventListener("submit", async function (event) {

    event.preventDefault();

    const nomination = {
        officerName: document.getElementById("officerName").value.trim(),
        trainingTitle: document.getElementById("trainingTitle").value.trim(),
        departmentName: document.getElementById("departmentName").value.trim()
    };

    try {

        const response = await fetch("/api/nominations", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(nomination)
        });

        if (response.ok) {

            const result = await response.json();

            message.textContent =
                "Nomination submitted successfully!";

            message.className = "success";

            form.reset();

            console.log(result);

        } else if (response.status === 409) {

            const errorMessage = await response.text();

            message.textContent = errorMessage;

            message.className = "error";

        } else if (response.status === 400) {

            message.textContent =
                "Please fill all required fields.";

            message.className = "error";

        } else {

            message.textContent =
                "Something went wrong.";

            message.className = "error";
        }

    } catch (error) {

        console.error(error);

        message.textContent =
            "Unable to connect to the server.";

        message.className = "error";
    }
});