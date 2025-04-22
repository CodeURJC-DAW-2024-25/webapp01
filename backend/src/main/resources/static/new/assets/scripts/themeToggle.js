const THEME_KEY = 'theme';
const DARK_CLASS = 'dark';

function setTheme(theme) {
    document.documentElement.classList.toggle(DARK_CLASS, theme === DARK_CLASS);
    window.localStorage.setItem(THEME_KEY, theme);
}

function getStoredTheme() {
    return window.localStorage.getItem(THEME_KEY);
}

function getSystemTheme() {
    return window.matchMedia('(prefers-color-scheme: dark)').matches ? DARK_CLASS : 'light';
}

function retrieveTheme() {
    const storedTheme = getStoredTheme();
    const theme = storedTheme ? storedTheme : getSystemTheme();
    setTheme(theme);
}

// Function to add click events to theme toggle buttons
function addClickEvents(){

    // Wait for the page to load before adding click events
    window.addEventListener('DOMContentLoaded', () => {
        const toggleButtons = document.querySelectorAll('.theme-toggle');
    
        toggleButtons.forEach(button => {
            button.addEventListener('click', () => {
                const newTheme = document.documentElement.classList.contains(DARK_CLASS) ? 'light' : DARK_CLASS;
                setTheme(newTheme);
            });
        });
    })
}

// Listen for the changes in the URL to re-add click events
window.addEventListener('popstate', addClickEvents);


retrieveTheme();
addClickEvents();