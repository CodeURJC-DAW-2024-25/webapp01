import { Product } from '@/types/Product';
import { getDefaultImage } from '@/utils/defaultImage';
import { Component, Input } from '@angular/core';

@Component({
    selector: 'app-product-card',
    templateUrl: './product-card.component.html',
    styleUrls: ['./product-card.component.css'],
})
export class ProductCardComponent {
    @Input() product: Product = {
        supermarket_name: '',
        product_id: '',
        product_url: '',
        display_name: '',
        normalized_name: '',
        product_type: '',
        product_categories: [],
        price: {
            total: 0,
            per_reference_unit: 0,
            reference_units: 0,
            reference_unit_name: '',
        },
        thumbnail: '',
        brand: '',
        keywords: [],
    };

    defaultImage: string = getDefaultImage();
}
