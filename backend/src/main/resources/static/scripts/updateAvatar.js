
const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;
const input = document.getElementById('avatar-input');
const submitButton = document.getElementById('submit-button');

input.addEventListener('change', () => {
    const formData = new FormData();
    formData.append('file', input.files[0])

    fetch('/api/profile/avatar', {
        method: 'POST',
        body: formData,
        headers: {
            [CSRF_HEADER]: CSRF_TOKEN
        }
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to update avatar');
        return response.json();
    })
    .then(data => {
        document.querySelector('.user-avatar').src = data.avatar;
    })
    .catch(error => {
        console.error(error);
    });
})

submitButton.addEventListener('click', () => {
    input.click();
}
)