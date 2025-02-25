
const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

const input = document.getElementById('avatar-input');
const submitButton = document.getElementById('submit-button');
const avatarImages = document.querySelectorAll('.avatar');

input.addEventListener('change', async () => {
    // Generate a FormData object
    const file = input.files[0];
    const formData = new FormData();
    formData.append('avatar', file);
    formData.enctype = 'multipart/form-data';

    try {
        await fetch('/api/profile/avatar', {
            method: 'POST',
            headers: {
                [CSRF_HEADER]: CSRF_TOKEN
            },
            body: formData
        })
    
        avatarImages.forEach(avatar => {
            avatar.src = URL.createObjectURL(file);
        });
        
    } catch (err) {
        console.error(err);
    }
});

submitButton.addEventListener('click', () => {
    input.click();
});