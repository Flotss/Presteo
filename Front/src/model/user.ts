import type { ProviderInformation } from "./providerInformation";
import type { Role } from "./role";
import type { UserDescription } from "./roleDescription";
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
  currentPassword?: string;
  newPassword?: string;
  confirmPassword?: string;
}
