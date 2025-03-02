

const posts = [...document.querySelectorAll('[data-post-id]')]

posts.forEach(post => {
    const postId = post.getAttribute('data-post-id')
    post.addEventListener('click', () => location.href = `/posts/${postId}`)
})
