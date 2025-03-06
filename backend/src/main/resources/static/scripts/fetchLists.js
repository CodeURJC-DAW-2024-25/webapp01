import { fetchData } from "./services/fetchService.js";

const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

const listsContainer = document.querySelector('.modal-message');

async function openModal() {



    const response = await fetch(`/api/user-lists`, {
        headers: {
            'Content-Type': 'application/json',
            [CSRF_HEADER]: CSRF_TOKEN
        }
    });

    const data = await response.json();

    console.log(data);
    if (data.length === 0) {
        listsContainer.innerHTML = '<p>No hay listas disponibles</p>';
    }
    else {
        listsContainer.innerHTML = '';
        data.forEach(list => {
            listsContainer.insertAdjacentHTML('beforeend', createHTMLList(list));
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
            <button type="submit" onclick="addProductToList(${list.id})" class="clickable clickable-tool bordered">
                <i class="bi bi-plus-lg"></i>
            </button>
        </div>
    `
}

async function addProductToList(listId) {
    
    const productId = document.querySelector('[data-product-id]').getAttribute('data-product-id');
    
    const response = await fetchData(`/user-lists/${listId}/product/${productId}`, 'POST');
    console.log(response);

}