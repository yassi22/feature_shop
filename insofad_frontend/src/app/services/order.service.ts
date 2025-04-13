import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { environment } from "../../environments/environment";
import { GetOrder } from '../models/getorder.model';
import { TokenService } from "./token.service";


@Injectable({
    providedIn: 'root'
})
export class OrderService {

    private _baseUrl: string = environment.base_url + "/orders";

    constructor(
        private http: HttpClient,
        private tokenService: TokenService
    ) { }

    getOrders(): Observable<GetOrder[]> {
        return this.http.get<GetOrder[]>(`${this._baseUrl}/user/${this.tokenService.getUserId()}`);
    }

    sortOrdersByDate(orders: GetOrder[]): GetOrder[] {
        return orders.sort((a, b) => new Date(b.datum).getTime() - new Date(a.datum).getTime());
    }
}
