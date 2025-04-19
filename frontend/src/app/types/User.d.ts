
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

export interface ModifyUser {
    name: string;
    username: string;
    email: string;
}

export interface UserPassword {
    oldPassword: string;
    newPassword: string;
    newPasswordConfirmation: string;
}