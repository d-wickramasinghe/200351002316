const form =
    document.getElementById("nominationForm");

const message =
    document.getElementById("message");

form.addEventListener("submit", async function (event) {

    event.preventDefault();

    // Clear previous message

    message.textContent = "";

    message.className = "";


    // ------------------------------------------
    // Get form values
    // ------------------------------------------

    const nomination = {

        officerId:
            document
                .getElementById("officerId")
                .value
                .trim(),

        officerName:
            document
                .getElementById("officerName")
                .value
                .trim(),

        email:
            document
                .getElementById("email")
                .value
                .trim(),

        trainingTitle:
            document
                .getElementById("trainingTitle")
                .value
                .trim(),

        departmentName:
            document
                .getElementById("departmentName")
                .value
                .trim()
    };


    // ------------------------------------------
    // Disable button while submitting
    // ------------------------------------------

    const button =
        form.querySelector("button");

    button.disabled = true;

    button.textContent = "Submitting...";


    try {

        const response = await fetch(
            "/api/nominations",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(nomination)
            }
        );


        // ------------------------------------------
        // Successful nomination
        // ------------------------------------------

        if (response.ok) {

            const result =
                await response.json();

            message.textContent =
                "Nomination submitted successfully!";

            message.className =
                "success";

            form.reset();

            console.log(
                "Saved nomination:",
                result
            );
        }


        // ------------------------------------------
        // Duplicate nomination
        // ------------------------------------------

        else if (response.status === 409) {

            const errorMessage =
                await response.text();

            message.textContent =
                errorMessage;

            message.className =
                "error";
        }


        // ------------------------------------------
        // Validation error
        // ------------------------------------------

        else if (response.status === 400) {

            message.textContent =
                "Please enter valid information in all required fields.";

            message.className =
                "error";
        }


        // ------------------------------------------
        // Other server errors
        // ------------------------------------------

        else {

            message.textContent =
                "Something went wrong. Please try again.";

            message.className =
                "error";
        }

    }

    catch (error) {

        console.error(
            "Error:",
            error
        );

        message.textContent =
            "Unable to connect to the server.";

        message.className =
            "error";
    }


    // ------------------------------------------
    // Enable button again
    // ------------------------------------------

    button.disabled = false;

    button.textContent =
        "Submit Nomination";

});