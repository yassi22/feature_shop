import { OrderProduct } from "./orderproduct.model";

export class Restitute {
    public id: number;
    public createdAt: string;
    public totalPrice: number;
    public status: string;
    public products: OrderProduct[];
}
