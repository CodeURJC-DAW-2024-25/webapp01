
export interface Product {
    supermarket_name: string;
    product_id: string;
    product_url: string;
    display_name: string;
    normalized_name: string;
    product_type: string;
    product_categories: string[];
    price: {
        total: number;
        per_reference_unit: number;
        reference_units: number;
        reference_unit_name: string;
    };
    thumbnail: string;
    brand: string;
    keywords: string[];
}