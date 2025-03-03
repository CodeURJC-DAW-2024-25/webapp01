// CSRF token and header for secure requests
const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

// Handle post card clicks to navigate to the post's page
const posts = [...document.querySelectorAll('[data-post-card]')];
posts.forEach(post => {
    const postId = post.getAttribute('data-post-id');
    post.addEventListener('click', (e) => {
        if (e.target !== e.currentTarget) return;
        location.href = `/posts/${postId}`;
    });
});

// Delete user buttons
const $deleteUserButtons = [...document.querySelectorAll('[data-admin-command="user-delete"]')];
$deleteUserButtons.forEach($deleteUserButton => {
    $deleteUserButton.addEventListener('click', (e) => {
        e.stopPropagation();

        const confirmDelete = confirm('¿Estás seguro de que deseas eliminar este usuario?');
        const uid = e.target.getAttribute('data-user-id');

        if (confirmDelete) {
            fetch(`/admin/user/${uid}`, {
                method: 'DELETE',
                headers: {
                    [CSRF_HEADER]: CSRF_TOKEN
                }
            })
            .then(() => {
                window.location.reload();
            });
        }
    });
});

// Delete post buttons
const $deletePostButtons = [...document.querySelectorAll('[data-admin-command="post-delete"]')];
$deletePostButtons.forEach($deletePostButton => {
    $deletePostButton.addEventListener('click', (e) => {
        e.stopPropagation();
        e.preventDefault();

        const confirmDelete = confirm('¿Estás seguro de que deseas eliminar este post?');
        const uid = e.target.getAttribute('data-post-id');

        if (confirmDelete) {
            fetch(`/admin/post/${uid}`, {
                method: 'DELETE',
                headers: {
                    [CSRF_HEADER]: CSRF_TOKEN
                }
            })
            .then(() => {
                window.location.reload();
            });
        }
    });
});
