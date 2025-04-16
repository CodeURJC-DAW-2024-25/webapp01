import { Component, inject, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ProductService } from "../../../services/products.service";
import { AuthService, AuthState } from "@/services/auth.service";
import { ShoppingListService } from "@/services/shoppingList.service";
import { Product } from "@/types/Product";
import { ShoppingList } from "@/types/ShoppingList";

interface Data<T> {
    data: T | null;
    isLoading: boolean;
}

@Component({
    selector: "app-product-details",
    templateUrl: "./products-details.component.html",
    styleUrls: ["./products-details.component.css"],
})
export class ProductDetailsComponent implements OnInit {
    private route: ActivatedRoute = inject(ActivatedRoute);
    private productService: ProductService = inject(ProductService);
    private authService: AuthService = inject(AuthService);
    private shoppingListService: ShoppingListService = inject(ShoppingListService);

    private _product: Data<Product> = { data: null, isLoading: true };
    private _relatedProducts: Data<Product[]> = { data: [], isLoading: true };
    private _shoppingListData: Data<ShoppingList[]> = { data: [], isLoading: true };
    private _authData = { isAuthenticated: false, isLoading: true };

    productId: string = "";

    isComparisonVisible: boolean = false;
    showPopup: boolean = false;

    ngOnInit(): void {
        this.route.params.subscribe((params) => {
            this.productId = params["id"];
            this._product.isLoading = true;
            this._relatedProducts.isLoading = true;
            this.loadProductDetails(this.productId);
        });
        
        this.authService.authState$.subscribe((authState: AuthState) => {
            this._authData.isAuthenticated = !!authState.user?.isAuthenticated;
            this._authData.isLoading = authState.isLoading;
            if (this._authData.isAuthenticated) {
                this.loadShoppingLists(authState.user?.user?.id ?? 0);
            }
        });
    }

    loadShoppingLists(id: number): void {
        this.shoppingListService.getUserLists(id).subscribe({
            next: (response) => {
                this._shoppingListData.data = response.data.page;
                this._shoppingListData.isLoading = false; 
            },
            error: (error) => {
                console.error(error);
            },
        });
    }

    loadProductDetails(productId: string): void {
        this.productService.getProductById(productId).subscribe({
            next: (response) => {
                this._product.isLoading = false;
                this._product.data = response.data;

                this.productService.getRelatedProducts(productId).subscribe({
                    next: (res) => {
                        this._relatedProducts.isLoading = false;
                        this._relatedProducts.data = res.data;
                    },
                    error: (error) => {
                        console.error(error);
                    },
                });
            },
            error: (error) => {
                console.error(error);
            },
        });
    }

    addToList(): void {
        this.showPopup = true;
    }

    closePopup(): void {
        this.showPopup = false;
    }

    showComparison(): void {
        this.isComparisonVisible = true;
    }

    // Getters =========================
    get authData() {
        return this._authData;
    }

    get product() {
        return this._product;
    }

    get relatedProducts() {
        return this._relatedProducts;
    }

    get shoppingLists() {
        return this._shoppingListData;
    }
}
