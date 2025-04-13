import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { CartService } from '../services/cart.service';
import { Product } from '../models/product.model';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { PromoCodeService } from "../services/promo-code.service";
import { ProductsService } from "../services/products.service";
import { TokenService } from "../services/token.service";
import { ProductVariant } from "../models/productvariant.model";
import { Options } from "../models/options.model";
import { GiftCardService } from '../services/giftcard.service';
import { GiftCard } from '../models/giftcard.model';
import { Topup } from '../models/topup.model';
import { Order } from "../models/order.model";

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, FormsModule, RouterLink],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  public products_in_cart: Product[] = [];
  public giftCards_in_cart: GiftCard[] = [];
  public topUpsInCart: Topup[] = [];
  public appliedGiftCard: GiftCard[] = [];
  public giftCardCode: string = '';
  public userIsLoggedIn: boolean = false;
  public amountOfProducts: number = 0;
  public errorNotLoggedInMessage: string = '';

  public totalPrice: number = 0;
  public shippingCosts: number = 4.95;
  private promoCodePercentageDiscount: number = 0;
  private promoCodeAmountDiscount: number = 0;
  private minimumAmount: number = 0;
  public discountAmount: number = 0;

  public orderEmail: string = '';
  public promoCode: string = '';
  public appliedPromoCode: string = '';

  public selectedProductVariant: [ProductVariant | null, number | null] = [null, null];
  public optionsDict: { [key: string]: Options } = {};
  quantity: number = 1;

  constructor(
      protected cartService: CartService,
      private http: HttpClient,
      private router: Router,
      private promoCodeService: PromoCodeService,
      private productService: ProductsService,
      private tokenService: TokenService,
      private giftCardService: GiftCardService
  ) {}

  ngOnInit() {
    this.products_in_cart = this.cartService.allProductsInCart() as Product[];
    this.cartService.$productInCart.subscribe((products: Product[]) => {
      this.products_in_cart = products;
      this.totalPrice = this.cartService.getCartPriceWithoutDiscounts();
    });

    this.giftCards_in_cart = this.cartService.allGiftCardsInCart();
    this.topUpsInCart = this.cartService.allTopUpsInCart();

    this.cartService.$giftCardInCart.subscribe((giftCards: GiftCard[]) => {
      this.giftCards_in_cart = giftCards;
      this.updateAmountOfProducts();
    });

    this.cartService.$topUpsInCart.subscribe((topUps: Topup[]) => {
      this.topUpsInCart = topUps;
      this.updateAmountOfProducts();
    });

    this.checkLoginState();
  }

  private updateAmountOfProducts() {
    this.amountOfProducts = (this.products_in_cart.length > 0 ? this.products_in_cart.length : 0)
        + (this.giftCards_in_cart.length > 0 ? this.giftCards_in_cart.length : 0)
        + (this.topUpsInCart.length > 0 ? this.topUpsInCart.length : 0);
  }

  reCalculateTotalPrice(onPromoCodeCheck: boolean = false) {
    this.totalPrice = this.cartService.getCartPriceWithoutDiscounts();

    if (this.promoCodeAmountDiscount > 0) {
      this.discountAmount = this.promoCodeAmountDiscount;
    } else if (this.promoCodePercentageDiscount > 0) {
      this.discountAmount = this.totalPrice * this.promoCodePercentageDiscount;
    }

    if (this.totalPrice > this.minimumAmount) {
      this.applyDiscount();
    } else {
      if (onPromoCodeCheck) {
        alert('Minimum amount not reached for promo code (' + this.minimumAmount + ')');
        this.promoCodeAmountDiscount = 0;
        this.promoCodePercentageDiscount = 0;
        this.appliedPromoCode = '';
      }
    }

    if (this.appliedGiftCard) {

      this.appliedGiftCard.forEach((giftCard) => {

        this.totalPrice -= giftCard.balance;
      });
    }

  }

  public applyDiscount(): void {
    this.totalPrice = this.totalPrice - this.discountAmount + this.shippingCosts;
  }

  public clearCart() {
    this.cartService.clearCart();
    this.appliedGiftCard = [];
    this.giftCardCode = '';
  }

  public removeProductFromCart(product_index: number) {
    this.cartService.removeProductFromCart(product_index);
    this.reCalculateTotalPrice();
  }

  public removeGiftCardFromCart(giftCardIndex: number): void {
    this.cartService.removeGiftCardFromCart(giftCardIndex);
    this.updateAmountOfProducts();
    this.reCalculateTotalPrice();
  }

  public removeTopUpFromCart(topUpIndex: number): void {
    this.cartService.removeTopUpFromCart(topUpIndex);
    this.updateAmountOfProducts();
    this.reCalculateTotalPrice();
  }

  calculateProductPrice(product: Product): number {
    return this.cartService.calculateProductPrice(product);
  }

  public updateOrderEmail(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input) {
      this.orderEmail = input.value;
    }
  }

  public updateProductQuantity(index: number, event: Event): void {
    const input = event.target as HTMLInputElement;
    const quantity = parseInt(input.value, 10);
    if (quantity >= 0) {
      this.cartService.updateProductQuantity(index, quantity);
    }
  }

  public onSubmit(): void {
    let user_email = this.tokenService.getEmail();
    console.log("order sent to server with email: " + user_email + " and promo code: " + this.appliedPromoCode + " and products: " + this.products_in_cart);


    if(this.appliedGiftCard.length > 0) {
        this.appliedGiftCard.forEach((giftCard) => {
            this.giftCardService.deductBalance(giftCard.code, giftCard.balance).subscribe({
            next: (response) => {
                console.log('Balance deducted:', response);
            },
            error: (error) => {
                console.error('Failed to deduct balance:', error);
            }
            });
        });
    }

    this.giftCards_in_cart.forEach((giftCard, index) => {
      this.giftCardService.createGiftCard(giftCard.amount, giftCard.recipientEmail).subscribe({
        next: (response) => {
          console.log('Gift card created:', response);
        },
        error: (error) => {
          console.error('Failed to create gift card:', error);
        }
      });
    });

    this.topUpsInCart.forEach((topUp, index) => {
      this.giftCardService.addToBalance(topUp.giftCardCode, topUp.topUpAmount).subscribe({
        next: (response) => {
          console.log('Top up added:', response);
        },
        error: (error) => {
          console.error('Failed to add top up:', error);
        }
      });
    });

    if (this.appliedPromoCode) {
      this.promoCodeService.usePromoCode(this.appliedPromoCode).subscribe({
        next: () => {
          this.createOrder(user_email);
        },
        error: (error) => {
          console.error('Failed to apply promo code:', error);
          this.createOrder(user_email);
        }
      });
    } else {
      this.createOrder(user_email);
    }
  }

  private createOrder(email: string): void {
    this.cartService.createOrder(email, this.totalPrice).subscribe({
      next: response => {
        console.log(response);
        this.clearCart();
        this.router.navigate(['/order']);
      },
      error: error => {
        console.error('Failed to create order:', error);
      }
    });
  }

  canPlaceOrder(): boolean {
    return this.products_in_cart.length > 0 || this.giftCards_in_cart.length > 0 || this.topUpsInCart.length > 0;
  }

  public applyGiftCard(giftCardCode: string): void {
    this.giftCardCode = giftCardCode;
    console.log('Applying gift card:', this.giftCardCode)
    if (!this.userIsLoggedIn) {
      this.errorNotLoggedInMessage = 'You need to be logged in to apply a giftcard!';
      return;
    }
    if (this.giftCardCode.trim() !== '') {
      this.giftCardService.validateGiftCard(this.giftCardCode).subscribe(
          (giftCard: GiftCard | null) => {
            if (giftCard) {
              console.log('Gift card applied:', giftCard);
              this.appliedGiftCard = this.appliedGiftCard.filter((card) => card.code !== giftCard.code);
              this.appliedGiftCard.push(giftCard);
              this.giftCardCode = '';
              this.reCalculateTotalPrice();
            } else {
              console.error('Gift card not found');
            }
          },
          (error: any) => {
            console.error('Failed to apply gift card:', error);
          }
      );
    } else {
        console.error('No gift card code entered');
    }
  }

  public onInvalidOrder() {
    return this.amountOfProducts === 0;
  }

  public onOrder() {
    if (!this.userIsLoggedIn) {
      this.router.navigateByUrl('/auth/login');
    } else {
      this.cartService.setAppliedGiftCardCodes(this.appliedGiftCard.map(giftcard => giftcard.code));
      const email = this.tokenService.getEmail();
      if (email) {
        this.cartService.createOrder(email, this.totalPrice).subscribe(
            response => {
              this.router.navigate(['/order']);
            },
            error => {
              console.error('Failed to create order:', error);
            }
        );
      } else {
        this.errorNotLoggedInMessage = 'You need to be logged in to place an order!';
      }
    }
  }

  onCheckPromoCode(code: string) {
    this.promoCodeAmountDiscount = 0;
    this.promoCodePercentageDiscount = 0;
    this.discountAmount = 0;
    this.appliedPromoCode = '';

    if (!code) {
      alert('No promo code entered');
      return;
    }

    this.promoCodeService.getPromoCode(code).subscribe((promoCode) => {
      if (promoCode.expiryDate < new Date()) {
        alert('Promo code has expired');
        return;
      }

      if (promoCode.type == 'percentage') {
        this.promoCodePercentageDiscount = promoCode.discount / 100; // Convert percentage to a fraction
      } else {
        this.promoCodeAmountDiscount = promoCode.discount;
      }

      this.appliedPromoCode = code;
      this.minimumAmount = promoCode.minimumAmount;
      this.reCalculateTotalPrice(true);
    }, error => {
      alert('Invalid promo code');
    });
  }

  public checkLoginState(): void {
    this.userIsLoggedIn = this.tokenService.isValid();
  }

}
