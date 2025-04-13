import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from "@angular/router";
import { CartService } from "../services/cart.service";
import { GetOrder } from '../models/getorder.model';
import { OrderService } from '../services/order.service';
import { GiftCard } from '../models/giftcard.model';

@Component({
    selector: 'app-order',
    standalone: true,
    imports: [CommonModule, RouterLinkActive, RouterLink],
    templateUrl: './order.component.html',
    styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
    isLoading: boolean = true;
    orders: GetOrder[] = [];

    constructor(
        private orderService: OrderService,
        protected cartService: CartService
    ) { }

    ngOnInit(): void {
        this.orderService.getOrders().subscribe((orders: GetOrder[]) => {
            this.isLoading = false;
            this.orders = this.orderService.sortOrdersByDate(orders);
        });
    }

    trackByOrder(index: number, order: GetOrder): number {
        return order.orderId;
    }

    trackByOrderProduct(index: number, orderProduct: any): number {
        return orderProduct.id;
    }

    trackByVariant(index: number, variant: any): number {
        return variant.id;
    }

    trackByGiftCard(index: number, giftCard: GiftCard): string {
        return giftCard.id;
    }
}
