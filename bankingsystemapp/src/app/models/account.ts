export interface Account {
    id: number;
    user_id: number;
    bank_id: number;
    number: bigint;
    type: string;
    balance:number;
    creation_date : Date;
    last_update_date:Date;
    is_deleted:boolean;
  }