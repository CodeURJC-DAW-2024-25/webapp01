const CSRF_TOKEN = document.querySelector('meta[name="_csrf"]').content;
const CSRF_HEADER = document.querySelector('meta[name="_csrf_header"]').content;

const listsContainer = document.querySelector('.modal-message');

async function openModal() {



    const response = await fetch(`/api/user-lists`, {
        headers: {
            'Content-Type': 'application/json',
            [CSRF_HEADER]: CSRF_TOKEN
        }
    });

    const data = await response.json();

    console.log(data);
    if (data.length === 0) {
        listsContainer.innerHTML = '<p>No hay listas disponibles</p>';
    }
    else {
        listsContainer.innerHTML = '';
        data.forEach(list => {
            listsContainer.insertAdjacentHTML('beforeend', createHTMLList(list));
        });
    }


    document.getElementById('modal').style.display = 'flex';
    document.querySelector('body').style.overflow = 'hidden';
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
    document.querySelector('body').style.overflow = 'scroll';
}


function createHTMLList(list) {
    return `
        <form action="#" method="post" class="modal-display-list">
            <input type="hidden" name="_csrf" value="{{token}}" />
            <label for="listName">${list.name}</label>
            <button type="submit" class="clickable clickable-tool bordered">
                <i class="bi bi-plus-lg"></i>
            </button>
        </form>
    `
}