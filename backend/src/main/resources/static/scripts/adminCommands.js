

// Buttons leading routes
const btns = [...document.querySelectorAll('[data-command-url]')]

btns.forEach(btn => {
    btn.addEventListener('click', () => {
        const url = btn.getAttribute('data-command-url')
        window.location.href = url
    })
})