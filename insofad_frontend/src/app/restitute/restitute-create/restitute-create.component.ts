import { CommonModule } from "@angular/common";
import { Component, OnInit } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { Router, RouterLink, RouterLinkActive } from "@angular/router";

import { CartService } from "../../services/cart.service";
import { GetOrder } from "../../models/getorder.model";
import { OrderProduct } from "../../models/orderproduct.model";
import { RestituteDamage } from "../../enums/restitute-damage.enum";
import { RestituteService } from "../../services/restitute.service";
import {OrderService} from "../../services/order.service";

@Component({
    selector: 'app-restitute-create',
    standalone: true,
    imports: [
        CommonModule,
        RouterLinkActive,
        RouterLink,
        FormsModule
    ],
    templateUrl: './restitute-create.component.html',
    styleUrl: './restitute-create.component.scss'
})
export class RestituteCreateComponent implements OnInit{

    isLoading: boolean = true;
    orders: GetOrder[] = [];
    selectedDamage: {
      [productId: number]: RestituteDamage
    } = {};

    constructor(
        private router: Router,
        private orderService: OrderService,
        protected restituteService: RestituteService,
        protected cartService: CartService
    ) { }

    ngOnInit(): void {
        this.restituteService
            .getUnrestitutedOrders()
            .subscribe((orders: GetOrder[]) => {
                const currentDate = new Date();
                const thirtyDaysAgo = new Date(currentDate.setDate(currentDate.getDate() - 30));

                this.isLoading = false;
                this.orders = this.orderService.sortOrdersByDate(orders.filter(order => new Date(order.datum) >= thirtyDaysAgo));
            });
    }

    onSubmit(): void {
        const selectedProducts = this.orders.flatMap(order =>
            order.orderProducts
                .filter((product: OrderProduct) => product.selected)
                .map((product: OrderProduct) => ({
                    orderId: order.orderId,
                    productId: product.id,
                    damage: this.selectedDamage[product.id] || RestituteDamage.UNDAMAGED
                }))
        );

        this.restituteService
            .createRestitute(selectedProducts)
            .subscribe(() => this.router.navigate(['/order']));
    }

    onDamageChange(event: Event, product: OrderProduct): void {
        const value = (event.target as HTMLSelectElement).value;

        this.selectedDamage[product.id] = RestituteDamage[value.toUpperCase() as keyof typeof RestituteDamage];
        this.updateProductDamage(product);
    }

    onProductDamageChange(product: OrderProduct): void {
        product.selected
            ? this.selectedDamage[product.id] = RestituteDamage.UNDAMAGED
            : delete this.selectedDamage[product.id];
    }

    updateProductDamage(product: OrderProduct): void {
        product.damage = this.selectedDamage[product.id];
    }

    isAnyProductSelected(): boolean {
        return this.orders.some(order => order.orderProducts.some((product: OrderProduct) => product.selected));
    }

    protected readonly RestituteDamage = RestituteDamage;
}
