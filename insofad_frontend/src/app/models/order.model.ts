import { Product } from "./product.model";

export class Order {
    public products:Product[];
    public email: string;
    public orderPrice: number;


    constructor(products:Product[], email:string, orderPrice:number){
        this.products = products;
        this.email = email;
        this.orderPrice = orderPrice
    }

}