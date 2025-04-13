import { OrderProductVariant } from "./orderproductvariant.model";
import { RestituteDamage } from "../enums/restitute-damage.enum";
import { GiftCard } from "./giftcard.model";

export class OrderProduct {
    id: number;
    orderProductVariants: OrderProductVariant[];
    imageUrl: string;
    name: string;
    price: number;
    selected?: boolean;
    damage?: RestituteDamage;
    giftCards?: GiftCard[];
}
