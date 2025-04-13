import { OrderProduct } from "./orderproduct.model";
import { GiftCard } from "./giftcard.model";
import { Topup } from "./topup.model";

export class GetOrder {
    public orderId: number;
    public datum: Date;
    public orderTitle: string;
    public orderPrice: number;
    public orderProducts: OrderProduct[];
    public giftCards: GiftCard[];
    public topUps: Topup[];
}
