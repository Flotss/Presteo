import type { Role } from "./role";
import type { UserDescription } from "./roleDescription";

export class User {
  id?: number;
  private _username!: string;
  lastName!: string;
  firstName!: string;
  address!: string;
  gender!: string;
  birthDate!: string;
  phoneNumber!: string;
  email!: string;
  password!: string;
  role?: Role;
  descriptions?: UserDescription;
  createdAt?: Date;
  updatedAt?: Date;

  set username(value: string) {
    this._username = value.charAt(0).toUpperCase() + value.slice(1);
  }

  get username(): string {
    return this._username.charAt(0).toUpperCase() + this._username.slice(1);
  }
}
