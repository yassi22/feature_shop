import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { environment } from "../../environments/environment";

import { CartService } from "./cart.service";
import { GetOrder } from "../models/getorder.model";
import { OrderProduct } from "../models/orderproduct.model";
import { Restitute } from "../models/restitute.model";
import { RestituteDamage } from "../enums/restitute-damage.enum";
import { RestituteStatus } from "../enums/restitute-status.enum";

@Injectable({
    providedIn: 'root'
})
export class RestituteService {

    private LIGHT_DAMAGE_PERCENTAGE = 0.90;
    private HEAVY_DAMAGE_PERCENTAGE = 0.85;

    private _baseUrl: string = `${environment.base_url}/restitutes`;

    constructor(
        private http: HttpClient,
        private cartService: CartService
    ) { }

    getRestitutes(): Observable<Restitute[]> {
        return this.http.get<[]>(this._baseUrl);
    }

    getUserRestitutes(): Observable<Restitute[]> {
        return this.http.get<[]>(`${this._baseUrl}/me`);
    }

    getUnrestitutedOrders(): Observable<GetOrder[]> {
        return this.http.get<GetOrder[]>(`${this._baseUrl}/me/unrestituted`);
    }

    createRestitute(products: { damage: RestituteDamage; productId: number; orderId: number }[]): Observable<Object> {
        return this.http.post<any>(this._baseUrl, products);
    }

    updateRestituteStatus(restituteId: number, status: RestituteStatus): Observable<Restitute> {
        const headers = new HttpHeaders().set('Content-Type', 'application/json');

        return this.http.put<Restitute>(`${this._baseUrl}/${restituteId}/status`, JSON.stringify(status), {
            headers: headers
        });
    }

    calculatePriceWithDamage(product: OrderProduct): number {
        const totalPrice: number = this.cartService.calculateTotalPrice(product);

        switch (product.damage) {
            case RestituteDamage.LIGHT_DAMAGE:
                return totalPrice * this.LIGHT_DAMAGE_PERCENTAGE;
            case RestituteDamage.HEAVY_DAMAGE:
                return totalPrice * this.HEAVY_DAMAGE_PERCENTAGE;
            case RestituteDamage.UNDAMAGED:
            default:
                return totalPrice;
        }
    }

    sortRestitutesByDate(restitutes: Restitute[]): Restitute[] {
        return restitutes.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
    }
}
