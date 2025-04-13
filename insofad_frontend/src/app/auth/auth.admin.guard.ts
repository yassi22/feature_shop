import { CanActivateFn } from '@angular/router';
import { TokenService } from '../services/token.service';
import { inject } from '@angular/core';
import {AuthService} from "./auth.service";

export const adminGuard: CanActivateFn = (route, state) => {

    const authService: AuthService = inject(AuthService);

    return authService.isAdministrator();
};