import type { User } from "./user";

export interface Review {
    customer: User;
    rating: number;
    reviewText: string;
}