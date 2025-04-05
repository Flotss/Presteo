import type { User } from "./user";

export class UserDescription {
  id!: number;
  user!: User;
  language!: string;
  description!: string;
}