import { fetchData } from './services/fetchService.js';
import { createHTMLPost } from './services/uiService.js';

const postsContainer = document.querySelector('#posts-preview-container');
const loadMoreButton = document.querySelector('#load-more-button');

const POSTS_SIZE = 4;

let currentPage = 0;
let loading = false;
let isEnd = false;

loadMoreButton.addEventListener('click', loadPosts);

async function loadPosts() {
    if (loading || isEnd) return;
    loading = true;

    // Fetch posts from the server
    const endpoint = `/posts?page=${currentPage}&size=${POSTS_SIZE}`;
    const data = await fetchData(endpoint, 'GET', { cacheData: false });

    // Insert posts to the DOM
    data.posts.forEach(post => {
        postsContainer.insertAdjacentHTML('beforeend', createHTMLPost(post));
    });

    // Update the current page and loading status
    currentPage++;
    loading = false;

    // Check if there are more posts to load
    if (data.isLastPage) {
        loadMoreButton.remove();
    }
}

// Load the initial posts
loadPosts();