import {OrderOptions} from "./orderoptions.model";

export class OrderProductVariant {
    id: number;
    name: string;
    description: string;
    orderOptions: OrderOptions[];
}