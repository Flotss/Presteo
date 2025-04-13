import { RoleType } from "~/model/roleType";
import type { User } from "~/model/user";

export const canUserModify = (
  user: User | null,
  targetUser: User | null
): boolean => {
  if (!user || !targetUser) {
    return false;
  }

  if (user.role?.name === RoleType.ADMIN) {
    return true;
  }

  return user.id === targetUser.id;
}