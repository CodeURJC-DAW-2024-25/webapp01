import { fetchData } from './services/fetchService.js';
import { createHTMLPost } from './services/uiService.js';

const postsContainer = document.querySelector('#posts-preview-container');
const loadMoreButton = document.querySelector('#load-more-button');

const POSTS_SIZE = 4;

let currentPage = 0;
let loading = false;

loadMoreButton.addEventListener('click', loadPosts);

async function loadPosts() {
    if (loading) return;
    loading = true;

    // Fetch posts from the server
    const endpoint = `/posts?page=${currentPage}&size=${POSTS_SIZE}`;
    const response = await fetchData(endpoint, 'GET', { cacheData: false });
    const data = response.data;

    // Insert posts to the DOM
    data.page.forEach(post => {
        postsContainer.insertAdjacentHTML('beforeend', createHTMLPost(post));
    });

    // Update the current page and loading status
    currentPage++;
    loading = false;

    // Check if there are more posts to load
    if (data.is_last_page) {
        loadMoreButton.remove();
    }
}

// Load the initial posts
loadPosts();