import type { RoleType } from "./roleType";

export interface User {
  id: number;
  username: string;
  lastName: string;
  firstName: string;
  address: string;
  city: string;
  postalCode: string;
  birthDate: string;
  phoneNumber: string;
  email: string;
  profileImageUrl?: string;
  role?: {
    id?: number;
    name?: RoleType;
  };
  description?: {
    id?: number;
    content?: string;
  };
  providerInformation?: {
    id?: number;
    experience?: string;
  };
  createdAt?: Date;
  updatedAt?: Date;

  // provider specific fields
  averageRating?: number;
  reviewCount?: number;
}
