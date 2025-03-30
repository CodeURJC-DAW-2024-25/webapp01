import { fetchLists } from "./fetchLists.js";

const button = document.getElementById('addToListBtn')
const closeButton = document.querySelector('.close-button');

const listsContainer = document.querySelector('.shopping-lists-container');
const loadMoreButton = document.querySelector('#load-more-shopping-lists');

// Check if the button exists
button && button.addEventListener('click', openModal);
closeButton && closeButton.addEventListener('click', closeModal);

let currentPage = 0;
let loading = false;

loadMoreButton.addEventListener('click', () => fetchLists(listsContainer, loadMoreButton, currentPage, loading));

async function openModal() {
    await fetchLists(listsContainer, loadMoreButton, currentPage, loading);
    document.getElementById('modal').style.display = 'flex';
    document.querySelector('body').style.overflow = 'hidden';
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
    document.querySelector('body').style.overflow = 'scroll';
    listsContainer.innerHTML = '';
}