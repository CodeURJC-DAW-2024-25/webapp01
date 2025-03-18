import { fetchData } from "./services/fetchService.js";

const loginForm = document.getElementById("login-form");
const logoutForm = document.getElementById("logout-form");

loginForm && loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    // Get the data from the form
    const formData = new FormData(loginForm);
    const data = { username: formData.get("username"), password: formData.get("password") };

    // Login the user in the API REST
    const response = await fetchData("/api/auth/login", "POST", {
        useBaseUrl: false,
        cacheData: false,
        body: JSON.stringify(data)
    })

    // Check the response
    if (response.status === "SUCCESS") {
        loginForm.submit();
    } else {
        location.href = "/login?error=true";
    }
});

logoutForm && logoutForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    // Logout the user in the API REST
    await fetchData("/api/auth/logout", "POST", {
        useBaseUrl: false,
        cacheData: false
    });

    // Logout the user in the frontend
    logoutForm.submit();
});