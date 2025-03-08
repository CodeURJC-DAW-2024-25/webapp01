import { fetchData } from './services/fetchService.js';

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
        await fetchData('/profile/avatar', 'POST', {
            requestType: 'FORM_DATA',
            cacheData: false,
            body: formData
        });
    
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