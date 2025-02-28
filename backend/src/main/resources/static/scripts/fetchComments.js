const $commentsContainer = document.querySelector(".comments-container");
const $loadMoreButton = document.querySelector("#load-more-button");

const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

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

    // Fetch for comments from the server
    const response = await fetch(`/api/posts/${POST_ID}/comments?page=${currentPage}&size=${COMMENTS_SIZE}`, {
        headers: {
            "Content-Type": "application/json",
            [CSRF_HEADER]: CSRF_TOKEN
        }
    })

    if (!response.ok) throw new Error("Failed to load comments");

    // Parse the response
    const data = await response.json();

    console.log(data);

    // Insert comments to the DOM
    data.comments.forEach(comment => {
        $commentsContainer.insertAdjacentHTML("beforeend", createHTMLComment(comment));
    });

    // Update the current page and loading status
    currentPage++;
    loading = false;

    // Check if there are more comments to load
    if (data.isLastPage) {
        $loadMoreButton.remove();
    }
}

function createHTMLComment(comment) {
    return `
    <div class="comment">
        <div class="comment-header">
            <div class="left">
                <img class="user-avatar" src="/assets/defaultAvatar.svg" alt="User">
                <span class="comment-author">@${comment.author.username}</span>
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