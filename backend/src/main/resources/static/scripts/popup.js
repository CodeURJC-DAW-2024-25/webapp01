document.addEventListener('DOMContentLoaded', function () {
    if (window.location.search) {
        const queryParams = new URLSearchParams(window.location.search);
        if (queryParams.has('error')) {
            const modal = document.getElementById('popup');
            if (modal) {
                modal.classList.add('show');
                modal.style.display = 'block';
                modal.setAttribute('aria-modal', 'true');
                modal.removeAttribute('aria-hidden');
            }
        }
    }
});

function closePopup() {
    const modal = document.getElementById('popup');
    if (modal) {
        modal.classList.remove('show');
        modal.style.display = 'none';
        modal.setAttribute('aria-hidden', 'true');
        modal.removeAttribute('aria-modal');
    }
}