import { fetchData } from "./services/fetchService.js";

const deleteButton = document.getElementById("delete-post-button");
let POST_ID = undefined;

// If the delete button exists, add an event listener to it
if (deleteButton) {
    POST_ID = deleteButton.dataset.postId;
    deleteButton.addEventListener("click", deletePost);
}

// Function to delete a post
async function deletePost() {
    const confirmDelete = confirm("¿Estás seguro de que quieres eliminar esta publicación?");
    if (!confirmDelete) return;

    // Send a DELETE request to the server to delete the post
    try {
        const endpoint = `/posts/${POST_ID}`;
        await fetchData(endpoint, "DELETE", { cacheData: false });

        // Redirect to the admin page
        window.location.routerLink = "/admin";
    } catch (error) {
        console.error(error);
    }
}