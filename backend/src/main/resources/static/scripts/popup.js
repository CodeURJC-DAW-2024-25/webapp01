document.addEventListener('DOMContentLoaded', function () {
    if (window.location.search) {
        const queryParams = new URLSearchParams(window.location.search);
        if (queryParams.has('error')) {
            const modal = document.getElementById('popup');
            if (modal) {
                modal.classList.add('show');
                modal.setAttribute('aria-modal', 'true');
                modal.removeAttribute('aria-hidden');
            }

            // Set timeout to close popup after 5 seconds
            setTimeout(closePopup, 5000);
        }
    }
});

function closePopup() {
    const modal = document.getElementById('popup');
    if (modal) {
        modal.classList.remove('show');
        modal.setAttribute('aria-hidden', 'true');
        modal.removeAttribute('aria-modal');
    }
}