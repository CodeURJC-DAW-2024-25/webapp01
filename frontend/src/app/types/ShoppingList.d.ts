import { Product } from "./Product";

export interface ShoppingList {
    id: number;
    name: string;
    description: string;
}

export interface ShoppingListDetails extends ShoppingList {
    numberOfProducts: number;
    products: Product[];
}
