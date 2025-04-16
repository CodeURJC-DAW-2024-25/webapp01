
export interface User {
    id: number;
    username: string;
    name: string;
    role: "USER" | "ADMIN";
};

export interface GlobalUser {
    id: number | null;
    user: User | null;
    isAuthenticated: boolean;
    isAdmin: boolean;
    avatar: string;
}

export interface RegisterUser {
    email: string;
    username: string;
    password: string;
}

export interface SettingsUser extends RegisterUser {
    name: string;
    avatar: string;
    confirmPassword: string;
    newPassword: string;
}