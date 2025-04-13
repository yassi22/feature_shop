import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe } from "@angular/common";
import { FormsModule } from "@angular/forms";

import { CartService } from "../services/cart.service";
import { Restitute } from "../models/restitute.model";
import { RestituteService } from "../services/restitute.service";
import {RestituteDamage} from "../enums/restitute-damage.enum";

@Component({
  selector: 'app-restitute',
  standalone: true,
  imports: [
    CurrencyPipe,
    DatePipe,
    FormsModule,
  ],
  templateUrl: './restitute.component.html',
  styleUrl: './restitute.component.scss'
})
export class RestituteComponent implements OnInit {

  isLoading: boolean = true;
  restitutes: Restitute[] = [];

  constructor(
      protected restituteService: RestituteService,
      protected cartService: CartService
  ) { }

  ngOnInit(): void {
    this.restituteService
        .getUserRestitutes()
        .subscribe((restitutes: Restitute[]) => {
          this.isLoading = false;
          this.restitutes = this.restituteService.sortRestitutesByDate(restitutes);
        });
  }

  protected readonly RestituteDamage = RestituteDamage;
}
