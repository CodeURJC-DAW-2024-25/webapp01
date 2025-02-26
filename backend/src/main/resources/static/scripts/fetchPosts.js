const postsContainer = document.querySelector('#posts-preview-container');
const loadMoreButton = document.querySelector('#load-more-button');

const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

const POSTS_SIZE = 4;

let currentPage = 0;
let loading = false;
let isEnd = false;

loadMoreButton.addEventListener('click', loadPosts);

async function loadPosts() {
    if (loading || isEnd) return;
    loading = true;

    // Fetch for posts from the server
    const response = await fetch(`/api/posts?page=${currentPage}&size=${POSTS_SIZE}`, {
        headers: {
            'Content-Type': 'application/json',
            [CSRF_HEADER]: CSRF_TOKEN
        }
    });

    if (!response.ok) throw new Error('Failed to load posts');

    // Parse the response
    const data = await response.json();

    console.log(data);

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

function createHTMLPost(post) {
    return `
    <a class="post extended" href="/posts/${post.id}">
        <i class="bi bi-box-arrow-up-right icon"></i>
        <div class="post-top">
            <h2 class="post-title">${post.title}</h2>
            <p class="post-description">Descubre cómo puedes ahorrar dinero en tus compras de supermercado con SaveX.</p>
            <div class="post-metadata">
                <div class="tags-container">
                    ${post.tags.map(tag => `<span class="tag">${tag}</span>`).join('')}
                </div>
            </div>
        </div>
        <div class="post-bottom">
            <div class="time-container">
                <span class="date">${post.date}</span>
                <span class="separator">•</span>
                <span class="read-time">${post.readingTime}</span>
            </div>
            <div class="img-container">
                <img src="/api/posts/${post.id}/banner" alt="${post.title}">
            </div>
        </div>
    </a>
    `;
}

// Load the initial posts
loadPosts();