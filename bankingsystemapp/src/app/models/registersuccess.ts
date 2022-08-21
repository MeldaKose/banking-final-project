import { MyUser } from "./myuser";

export interface RegisterSuccess {
    success: boolean;
    message: string;
    user: MyUser;
  }