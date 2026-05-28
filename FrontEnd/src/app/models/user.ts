export interface User {
    id?: number;
    email: string;
    name: string;
    surname: string;
    password?: string;
    isAdmin?: boolean;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface AuthResponse {
    message: string;
}