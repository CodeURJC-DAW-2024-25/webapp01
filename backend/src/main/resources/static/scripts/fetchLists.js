import { fetchData } from "./services/fetchService.js";

const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

const listsContainer = document.querySelector('.modal-message');
const button = document.getElementById('addToListBtn')

const closeButton = document.getElementsByClassName('close-button')[0];

// Check if the button exists
button && button.addEventListener('click', openModal);
closeButton && closeButton.addEventListener('click', closeModal);

async function openModal() {
    const res = await fetchData(`/user-lists`, 'GET', { cacheData: false });
    const data = res.data

    if (data.length === 0) {
        listsContainer.innerHTML = '<p>No hay listas disponibles</p>';
    }
    else {
        listsContainer.innerHTML = '';
        data.forEach(list => {
            const content = createHTMLList(list);
            listsContainer.insertAdjacentHTML('beforeend', content);
            document.querySelector(`button[data-list-id="${list.id}"]`).addEventListener('click', () => addProductToList(list.id));
        });
    }


    document.getElementById('modal').style.display = 'flex';
    document.querySelector('body').style.overflow = 'hidden';
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
    document.querySelector('body').style.overflow = 'scroll';
}


function createHTMLList(list) {
    return `
        <div class="modal-display-list">
            <label for="listName">${list.name}</label>
            <button data-list-id="${list.id}" class="clickable clickable-tool bordered">
                <i class="bi bi-plus-lg"></i>
            </button>
        </div>
    `
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