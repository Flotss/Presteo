import type { User } from "./user";

export class UserDescription {
  id!: number;
  user!: User;
  description!: string;
}