// =========================================================
// WISHLIST TOGGLE
// =========================================================

document.querySelectorAll(".wishlist").forEach((item) => {

    item.addEventListener("click", () => {

        const icon = item.querySelector("i");

        if (icon) {

            icon.classList.toggle("fa-regular");
            icon.classList.toggle("fa-solid");

            icon.style.color = "red";

        }

    });

});


// =========================================================
// SAMANA BACKEND API
// =========================================================

const API_BASE_URL = "https://samana-backend-82716904797.asia-south1.run.app/api";


// =========================================================
// BACKEND SERVER URL
// =========================================================

const BACKEND_URL = "https://samana-backend-82716904797.asia-south1.run.app";


// =========================================================
// SAFELY READ BACKEND JSON RESPONSE
// =========================================================

async function readJsonResponse(response) {

    const responseText = await response.text();

    if (!responseText.trim()) {

        return {};

    }

    try {

        return JSON.parse(responseText);

    } catch (error) {

        throw new Error(
            `Server returned an invalid response. Status: ${response.status}`
        );

    }

}


// =========================================================
// LOGIN FUNCTION
// =========================================================

async function loginUser(email, password) {

    try {

        // -------------------------------------------------
        // LOGIN REQUEST
        // -------------------------------------------------

        const response =
            await fetch(
                `${API_BASE_URL}/users/login`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        email: email,
                        password: password
                    })
                }
            );


        // -------------------------------------------------
        // READ RESPONSE
        // -------------------------------------------------

        const data =
            await readJsonResponse(response);


        // -------------------------------------------------
        // LOGIN FAILED
        // -------------------------------------------------

        if (!response.ok) {

            throw new Error(
                data.message ||
                `Login failed. Status: ${response.status}`
            );

        }


        // -------------------------------------------------
        // JWT CHECK
        // -------------------------------------------------

        if (!data.token) {

            throw new Error(
                "Login response did not contain a JWT token."
            );

        }


        // =================================================
        // IMPORTANT
        // =================================================
        //
        // Clear the previous user's login information
        // only AFTER the new login has been successful.
        //
        // This prevents an old HOST user's role from
        // remaining when a different USER logs in.
        //
        // =================================================

        localStorage.removeItem("token");

        localStorage.removeItem("userId");

        localStorage.removeItem("userName");

        localStorage.removeItem("userEmail");

        localStorage.removeItem("userRole");


        // -------------------------------------------------
        // SAVE CURRENT USER TOKEN
        // -------------------------------------------------

        localStorage.setItem(
            "token",
            data.token
        );


        // -------------------------------------------------
        // SAVE CURRENT USER ID
        // -------------------------------------------------

        localStorage.setItem(
            "userId",
            data.userId
        );


        // -------------------------------------------------
        // SAVE CURRENT USER NAME
        // -------------------------------------------------

        localStorage.setItem(
            "userName",
            data.name
        );


        // -------------------------------------------------
        // SAVE CURRENT USER EMAIL
        // -------------------------------------------------

        localStorage.setItem(
            "userEmail",
            data.email || email
        );


        // -------------------------------------------------
        // SAVE CURRENT USER ROLE
        // -------------------------------------------------
        //
        // USER  -> USER
        // HOST  -> HOST
        // ADMIN -> ADMIN
        //
        // If backend does not return a role, default to USER.
        //
        // -------------------------------------------------

        const currentRole =
            data.role
                ? String(data.role).toUpperCase()
                : "USER";


        localStorage.setItem(
            "userRole",
            currentRole
        );


        // -------------------------------------------------
        // DEBUG INFORMATION
        // -------------------------------------------------

        console.log(
            "Login successful:",
            data
        );

        console.log(
            "Current user:",
            data.name
        );

        console.log(
            "Current role:",
            currentRole
        );

        console.log(
            "Current user ID:",
            data.userId
        );


        return data;


    } catch (error) {

        console.error(
            "Login error:",
            error
        );

        throw error;

    }

}


// =========================================================
// AUTHENTICATED API REQUEST
// =========================================================

