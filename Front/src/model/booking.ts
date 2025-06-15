import type { Service } from "./service";
import type { User } from "./user";

export enum BookingStatusType {
  PENDING = "PENDING",
  CONFIRMED = "CONFIRMED",
  IN_PROGRESS = "IN_PROGRESS",
  CANCELLED = "CANCELLED",
  COMPLETED = "COMPLETED",
}

export interface Booking {
  id: number;
  service: Service;
  customer: User;
  additionalInfo: string;
  address: string;
  bookingDate: Date;
  finalPrice: number;
  status: BookingStatusType;
  createdAt: string;
  updatedAt: string;
  hasReview?: boolean;
  userRating?: number;
  _pendingRating?: number;
}
