
export interface User {
    id: number;
    username: string;
    name: string;
    role: "USER" | "ADMIN";
};

export interface GlobalUser {
    user: User | null;
    isAuthenticated: boolean;
    isAdmin: boolean;
    avatar: string;
}