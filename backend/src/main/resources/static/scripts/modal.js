
function openModal() {
    document.getElementById('modal').style.display = 'flex';
    document.querySelector('body').style.overflow = 'hidden';
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
    document.querySelector('body').style.overflow = 'scroll';
}