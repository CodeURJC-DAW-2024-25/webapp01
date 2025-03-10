import { fetchData } from "./services/fetchService.js";
import { createHTMLList } from "./services/uiService.js";

export async function fetchLists(listsContainer, loadMoreButton, currentPage = 0, loading = false) {
    const LISTS_SIZE = 4;

    // Prevent multiple requests
    if (loading) return;
    loading = true;

    // Fetch lists from the server
    const endpoint = `/user-lists?page=${currentPage}&size=${LISTS_SIZE}`;
    const res = await fetchData(endpoint, 'GET', { cacheData: false });

    // Insert lists to the DOM
    res.data.forEach(list => {
        listsContainer?.insertAdjacentHTML('beforeend', createHTMLList(list));

        // Add event listener to the add to list button
        const addButton = document.querySelector(`button[data-list-id="${list.id}"]`);
        addButton.addEventListener('click', () => addProductToList(list.id));
    });

    // Update the current page and loading status
    currentPage++;
    loading = false;

    // Check if there are more lists to load
    if (res.isLastPage) {
        loadMoreButton?.remove();
    }
}

async function addProductToList(listId) {
    // Get the product id from the data attribute
    const productId = document.querySelector('[data-product-id]').getAttribute('data-product-id');

    // Send the request to the server
    try {
        await fetchData(`/user-lists/${listId}/product/${productId}`, 'POST');

        // If the response is successful, update the UI
        document.querySelector(`button[data-list-id="${listId}"]`).innerHTML = '<i class="bi bi-check-lg checked"></i>';
        document.querySelector(`button[data-list-id="${listId}"]`).disabled = true

    } catch (error) {
        console.error(error);
    }
}