async function apiRequest(endpoint, options = {}) {

    const token =
        localStorage.getItem("token");


    const headers = {

        "Content-Type": "application/json",

        ...(options.headers || {})

    };


    if (token) {

        headers["Authorization"] =
            `Bearer ${token}`;

    }


    const response =
        await fetch(
            `${API_BASE_URL}${endpoint}`,
            {
                ...options,
                headers: headers
            }
        );


    const data =
        await readJsonResponse(response);


    if (!response.ok) {

        throw new Error(
            data.message ||
            `Request failed. Status: ${response.status}`
        );

    }


    return data;

}


// =========================================================
// LOGIN FORM
// =========================================================

const loginForm =
    document.getElementById("loginForm");


if (loginForm) {

    loginForm.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();


            const email =
                document
                    .getElementById("email")
                    .value
                    .trim();


            const password =
                document
                    .getElementById("password")
                    .value;


            const loginButton =
                document.getElementById("loginButton");


            try {

                loginButton.disabled = true;

                loginButton.innerText =
                    "Logging in...";


                const data =
                    await loginUser(
                        email,
                        password
                    );


                toast.success(
                    "Login successful! Welcome " +
                    data.name
                );


                window.location.href =
                    "index.html";


            } catch (error) {

                console.error(
                    "Login error:",
                    error
                );


                toast.error(
                    error.message ||
                    "Login failed. Please try again."
                );


            } finally {

                loginButton.disabled = false;

                loginButton.innerText =
                    "Login";

            }

        }
    );

}


// =========================================================
// SHOW / HIDE PASSWORD
// =========================================================

const togglePassword =
    document.getElementById(
        "togglePassword"
    );


const passwordInput =
    document.getElementById(
        "password"
    );


if (
    togglePassword &&
    passwordInput
) {

    togglePassword.addEventListener(
        "click",
        function () {

            if (
                passwordInput.type ===
                "password"
            ) {

                passwordInput.type =
                    "text";


                togglePassword.innerHTML =
                    '<i class="fa-solid fa-eye-slash"></i>';

            } else {

                passwordInput.type =
                    "password";


                togglePassword.innerHTML =
                    '<i class="fa-solid fa-eye"></i>';

            }

        }
    );

}


// =========================================================
// TEST PROTECTED API - MY BOOKINGS
// =========================================================

async function loadMyBookings() {

    try {

        const bookings =
            await apiRequest(
                "/bookings/my"
            );


        console.log(
            "My bookings:",
            bookings
        );


        return bookings;


    } catch (error) {

        console.error(
            "Failed to load my bookings:",
            error
        );

    }

}


// =========================================================
// CANCEL BOOKING
// =========================================================

async function cancelBooking(bookingId) {

    const confirmed =
        confirm(
            "Are you sure you want to cancel this booking?"
        );


    if (!confirmed) {

        return;

    }


    try {

        await apiRequest(
            `/bookings/${bookingId}/cancel`,
            {
                method: "PUT"
            }
        );


        toast.success(
            "Booking cancelled successfully!"
        );


        if (
            typeof displayMyBookings ===
            "function"
        ) {

            displayMyBookings();

        }


    } catch (error) {

        console.error(
            "Cancellation error:",
            error
        );


        toast.error(
            error.message ||
            "Failed to cancel booking."
        );

    }

}


// =========================================================
// REGISTER FORM
// =========================================================

const registerForm =
    document.getElementById(
        "registerForm"
    );


