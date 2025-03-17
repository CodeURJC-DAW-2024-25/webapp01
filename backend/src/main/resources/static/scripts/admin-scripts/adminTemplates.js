export function getUsersTBodyTemplate(users, total, maxReached) {
    let template = '';

    // If there are no users, show a message
    if (users.length === 0) template += `<tr><td colspan="5" class="text-center">No hay usuarios</td></tr>`;
    // Otherwise, show the users
    else {
        users.forEach(user => {
            template += `
                <tr>
                    <td><kbd>@${user.username}</kbd></td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>${user.nComments}</td>
                    <td class="text-end">
                        <button class="btn btn-danger" data-admin-command="user-delete" data-user-id="${user.id}">Eliminar</button>
                    </td>
                </tr>
            `;
        });
    }

    // If the max number of users has not been reached, show a button to load more
    if (!maxReached) template += `<tr class="load-more-btn"><td><button class="clickable clickable-primary" data-admin-command="load-more-users">Cargar más (${users.length}/${total})</button></td></tr>`;

    return `<tbody data-replace="admin-users">${template}</tbody>`;
}


export function getPostsTBodyTemplate(posts, total, maxReached) {
    let template = '';

    // If there are no posts, show a message
    if (posts.length === 0) template += `<tr><td colspan="5" class="text-center">No hay posts</td></tr>`;
    // Otherwise, show the posts
    else {
        posts.forEach(post => {
            template += `
                <tr data-post-id="${post.id}">
                    <td>${post.title}</td>
                    <td>${post.author}</td>
                    <td>${post.nComments}</td>
                    <td>${post.date}</td>
                    <td class="text-end">
                        <button class="btn btn-danger" data-admin-command="post-delete">Eliminar</button>
                    </td>
                </tr>
            `;
        });
    }

    // If the max number of posts has not been reached, show a button to load more
    if (!maxReached) template += `<tr class="load-more-btn"><td><button class="clickable clickable-primary" data-admin-command="load-more-posts">Cargar más (${posts.length}/${total})</button></td></tr>`;

    return `<tbody data-replace="admin-posts">${template}</tbody>`;
}