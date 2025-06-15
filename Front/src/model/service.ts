import type { Review } from "./Review";
import type { User } from "./user";

export interface Service {
  id: number;
  provider: User;
  title: string;
  description: string;
  durationHours: number;
  city: string;
  domain: string;
  imageUrl: string;
  price: number;
  createdAt: string;
  updatedAt: string;
  active: boolean;
  averageRating?: number;
  reviewCount?: number;
  reviews: Review[];
}