if (registerForm) {

    registerForm.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();


            const name =
                document
                    .getElementById("name")
                    .value
                    .trim();


            const email =
                document
                    .getElementById("email")
                    .value
                    .trim();


            const password =
                document
                    .getElementById("password")
                    .value;


            const confirmPassword =
                document
                    .getElementById(
                        "confirmPassword"
                    )
                    .value;


            const terms =
                document.getElementById(
                    "terms"
                );


            const registerButton =
                document.getElementById(
                    "registerButton"
                );


            if (
                password !==
                confirmPassword
            ) {

                toast.error(
                    "Passwords do not match."
                );

                return;

            }


            if (
                terms &&
                !terms.checked
            ) {

                toast.error(
                    "Please agree to the Terms & Conditions."
                );

                return;

            }


            try {

                registerButton.disabled =
                    true;


                registerButton.innerText =
                    "Registering...";


                const response =
                    await fetch(
                        `${API_BASE_URL}/users/register`,
                        {
                            method: "POST",

                            headers: {
                                "Content-Type":
                                    "application/json"
                            },

                            body: JSON.stringify({
                                name: name,
                                email: email,
                                password: password
                            })
                        }
                    );


                const data =
                    await readJsonResponse(
                        response
                    );


                if (!response.ok) {

                    throw new Error(
                        data.message ||
                        `Registration failed. Status: ${response.status}`
                    );

                }


                toast.success(
                    "Registration successful! Please login."
                );


                console.log(
                    "Registration successful:",
                    data
                );


                window.location.href =
                    "login.html";


            } catch (error) {

                console.error(
                    "Registration error:",
                    error
                );


                toast.error(
                    error.message ||
                    "Registration failed. Please try again."
                );


            } finally {

                registerButton.disabled =
                    false;


                registerButton.innerText =
                    "Register";

            }

        }
    );

}


// =========================================================
// CONVERT BACKEND IMAGE URL
// =========================================================

function getExperienceImageUrl(url) {

    if (!url) {

        return "";

    }


    const cleanUrl =
        String(url).trim();


    if (!cleanUrl) {

        return "";

    }


    if (
        cleanUrl.startsWith("http://") ||
        cleanUrl.startsWith("https://")
    ) {

        return cleanUrl;

    }


    if (
        cleanUrl.startsWith("/")
    ) {

        return `${BACKEND_URL}${cleanUrl}`;

    }


    return `${BACKEND_URL}/${cleanUrl}`;

}


// =========================================================
// ESCAPE HTML
// =========================================================

function escapeHtml(value) {

    if (
        value === null ||
        value === undefined
    ) {

        return "";

    }


    return String(value)
        .replace(
            /&/g,
            "&amp;"
        )
        .replace(
            /</g,
            "&lt;"
        )
        .replace(
            />/g,
            "&gt;"
        )
        .replace(
            /"/g,
            "&quot;"
        )
        .replace(
            /'/g,
            "&#039;"
        );

}


// =========================================================
// LOAD POPULAR EXPERIENCES
// =========================================================

async function loadPopularExperiences() {

    const container =
        document.getElementById(
            "popularExperiences"
        );


    if (!container) {

        return;

    }


    try {

        const response =
            await fetch(
                `${API_BASE_URL}/experiences`
            );


        const experiences =
            await readJsonResponse(
                response
            );


        if (!response.ok) {

            throw new Error(
                `Failed to load experiences. Status: ${response.status}`
            );

        }


        // -------------------------------------------------
        // MAKE SURE RESPONSE IS AN ARRAY
        // -------------------------------------------------

        if (
            !Array.isArray(
                experiences
            )
        ) {

            throw new Error(
                "Experiences response is not a list."
            );

        }


        // -------------------------------------------------
        // NO EXPERIENCES
        // -------------------------------------------------

        if (
            experiences.length === 0
        ) {

            container.innerHTML = `

                <div class="col-12 text-center">

                    <div class="py-5">

                        <i class="fa-solid fa-compass fa-3x text-muted mb-3"></i>

                        <h4>
                            No experiences available yet
                        </h4>

                        <p class="text-muted">
                            New cultural experiences will appear here soon.
                        </p>

                    </div>

                </div>

            `;

            return;

        }


        // -------------------------------------------------
        // SHOW MAXIMUM 3 EXPERIENCES
        // ON HOMEPAGE
        // -------------------------------------------------

        const popularExperiences =
            experiences.slice(
                0,
                3
            );


        container.innerHTML =
            popularExperiences
                .map(
                    experience =>
                        createExperienceCard(
                            experience
                        )
                )
                .join("");


        // -------------------------------------------------
        // ADD WISHLIST FUNCTIONALITY
        // TO NEWLY CREATED CARDS
        // -------------------------------------------------

        setupWishlistButtons();


    } catch (error) {

        console.error(
            "Failed to load popular experiences:",
            error
        );


        container.innerHTML = `

            <div class="col-12 text-center">

                <div class="py-5">

                    <i class="fa-solid fa-triangle-exclamation fa-3x text-warning mb-3"></i>

                    <h4>
                        Unable to load experiences
                    </h4>

                    <p class="text-muted">
                        Please make sure the SAMANA backend is running.
                    </p>

                    <button
                        type="button"
                        class="btn btn-success"
                        onclick="loadPopularExperiences()">

                        <i class="fa-solid fa-rotate"></i>

                        Try Again

                    </button>

                </div>

            </div>

        `;

    }

}


