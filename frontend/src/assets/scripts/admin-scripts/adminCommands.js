
import { fetchData } from '../services/fetchService.js'
import { getPostsTBodyTemplate, getUsersTBodyTemplate } from './adminTemplates.js'

// CSRF token and header for secure requests
const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content


const setPostsEvents = () => {
    // Handle post card clicks to navigate to the post's page
    const posts = [...document.querySelectorAll('[data-post-id]')]
    posts.forEach(post => {
        const postId = post.getAttribute('data-post-id')
        post.addEventListener('click', (e) => {
            location.href = `/posts/${postId}`
        })

        post.querySelector('[data-admin-command="post-delete"]')?.addEventListener('click', (e) => {
            e.stopPropagation()
            console.log('click')

            const confirmDelete = confirm('¿Estás seguro de que deseas eliminar este post?')
            if (confirmDelete) {
                const endpoint = `/posts/${postId}`
                fetchData(endpoint, "DELETE", { cacheData: false })
                .then(() => {
                    pagePosts = 0
                    postsByPage = {}
                    fetchPostsTable(false)
                })
            }
        })
    })
}


const setUsersEvents = () => {
    // Delete user buttons
    const $deleteUserButtons = [...document.querySelectorAll('[data-admin-command="user-delete"]')]
    $deleteUserButtons.forEach($deleteUserButton => {
        $deleteUserButton.addEventListener('click', (e) => {
            e.stopPropagation()

            const confirmDelete = confirm('¿Estás seguro de que deseas eliminar este usuario?')
            const uid = e.target.getAttribute('data-user-id')

            if (confirmDelete) {
                fetch(`/api/v1/users/${uid}`, {
                    method: 'DELETE',
                    headers: {
                        [CSRF_HEADER]: CSRF_TOKEN
                    }
                })
                .then(() => {
                    pageUsers = 0
                    usersByPage = {}
                    fetchUsersTable(false)
                })
            }
        })
    })
}



let pageUsers = 0
let usersByPage = {}
const fetchUsersTable = async (incLimit=true) => {
    if (incLimit) pageUsers += 1
    const res = await fetchData(`/users?page=${pageUsers}&size=5`)
    const data = res.data

    usersByPage[pageUsers] = data.page
    const totalUsers = Object.values(usersByPage).flat()

    document.querySelector('[data-replace="admin-users"]').outerHTML = getUsersTBodyTemplate(totalUsers, data.total_items, data.current_page >= data.total_pages-1)
    setUserButtonListener()
    setUsersEvents()
}

let pagePosts = 0
let postsByPage = {}
const fetchPostsTable = async (incLimit=true) => {
    if (incLimit) pagePosts += 1
    const res = await fetchData(`/posts?page=${pagePosts}&size=5`)
    const data = res.data

    postsByPage[pagePosts] = data.page
    const totalPosts = Object.values(postsByPage).flat()

    document.querySelector('[data-replace="admin-posts"]').outerHTML = getPostsTBodyTemplate(totalPosts, data.total_items, data.current_page >= data.total_pages-1)
    setPostButtonListener()
    setPostsEvents()
}

const setUserButtonListener = () => {
    const $loadMoreUsers = document.querySelector('[data-admin-command="load-more-users"]')
    $loadMoreUsers?.addEventListener('click', ()=>{
        $loadMoreUsers.innerText = 'Cargando...'
        fetchUsersTable()
    })
}

const setPostButtonListener = () => {
    const $loadMorePosts = document.querySelector('[data-admin-command="load-more-posts"]')
    $loadMorePosts?.addEventListener('click', ()=>{
        $loadMorePosts.innerText = 'Cargando...'
        fetchPostsTable()
    })
}

// ---------------------------------------------------------------------------------------------------------

fetchUsersTable(false)
fetchPostsTable(false)