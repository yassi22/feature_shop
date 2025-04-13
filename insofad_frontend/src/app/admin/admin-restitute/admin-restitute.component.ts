import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe} from "@angular/common";
import { FormsModule } from "@angular/forms";

import { CartService } from "../../services/cart.service";
import { Restitute } from "../../models/restitute.model";
import { RestituteService } from "../../services/restitute.service";
import { RestituteStatus } from "../../enums/restitute-status.enum";
import {RestituteDamage} from "../../enums/restitute-damage.enum";

@Component({
  selector: 'app-admin-restitute',
  standalone: true,
  imports: [
    CurrencyPipe,
    DatePipe,
    FormsModule,
  ],
  templateUrl: './admin-restitute.component.html',
  styleUrl: './admin-restitute.component.scss'
})
export class AdminRestituteComponent implements OnInit {

  isLoading: boolean = true;
  restitutes: Restitute[] = [];
  selectedStatus: { [restituteId: number]: RestituteStatus } = {};

  constructor(
      protected restituteService: RestituteService,
      protected cartService: CartService
  ) { }

  ngOnInit(): void {
    this.loadRestituteHistory();
  }

  onStatusChange(value: string, restituteId: number): void {
    this.selectedStatus[restituteId] = RestituteStatus[value as keyof typeof RestituteStatus];
  }

  onStatusUpdate(restituteId: number): void {
    const selectedStatus: RestituteStatus = this.selectedStatus[restituteId];

    if (selectedStatus && restituteId) {
      this.restituteService
          .updateRestituteStatus(restituteId, selectedStatus)
          .subscribe(() => this.loadRestituteHistory());
    }
  }

  trackByFn(index: number, restitute: any): any {
    return restitute.id || index;
  }

  private loadRestituteHistory(): void {
    this.restituteService
        .getRestitutes()
        .subscribe((restitutes: Restitute[]) => {
          this.isLoading = false;
          this.restitutes = this.restituteService.sortRestitutesByDate(restitutes);
          this.restitutes.forEach((restitute: Restitute) => this.selectedStatus[restitute.id] = restitute.status as RestituteStatus);
        });
  }

  protected readonly RestituteDamage = RestituteDamage;
  protected readonly RestituteStatus = RestituteStatus;
}
