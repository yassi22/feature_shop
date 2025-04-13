import { Component, OnDestroy, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CartService } from '../services/cart.service';
import { Product } from '../models/product.model';
import { GiftCard } from '../models/giftcard.model';
import { Topup } from '../models/topup.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Subscription } from "rxjs";
import { AuthService } from "../auth/auth.service";
import { User } from "../models/user.model";

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit, OnDestroy {

  title = 'J.A.D.';

  public amountOfProducts: number = 0;

  isAuthenticated = false;
  isAdmin = false;

  private userSub: Subscription;
  private productSub: Subscription;
  private giftCardSub: Subscription;
  private topUpSub: Subscription;

  constructor(private authService: AuthService, private cartService: CartService) {}

  ngOnInit(): void {
    this.userSub = this.authService.user.subscribe(user => this.ProcessUser(user));

    this.productSub = this.cartService.$productInCart.subscribe((products: Product[]) => {
      this.updateAmountOfItems();
    });

    this.giftCardSub = this.cartService.$giftCardInCart.subscribe((giftCards: GiftCard[]) => {
      this.updateAmountOfItems();
    });

    this.topUpSub = this.cartService.$topUpsInCart.subscribe((topUps: Topup[]) => {
      this.updateAmountOfItems();
    });
  }

  private updateAmountOfItems(): void {
    this.amountOfProducts = this.cartService.allProductsInCart().length
        + this.cartService.allGiftCardsInCart().length
        + this.cartService.allTopUpsInCart().length;
  }

  private ProcessUser(user: User | null): void {
    if (user) {
      console.log('User:', user);

      this.isAuthenticated = !!user;
      console.log("UserRole In Nav bar", user.role)
      this.isAdmin = user.role === 'ROLE_ADMIN';

    } else {
      this.isAuthenticated = false;
      this.isAdmin = false;
      console.log('User is not logged in');
    }
  }

  ngOnDestroy() {
    if (this.userSub) this.userSub.unsubscribe();
    if (this.productSub) this.productSub.unsubscribe();
    if (this.giftCardSub) this.giftCardSub.unsubscribe();
    if (this.topUpSub) this.topUpSub.unsubscribe();
  }
}
