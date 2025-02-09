const toggleButtons = document.querySelectorAll('.theme-toggle');

toggleButtons.forEach(button => {
    button.addEventListener('click', () => {
        document.body.classList.toggle('dark');
    });
});