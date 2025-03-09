import { fetchData } from "./services/fetchService.js";


const buttons = document.querySelectorAll('.delete-button');
const ListId = document.querySelector('[data-list-id]').getAttribute('data-list-id');

buttons.forEach(button => {
    button.addEventListener('click', deleteProduct);
});

async function deleteProduct(){
    const id = this.getAttribute('data-product-id');

    try {
        await fetchData( `/user-lists/${ListId}/product/${id}`, 'DELETE', {cacheData: false});
        window.location.reload();
    }catch(error){
        console.error(error);
    }
}