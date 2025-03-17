import { fetchData } from "./services/fetchService.js";

const $commentsContainer = document.querySelector(".comments-container");
const $loadMoreButton = document.querySelector("#load-more-button");

const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;

const POST_ID = $commentsContainer.dataset.postId;
const COMMENTS_SIZE = 3;

let currentPage = 0;
let loading = false;
let isEnd = false;

// Add event listener
$loadMoreButton.addEventListener("click", loadComments);

async function loadComments() {
    if (loading || isEnd) return;
    loading = true;

    const endpoint = `/posts/${POST_ID}/comments?page=${currentPage}&size=${COMMENTS_SIZE}`;
    const response = await fetchData(endpoint, "GET", { cacheData: false });

    response.data.forEach(comment => {
        $commentsContainer.insertAdjacentHTML("beforeend", createHTMLComment(comment));
    });

    currentPage++;
    loading = false;

    if (response.is_last_page) $loadMoreButton.remove();
    loading = false;
}

function createHTMLComment(comment) {
    return `
    <div class="comment">
        <div class="comment-header">
            <div class="left">
                <img class="user-avatar"
                src="/api/v1/users/${comment.authorId}/avatar"
                onerror="this.src='/assets/defaultAvatar.jpg'"
                alt="User">
                <span class="comment-author">@${comment.author}</span>
            </div>
            <div class="right">
                ${comment.canDelete ? `
                <form action="/posts/${POST_ID}/deleteComment/${comment.id}" method="post">
                    <input type="hidden" name="_csrf" value="${CSRF_TOKEN}"/>
                    <button class="clickable clickable-tool">
                        <i class="bi bi-trash"></i>
                    </button>
                </form>
                ` : ''}
            </div>
        </div>
        <p class="comment-content">${comment.content}</p>
        <span class="comment-date">${comment.formatedDate}</span>
    </div>
    `;
}

// Load comments when the page is loaded
loadComments();
