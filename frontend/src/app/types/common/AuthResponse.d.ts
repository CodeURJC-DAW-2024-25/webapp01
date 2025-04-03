import { User } from "@/types/User";

export interface AuthResponse {
    status: "SUCCESS" | "FAILURE";
    message: string | null;
    error: string | null;
    user: User | null;
    authenticated: boolean;
}