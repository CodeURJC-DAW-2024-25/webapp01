
export interface PaginatedData<T> {
    page: T[];
    current_page: number;
    total_pages: number;
    total_items: number;
    items_per_page: number;
    is_last_page: boolean;
}