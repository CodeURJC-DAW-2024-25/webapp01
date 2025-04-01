import { PaginatedData } from "./PaginatedData";

export interface PaginatedResponse<T> {
    data: PaginatedData<T>;
    error: string | null;
}