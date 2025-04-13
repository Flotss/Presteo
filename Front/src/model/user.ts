import type { Role } from "./role";
import type { UserDescription } from "./roleDescription";

export class User {
  id!: number;
  username!: string;
  lastName!: string;
  firstName!: string;
  address!: string;
  birthDate!: string;
  phoneNumber!: string;
  email!: string;
  role?: Role;
  description?: UserDescription;
  createdAt?: Date;
  updatedAt?: Date;
}
