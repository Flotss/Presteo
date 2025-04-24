import type { ProviderInformation } from "./providerInformation";
import type { Role } from "./role";
import type { UserDescription } from "./roleDescription";
import { RoleType } from "./roleType";

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
  providerInformation?: ProviderInformation;
}


declare module "./user" {
  interface User {
    isProvider(): boolean;
  }
}

User.prototype.isProvider = function (): boolean {
  return this.role?.name === RoleType.PROVIDER;
};