// =========================================================
// CREATE EXPERIENCE CARD
// =========================================================

function createExperienceCard(experience) {

    const imageUrl =
        getExperienceImageUrl(
            experience.coverImageUrl
        );


    // -----------------------------------------------------
    // FALLBACK IMAGE
    // -----------------------------------------------------

    const finalImage =
        imageUrl ||
        "images/placeholder.jpg";


    const title =
        escapeHtml(
            experience.title ||
            "Cultural Experience"
        );


    const description =
        escapeHtml(
            experience.description ||
            "Discover an authentic cultural experience."
        );


    const location =
        escapeHtml(
            experience.location ||
            "India"
        );


    const price =
        experience.price !== null &&
        experience.price !== undefined
            ? Number(
                experience.price
            ).toLocaleString(
                "en-IN"
            )
            : "Price unavailable";


    const duration =
        experience.duration !== null &&
        experience.duration !== undefined
            ? `${escapeHtml(
                experience.duration
            )} hours`
            : "";


    const category =
        experience.category &&
        experience.category.name
            ? escapeHtml(
                experience.category.name
            )
            : "Cultural";


    const experienceId =
        experience.id;


    return `

        <div class="col-lg-4 col-md-6">

            <div class="experience-card">

                <div class="experience-image">

                    <img
                        src="${finalImage}"
                        alt="${title}"
                        onerror="this.onerror=null;this.src='images/placeholder.jpg';"
                    >

                    <span
                        class="wishlist"
                        role="button"
                        title="Add to wishlist">

                        <i class="fa-regular fa-heart"></i>

                    </span>

                </div>


                <div class="experience-body">

                    <span class="badge bg-success">

                        ${category}

                    </span>


                    <h4 class="mt-3">

                        ${title}

                    </h4>


                    <p class="text-muted">

                        ${
                            description.length > 100
                                ? description.substring(
                                    0,
                                    100
                                ) + "..."
                                : description
                        }

                    </p>


                    <p>

                        <i class="fa-solid fa-location-dot"></i>

                        ${location}

                    </p>


                    ${
                        duration
                            ? `

                                <p>

                                    <i class="fa-regular fa-clock"></i>

                                    ${duration}

                                </p>

                              `
                            : ""
                    }


                    <h5>

                        ₹${price}

                        <small>
                            /person
                        </small>

                    </h5>


                    <a
                        href="experience.html?id=${experienceId}"
                        class="btn btn-success w-100">

                        View Details

                    </a>

                </div>

            </div>

        </div>

    `;

}


// =========================================================
// WISHLIST FOR DYNAMIC CARDS
// =========================================================

function setupWishlistButtons() {

    document
        .querySelectorAll(".wishlist")
        .forEach((item) => {

            item.onclick = function () {

                const icon =
                    item.querySelector(
                        "i"
                    );


                if (icon) {

                    icon.classList.toggle(
                        "fa-regular"
                    );


                    icon.classList.toggle(
                        "fa-solid"
                    );


                    icon.style.color =
                        "red";

                }

            };

        });

}


// =========================================================
// LOAD HOMEPAGE EXPERIENCES
// =========================================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        loadPopularExperiences();

    }
);